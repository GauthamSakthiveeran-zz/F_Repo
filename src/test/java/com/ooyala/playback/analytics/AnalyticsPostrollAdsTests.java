package com.ooyala.playback.analytics;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.SetEmbedCodeValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by suraj on 6/30/17.
 */
public class AnalyticsPostrollAdsTests extends PlaybackWebTest{
    public AnalyticsPostrollAdsTests() throws OoyalaException {
        super();
    }

    protected Logger logger = Logger.getLogger(AnalyticsPostrollAdsTests.class);
    private EventValidator event;
    private PlayValidator playValidator;
    private SeekValidator seekValidator;
    private SetEmbedCodeValidator setEmbedCodeValidator;

    @Test(groups = {"amf", "postroll"}, dataProvider = "testUrls")
    public void verifyPostroll(String testName, UrlObject url) {

        boolean result = true;

        try {

            driver.get(url.getUrl());

            result = result && playValidator.waitForPage();

            injectScript();

            result = result && playValidator.validate("playing_1", 60000);

            result = result && seekValidator.validate("seeked_1", 10000);

            result = result && event.validate("played_1", 200000);

            result = result && event.validate("PostRoll_willPlaySingleAd_1", 90000);

            result = result && event.validate("singleAdPlayed_1", 190000);

        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL,e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Analytics Postroll Ads Tests failed");
    }
}
