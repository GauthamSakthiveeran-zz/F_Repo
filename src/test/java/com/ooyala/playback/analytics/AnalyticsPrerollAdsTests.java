package com.ooyala.playback.analytics;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.annotations.Test;

/**
 * Created by suraj on 6/28/17.
 */
public class AnalyticsPrerollAdsTests extends PlaybackWebTest {

    public AnalyticsPrerollAdsTests() throws OoyalaException {
        super();
    }

    private EventValidator event;
    private PlayAction playAction;
    private PlayValidator playValidator;
    private SeekValidator seek;

    @Test(groups = { "amf", "preroll",}, dataProvider = "testUrls")
    public void verifyPreroll(String testName, UrlObject url) throws Exception {
        boolean result = true;

        try {
            driver.get(url.getUrl());
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playAction.startAction();
            result = result && event.validate("PreRoll_willPlaySingleAd_1", 30000);

            if (event.isAdPluginPresent("pulse"))
                result = result && event.validate("singleAdPlayed_2", 120000);
            else
                result = result && event.validate("singleAdPlayed_1", 120000);

            result = result && event.validate("playing_1", 35000);
            result = result && event.loadingSpinner();

            result = result && seek.validate("seeked_1", 60000);

            result = result && event.validate("seeked_1",10000);

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
