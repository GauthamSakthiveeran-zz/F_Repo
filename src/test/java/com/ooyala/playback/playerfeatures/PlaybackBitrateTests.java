package com.ooyala.playback.playerfeatures;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * Created by soundarya on 11/17/16.
 */
public class PlaybackBitrateTests extends PlaybackWebTest {

    private static Logger logger = Logger.getLogger(PlaybackBitrateTests.class);
    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;
    private EventValidator eventValidator;
    private Bitratevalidator bitratevalidator;
    private PlayAction playAction;

    public PlaybackBitrateTests() throws OoyalaException {
        super();
    }

    @Test(groups = "playerFeatures", dataProvider = "testUrls")
    public void testBitrate(String testName, UrlObject url) throws OoyalaException {

        boolean result = true;

        try {
            driver.get(url.getUrl());

            result = result && play.clearCache();

            result = result && play.waitForPage();

            injectScript();

            result = result && playAction.startAction();

            result = result && eventValidator.loadingSpinner();

            result = result && eventValidator.validate("playing_1", 60000);

            result = result && pause.validate("paused_1", 60000);

            result = result && bitratevalidator.validate("", 60000);

            result = result && seek.validate("seeked_1", 60000);

            result = result && eventValidator.validate("videoPlayed_1", 60000);

        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback bitrate/Quality tests failed" + testName);

    }

}
