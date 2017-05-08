package com.ooyala.playback.amf;

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
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 2/3/17.
 */
public class PlaybackVerifyVideoElementCreatedAllPreMiPostAdsTests extends PlaybackWebTest {

    private static Logger logger = Logger.getLogger(PlaybackVerifyVideoElementCreatedAllPreMiPostAdsTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private PlayAction playAction;
    private DifferentElementValidator differentElementValidator;
    private SeekAction seekAction;

    PlaybackVerifyVideoElementCreatedAllPreMiPostAdsTests() throws OoyalaException {
        super();
    }

    @Test(groups = "VideoCreated", dataProvider = "testUrls")
    public void testVideoElementCreated(String testName, UrlObject url)
            throws OoyalaException {

        boolean result = true;
        int counter = 0 ;
        int noOfPoddedAdsMid = 0;
        int noOfPoddedAdsPre = 0;
        int noOfPoddedAdsPost =0;
        try {
            driver.get(url.getUrl());

            result=result && play.waitForPage();

            injectScript();

			result = result && playAction.startAction();

            result = result && eventValidator.validate("PreRoll_willPlayAds", 60000);

            result = result && eventValidator.validate("adsPlayed_1", 600000);

//            result = result && poddedAdValidator.setPosition("PreRoll").validate("countPoddedAds_1", 60000);

            // for IMA ad videoControllerVideoElementCreated event is not triggering
            if (!eventValidator.isAdPluginPresent("ima")) {
                noOfPoddedAdsPre = parseInt(
                        (((JavascriptExecutor) driver).executeScript("return countPoddedAds_1.textContent")).toString());

                for (int i = 1 + counter; i <= noOfPoddedAdsPre; i++) {
                    result = result && eventValidator.validate("videoCreatedForAds_" + i + "", 20000);
                }
            }

            result = result && eventValidator.validate("playing_1", 90000);


            if (!eventValidator.validate("MidRoll_willPlayAds", 5000)) {
                result = result && seekAction.setFactor(2).fromLast().setTime(10).startAction();
            }

            result = result && eventValidator.validate("MidRoll_willPlayAds", 200000);

            result = result && eventValidator.validate("adsPlayed_2", 600000);

//            result = result && poddedAdValidator.setPosition("MidRoll").validate("countPoddedAds_2", 600000);

            // for IMA ad videoControllerVideoElementCreated event is not triggering
            if (!eventValidator.isAdPluginPresent("ima")) {
                noOfPoddedAdsMid = parseInt(
                        (((JavascriptExecutor) driver).executeScript("return countPoddedAds_2.textContent")).toString())-noOfPoddedAdsPre;

                for (int i = 1 + counter; i <= noOfPoddedAdsMid; i++) {
                    result = result && eventValidator.validate("videoCreatedForAds_" + i + "", 20000);
                }
            }

            if (!eventValidator.validate("played_1", 4000)) {
                result = result && seekAction.seekTillEnd().startAction();
            }

            result = result && eventValidator.validate("PostRoll_willPlayAds", 200000);
            result = result && eventValidator.validate("adsPlayed_3", 600000);

//            result = result && poddedAdValidator.setPosition("PostRoll").validate("countPoddedAds_3", 600000);

            // for IMA ad videoControllerVideoElementCreated event is not triggering
            if (!eventValidator.isAdPluginPresent("ima")) {
                noOfPoddedAdsPost = parseInt(
                        (((JavascriptExecutor) driver).executeScript("return countPoddedAds_3.textContent")).toString())-noOfPoddedAdsPre-noOfPoddedAdsMid;

                for (int i = 1 + counter; i <= noOfPoddedAdsPost; i++) {
                    result = result && eventValidator.validate("videoCreatedForAds_" + i + "", 20000);
                }
            }

            result = result && eventValidator.validate("played_1", 180000);
            result = result && differentElementValidator.validateMainVideoElementId("VIDEO_ELEMENT",20000);

            if (!eventValidator.isAdPluginPresent("ima") ){
                result = result && differentElementValidator.validateAdElementId("AD_ELEMENT",5000);
            }

        } catch (Exception e) {
            logger.error("Exception while checking Video Element tests  "+e.getMessage());
            extentTest.log(LogStatus.FAIL, e);
            result = false;
        }
        Assert.assertTrue(result, "Playback Video Element test failed");

    }
}
