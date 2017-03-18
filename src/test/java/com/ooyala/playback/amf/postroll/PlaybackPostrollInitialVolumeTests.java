package com.ooyala.playback.amf.postroll;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.IsAdPlayingValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.VolumeValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 28/2/17.
 */
public class PlaybackPostrollInitialVolumeTests extends PlaybackWebTest {
    private static Logger logger = Logger.getLogger(PlaybackPostrollInitialVolumeTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private VolumeValidator volumeValidator;
    private PlayAction playAction;
    private SeekValidator seekValidator;
    private IsAdPlayingValidator isAdPlayingValidator;

    PlaybackPostrollInitialVolumeTests() throws OoyalaException {
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

            result=result && seekValidator.validate("seeked_1", 30000);

            result=result && eventValidator.validate("willPlaySingleAd_1", 60000);

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

            result = result && eventValidator.validate("played_1", 60000);

        } catch (Exception e) {
            logger.error("Exception while checking  initial Volume tests "+ e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback initial Volume tests failed");
    }
}
