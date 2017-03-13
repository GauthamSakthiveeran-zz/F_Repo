package com.ooyala.playback.amf.midroll;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 28/2/17.
 */
public class PlaybackMidrollInitialVolumeTests extends PlaybackWebTest {
    private static Logger logger = Logger.getLogger(PlaybackMidrollInitialVolumeTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private VolumeValidator volumeValidator;
    private PlayAction playAction;
    private SeekValidator seekValidator;
    private IsAdPlayingValidator isAdPlayingValidator;
    private SeekAction seekAction;

    PlaybackMidrollInitialVolumeTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testInitialVolumeVTC(String testName, String url)
            throws OoyalaException {

        boolean result = true;
        try {
            driver.get(url);

            result = result && play.waitForPage();

            injectScript();

            result = result && playAction.startAction();

            result=result && eventValidator.validate("playing_1", 60000);

            result=result && volumeValidator.checkInitialVolume("video");

            result=result && seekAction.seek(20,true);

            result=result && eventValidator.validate("videoPlayingAd_1", 40000);

            result = result && eventValidator.validate("adPodStarted_2",10000);

            int noOfAds = Integer.parseInt(driver.executeScript("return adPodStarted_2.textContent").toString());

            for (int i = 1 ; i <= noOfAds ; i++){
                Boolean isPreRollAdplaying = isAdPlayingValidator.validate("", 20000);
                if (isPreRollAdplaying) {
                    logger.info("Checking initial volume for PrerollPodded Ad");
                    result = result && eventValidator.validate("willPlaySingleAd_"+i+"",50000);
                    result = result && volumeValidator.checkInitialVolume("ad");
                }else{
                    logger.error("PrerollPodded ad is not played");
                }
            }

            result = result && seekValidator.validate("seeked_1", 60000);

            result = result && eventValidator.validate("played_1", 60000);

        } catch (Exception e) {
            logger.error("Exception while checking  initial Volume tests "+ e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback initial Volume tests failed");
    }
}
