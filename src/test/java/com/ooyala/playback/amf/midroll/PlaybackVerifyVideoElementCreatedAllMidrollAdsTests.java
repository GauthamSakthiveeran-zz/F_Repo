package com.ooyala.playback.amf.midroll;

import static java.lang.Integer.parseInt;

import org.apache.log4j.Logger;
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
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 2/3/17.
 */
public class PlaybackVerifyVideoElementCreatedAllMidrollAdsTests extends PlaybackWebTest {

    private static Logger logger = Logger.getLogger(PlaybackVerifyVideoElementCreatedAllMidrollAdsTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private PlayAction playAction;
    private DifferentElementValidator differentElementValidator;
    private SeekValidator seekValidator;
    private PoddedAdValidator poddedAdValidator;

    PlaybackVerifyVideoElementCreatedAllMidrollAdsTests() throws OoyalaException {
        super();
    }

    @Test(groups = "VideoCreated", dataProvider = "testUrls")
    public void testVideoElementCreated(String testName, String url)
            throws OoyalaException {

        boolean result = true;
        int counter = 0;
        try {
            driver.get(url);

            result=result && play.waitForPage();

            injectScript();

            result=result && playAction.startAction();

            result = result && eventValidator.validate("playing_1", 5000);

            result = result && seekValidator.validate("seeked_1", 60000);

            result = result && eventValidator.validate("MidRoll_willPlayAds", 60000);

            result = result && eventValidator.validate("adsPlayed_1", 40000);

            result = result && poddedAdValidator.setPosition("MidRoll").validate("countPoddedAds_1", 120000);

            // for IMA ad videoControllerVideoElementCreated event is not triggering
            if (!eventValidator.isAdPluginPresent("ima")) {
                int noOfPoddedAds = parseInt(
                        (((JavascriptExecutor) driver).executeScript("return adPodStarted_1.textContent")).toString());

                for (int i = 1 + counter; i <= noOfPoddedAds; i++) {
                    result = result && eventValidator.validate("videoCreatedForAds_" + i + "", 20000);
                }
            }

            result = result && differentElementValidator.validateMainVideoElementId("VIDEO_ELEMENT",20000);

            if (!eventValidator.isAdPluginPresent("ima") ){
                result = result && differentElementValidator.validateAdElementId("AD_ELEMENT",5000);
            }

        } catch (Exception e) {
            logger.error("Exception while checking Video Element tests  "+e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback Video Element test failed");

    }
}
