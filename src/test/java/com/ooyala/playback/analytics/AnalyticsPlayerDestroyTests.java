package com.ooyala.playback.analytics;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AnalyticsValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by suraj on 6/29/17.
 */
public class AnalyticsPlayerDestroyTests extends PlaybackWebTest {

    public AnalyticsPlayerDestroyTests() throws OoyalaException {
        super();
    }

    private PlayValidator play;
    private AnalyticsValidator analyticsValidator;
    protected Logger logger = Logger.getLogger(AnalyticsPlayerDestroyTests.class);

    @Test(groups = "playerLifecycle", dataProvider = "testUrls")
    public void testPlayerDestroy(String testName, UrlObject url)
            throws OoyalaException {

        boolean result = true;

        try {
            driver.get(url.getUrl());

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1", 60000);

            executeScript("pp.destroy();");

            result = result && analyticsValidator.validate("destroy", 50000);

        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Analytics Player Destroy tests failed");
    }
}
