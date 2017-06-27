package com.ooyala.playback.page;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by suraj on 6/22/17.
 */
public class UIControlValidator extends PlayBackPage implements PlaybackValidator {
    private static Logger logger = Logger.getLogger(UIControlValidator.class);

    public UIControlValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        addElementToPageElements("volume");
        addElementToPageElements("play");
        addElementToPageElements("pause");
        addElementToPageElements("scrubber");
        addElementToPageElements("replay");
    }

    public boolean validate(String element, int timeout) throws Exception {
        if (!waitOnElement(By.id(element), timeout)) {
            logger.error(element + " Element not found");
            extentTest.log(LogStatus.FAIL, element + " Element not found");
            return false;
        }
        logger.info(element + " Element found");
        extentTest.log(LogStatus.PASS, element + " Element found");
        return true;
    }

    public boolean playAction(String element, int timeout) throws Exception {
        try {
            WebElement playButton = getWebElement("PLAY_BUTTON");
            clickOnElementThroughUI(playButton);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return validate(element, timeout);
    }

    public boolean pauseAction(String element, int timeout) throws Exception {
        //WebElement pauseButton = driver.findElement(By.className("oo-icon-pause-slick"));
        boolean flag;
        flag = switchToControlBar();
        if (flag) {
            try {
                WebElement pauseButton = getWebElement("PAUSE_BUTTON");
                clickOnElementThroughUI(pauseButton);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
            return validate(element, timeout);
        }
        return flag;
    }

    public boolean validateVolumeMute(int expectedVolume) {
        try {
            WebElement volumeMaxButton = getWebElement("VOLUME_MAX");
            clickOnElementThroughUI(volumeMaxButton);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return verifyVolume(expectedVolume);
    }

    public boolean validateVolumeMax(int expectedVolume) {
        try {
            WebElement volumeMuteButton = getWebElement("VOLUME_MUTE");
            clickOnElementThroughUI(volumeMuteButton);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return verifyVolume(expectedVolume);
    }

    public boolean validateSeek(String element, int timeout) throws Exception {
        int seekPercent = 0;
        try {
            WebElement pointer = getWebElement("POINTER");
            seekPercent = (pointer.getSize().getWidth() * 2000 / 100);
            Actions action = new Actions(driver);
            action.dragAndDropBy(pointer, seekPercent, 0).build().perform();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        logger.info("Sucessfully seeked till :" + seekPercent);
        return validate(element, timeout);
    }

    public boolean validateReplay(String element, int timeout) throws Exception {
        try {
            driver.executeScript("scroll(0, -250);");
            WebElement replayButton = getWebElement("REPLAY");
            clickOnElementThroughUI(replayButton);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return validate(element, timeout);
    }

    public void clickOnElementThroughUI(WebElement element) {
        if (element.isDisplayed()) {
            element.click();
            logger.info("clicked on element :" + element);
        }
    }

    public boolean verifyVolume(int expectedVolume) {
        String volume = driver.executeScript("return pp.getVolume();").toString();
        int volumeLevel = Integer.parseInt(volume);
        if (volumeLevel != expectedVolume) {
            logger.error("Actual Volume :" + volumeLevel + " and Expected Volume :" + expectedVolume + " do not match");
            extentTest.log(LogStatus.FAIL, "Actual Volume :" + volumeLevel + " and Expected Volume :" + expectedVolume + " do not match");
            return false;
        }
        logger.info("Actual Volume :" + volumeLevel + " and Expected Volume :" + expectedVolume + " do  match");
        extentTest.log(LogStatus.PASS, "Actual Volume :" + volumeLevel + " and Expected Volume :" + expectedVolume + " do match");
        return true;
    }
}
