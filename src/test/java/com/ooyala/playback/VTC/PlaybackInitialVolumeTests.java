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
        String[] parts= testName.split(":");
        String adType = parts[2].trim();
        double volumeValue = Double.parseDouble(parts[3]);

        boolean result = true;
        try {
            driver.get(url);

            result = result && play.waitForPage();

            injectScript();

            result = result && playAction.startAction();

            Boolean isPrerollAdplaying = isAdPlayingValidator.validate("", 20000);

            if (isPrerollAdplaying) {
                logger.info("Checking initial volume for Preroll Ad");
                result = result && volumeValidator.checkInitialVolume("ad",volumeValue);
            }else{
                logger.error("Preroll ad is not played");
            }

            result=result && eventValidator.validate("playing_1", 60000);

            result=result && volumeValidator.checkInitialVolume("video",volumeValue);

            if(adType.equalsIgnoreCase("midroll")){
                result=result && eventValidator.validate("videoWillPlay_ads", 60000);
                Thread.sleep(1000);
                Boolean isMidrollAdplaying = isAdPlayingValidator.validate("", 20000);
                if (isMidrollAdplaying) {
                    logger.info("Checking initial volume for Midroll Ad");
                    result = result && volumeValidator.checkInitialVolume("ad",volumeValue);
                    result=result && eventValidator.validate("playing_2", 60000);
                }else{
                    logger.error("Midroll ad is not played");
                }
            }

            result=result && seekValidator.validate("seeked_1",20000);

            if(adType.equalsIgnoreCase("postroll")){
                result=result && eventValidator.validate("videoWillPlay_ads", 60000);
                Thread.sleep(1000);
                Boolean isPostrollAdplaying = isAdPlayingValidator.validate("", 20000);
                if (isPostrollAdplaying) {
                    logger.info("Checking initial volume for Postroll Ad");
                    result = result && volumeValidator.checkInitialVolume("ad",volumeValue);
                    result=result && eventValidator.validate("playing_2", 60000);
                }else{
                    logger.error("Postroll ad is not played");
                }
            }

            result = result && eventValidator.validate("played_1", 60000);

        } catch (Exception e) {
            logger.error("Exception while checking  initial Volume tests  "+e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback initial Volume tests failed");
    }
}
