package com.ooyala.playback.amf.postroll;

import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdClickThroughValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPostRollAdsTests extends PlaybackWebTest {

    public PlaybackPostRollAdsTests() throws OoyalaException {
        super();
    }

    private EventValidator event;
    private PlayValidator playValidator;
    private AdClickThroughValidator clickThroughValidator;
    private SeekAction seek;

    @Test(groups = {"amf", "postroll"}, dataProvider = "testUrls")
    public void verifyPostroll(String testName, UrlObject url) {

        boolean result = true;
        boolean clickthrough = !clickThroughValidator.ignoreClickThrough(url);

        try {

            driver.get(url.getUrl());

            result = result && playValidator.waitForPage();

            injectScript();

            result = result && playValidator.validate("playing_1", 60000);
            
            result = result && seek.fromLast().setTime(2).startAction();

            result = result && event.validate("seeked_1", 10000);

            result = result && event.validate("PostRoll_willPlaySingleAd_1", 60000);

            if (result && clickthrough){
                s_assert.assertTrue(clickThroughValidator.validate("videoPausedAds_1", 120000), "Postroll");
            }

            result = result && event.validate("singleAdPlayed_1", 190000);

            result = result && event.validate("played_1", 200000);

        } catch (Exception e) {
            logger.error(e.getMessage());
            result = false;
        }
        s_assert.assertTrue(result, "Postroll");
        s_assert.assertAll();
    }
    
}