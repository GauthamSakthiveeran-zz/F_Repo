package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/16/16.
 */

public class AspectRatioValidator extends PlayBackPage implements
        PlaybackValidator {

    public static Logger logger = Logger.getLogger(AspectRatioValidator.class);

    private boolean verticalVideo = false;

    public AspectRatioValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        addElementToPageElements("aspectratio");
    }

    public AspectRatioValidator setVerticalVideo() {
        this.verticalVideo = true;
        return this;
    }

    public boolean validate(String element, int timeout) throws Exception {
    	
    	if(driver.getCurrentUrl().contains("osmf_flash")) {
    		extentTest.log(LogStatus.INFO, element + " element not present for OSMF.");
    		return true;
    	}

        if (isElementPresent(By.id(element))) {

            int width = Integer.parseInt(getWebElement(element).getAttribute(
                    "width"));
            int height = Integer.parseInt(getWebElement(element).getAttribute(
                    "height"));

            logger.info("Width : " + width + " Height : " + height);

            int factor = greatestCommonFactor(width, height);
            int widthRatio = width / factor;
            int heightRatio = height / factor;

            logger.info("Resolution: " + width + "x" + height);
            logger.info("Aspect Ratio: " + widthRatio + ":" + heightRatio);
            logger.info("Decimal Equivalent: " + widthRatio / heightRatio);

            if (verticalVideo) {
                Assert.assertEquals(widthRatio, 9, "Width Matches");
                Assert.assertEquals(heightRatio, 16, "Heigth Matches");
                extentTest.log(LogStatus.PASS, " Verified Vertical Video");
            } else {
                Assert.assertEquals(widthRatio, 4, "Width Matches");
                Assert.assertEquals(heightRatio, 3, "Heigth Matches");
                extentTest.log(LogStatus.PASS, "Verified Aspect Ratio Video");
            }
            return true;
        } else {
            logger.error(element + " is not found.");
            extentTest.log(LogStatus.FAIL, "Aspect ratio element not present");
            return false;
        }
    }

    String sizeBeforePlayerLoading;
    String sizeAfterPlayerLoading;

    public void getDimensions() {
        WebElement element = getWebElement("playerScreen");
        sizeBeforePlayerLoading = element.getSize().toString();
        logger.info(sizeBeforePlayerLoading);
    }

    public boolean validateDimensions() {
        WebElement element = getWebElement("playerScreen");
        sizeAfterPlayerLoading = element.getSize().toString();
        logger.info(sizeAfterPlayerLoading);
        if (!sizeAfterPlayerLoading.equals(sizeBeforePlayerLoading)) {
            logger.error("Player dimensions did not match");
            extentTest.log(LogStatus.FAIL, "Player dimensions did not match");
            return false;
        }
        logger.info("Player dimensions matched");
        extentTest.log(LogStatus.PASS, "Player dimensions matched");
        return true;
    }

    public int greatestCommonFactor(int width, int height) {
        return (height == 0) ? width : greatestCommonFactor(height, width % height);
    }
}
