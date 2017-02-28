package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 28/2/17.
 */
public class PlaybackInitialVolumeTests extends PlaybackWebTest {
    private static Logger logger = Logger.getLogger(PlaybackInitialVolumeTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private VolumeValidator volumeValidator;
    private PlayAction playAction;
    private SeekValidator seekValidator;
    private IsAdPlayingValidator isAdPlayingValidator;

    PlaybackInitialVolumeTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testInitialVolumeVTC(String testName, String url)
            throws OoyalaException {

        logger.info("Url is : " + url);

        boolean result = true;
        try {
            driver.get(url);

            result=result && play.waitForPage();

            injectScript();

            result=result && playAction.startAction();

            Boolean isAdplaying = isAdPlayingValidator.validate("", 20000);

            if (isAdplaying) {
                logger.info("Checking initial volume for Ad");
                result = result && volumeValidator.checkInitialVolume("ad");
            }

            result=result && eventValidator.validate("playing_1", 60000);

            result=result && volumeValidator.checkInitialVolume("video");

            result=result && seekValidator.validate("seeked_1",20000);

            result = result && eventValidator.validate("played_1", 60000);

        } catch (Exception e) {
            logger.error("Exception while checking  initial Volume tests  "+e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback initial Volume tests failed");
    }
}
