package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 28/2/17.
 */
public class PlaybackInitialVolumeTests extends PlaybackWebTest {
    private static Logger logger = Logger.getLogger(PlaybackInitialVolumeTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private VolumeValidator volumeValidator;
    private PlayAction playAction;
    private SeekValidator seekValidator;
    private IsAdPlayingValidator isAdPlayingValidator;
    private SeekAction seekAction;

    PlaybackInitialVolumeTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testInitialVolumeVTC(String testName, String url)
            throws OoyalaException {
        String[] parts= testName.split(":");
        String adType = parts[2].trim();
        double volumeValue = Double.parseDouble(parts[3]);

        boolean result = true;
        try {
            driver.get(url);

            result = result && play.waitForPage();

            injectScript();

            result = result && playAction.startAction();


            if (adType.equalsIgnoreCase("Preroll")){
                result=result && eventValidator.validate("videoPlayingAd_1", 10000);
                driver.executeScript("pp.pause()");
                Boolean isPrerollAdplaying = isAdPlayingValidator.validate("", 20000);
                if (isPrerollAdplaying) {
                    logger.info("Checking initial volume for Preroll Ad");
                    result = result && volumeValidator.checkInitialVolume("ad",volumeValue);
                }else{
                    logger.error("Preroll ad is not played");
                }
                driver.executeScript("pp.play()");
                result=result && eventValidator.validate("playing_1", 60000);
                result=result && volumeValidator.checkInitialVolume("video",volumeValue);
            }

            if(adType.equalsIgnoreCase("midroll")){
                result = result && eventValidator.validate("playing_1",20000);
                result = result && volumeValidator.checkInitialVolume("video",volumeValue);
                result=result && eventValidator.validate("willPlaySingleAd_1", 60000);
                driver.executeScript("pp.pause()");
                Boolean isMidrollAdplaying = isAdPlayingValidator.validate("", 20000);
                if (isMidrollAdplaying) {
                    logger.info("Checking initial volume for Midroll Ad");
                    result = result && volumeValidator.checkInitialVolume("ad",volumeValue);
                    result=result && eventValidator.validate("playing_2", 60000);
                }else{
                    logger.error("Midroll ad is not played");
                }
                driver.executeScript("pp.play()");
            }

            if(adType.equalsIgnoreCase("postroll")){
                result = result && eventValidator.validate("playing_1",20000);
                result = result && volumeValidator.checkInitialVolume("video",volumeValue);
                result=result && seekValidator.validate("seeked_1",20000);
                result=result && eventValidator.validate("willPlaySingleAd_1", 60000);
                Boolean isPostrollAdplaying = isAdPlayingValidator.validate("", 20000);
                if (isPostrollAdplaying) {
                    logger.info("Checking initial volume for Postroll Ad");
                    result = result && volumeValidator.checkInitialVolume("ad",volumeValue);
                    result=result && eventValidator.validate("playing_2", 60000);
                }else{
                    logger.error("Postroll ad is not played");
                }
            }

            if (adType.equalsIgnoreCase("PrerollPodded")){
                result=result && eventValidator.validate("willPlaySingleAd_1", 60000);
                result = result && eventValidator.validate("adPodStarted_2",10000);
                int noOfAds = Integer.parseInt(driver.executeScript("return adPodStarted_2.textContent").toString());
                for (int i = 1 ; i <= noOfAds ; i++){
                    Boolean isPostrollAdplaying = isAdPlayingValidator.validate("", 20000);
                    if (isPostrollAdplaying) {
                        logger.info("Checking initial volume for PrerollPodded Ad");
                        result = result && eventValidator.validate("willPlaySingleAd_"+i+"",50000);
                        result = result && volumeValidator.checkInitialVolume("ad",volumeValue);
                    }else{
                        logger.error("PrerollPodded ad is not played");
                    }
                }
                result = result && eventValidator.validate("playing_1", 60000);
                result = result && volumeValidator.checkInitialVolume("video",volumeValue);
            }


            if (adType.equalsIgnoreCase("MidrollPodded")){
                result = result && eventValidator.validate("playing_1",20000);
                result = result && volumeValidator.checkInitialVolume("video",volumeValue);
                result=result && seekAction.seek(20,true);
                result=result && eventValidator.validate("willPlaySingleAd_1", 60000);
                result = result && eventValidator.validate("adPodStarted_2",10000);
                int noOfAds = Integer.parseInt(driver.executeScript("return adPodStarted_2.textContent").toString());
                for (int i = 1 ; i <= noOfAds ; i++){
                    Boolean isPostrollAdplaying = isAdPlayingValidator.validate("", 20000);
                    if (isPostrollAdplaying) {
                        logger.info("Checking initial volume for MidrollPodded Ad");
                        result = result && eventValidator.validate("willPlaySingleAd_"+i+"",50000);
                        result = result && volumeValidator.checkInitialVolume("ad",volumeValue);
                    }else{
                        logger.error("MidrollPodded ad is not played");
                    }
                }
            }

            if (adType.equalsIgnoreCase("PostrollPodded")){
                result = result && eventValidator.validate("playing_1",20000);
                result = result && volumeValidator.checkInitialVolume("video",volumeValue);
                result=result && seekValidator.validate("seeked_1",20000);
                result=result && eventValidator.validate("willPlaySingleAd_1", 60000);
                result = result && eventValidator.validate("adPodStarted_2",10000);
                int noOfAds = Integer.parseInt(driver.executeScript("return adPodStarted_2.textContent").toString());
                logger.info("Total Number of ads are : "+noOfAds);
                for (int i = 1 ; i <= noOfAds ; i++){
                    Boolean isPostrollAdplaying = isAdPlayingValidator.validate("", 20000);
                    if (isPostrollAdplaying) {
                        logger.info("Checking initial volume for MidrollPodded Ad");
                        result = result && eventValidator.validate("willPlaySingleAd_"+i+"",50000);
                        result = result && volumeValidator.checkInitialVolume("ad",volumeValue);
                    }else{
                        logger.error("MidrollPodded ad is not played");
                    }
                }
            }


            if (adType.equalsIgnoreCase("PreMidPostPodded")){

                result=result && eventValidator.validate("willPlaySingleAd_1", 60000);
                result = result && eventValidator.validate("adPodStarted_2",10000);
                int noOfAds = Integer.parseInt(driver.executeScript("return adPodStarted_2.textContent").toString());
                for (int i = 1 ; i <= noOfAds ; i++){
                    Boolean isPostrollAdplaying = isAdPlayingValidator.validate("", 20000);
                    if (isPostrollAdplaying) {
                        logger.info("Checking initial volume for PrerollPodded Ad");
                        result = result && eventValidator.validate("willPlaySingleAd_"+i+"",50000);
                        result = result && volumeValidator.checkInitialVolume("ad",volumeValue);
                    }else{
                        logger.error("PrerollPodded ad is not played");
                    }
                }
                result = result && eventValidator.validate("playing_1", 60000);
                result = result && volumeValidator.checkInitialVolume("video",volumeValue);


                result=result && seekAction.seek(20,true);
                result=result && eventValidator.validate("willPlaySingleAd_1", 60000);
                result = result && eventValidator.validate("adPodStarted_3",60000);
                int noOfAdsmid = Integer.parseInt(driver.executeScript("return adPodStarted_3.textContent").toString())+noOfAds;
                for (int i = 1+noOfAds ; i <= noOfAdsmid ; i++){
                    Boolean isPostrollAdplaying = isAdPlayingValidator.validate("", 20000);
                    if (isPostrollAdplaying) {
                        logger.info("Checking initial volume for MidrollPodded Ad");
                        result = result && eventValidator.validate("willPlaySingleAd_"+i+"",50000);
                        result = result && volumeValidator.checkInitialVolume("ad",volumeValue);
                    }else{
                        logger.error("MidrollPodded ad is not played");
                    }
                }


                result = result && eventValidator.validate("adPodStarted_4",60000);
                int noOfAdsPost = Integer.parseInt(driver.executeScript("return adPodStarted_4.textContent").toString())+noOfAdsmid;
                logger.info("Total Number of ads are : "+noOfAdsPost);
                for (int i = 1+noOfAdsmid ; i <= noOfAdsPost ; i++){
                    Boolean isPostrollAdplaying = isAdPlayingValidator.validate("", 20000);
                    if (isPostrollAdplaying) {
                        logger.info("Checking initial volume for MidrollPodded Ad");
                        result = result && eventValidator.validate("willPlaySingleAd_"+i+"",50000);
                        result = result && volumeValidator.checkInitialVolume("ad",volumeValue);
                    }else{
                        logger.error("MidrollPodded ad is not played");
                    }
                }


            }


        } catch (Exception e) {
            logger.error("Exception while checking  initial Volume tests  "+e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback initial Volume tests failed");
    }
}
