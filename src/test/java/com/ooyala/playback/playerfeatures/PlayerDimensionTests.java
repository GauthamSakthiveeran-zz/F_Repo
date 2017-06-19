package com.ooyala.playback.playerfeatures;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by suraj on 6/19/17.
 */
public class PlayerDimensionTests extends PlaybackWebTest {

    private static Logger logger = Logger
            .getLogger(PlayerDimensionTests.class);

    private EventValidator eventValidator;
    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;
    private PlayAction playAction;
    private AspectRatioValidator aspectRatioValidator;

    public PlayerDimensionTests() throws OoyalaException {
        super();
    }

    @Test(groups = "playerFeatures", dataProvider = "testUrls")
    public void testPlayerDimensions(String testName, UrlObject url)
            throws OoyalaException {
        boolean result = true;

        try {
            driver.get(url.getUrl());

            result = result && play.waitForPage();

            aspectRatioValidator.getDimensions();

            injectScript();

            result = result && play.validate("playing_1", 60000);

            result = result && eventValidator.playVideoForSometime(2);

            result = result && pause.validate("paused_1", 60000);

            result = result && playAction.startAction();

            result = result && seek.validate("seeked_1", 60000);

            result = result && aspectRatioValidator.validateDimensions();

            result = result && eventValidator.validate("played_1", 60000);

        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Player Dimension tests failed");
    }
}
