
package com.ooyala.playback.apps.validators;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.utils.CommandLineParameters;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

/**
 * Created by Gautham
 */
public class FullScreenOrientationValidator extends PlaybackApps implements Validators {

    private Logger logger = Logger.getLogger(FullScreenOrientationValidator.class);

    public FullScreenOrientationValidator(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("fullscreen");
    }

    @Override
    public boolean validate(String element, int timeout) throws Exception {

        Boolean result = true;

        result = result && isCloseScreenButtonFound(element);
        result = result && isOrientationCorrect("POTRAIT");
        result = result && changeOrientation("LANDSCAPE");
        result = result && isOrientationCorrect("LANDSCAPE");

        return result;
    }

    public boolean changeOrientation(String orientation) {

        try {
            if (orientation.equals("LANDSCAPE")){
                ((AppiumDriver) driver).rotate(ScreenOrientation.LANDSCAPE);
                logger.info("Orientation Changed");
                extentTest.log(LogStatus.INFO, "Orientation Changed");
                return true;
            }
            else{
                ((AppiumDriver) driver).rotate(ScreenOrientation.PORTRAIT);
                logger.info("Orientation Changed");
                extentTest.log(LogStatus.INFO, "Orientation Changed");
                return true;
            }
            
        } catch (Exception e) {
            extentTest.log(LogStatus.FAIL, "Unable to Change Orientation");
            return false;
        }
    }

    public boolean isOrientationCorrect(String orientation) {
        if (((AppiumDriver) driver).getOrientation().toString().equals(orientation)) {
        	logger.info("Orientation is correct");
            extentTest.log(LogStatus.PASS, "Orientation is correct" + orientation);
            return true;
        } else {
            extentTest.log(LogStatus.PASS, "Orientation is not correct");
            return false;
        }

    }

    public Boolean isCloseScreenButtonFound(String element) {
        try {

            if (waitOnElement(element, 5000)) {
                extentTest.log(LogStatus.PASS, element + " not present ");
                logger.info("Close FullScreen Found");
                return true;
            } else {
                extentTest.log(LogStatus.FAIL, element + " not present ");
                logger.info("Close FullScreen not  Found");
                return false;

            }

        } catch (Exception e) {

            e.printStackTrace();
            logger.info("Failed to locate Element");
            extentTest.log(LogStatus.FAIL, " Failed to locate Element");
            return false;

        }
    }
}

