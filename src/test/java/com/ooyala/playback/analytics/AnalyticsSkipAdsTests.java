package com.ooyala.playback.analytics;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

/**
 * Created by suraj on 6/28/17.
 */
public class AnalyticsSkipAdsTests extends PlaybackWebTest {

    public AnalyticsSkipAdsTests() throws OoyalaException {
    }

    protected Logger logger = Logger.getLogger(AnalyticsSkipAdsTests.class);
    private EventValidator event;
    private PlayAction playAction;
    private PlayValidator playValidator;
    private SeekValidator seek;
    private AnalyticsValidator analyticsValidator;
    private AdSkipButtonValidator skipButtonValidator;

    @Test(groups = { "amf", "preroll",}, dataProvider = "testUrls")
    public void verifyPreroll(String testName, UrlObject url) throws Exception {
        boolean result = true;

        try {
            driver.get(url.getUrl());

            result = result && playValidator.waitForPage();

            injectScript();

            result = result && playAction.startAction();
            result = result && event.validate("willPlaySingleAd_1", 150000);

            if (!event.isAdPluginPresent("ima")) // Unable to click skip ad button for IMA

                result = result && skipButtonValidator.validate("", 120000);
            else
                result = result && event.validate("showAdSkipButton_1", 150000);

            result = result && event.validate("singleAdPlayed_1", 150000);

            if (event.isAdPluginPresent("pulse"))
                result = result && event.validate("singleAdPlayed_2", 60000);

            result = result && analyticsValidator.validate("analytics_ad_skipped_1",10000);

            result = result && event.validate("playing_1", 150000);

            result = result && seek.validate("seeked_1", 150000);
            result = result && event.validate("played_1", 200000);
        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        s_assert.assertTrue(result, "PreRoll");
        s_assert.assertAll();
    }
}
