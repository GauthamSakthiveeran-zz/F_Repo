package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 2/3/17.
 */
public class PlaybackVerifyVideoElementCreatedTests extends PlaybackWebTest {

    private static Logger logger = Logger.getLogger(PlaybackVerifyVideoElementCreatedTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private PlayAction playAction;
    private IsAdPlayingValidator isAdPlayingValidator;
    private DifferentElementValidator differentElementValidator;

    PlaybackVerifyVideoElementCreatedTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testVideoElementCreated(String testName, String url)
            throws OoyalaException {

        boolean result = true;
        try {
            driver.get(url);

            result=result && play.waitForPage();

            injectScript();

            result=result && playAction.startAction();

            // Can not check main video's videoControllerVideoElementCreated event
            result = result && eventValidator.validate("CreateVideo_1", 20000);

            Boolean isAdplaying = isAdPlayingValidator.validate("", 2000);

            if (isAdplaying) {
                result = result && eventValidator.validate("videoCreatedForAds", 20000);
            }

            result=result && eventValidator.validate("playing_1", 60000);

            result = result && differentElementValidator.validateMainVideoElementId("VIDEO_ELEMENT",20000);

            if (!eventValidator.isAdPluginPresent("ima"))
                result = result && differentElementValidator.validateAdElementId("AD_ELEMENT",5000);

        } catch (Exception e) {
            logger.error("Exception while checking  Volume tests  "+e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback Volume tests failed");

    }
}
