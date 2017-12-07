package com.ooyala.playback.contentProtection;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.ErrorDescriptionValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by suraj on 12/4/17.
 */
public class PlaybackFutureTimeTests extends PlaybackWebTest {
    public PlaybackFutureTimeTests() throws OoyalaException {
    }

    private static Logger logger = Logger.getLogger(PlaybackFutureTimeTests.class);
    private ErrorDescriptionValidator error;
    private EventValidator event;

    @Test(groups = "syndicationRules", dataProvider = "testUrls")
    public void testFlightTime(String testName, UrlObject url) {
        boolean result = true;
        String language = url.getDescription().split("-")[1].toString();
        try {
            driver.get(url.getUrl());
            result = result && event.isPageLoaded();
            result = result && error.expectedErrorCode("future").validateErrorDescription(language).validate("",60000);
        } catch (Exception e) {
            logger.error("Error while checking future time syndication" + e);
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "future time syndication test failed");
    }
}
