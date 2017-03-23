package com.ooyala.playback.playerfeatures;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.log4j.Logger;
import static java.lang.Thread.sleep;

/**
 * Created by soundarya on 11/17/16.
 */
public class PlaybackVerticalVideoTests extends PlaybackWebTest {

    private static Logger logger = Logger
            .getLogger(PlaybackVerticalVideoTests.class);
    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;
    private PlayAction playAction;
    private PauseAction pauseAction;
    private EventValidator eventValidator;
    private AspectRatioValidator aspectRatioValidator;

    public PlaybackVerticalVideoTests() throws OoyalaException {
        super();
    }

    @Test(groups = "playerFeatures", dataProvider = "testUrls")
    public void testVerticalVideo(String testName, String url)
            throws OoyalaException {

        boolean result = true;
        try {
            driver.get(url);

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1", 60000);

            result = result && aspectRatioValidator.setVerticalVideo().validate("assetDimension_1", 60000);

            executeScript("pp.pause();");  // pausing video by js as for this test video screen size vertical.

            result = result && play.validate("playing_2", 60000);

            result = result && seek.validate("seeked_1", 60000);

            result = result
                    && aspectRatioValidator.setVerticalVideo().validate(
                    "assetDimension_1", 60000);

            result = result && eventValidator.validate("videoPlayed_1", 60000);

        } catch (Exception e) {
            logger.error(e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Vertical Video tests failed");
    }

}