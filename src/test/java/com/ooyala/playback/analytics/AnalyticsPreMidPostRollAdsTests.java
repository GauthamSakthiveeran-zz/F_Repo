package com.ooyala.playback.analytics;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AnalyticsValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by suraj on 6/30/17.
 */
public class AnalyticsPreMidPostRollAdsTests extends PlaybackWebTest {
    public AnalyticsPreMidPostRollAdsTests() throws OoyalaException {
        super();
    }

    private EventValidator event;
    private PlayAction playAction;
    private PlayValidator playValidator;
    private SeekAction seekAction;
    private AnalyticsValidator analyticsValidator;

    @Test(groups = {"amf", "preroll", "midroll", "postroll"}, dataProvider = "testUrls")
    public void verifyPreMidPostroll(String testName, UrlObject url) throws OoyalaException {
        boolean result = true;
        try {
            driver.get(url.getUrl());
            result = result && playValidator.waitForPage();
            injectScript();
            boolean isPulse = event.isAdPluginPresent("pulse");
            result = result && playAction.startAction();
            //validate preroll
            result = result && event.validate("PreRoll_willPlaySingleAd_1", 30000);

            result = result && event.validateSingleAdPlayedEvent(1);

            result = result && event.validate("playing_1", 150000);

            //validate midroll
            result = result && event.validateSingleAdPlayedEvent(2);

            executeScript("pp.skipAd()");

            result = result && seekAction.seekTillEnd().startAction();

            //validate postroll
            if (isPulse) {
                result = result && analyticsValidator.validate("analytics_ad_started_6", 10000);
                result = result && analyticsValidator.validate("analytics_ad_ended_6", 10000);
                result = result && event.validate("seeked_1", 60000);
            } else {
                result = result && analyticsValidator.validate("analytics_ad_started_3", 10000);
                result = result && analyticsValidator.validate("analytics_ad_ended_3", 10000);
            }
            result = result && event.validate("played_1", 200000);
        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Analytics Pre Mid Post Roll Ads failed.");
    }
}
