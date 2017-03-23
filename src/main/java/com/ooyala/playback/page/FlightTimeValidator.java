package com.ooyala.playback.page;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by suraj on 3/23/17.
 */
public class FlightTimeValidator extends PlayBackPage implements PlaybackValidator {

    private static Logger logger = Logger.getLogger(FlightTimeValidator.class);

    public FlightTimeValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(driver, this);
    }


    public boolean validate(String element, int timeout) throws Exception {
        if (!loadingSpinner()){
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

}
