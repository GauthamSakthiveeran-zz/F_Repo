package com.ooyala.playback.playerfeatures;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.UIControlValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by suraj on 6/22/17.
 */
public class PlaybackUIControlOptionsTests extends PlaybackWebTest {
    public PlaybackUIControlOptionsTests() throws OoyalaException {
        super();
    }

    private PlayValidator play;
    private UIControlValidator uicontroller;
    private EventValidator eventValidator;

    @Test(groups = "playerFeatures", dataProvider = "testUrls", invocationCount = 1)
    public void testPlayerMetadataStates(String testName, UrlObject url)
            throws OoyalaException {
        boolean result = true;

        try {
            driver.get(url.getUrl());
            result = result && play.waitForPage();
            injectScript();
            result = result && uicontroller.playAction("playing_1",30000);
            result = result && eventValidator.playVideoForSometime(3);
            result = result && uicontroller.pauseAction("paused_1",30000);
            result = result && uicontroller.validateVolumeMute(0);
            result = result && uicontroller.validateVolumeMax(1);
            result = result && uicontroller.validateSeek("seeked_1",30000);
            result = result && uicontroller.playAction("playing_2",30000);
            result = result && uicontroller.validateVolumeMute(0);
            result = result && uicontroller.validateVolumeMax(1);
            result = result && eventValidator.validate("played_1",50000);
            result = result && uicontroller.validateReplay("playing_3",10000);
        }catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback UI Control Options tests failed");
    }
}
