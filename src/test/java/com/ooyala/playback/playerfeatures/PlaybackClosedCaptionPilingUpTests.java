package com.ooyala.playback.playerfeatures;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by suraj on 7/12/17.
 */
public class PlaybackClosedCaptionPilingUpTests extends PlaybackWebTest {

    public PlaybackClosedCaptionPilingUpTests() throws OoyalaException {
    }

    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;
    private EventValidator eventValidator;
    private CCValidator ccValidator;

    @Test(groups = "playerFeatures", dataProvider = "testUrls")
    public void testClosedCaption(String testName, UrlObject url)
            throws OoyalaException {

        boolean result = true;

        try {
            driver.get(url.getUrl());

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1", 60000);

            result = result && eventValidator.playVideoForSometime(12);

            result = result && ccValidator.verifyDiscoveryTextShown("ccShowingText_");

            result = result && seek.validate("seeked_1", 60000);

            result = result && eventValidator.validate("played_1", 60000);

        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, "Playback Closed Caption piling up tests failed", e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Closed Caption piling up tests failed");
    }
}
