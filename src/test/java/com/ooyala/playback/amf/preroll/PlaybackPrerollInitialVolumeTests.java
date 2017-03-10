package com.ooyala.playback.amf.preroll;

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
public class PlaybackPrerollInitialVolumeTests extends PlaybackWebTest {
    private static Logger logger = Logger.getLogger(PlaybackPrerollInitialVolumeTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private VolumeValidator volumeValidator;
    private PlayAction playAction;
    private SeekValidator seekValidator;
    private IsAdPlayingValidator isAdPlayingValidator;
    private SeekAction seekAction;

    PlaybackPrerollInitialVolumeTests() throws OoyalaException {
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

            result=result && eventValidator.validate("videoPlayingAd_1", 10000);

            driver.executeScript("pp.pause()");

            Boolean isPrerollAdplaying = isAdPlayingValidator.validate("", 20000);
            if (isPrerollAdplaying) {
                logger.info("Checking initial volume for Preroll Ad");
                result = result && volumeValidator.checkInitialVolume("ad",Double.parseDouble("0.5"));
            }else{
                logger.error("Preroll ad is not played");
            }

            driver.executeScript("pp.play()");

            result=result && eventValidator.validate("playing_1", 60000);

            result=result && volumeValidator.checkInitialVolume("video",Double.parseDouble("0.5"));

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
