package com.ooyala.playback.amf.postroll;

import com.ooyala.playback.page.*;
import com.ooyala.playback.url.UrlObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackPostRollPoddedAdsTests extends PlaybackWebTest {

    public PlaybackPostRollPoddedAdsTests() throws OoyalaException {
        super();
    }

    private EventValidator event;
    private PlayValidator playValidator;
    private SeekValidator seekValidator;
    private PoddedAdValidator poddedAdValidator;
    private SetEmbedCodeValidator setEmbedCodeValidator;
    private AdClickThroughValidator clickthrough;

    @Test(groups = {"amf", "postroll", "podded"}, dataProvider = "testUrls")
    public void verifyPostrollPodded(String testName, UrlObject url) throws OoyalaException {
        boolean result = true;
        boolean click = testName.contains("Clickthrough");
        try {
            driver.get(url.getUrl());
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playValidator.validate("playing_1", 60000);
            result = result && seekValidator.validate("seeked_1", 60000);
            if (result && click){
                s_assert.assertTrue(clickthrough.validateClickThroughForPoddedAds("postroll"),"Clickthrough");
            }
            result = result && seekValidator.validate("videoPlayed_1", 60000);
            result = result && poddedAdValidator.setPosition("PostRoll").validate("countPoddedAds", 120000);
            result = result && event.validate("played_1", 60000);
            if (testName.contains("SetEmbedCode")) {
                result = result && setEmbedCodeValidator.validate("", 6000);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = false;
            extentTest.log(LogStatus.FAIL, e);
        }
        s_assert.assertTrue(result, "Tests failed");
        s_assert.assertAll();
    }
}
