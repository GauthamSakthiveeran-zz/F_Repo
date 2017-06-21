package com.ooyala.playback.playerfeatures;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.UpNextValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

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

    @Test(groups = "playerFeatures", dataProvider = "testUrls")
    public void testDiscoveryUpNext(String testName, UrlObject url)
            throws OoyalaException {
        boolean result = true;

        try {
            driver.get(url.getUrl());

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1", 60000);

            String embedCode = driver.executeScript("return pp.getEmbedCode();").toString();

            result = result && discoveryValidator.clickOnDiscoveryButton();

            result = result && discoveryValidator.validateImageStyle();

            result = result && eventValidator.validate("playing_2", 60000);

            result = result && discoveryValidator.verifyPlayingAssetEmbedCode(embedCode);

            result = result && seekAction.setTime(20).fromLast().startAction();

            result = result && eventValidator.validate("played_1", 60000);

        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback Discovery Asset tests failed");
    }
}
