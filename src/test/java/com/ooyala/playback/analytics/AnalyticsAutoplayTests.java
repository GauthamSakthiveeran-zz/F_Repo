package com.ooyala.playback.analytics;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.AutoplayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by suraj on 6/27/17.
 */
public class PlaybackAutoplayAnalyticsTests extends PlaybackWebTest {
    public PlaybackAutoplayAnalyticsTests() throws OoyalaException {
        super();
    }
    private static Logger logger = Logger.getLogger(PlaybackAnalyticsBasicTests.class);
    private PlayValidator play;
    private SeekValidator seek;
    private EventValidator eventValidator;
    private PauseValidator pause;
    private EndScreenValidator endScreenValidator;
    private StartScreenValidator startScreenValidator;
    private AnalyticsValidator analyticsValidator;
    private VolumeValidator volumeValidator;
    private FullScreenValidator fullScreenValidator;
    private AutoplayAction autoplayAction;

    @Test(groups = "analytics", dataProvider = "testUrls")
    public void testAutoplayAnalytics(String testName, UrlObject url)
            throws OoyalaException {

        boolean result = true;

        try {
            driver.get(url.getUrl());

            result = result && eventValidator.isPageLoaded();

            injectScript();

            result = result && eventValidator.loadingSpinner();

            result = result && autoplayAction.startAction();

            result = result && eventValidator.playVideoForSometime(2);

            result = result && pause.validate("paused_1", 30000);

            result = result && play.validate("playing_2", 30000);

            result = result && volumeValidator.validate("", 60000);

            result = result && eventValidator.validate("volumeChanged_2",10000);

            result = result && seek.validate("seeked_1", 30000);

            result = result && eventValidator.validate("seeked_1",10000);

            result = result && eventValidator.validate("played_1", 60000);

        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback autoplay analytics tests failed");
    }
}
