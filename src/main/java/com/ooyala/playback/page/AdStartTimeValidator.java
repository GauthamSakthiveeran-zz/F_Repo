package com.ooyala.playback.page;

import com.ooyala.playback.url.StreamType;
import com.ooyala.playback.url.UrlObject;
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

    private int adStartTime;
    private int overlayPlayTime;

    public boolean isOverlayPlayTimePresent(UrlObject url){
        if (url.getOverlayPlayTime() != null && !url.getOverlayPlayTime().isEmpty()){
            overlayPlayTime = Integer.parseInt(url.getOverlayPlayTime());
            return true;
        }
        return false;
    }

    public boolean isAdPlayTimePresent(UrlObject url){
        if (url.getAdStartTime() != null && !url.getAdStartTime().isEmpty()){
            adStartTime = Integer.parseInt(url.getAdStartTime());
            return true;
        }
        return false;
    }

    @Override
    public boolean validate(String element, int timeout) throws Exception {
        return false;
    }

    public boolean validateAdStartTime(String adEventLocator){

        if (!loadingSpinner()){
            return false;
        }

        if (!waitOnElement(By.id("adStartTime"),50000)){
            logger.error("adStartTime element id is not present");
            extentTest.log(LogStatus.FAIL,"adStartTime element id is not present");
            return false;
        }

        int time = Integer.parseInt(driver.findElement(By.id("adStartTime")).getText());

        logger.info("Ad start time is :"+time);

        if (!(time>=adStartTime && time<=(adStartTime+3))){
            logger.error("Ad is not starting at "+adStartTime+" instead it gets started at "+time);
            extentTest.log(LogStatus.FAIL,"Ad is not starting at "+adStartTime+" instead it gets started at "+time);
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

        if (!waitOnElement(By.id("overlay-ad-position"),20000)){
            logger.error("overlay-ad-position element id is not present");
            extentTest.log(LogStatus.FAIL,"overlay-ad-position element id is not present");
            return false;
        }

        if (!waitOnElement(By.id("play-overaly-ad"),20000)){
            logger.error("play-overaly-ad element id is not present");
            extentTest.log(LogStatus.FAIL,"play-overaly-ad element id is not present");
            return false;
        }

        logger.info("overlay position is at "+overlayPlayTime+"th second");

        int overlayPlayingAt = Integer.parseInt(driver.findElement(By.id("play-overaly-ad")).getText());

        logger.info("overlay is playing at "+overlayPlayingAt+"th second");

        // in following if statement, we are giving offset of 3 sec so that test won't get fail
        if (overlayPlayingAt>=overlayPlayTime && overlayPlayingAt<=(overlayPlayTime+3)){
            logger.info("Overlay is playing at expected position i.e : "+overlayPlayingAt);
            extentTest.log(LogStatus.PASS,"Overlay is playing at expected position i.e : "+overlayPlayingAt);
            return true;
        }

        logger.error("Overlay is not playing at expected position i.e : "+overlayPlayingAt);
        extentTest.log(LogStatus.FAIL,"Overlay is not playing at expected position i.e : "+overlayPlayingAt);
        return false;
    }

    public boolean validateMultipleMidrollAdStartTime(String adStartTime){

        String [] midrollAdStartTimes = null;
        int timeForFirstMidrollAd;
        int timeForSecondMidrollAd;
        int firstMidrollAdPlayingAt;
        int secondMidrollAdPlayingAt;

        if (!loadingSpinner()){
            extentTest.log(LogStatus.FAIL, "Loading spinner seems to be there for a really long time.");
            return false;
        }

        if (!waitOnElement(By.id("MidRoll_willPlaySingleAd_1"),200000)){
            logger.error("MidRoll_willPlaySingleAd_1 is not present");
            extentTest.log(LogStatus.FAIL,"MidRoll_willPlaySingleAd_1 is not present");
            return false;
        }

        if (!waitOnElement(By.id("MidRoll_willPlaySingleAd_2"),200000)){
            logger.error("MidRoll_willPlaySingleAd_2 is not present");
            extentTest.log(LogStatus.FAIL,"MidRoll_willPlaySingleAd_2 is not present");
            return false;
        }

        if (adStartTime.contains(",")){
            midrollAdStartTimes = adStartTime.split(",");
        }

        timeForFirstMidrollAd = Integer.parseInt(midrollAdStartTimes[0]);

        timeForSecondMidrollAd = Integer.parseInt(midrollAdStartTimes[1]);

        if (!waitOnElement(By.id("adStartTime"),20000)){
            logger.error("adStartTime element id is not present");
            extentTest.log(LogStatus.FAIL,"adStartTime element id is not present");
            return false;
        }

        if (!waitOnElement(By.id("multimidrollAdStartTime"),20000)){
            logger.error("multimidrollAdStartTime element id is not present");
            extentTest.log(LogStatus.FAIL,"multimidrollAdStartTime element id is not present");
            return false;
        }

        firstMidrollAdPlayingAt = Integer.parseInt(driver.findElement(By.id("adStartTime")).getText());

        secondMidrollAdPlayingAt = Integer.parseInt(driver.findElement(By.id("multimidrollAdStartTime")).getText());

        logger.info("Expected first ad time : "+timeForFirstMidrollAd);

        logger.info("Expected second ad time : "+timeForSecondMidrollAd);

        if (!(firstMidrollAdPlayingAt>=timeForFirstMidrollAd && firstMidrollAdPlayingAt<=(timeForFirstMidrollAd+3))){
            logger.error("first midroll ad is not playing at "+firstMidrollAdPlayingAt);
            extentTest.log(LogStatus.FAIL,"first midroll ad is not playing at "+firstMidrollAdPlayingAt);
            return false;
        }

        logger.info("First midroll ad is playing at "+firstMidrollAdPlayingAt+"th second");
        extentTest.log(LogStatus.PASS,"first midroll ad is playing at "+firstMidrollAdPlayingAt);

        if (!(secondMidrollAdPlayingAt>=timeForSecondMidrollAd && secondMidrollAdPlayingAt<=(timeForSecondMidrollAd+3))){
            logger.error("second midroll ad is not playing at "+secondMidrollAdPlayingAt);
            extentTest.log(LogStatus.FAIL,"second midroll ad is not playing at "+secondMidrollAdPlayingAt);
            return false;
        }

        logger.info("Second midroll ad is playing at "+secondMidrollAdPlayingAt+"th second");
        extentTest.log(LogStatus.FAIL,"second midroll ad is playing at "+secondMidrollAdPlayingAt);

        return true;
    }
}
