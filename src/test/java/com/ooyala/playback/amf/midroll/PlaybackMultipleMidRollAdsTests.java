package com.ooyala.playback.amf.midroll;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.MidrollAdValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackMultipleMidRollAdsTests extends PlaybackWebTest {

    public PlaybackMultipleMidRollAdsTests() throws OoyalaException {
        super();
    }

    private EventValidator event;
    private PlayValidator playValidator;
    private SeekValidator seek;
    private MidrollAdValidator midrollValidator;
    PlayAction playAction;

    @Test(groups = {"amf", "midroll"}, dataProvider = "testUrls")
    public void verifyMultipleMidroll(String testName, UrlObject url) throws OoyalaException {
        boolean result = true;
        try {
            driver.get(url.getUrl());
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playValidator.validate("playing_1", 60000);
            result = result && midrollValidator.validateMultipleMidrollAdStartTime(url, testName);
            result = result && event.loadingSpinner();
            Thread.sleep(3000);
            result = result && seek.validate("seeked_1", 10000);
            result = result && event.validate("played_1", 200000);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = false;
            extentTest.log(LogStatus.FAIL, e);
        }
        Assert.assertTrue(result, "Verified Multiple MidRoll Ads");
    }
}
