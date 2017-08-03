package com.ooyala.playback.amf.postroll;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PlaybackPostRollAdsClickthroughTests extends PlaybackWebTest {

    public PlaybackPostRollAdsClickthroughTests() throws OoyalaException {
        super();
    }

    private EventValidator event;
    private PlayValidator playValidator;
    private SeekValidator seekValidator;
    private SetEmbedCodeValidator setEmbedCodeValidator;
    private AdClickThroughValidator clickThrough;

    @Test(groups = {"amf", "postroll","clickthrough"}, dataProvider = "testUrls")
    public void verifyPostroll(String testName, UrlObject url) {

        boolean result = true;

        try {

            driver.get(url.getUrl());

            result = result && playValidator.waitForPage();

            injectScript();

            result = result && playValidator.validate("playing_1", 60000);

            result = result && seekValidator.validate("seeked_1", 10000);

            result = result && event.validate("PostRoll_willPlaySingleAd_1", 90000);

            result = result && clickThrough.validate("", 120000);

            result = result && event.validate("singleAdPlayed_1", 190000);

        } catch (Exception e) {
            logger.error(e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Tests failed");
    }
}
