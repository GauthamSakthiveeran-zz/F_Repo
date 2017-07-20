package com.ooyala.playback.amf.postroll;

import static java.lang.Integer.parseInt;

import com.ooyala.playback.page.action.SeekAction;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DifferentElementValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 2/3/17.
 */
public class PlaybackVerifyVideoElementCreatedAllPostrollAdsTests extends PlaybackWebTest {

    private static Logger logger = Logger.getLogger(PlaybackVerifyVideoElementCreatedAllPostrollAdsTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private PlayAction playAction;
    private DifferentElementValidator differentElementValidator;
    private SeekValidator seekValidator;
    private PoddedAdValidator poddedAdValidator;
    private SeekAction seekAction;

    PlaybackVerifyVideoElementCreatedAllPostrollAdsTests() throws OoyalaException {
        super();
    }

    @Test(groups = {"videoCreated", "vtc", "amf"}, dataProvider = "testUrls")
    public void testVideoElementCreated(String testName, UrlObject url) throws OoyalaException {

        boolean result = true;
        int counter = 0;
        try {
            driver.get(url.getUrl());

            result = result && play.waitForPage();

            injectScript();

            result = result && playAction.startAction();

            result = result && eventValidator.validate("playing_1", 60000);

            result = result && seekAction.seek(15, true);

            result = result && eventValidator.waitOnElement(By.id("seeked_1"), 20000);

            result = result && eventValidator.waitOnElement(By.id("played_1"), 20000);

            result = result && poddedAdValidator.setPosition("PostRoll").validate("countPoddedAds", 10000);

            // for IMA ad videoControllerVideoElementCreated event is not
            // triggering
            if (!eventValidator.isAdPluginPresent("ima")) {
                int noOfPoddedAds = parseInt(
                        (((JavascriptExecutor) driver).executeScript("return document.getElementById('adPodStarted_1').textContent")).toString());

                for (int i = 1 + counter; i <= noOfPoddedAds; i++) {
                    result = result && eventValidator.validate("videoCreatedForAds_" + i + "", 20000);
                }
            }

            result = result && differentElementValidator.validateMainVideoElementId("VIDEO_ELEMENT", 20000);

            if (!eventValidator.isAdPluginPresent("ima")) {
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
