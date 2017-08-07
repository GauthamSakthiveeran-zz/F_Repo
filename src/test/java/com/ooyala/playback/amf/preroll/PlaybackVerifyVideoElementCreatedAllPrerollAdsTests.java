package com.ooyala.playback.amf.preroll;

import static java.lang.Integer.parseInt;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DifferentElementValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 2/3/17.
 */
public class PlaybackVerifyVideoElementCreatedAllPrerollAdsTests extends PlaybackWebTest {

    private static Logger logger = Logger.getLogger(PlaybackVerifyVideoElementCreatedAllPrerollAdsTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private PlayAction playAction;
    private DifferentElementValidator differentElementValidator;

    PlaybackVerifyVideoElementCreatedAllPrerollAdsTests() throws OoyalaException {
        super();
    }

    @Test(groups = {"VideoCreated", "preroll", "podded"}, dataProvider = "testUrls")
    public void testVideoElementCreated(String testName, UrlObject url) throws OoyalaException {

        boolean result = true;
        int counter = 0;
        try {
            driver.get(url.getUrl());

            result = result && play.waitForPage();

            injectScript();

            result = result && playAction.startAction();

            result = result && eventValidator.validate("PreRoll_willPlayAds", 5000);
            result = result && eventValidator.validate("adsPlayed_1", 180000);

            result = result && differentElementValidator.validateMainVideoElementId("VIDEO_ELEMENT", 20000);

            // for IMA ad videoControllerVideoElementCreated event is not triggering
            if (!eventValidator.isAdPluginPresent("ima")) {
                result = result && eventValidator.validateVideoElementOccuredCount(40000);
                result = result && differentElementValidator.validateAdElementId("AD_ELEMENT", 5000);
            }

        } catch (Exception e) {
            logger.error("Exception while checking Video Element tests  " + e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback Video Element test failed");

    }
}
