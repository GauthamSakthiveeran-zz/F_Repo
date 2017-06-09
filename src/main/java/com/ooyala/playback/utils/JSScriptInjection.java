package com.ooyala.playback.utils;

import com.ooyala.playback.httpserver.SimpleHttpServer;
import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.playback.url.UrlObject;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import java.net.InetAddress;

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

    public void injectScript() throws Exception {
        if (urlObject.getVideoPlugins().contains("ANALYTICS")){
            InetAddress inetAdd = InetAddress.getLocalHost();
            String url = "http://" + inetAdd.getHostAddress() + ":"
                    + SimpleHttpServer.portNumber + "/js?fileName=analytics/AnalyticsQEPlugin.js";
            logger.info("JS - "+url);
            scriptToInjectJS(url);
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

    @SuppressWarnings("unused")
    private void injectScript(String scriptURL) throws Exception {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        scriptToInjectJS(scriptURL);
        Thread.sleep(1000); // to avoid js failures
        if (scriptURL.contains("common"))
            js.executeScript("subscribeToCommonEvents();");
        else
            js.executeScript("subscribeToEvents();");
    }

    private void scriptToInjectJS(String scriptURL){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("function injectScript(url) {\n"
                + "   var script = document.createElement ('script');\n" + "   script.src = url;\n"
                + "   var head = document.getElementsByTagName( 'head')[0];\n" + "   head.appendChild(script);\n"
                + "}\n" + "\n" + "var scriptURL = arguments[0];\n" + "injectScript(scriptURL);", scriptURL);
    }
}
