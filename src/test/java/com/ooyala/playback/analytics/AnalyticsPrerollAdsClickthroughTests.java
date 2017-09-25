package com.ooyala.playback.analytics;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdClickThroughValidator;
import com.ooyala.playback.page.AnalyticsValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.PlayerAPIAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by suraj on 6/29/17.
 */
public class AnalyticsPrerollAdsClickthroughTests extends PlaybackWebTest {

    public AnalyticsPrerollAdsClickthroughTests() throws OoyalaException {
        super();
    }

    protected Logger logger = Logger.getLogger(AnalyticsPrerollAdsClickthroughTests.class);
    private EventValidator event;
    private PlayAction playAction;
    private PlayValidator playValidator;
    private SeekAction seekAction;
    private AdClickThroughValidator clickThrough;
    private AnalyticsValidator analyticsValidator;
    private PlayerAPIAction playerAPI;

    @Test(groups = { "amf", "preroll", "sequential", "clickThrough" }, dataProvider = "testUrls")
    public void verifyPreroll(String testName, UrlObject url) throws Exception {
        boolean result = true;

        try {
            driver.get(url.getUrl());
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playAction.startAction();
            result = result && event.validate("PreRoll_willPlaySingleAd_1", 60000);

            s_assert.assertTrue(clickThrough.setUrlObject(url).validate("", 120000), "Clickthrough");

            if (event.isAdPluginPresent("pulse"))
                result = result && event.validate("singleAdPlayed_2", 120000);
            else
                result = result && event.validate("singleAdPlayed_1", 120000);

            result = result && analyticsValidator.validate("analytics_video_requested_paused_1",10000);

            result = result && event.validate("playing_1", 35000);

            result = result && seekAction.seekTillEnd().startAction();
            result = result && event.validate("seeked_1", 120000);
            result = result && event.validate("played_1", 200000);
        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e);
            result = false;
        }
        s_assert.assertTrue(result, "PreRoll AdsClickthrough tests");
        s_assert.assertAll();
    }
}
