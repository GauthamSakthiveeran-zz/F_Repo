package com.ooyala.playback.analytics;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by suraj on 6/30/17.
 */
public class AnalyticsMidrollAdsTests extends PlaybackWebTest {
    public AnalyticsMidrollAdsTests() throws OoyalaException {
        super();
    }

    protected Logger logger = Logger.getLogger(AnalyticsMidrollAdsTests.class);
    private EventValidator event;
    private PlayValidator playValidator;
    private SeekValidator seekValidator;
    private MidrollAdValidator midrollAdValidator;
    private SeekAction seekAction;
    private AnalyticsValidator analyticsValidator;

    @Test(groups = {"amf", "midroll"}, dataProvider = "testUrls")
    public void verifyAnalyticsMidRoll(String testName, UrlObject url) throws OoyalaException {

        boolean result = true;

        try {
            driver.get(url.getUrl());
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playValidator.validate("playing_1", 60000);
            result = result && midrollAdValidator.validateMidrollAd(url);
            result = result && analyticsValidator.validate("analytics_ad_started_1",10000);
            result = result && analyticsValidator.validate("analytics_ad_ended_1",10000);
            result = result && event.validate("playing_2", 160000);
            result = result && seekValidator.validate("seeked_1", 160000);
            result = result && event.validate("played_1", 160000);

        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e);
            result = false;
        }
        Assert.assertTrue(result, "Verified");
    }
}
