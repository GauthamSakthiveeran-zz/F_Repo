package com.ooyala.playback.utils;

import com.ooyala.playback.httpserver.SimpleHttpServer;
import com.ooyala.playback.url.UrlObject;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class JSScriptInjection {

    private Logger logger = Logger.getLogger(JSScriptInjection.class);

    String[] jsUrl;
    ExtentTest extentTest;
    WebDriver driver;
    UrlObject urlObject;

    public JSScriptInjection(String[] jsUrl, ExtentTest extentTest, WebDriver driver, UrlObject urlObject) {
        this.jsUrl = jsUrl;
        this.extentTest = extentTest;
        this.driver = driver;
        this.urlObject = urlObject;
    }
    
    public JSScriptInjection(String[] jsUrl, ExtentTest extentTest, WebDriver driver) {
        this.jsUrl = jsUrl;
        this.extentTest = extentTest;
        this.driver = driver;
        urlObject = null;
    }

    public JSScriptInjection(WebDriver driver){
        this.driver = driver;
    }

    public void injectScript() throws Exception {
        if (urlObject!=null && urlObject.getVideoPlugins().contains("ANALYTICS")){
            String hostUrl = getJsUrl("analytics/analytics_events.js");
            scriptToInjectJS(hostUrl);
        }
        if (jsUrl != null && jsUrl.length > 0) {
            for (String url : jsUrl) {
                try {
                    logger.info("JS - " + url);
                    injectScript(url);
                } catch (Exception e) {
                    // e.printStackTrace();
                    logger.error(e.getMessage());
                    logger.info("Retrying...");
                    injectScript(url);
                }
            }
            extentTest.log(LogStatus.PASS, "Javascript injection is successful");
        }
    }

    private void injectScript(String scriptURL) throws Exception {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        if(scriptURL.contains(".css")) {
            System.out.println("Injecting Css");
               scriptToInjectCSS(scriptURL);
        }
        else
        {
           scriptToInjectJS(scriptURL);
        }
        Thread.sleep(1000); // to avoid js failures
        if (scriptURL.contains("common"))
            js.executeScript("subscribeToCommonEvents();");
        else
            js.executeScript("subscribeToEvents();");
    }

    public void scriptToInjectJS(String scriptURL){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("function injectScript(url) {\n"
                + "   var script = document.createElement ('script');\n" + "   script.src = url;\n"
                + "   var head = document.getElementsByTagName( 'head')[0];\n" + "   head.appendChild(script);\n"
                + "}\n" + "\n" + "var scriptURL = arguments[0];\n" + "injectScript(scriptURL);", scriptURL);
    }
    //
	private void scriptToInjectCSS(String scriptURL) throws Exception {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("function injectScript(url) {\n"
                + "   var fileref = document.createElement(\"link\");\n" + "   fileref.setAttribute(\"rel\", \"stylesheet\")\n"
                + "   fileref.setAttribute(\"type\", \"text/css\");\n" + "   fileref.setAttribute(\"href\", url);\n" +"var head = document.getElementsByTagName( 'head')[0];\n"+
                 "head.appendChild(fileref);\n"+"}\n" + "\n" + "var scriptURL = arguments[0];\n" + "injectScript(scriptURL);", scriptURL);
    


 }

    public String getJsUrl(String jsName) throws UnknownHostException {
        InetAddress inetAdd = InetAddress.getLocalHost();
        String hostUrl = "http://" + inetAdd.getHostAddress() + ":"
                + SimpleHttpServer.portNumber + "/js?fileName="+jsName+"";
        logger.info("JS - " + hostUrl);
        return hostUrl;
    }
}
