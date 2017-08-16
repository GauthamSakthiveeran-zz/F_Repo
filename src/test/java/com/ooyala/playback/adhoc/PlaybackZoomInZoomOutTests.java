package com.ooyala.playback.adhoc;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AspectRatioValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.FullScreenValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by suraj on 8/7/17.
 */
public class PlaybackZoomInZoomOutTests extends PlaybackWebTest {
    public PlaybackZoomInZoomOutTests() throws OoyalaException {
        super();
    }

    private static Logger logger = Logger.getLogger(PlaybackZoomInZoomOutTests.class);
    PlayValidator playValidator;
    PlayAction playAction;
    AspectRatioValidator aspectRatioValidator;
    FullScreenValidator fullScreenValidator;
    EventValidator eventValidator;

    @Test(dataProvider = "testUrls",groups = "adhoc")
    public void testZoomInZoomOut(String testName, UrlObject url){
        boolean result = true;
        try {
            driver.get(url.getUrl());
            result = result && playValidator.clearCache();
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playAction.startAction();
            result = result && eventValidator.validate("playing_1",10000);
            aspectRatioValidator.getDimensions();
            result = result && fullScreenValidator.getFullScreen();
            result = result && fullScreenValidator.getNormalScreen();
            result = result && aspectRatioValidator.validateDimensions();
        } catch (Exception e) {
            logger.error(e);
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback Zoom-In Zoom-Out Tests failed :"+testName);
    }
}
