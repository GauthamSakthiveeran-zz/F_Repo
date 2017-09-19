package com.ooyala.playback.analytics;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.AutoplayAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AnalyticsIMATests extends PlaybackWebTest {
    public AnalyticsIMATests() throws OoyalaException {
        super();
    }
    private static Logger logger = Logger.getLogger(AnalyticsBasicTests.class);

    private PlayValidator play;
    private AnalyticsValidator analytics;
    private EventValidator event;
    private PlayAction playAction;

    @Test(groups = "analytics", dataProvider = "testUrls")
    public void testIMAAnalytics(String testName, UrlObject url)
            throws OoyalaException {

        boolean result = true;

        try {
            driver.get(url.getUrl());
            result = result && play.waitForPage();
            injectScript();
            result = result && analytics.injectIQAnalyticsLogRecorder();
            result = result && playAction.startAction();
            result = result && event.validate("PreRoll_willPlaySingleAd_1", 30000);
            result = result && event.validate("singleAdPlayed_1", 120000);
            result = result && event.validate("playing_1",10000);
            result = result && analytics.validateIQAnalyticsLogs();
            result = result && analytics.injectNetworkLogRecorder();
            result = result && analytics.validateAnalyticsNetworkLogs();
        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback autoplay analytics tests failed");
    }
}
