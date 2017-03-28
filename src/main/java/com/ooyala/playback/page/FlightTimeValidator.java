package com.ooyala.playback.page;

import com.google.common.base.Predicate;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;

/**
 * Created by suraj on 3/23/17.
 */
public class FlightTimeValidator extends PlayBackPage implements PlaybackValidator {

    private static Logger logger = Logger.getLogger(FlightTimeValidator.class);

    public FlightTimeValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("play");
    }


    public boolean validate(String element, int timeout) throws Exception {
        if (!loadingSpinner()){
            return false;
        }

        if (!errorDescription()){
            return false;
        }
        String errorCode = driver.executeScript("return pp.getErrorCode()").toString();
        logger.info("Error code :"+errorCode);
            if (!errorCode.equalsIgnoreCase("past")) {
                logger.error("Flight time Syndication is not working");
                extentTest.log(LogStatus.FAIL, "Flight Time Syndication is not working");
                return false;
            }
        return true;
    }

    public boolean errorDescription(){

        logger.info("Checking error description");

        if (!waitOnElement("ERROR_SCREEN",20000)){
            logger.error("Error screen is not showing");
            extentTest.log(LogStatus.FAIL,"Error screen is not showing");
            return false;
        }

        logger.info("Error description is present");
        return true;
    }

    public boolean isPageLoaded(){
        WebDriverWait wait = new WebDriverWait(driver,15);
        wait.until(new Predicate<WebDriver>() {
            public boolean apply(WebDriver webDriver) {
                return driver.executeScript("return typeof pp")
                        .toString().equals("object");
            }
        });
        if (!driver.executeScript("return typeof pp")
                .toString().equals("object")){
            logger.error("pp object is not loaded");
            extentTest.log(LogStatus.FAIL,"pp object is not loaded");
            return false;
        }
        return true;
    }
}
