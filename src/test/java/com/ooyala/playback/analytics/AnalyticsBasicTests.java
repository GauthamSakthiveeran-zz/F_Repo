package com.ooyala.playback.analytics;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by suraj on 6/26/17.
 */
public class AnalyticsBasicTests extends PlaybackWebTest {

    private static Logger logger = Logger.getLogger(AnalyticsBasicTests.class);
    private PlayValidator play;
    private SeekValidator seek;
    private EventValidator eventValidator;
    private PauseValidator pause;
    private EndScreenValidator endScreenValidator;
    private StartScreenValidator startScreenValidator;
    private AnalyticsValidator analyticsValidator;
    private VolumeValidator volumeValidator;
    private FullScreenValidator fullScreenValidator;

    public AnalyticsBasicTests() throws OoyalaException {
        super();
    }

    @Test(groups = "analytics", dataProvider = "testUrls")
    public void testAnalyticsBasic(String testName, UrlObject url)
            throws OoyalaException {

        boolean result = true;

        try {
            driver.get(url.getUrl());

            result = result && play.waitForPage();

            result = result && startScreenValidator.validateMetadata(url);

            injectScript();

            result = result && play.validate("playing_1", 60000);

            result = result && eventValidator.validate("buffering_1",10000);

            result = result && eventValidator.playVideoForSometime(2);

            result = result && pause.validate("paused_1", 60000);

            result = result && play.validate("playing_2", 60000);

            result = result && volumeValidator.validate("", 60000);

            result = result && eventValidator.validate("volumeChanged_2",10000);

            result = result && seek.validate("seeked_1", 60000);

            result = result && eventValidator.validate("seeked_1",10000);

            result = result && fullScreenValidator.validate("",10000);

            result = result && analyticsValidator.validate("analytics_fullscreen_changed_2",10000);

            result = result && eventValidator.validate("played_1", 60000);

        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "analytics basic tests failed");
    }
}
