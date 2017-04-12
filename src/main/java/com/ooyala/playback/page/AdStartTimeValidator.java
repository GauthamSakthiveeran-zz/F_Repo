package com.ooyala.playback.page;

import com.ooyala.playback.url.StreamType;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by jitendra on 10/4/17.
 */
public class AdStartTimeValidator extends PlayBackPage implements PlaybackValidator{

    public static Logger logger = Logger.getLogger(AdStartTimeValidator.class);

    public AdStartTimeValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
    }

    @Override
    public boolean validate(String element, int timeout) throws Exception {
        return false;
    }

    public boolean validateAdStartTime(String adStartTime,String adEventLocator){

        int adTime = Integer.parseInt(adStartTime);

        waitOnElement(By.id("adStartTime"),200000);

        int time = Integer.parseInt(driver.findElement(By.id("adStartTime")).getText());

        logger.info("Ad start time is :"+time);

        if (!(time>=adTime && time<=(adTime+3))){
            logger.error("Ad is not starting at "+adTime+" instead it gets started at "+time);
            return false;
        }

        if (!waitOnElement(By.id(adEventLocator),1000)){
            logger.error(adEventLocator + "element is not found or midroll ad is not playing at "+time);
            extentTest.log(LogStatus.FAIL,adEventLocator + "element is not found or midroll ad is not playing at "+time);
            return false;
        }

        logger.info(adEventLocator + " is present and midroll ad played at "+time);
        extentTest.log(LogStatus.PASS,adEventLocator + " is present and midroll ad played at "+time);
        return true;
    }

    public boolean validateNonLinearAdStartTime(String element){

        if (!loadingSpinner()){
            extentTest.log(LogStatus.FAIL, "Loading spinner seems to be there for a really long time.");
            return false;
        }

        if (!waitOnElement(By.id(element),20000)){
            logger.error(element +"is not present");
            extentTest.log(LogStatus.FAIL,element +"is not present");
            return false;
        }

        waitOnElement(By.id("overlay-ad-position"),20000);

        waitOnElement(By.id("play-overaly-ad"),20000);

        int overlayPositionAt = Integer.parseInt(driver.findElement(By.id("overlay-ad-position")).getText());

        logger.info("overlay position is at "+overlayPositionAt+"th second");

        int overlayPlayingAt = Integer.parseInt(driver.findElement(By.id("play-overaly-ad")).getText());

        logger.info("overlay is playing at "+overlayPlayingAt+"th second");

        // if following if statement, we are giving offset of 3 sec so that test wonn't get failed
        if (overlayPlayingAt>=overlayPositionAt && overlayPlayingAt<=(overlayPositionAt+3)){
            logger.info("Overlay is playing at expected position i.e : "+overlayPlayingAt);
            extentTest.log(LogStatus.PASS,"Overlay is playing at expected position i.e : "+overlayPlayingAt);
            return true;
        }

        logger.error("Overlay is not playing at expected position i.e : "+overlayPlayingAt);
        extentTest.log(LogStatus.FAIL,"Overlay is not playing at expected position i.e : "+overlayPlayingAt);
        return false;
    }
}
