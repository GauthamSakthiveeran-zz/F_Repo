package com.ooyala.playback.adhoc;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.annotations.Test;

/**
 * Created by suraj on 11/17/17.
 */
public class InitialTimeSetToXSecondsBeforeMidrollAdPlaysTests extends PlaybackWebTest {
    public InitialTimeSetToXSecondsBeforeMidrollAdPlaysTests() throws OoyalaException {
    }

    private EventValidator event;
    private PlayValidator playValidator;
    private PlayAction playAction;
    private PoddedAdValidator poddedAdValidator;
    private AdClickThroughValidator clickthrough;
    private InitialTimeValidator initialTimeValidator;
    private MidrollAdValidator adStartTimeValidator;
    private SeekAction seekAction;
    private ReplayValidator replayValidator;

    @Test(groups = { "amf", "preroll", "podded" }, dataProvider = "testUrls")
    public void verifyInitialTimeAndPoddedAds(String testName, UrlObject url) throws OoyalaException {
        boolean result = true;
        try {
            driver.get(url.getUrl());
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playAction.startAction();
            result = result && event.validate("PreRoll_willPlayAds", 5000);
            result = result && event.validate("adsPlayed_1", 180000);
            result = result && poddedAdValidator.setPosition("PreRoll").validate("countPoddedAds_1", 120000);
            result = result && event.validate("playing_1", 15000);
            result = result && initialTimeValidator.validatePlayHeadTime();
            result = result
                    && adStartTimeValidator.setTime(url.getAdStartTime()).validateAdStartTime("MidRoll_willPlayAds");
            result = result && event.validate("adsPlayed_2", 600000);
            result = result && poddedAdValidator.setPosition("MidRoll").validate("countPoddedAds_2", 600000);
            result = result && seekAction.seekTillEnd().startAction();
            result = result && event.validate("PostRoll_willPlayAds", 200000);
            result = result && event.validate("adsPlayed_3", 600000);
            result = result && poddedAdValidator.setPosition("PostRoll").validate("countPoddedAds_3", 600000);
            result = result && event.skipScrubberValidation().validate("played_1", 180000);
            result = result && replayValidator.validate("replay_1", 30000);
            result = result && event.validate("PreRoll_willPlayAds_OnReplay", 5000);
            result = result && event.validate("playing_4", 150000);
            result = result && initialTimeValidator.validatePlayHeadTimeAtStart();
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = false;
        }
        s_assert.assertTrue(result, "Test failed");
        s_assert.assertAll();
    }
}
