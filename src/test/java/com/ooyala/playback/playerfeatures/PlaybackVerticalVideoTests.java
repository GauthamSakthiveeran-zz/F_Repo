package com.ooyala.playback.playerfeatures;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AspectRatioValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/17/16.
 */
public class PlaybackVerticalVideoTests extends PlaybackWebTest {

    private static Logger logger = Logger
            .getLogger(PlaybackVerticalVideoTests.class);
    private PlayValidator play;
    private SeekValidator seek;
    private EventValidator eventValidator;
    private AspectRatioValidator aspectRatioValidator;

    public PlaybackVerticalVideoTests() throws OoyalaException {
        super();
    }

    @Test(groups = "playerFeatures", dataProvider = "testUrls")
    public void testVerticalVideo(String testName, UrlObject url)
            throws OoyalaException {

        boolean result = true;
        try {
            driver.get(url.getUrl());

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1", 60000);

            result = result && aspectRatioValidator.setVerticalVideo().validate("assetDimension_1", 60000);

            executeScript("pp.pause();");  // pausing video by js as for this test video screen size vertical.

            result = result && play.validate("playing_2", 60000);

            result = result && seek.validate("seeked_1", 60000);

            result = result
                    && aspectRatioValidator.setVerticalVideo().validate(
                    "assetDimension_1", 60000);

            result = result && eventValidator.validate("videoPlayed_1", 60000);

        } catch (Exception e) {
            logger.error(e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Vertical Video tests failed");
    }

}