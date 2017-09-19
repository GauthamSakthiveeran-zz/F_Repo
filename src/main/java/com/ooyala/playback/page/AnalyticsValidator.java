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
        addElementToPageElements("adclicks");
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

    public boolean injectIQAnalyticsLogRecorder(){
        try {
            driver.executeScript("(function(){var oldf = console.log;\n" +
                    "console.log = function() {\n" +
                    "\toldf.apply(console, arguments);\n" +
                    "\tif(arguments[0].includes(\"IQ: Reported: reportCustomEvent() for event: ad_ended with args\")){\n" +
                    "\t\tOO.$(\"#ooplayer\").append(\"<p id=afterAdIQ>\" + arguments[0] + \"</p>\");\n" +
                    "\t}\n" +
                    "\n" +
                    "\tif(arguments[0].includes(\"IQ: Reported: reportCustomEvent() for event: adRequest with args\")){\n" +
                    "\t\tOO.$(\"#ooplayer\").append(\"<p id=beforeAdIQ>\" + arguments[0] + \"</p>\");\n" +
                    "\t}\n" +
                    "\n" +
                    "}\n" +
                    "})();");
            return true;
        } catch (Exception ex){
            ex.getStackTrace();
            return false;
        }
    }

    public boolean validateIQAnalyticsLogs(){
        if (!driver.findElement(By.id("beforeAdIQ")).isDisplayed()){
            extentTest.log(LogStatus.FAIL,"IQ: Reported: reportCustomEvent() is not triggering before playing ad..");
            return false;
        }

        if (!driver.findElement(By.id("afterAdIQ")).isDisplayed()){
            extentTest.log(LogStatus.FAIL,"IQ: Reported: reportCustomEvent() is not triggering after playing ad..");
            return false;
        }

        return true;
    }
}
