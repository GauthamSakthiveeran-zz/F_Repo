package com.ooyala.playback.amf.ima;

import com.ooyala.playback.page.OverlayValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPreRollOverlayPostRollAdTests extends PlaybackWebTest {

    public PlaybackPreRollOverlayPostRollAdTests() throws OoyalaException {
        super();
    }

    private EventValidator event;
    private PlayAction playAction;
    private PlayValidator playValidator;
    private SeekValidator seekValidator;
    private OverlayValidator overlayValidator;

    @Test(groups = {"amf", "preroll", "overlay", "postroll"}, dataProvider = "testUrls")
    public void verifyPrerollOverlayPostrollAd(String testDescription, UrlObject url) throws OoyalaException {
        boolean result = true;
        try {
            driver.get(url.getUrl());
            Assert.assertEquals(playValidator.waitForPage(), true, "failed to load the page properly");
            injectScript();
            Assert.assertEquals(playAction.startAction(), true, "failed to click on start button");
            Assert.assertEquals(event.validate("willPlayNonlinearAd_1", 1000), true, "Failed to validate 'willPlayNonlinearAd_1' event");
            Assert.assertEquals(overlayValidator.validateClickThrough("paused_1", 7000, url.getAdPlugins()), true, "Failed to validate 'paused_1' event");
            Assert.assertEquals(overlayValidator.validateOverlayRenderingEvent(6000), true, "Failed to validate 'overlay rendering' event");
            Assert.assertEquals(event.validate("videoPlaying_1", 90000), true, "Failed to validate 'videoPlaying_1' event");
            if (!getBrowser().equalsIgnoreCase("MicrosoftEdge"))
                Assert.assertEquals(seekValidator.validate("seeked_1", 6000), true, "Failed to validate 'seeked_1' event");
            Assert.assertEquals(event.validate("videoPlayed_1", 160000), true, "Failed to validate 'videoPlayed_1' event");
            Assert.assertEquals(event.validate("PostRoll_willPlaySingleAd_1", 90000), true, "Failed to validate 'PostRoll_willPlaySingleAd_1' event");
            Assert.assertEquals(event.validate("singleAdPlayed_1", 190000), true, "Failed to validate 'singleAdPlayed_1' event");
            Assert.assertEquals(event.validate("played_1", 200000), true, "Failed to validate 'played_1' event");
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Test failed");
    }
}
