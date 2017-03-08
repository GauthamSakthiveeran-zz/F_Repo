package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import static java.lang.Integer.parseInt;

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
    private SeekAction seekAction;
    private SeekValidator seekValidator;
    private PoddedAdValidator poddedAdValidator;

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

            // Checking Video element for Preroll and Midroll
            if (testName.contains("Preroll") || testName.contains("Midroll")){

                result = result && eventValidator.validate("CreateVideo_1", 20000);

                result = result && eventValidator.validate("willPlaySingleAd_1", 40000);

                result = result && eventValidator.validate("adsPlayed_1", 30000);

                result=result && eventValidator.validate("playing_1", 60000);

                result = result && eventValidator.validate("videoCreatedForAds_1", 20000);
            }

            // Checking Video element for Postroll
            if (testName.contains("Postroll")){

                result = result && seekAction.seek(15,true);

                result = result && eventValidator.validate("seeked_1",20000);

                result = result && eventValidator.validate("willPlaySingleAd_1", 40000);

                result = result && eventValidator.validate("videoCreatedForAds_1", 20000);

                result = result && eventValidator.validate("adsPlayed_1", 30000);
            }


            // Checking Video element for PreMidPostroll
            if (testName.contains("PreMidPost")){

                result = result && eventValidator.validate("CreateVideo_1", 20000);

                result = result && eventValidator.validate("PreRoll_willPlaySingleAd_1", 40000);

                result = result && eventValidator.validate("videoCreatedForAds_1", 20000);

                result = result && eventValidator.validate("singleAdPlayed_1", 30000);

                result = result && eventValidator.validate("MidRoll_willPlayAds", 40000);

                result = result && eventValidator.validate("singleAdPlayed_2", 30000);

                result = result && seekAction.seek(15,true);

                result = result && eventValidator.validate("seeked_1",20000);

                result = result && eventValidator.validate("PostRoll_willPlayAds",40000);

                result = result && eventValidator.validate("singleAdPlayed_3", 30000);

            }

            // Checking Video element for Podded
            if (testName.contains("Podded")){

                int counter = 0;

                // Checking Video element for Preroll Podded
                if (testName.contains("PrePodded")){

                    result = result && eventValidator.validate("videoPlaying_1",50000);

                    result = result && poddedAdValidator.setPosition("PreRoll").validate("countPoddedAds_1", 120000);

                    // for IMA ad videoControllerVideoElementCreated event is not triggering
                    if (!eventValidator.isAdPluginPresent("ima")) {
                        int noOfPoddedAds = parseInt(
                                (((JavascriptExecutor) driver).executeScript("return countPoddedAds_1.textContent")).toString());

                        for (int i = 1 + counter; i <= noOfPoddedAds; i++) {
                            result = result && eventValidator.validate("videoCreatedForAds_" + i + "", 20000);
                        }
                    }

                }

                // Checking Video element for Midroll Podded
                if (testName.contains("MidPodded")){

                    result = result && eventValidator.validate("playing_1", 5000);

                    result = result && seekValidator.validate("seeked_1", 60000);

                    result = result && eventValidator.validate("MidRoll_willPlayAds", 60000);

                    result = result && eventValidator.validate("adsPlayed_1", 40000);

                    result = result && poddedAdValidator.setPosition("MidRoll").validate("countPoddedAds_1", 120000);

                    // for IMA ad videoControllerVideoElementCreated event is not triggering
                    if (!eventValidator.isAdPluginPresent("ima")) {
                        int noOfPoddedAds = parseInt(
                                (((JavascriptExecutor) driver).executeScript("return countPoddedAds_1.textContent")).toString());

                        for (int i = 1 + counter; i <= noOfPoddedAds; i++) {
                            result = result && eventValidator.validate("videoCreatedForAds_" + i + "", 20000);
                        }
                    }
                }


                // Checking Video element for Postroll Podded
                if (testName.contains("PostPodded")){

                    result = result && eventValidator.validate("playing_1", 60000);

                    result = result && seekValidator.validate("seeked_1", 60000);

                    result = result && eventValidator.validate("played_1", 60000);

                    result = result && poddedAdValidator.setPosition("PostRoll").validate("countPoddedAds", 10000);

                    // for IMA ad videoControllerVideoElementCreated event is not triggering
                    if (!eventValidator.isAdPluginPresent("ima")) {
                        int noOfPoddedAds = parseInt(
                                (((JavascriptExecutor) driver).executeScript("return countPoddedAds.textContent")).toString());

                        for (int i = 1 + counter; i <= noOfPoddedAds; i++) {
                            result = result && eventValidator.validate("videoCreatedForAds_" + i + "", 20000);
                        }
                    }
                }

                // Checking Video element for PreMidPostroll Podded
                if (testName.contains("PreMidPosPodded")){

                    int noOfPoddedAdsMid = 0;
                    int noOfPoddedAdsPre = 0;
                    int noOfPoddedAdsPost =0;

                    result = result && eventValidator.validate("PreRoll_willPlayAds", 60000);

                    result = result && eventValidator.validate("adsPlayed_1", 600000);

                    result = result && poddedAdValidator.setPosition("PreRoll").validate("countPoddedAds_1", 60000);

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

                    result = result && poddedAdValidator.setPosition("MidRoll").validate("countPoddedAds_2", 600000);

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

                    result = result && poddedAdValidator.setPosition("PostRoll").validate("countPoddedAds_3", 600000);

                    // for IMA ad videoControllerVideoElementCreated event is not triggering
                    if (!eventValidator.isAdPluginPresent("ima")) {
                        noOfPoddedAdsPost = parseInt(
                                (((JavascriptExecutor) driver).executeScript("return countPoddedAds_3.textContent")).toString())-noOfPoddedAdsPre-noOfPoddedAdsMid;

                        for (int i = 1 + counter; i <= noOfPoddedAdsPost; i++) {
                            result = result && eventValidator.validate("videoCreatedForAds_" + i + "", 20000);
                        }
                    }

                    result = result && eventValidator.validate("played_1", 180000);
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
