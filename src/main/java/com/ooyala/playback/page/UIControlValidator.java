package com.ooyala.playback.page;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
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
    }

    public boolean validate(String element, int timeout) throws Exception {
        if (!waitOnElement(By.id(element), timeout)) {
            logger.error(element+" Element not found");
            extentTest.log(LogStatus.FAIL,element+" Element not found");
            return false;
        }
        logger.info(element+" Element found");
        extentTest.log(LogStatus.PASS,element+" Element found");
        return true;
    }

    public boolean playAction(String element, int timeout) throws Exception{
        WebElement playButton = driver.findElement(By.className("oo-icon-play-slick"));
        clickOnElementThroughUI(playButton);
        return validate(element,timeout);
    }

    public boolean pauseAction(String element, int timeout) throws Exception{
        WebElement pauseButton = driver.findElement(By.className("oo-icon-pause-slick"));
        clickOnElementThroughUI(pauseButton);
        return validate(element,timeout);
    }

    public boolean validateVolumeMute(int expectedVolume) {
        WebElement volumeMaxButton = driver.findElement(By.className("oo-icon-volume-on-ooyala-default "));
        clickOnElementThroughUI(volumeMaxButton);
        return verifyVolume(expectedVolume);
    }

    public boolean validateVolumeMax(int expectedVolume) {
        WebElement volumeMuteButton = driver.findElement(By.className("oo-icon-volume-mute-ooyala-default "));
        clickOnElementThroughUI(volumeMuteButton);
        return verifyVolume(expectedVolume);
    }

    public boolean validateSeek(String element, int timeout) throws Exception{
        WebElement element1 = driver.findElement(By.className("oo-playhead-padding"));
        int seekPercent = (element1.getSize().getWidth()*2000/100);
        Actions action = new Actions(driver);
        action.dragAndDropBy(element1, seekPercent,0).build().perform();
        logger.info("Sucessfully seeked till :"+seekPercent);
        return validate(element,timeout);
    }

    public boolean validateReplay(String element, int timeout) throws Exception{
        WebElement replayButton = driver.findElement(By.xpath(".//*[normalize-space(@class)='oo-icon oo-icon-system-replay']"));
        clickOnElementThroughUI(replayButton);
        return validate(element,timeout);
    }

    public void clickOnElementThroughUI(WebElement element) {
        if (element.isDisplayed()) {
            element.click();
            logger.info("clicked on element :" + element);
        }
    }

    public boolean verifyVolume(int expectedVolume){
        String volume = driver.executeScript("return pp.getVolume();").toString();
        int volumeLevel = Integer.parseInt(volume);
        if (volumeLevel != expectedVolume) {
            logger.error("Actual Volume :" + volumeLevel + " and Expected Volume :" + expectedVolume + " do not match");
            extentTest.log(LogStatus.FAIL,"Actual Volume :" + volumeLevel + " and Expected Volume :" + expectedVolume + " do not match");
            return false;
        }
        logger.info("Actual Volume :" + volumeLevel + " and Expected Volume :" + expectedVolume + " do  match");
        extentTest.log(LogStatus.PASS,"Actual Volume :" + volumeLevel + " and Expected Volume :" + expectedVolume + " do match");
        return true;
    }
}
