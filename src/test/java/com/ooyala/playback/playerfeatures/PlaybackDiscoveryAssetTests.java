package com.ooyala.playback.playerfeatures;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayerAPIAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by suraj on 6/21/17.
 */
public class PlaybackDiscoveryAssetTests extends PlaybackWebTest {

    public PlaybackDiscoveryAssetTests() throws OoyalaException {
    }

    private PlayValidator play;
    private DiscoveryValidator discoveryValidator;
    private SeekAction seekAction;
    private EventValidator eventValidator;
    private PlayerAPIAction playerAPI;

    @Test(groups = "playerFeatures", dataProvider = "testUrls")
    public void testPlaybackOfDiscoveryAssets(String testName, UrlObject url)
            throws OoyalaException {
        boolean result = true;

        try {
            driver.get(url.getUrl());

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1", 60000);

            String embedCode = playerAPI.getEmbedCode();

            result = result && discoveryValidator.clickOnDiscoveryButton();

            result = result && discoveryValidator.validateImageStyle();

            result = result && eventValidator.validate("playing_2", 60000);

            result = result && discoveryValidator.verifyPlayingAssetEmbedCode(embedCode);

            result = result && seekAction.setTime(20).fromLast().startAction();

            result = result && eventValidator.skipScrubberValidation().validate("played_1", 60000);

        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback Discovery Asset tests failed");
    }
}
