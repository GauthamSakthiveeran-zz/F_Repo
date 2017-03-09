package com.ooyala.playback.VTC;

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
 * Created by snehal on 01/03/17.
 */
public class PlaybackBasicMidrollTests extends PlaybackWebTest {

    private static Logger logger = Logger.getLogger(PlaybackBasicMidrollTests.class);
    private PlayValidator play;
    private EventValidator eventValidator;
    private PlayAction playAction;
    private SeekValidator seek;
    private SeekAction seekAction;
    private ReplayValidator replayValidator;
    private PauseValidator pause;
    private IsAdPlayingValidator isAdPlaying;
    private PoddedAdValidator poddedAdValidator;

    public PlaybackBasicMidrollTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void basicPlaybackMidrollTests(String testName, String url)
            throws OoyalaException {

        boolean result = true;
        try {
            driver.get(url);

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1", 60000);

            result = result && seekAction.seek(10,true);

            if(testName.contains("IMA") || testName.contains("FW")){
                result = result && eventValidator.validate("adsPlayed_2", 60000);
                result = result && poddedAdValidator.setPosition("MidRoll").validate("countPoddedAds_2", 20000);
            }else {
                result = result && eventValidator.validate("adsPlayed_1", 60000);
                result = result && poddedAdValidator.setPosition("MidRoll").validate("countPoddedAds_1", 20000);
            }

            result = result && pause.validate("paused_1", 30000);

            result = result && eventValidator.validate("seeked_1", 30000);

            result = result && seekAction.seek("10");

            result = result && playAction.startAction();

            result = result && eventValidator.validate("seeked_2", 30000);

            if(isAdPlaying.validate("",1000)){
                result = false;
                extentTest.log(LogStatus.FAIL, "Ad is played after doing backward seek.");
            }

            result = result && seek.validate("seeked_3", 30000);

            result = result && eventValidator.validate("played_1", 60000);

            result = result && replayValidator.validate("replay_1", 30000);

            if(testName.contains("IMA") || testName.contains("FW")) {
                result = result && eventValidator.validate("adsPlayed_4", 60000);
            } else {
                result = result && eventValidator.validate("adsPlayed_2", 60000);
            }

        }catch (Exception e) {
            logger.error(e);
            result = false;
            extentTest.log(LogStatus.FAIL, "Basic Playback Midroll test failed", e);
        }
        Assert.assertTrue(result, "Basic Playback Midroll test failed");

    }
}
