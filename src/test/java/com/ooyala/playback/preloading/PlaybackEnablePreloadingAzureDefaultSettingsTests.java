package com.ooyala.playback.preloading;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PreloadingValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by suraj on 9/19/17.
 */
public class PlaybackEnablePreloadingAzureDefaultSettingsTests extends PlaybackWebTest{
    public PlaybackEnablePreloadingAzureDefaultSettingsTests() throws OoyalaException {
    }
    private static final Logger logger = Logger.getLogger(PlaybackEnablePreloadingForPrerollTests.class);
    private EventValidator eventValidator;
    private PlayAction playAction;
    private PreloadingValidator preloadingValidator;

    @Test(dataProvider = "testUrls")
    public void enablePreloadingAzureDefaultSettingsTest(String testName, UrlObject url) throws OoyalaException {
        boolean result = true;
        try {
            driver.get(url.getUrl());
            result = result && eventValidator.isPageLoaded();
            injectScript();
            preloadingValidator.getConsoleLogs();
            result = result && playAction.startAction();
            result = result && preloadingValidator.validate("segments_1", 10000);
            result = result && preloadingValidator.validate("downloads_2",15000);
            result = result && eventValidator.validate("playing_1",5000);
            result = result && preloadingValidator.verifyFragementsAndm3u8Segments();
        } catch (Exception ex) {
            ex.printStackTrace();
            extentTest.log(LogStatus.FAIL, ex.getMessage());
            logger.error(ex.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback Enable Preloading Azure Default Settings Tests failed");
    }
}