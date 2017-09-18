package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 08/05/17.
 */
public class AnalyticsValidator extends PlayBackPage implements PlaybackValidator {

    public static Logger logger = Logger.getLogger(AnalyticsValidator.class);

    public AnalyticsValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
    }

    public boolean validate(String element, int timeout) throws Exception {
        if (!waitOnElement(By.id(element), timeout)) {
            extentTest.log(LogStatus.FAIL, "Event not verified : " + element);
            logger.error("Event not verified : " + element);
            return false;
        }
        extentTest.log(LogStatus.PASS, "Event verified : " + element);
        logger.error("Event verified :" + element);
        return true;
    }

    public boolean injectNetworkLogRecorder(){
        try{
            driver.executeScript("var urls = Array.prototype.map.call(\n" +
                    "    document.querySelectorAll(\"link,script\"), // Elements which request external resources\n" +
                    "    function(e) { // Loop over and return their href/src\n" +
                    "        return e.href || e.src; \n" +
                    "    }\n" +
                    ");\n" +
                    "\tOO.$(\"#ooplayer\").append(\"<p id=netwoklogs>\"+urls+\"</p>\");");
            return true;
        } catch (Exception ex){
            ex.getStackTrace();
            return false;
        }
    }

    public boolean validateAnalyticsNetworkLogs(){
        String logs = driver.findElement(By.id("netwoklogs")).getText();
        String[] a = logs.split(",");
        boolean flag = false;
        for(String b : a){
            if (!b.contains("http://analytics.ooyala.com/static/v3/analytics.js")){
                flag = false;
            } else
                flag = true;
        }

        if (!flag){
            return false;
        }
        return true;
    }
}
