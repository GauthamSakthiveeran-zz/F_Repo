package com.ooyala.playback.amf;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.IsAdPlayingValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.ReplayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by snehal on 01/03/17.
 */
public class PlaybackBasicSeekAndReplayTests extends PlaybackWebTest {

    private static Logger logger = Logger.getLogger(PlaybackBasicSeekAndReplayTests.class);
    private PlayValidator play;
    private EventValidator eventValidator;
    private SeekValidator seek;
    private SeekAction seekAction;
    private ReplayValidator replayValidator;
    private IsAdPlayingValidator isAdPlaying;
    private PoddedAdValidator poddedAdValidator;

    public PlaybackBasicSeekAndReplayTests() throws OoyalaException {
        super();
    }

	@Test(groups = { "amf", "premidpost", "replay" }, dataProvider = "testUrls")
    public void testBasicSeekAndReplay(String testName, UrlObject url)
            throws OoyalaException {

        String[] parts= testName.split("-")[1].trim().split(":");
        String adPosition = parts[0].trim();

        boolean result = true;
        try {
            driver.get(url.getUrl());

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1", 60000);

            result = result && seekAction.seek(10,true);

            if(!testName.contains("PreRoll") && (eventValidator.isAdPluginPresent("ima") || eventValidator.isAdPluginPresent("freewheel"))){
                result = result && eventValidator.validate("adsPlayed_2", 60000);
                result = result && poddedAdValidator.setPosition(adPosition).validate("countPoddedAds_2", 20000);
            }else {
                result = result && eventValidator.validate("adsPlayed_1", 60000);
                result = result && poddedAdValidator.setPosition(adPosition).validate("countPoddedAds_1", 20000);
            }
            
            result = result && eventValidator.validate("playing_2", 60000);

            result = result && eventValidator.validate("seeked_1", 30000);

            result = result && seekAction.seek("10");

            result = result && eventValidator.validate("seeked_2", 30000);

            if(isAdPlaying.validate("",1000)){
                result = false;
                extentTest.log(LogStatus.FAIL, "Ad is played after doing backward seek.");
            }

            result = result && seek.validate("seeked_3", 30000);

            result = result && eventValidator.validate("played_1", 60000);

            result = result && replayValidator.validate("replay_1", 30000);

            if(!testName.contains("PreRoll") && (eventValidator.isAdPluginPresent("ima") || eventValidator.isAdPluginPresent("freewheel"))){
                result = result && eventValidator.validate("adsPlayed_4", 60000);
            } else {
                result = result && eventValidator.validate("adsPlayed_2", 60000);
            }

        }catch (Exception e) {
            logger.error(e);
            result = false;
            extentTest.log(LogStatus.FAIL, e);
        }
        Assert.assertTrue(result, "Basic Playback Midroll test failed");

    }
}
