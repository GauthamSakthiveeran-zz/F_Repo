package com.ooyala.playback.adhoc;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.ReplayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by suraj on 8/9/17.
 */
public class CheckPlaybackAfterWaitOnEndScreenAndReplayTests extends PlaybackWebTest {
    public CheckPlaybackAfterWaitOnEndScreenAndReplayTests() throws OoyalaException {
        super();
    }
    private static Logger logger = Logger.getLogger(CheckPlaybackAfterWaitOnEndScreenAndReplayTests.class);
    private PlayValidator playValidator;
    private PlayAction playAction;
    private EventValidator eventValidator;
    private SeekValidator seekValidator;
    private ReplayValidator replayValidator;

    @Test(groups = "EncodingPriority", dataProvider = "testUrls")
    public void testOverrideEncodingPriorities(String testName, UrlObject url) {

        boolean result = true;

        try {

            driver.get(url.getUrl());

            result = result && playValidator.waitForPage();

            injectScript();

            result = result && playAction.startAction();

            result = result && eventValidator.validate("playing_1", 10000);

            result = result && seekValidator.validate("seeked_1", 60000);

            result = result && eventValidator.validate("played_1",60000);

            Thread.sleep(18000);

            result = result && replayValidator.validate("replay_1",60000);

            result = result && eventValidator.validate("playing_4", 60000);

        } catch (Exception e) {
            logger.error("Exception while checking CheckPlaybackAfterWaitOnEndScreenAndReplayTests  " + e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "OverrideEncoding Priority test failed");
    }
}
