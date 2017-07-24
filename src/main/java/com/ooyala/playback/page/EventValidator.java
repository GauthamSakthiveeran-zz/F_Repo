package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;
import java.util.Map;
import static java.lang.Integer.parseInt;

/**
 * Created by soundarya on 11/14/16.
 */
public class EventValidator extends PlayBackPage implements PlaybackValidator {

    public static Logger logger = Logger.getLogger(EventValidator.class);

    public EventValidator(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        /**
         * Here we will tell Facile to add the page elements of our Login Page
         */
        addElementToPageElements("discovery");
        addElementToPageElements("play");
        addElementToPageElements("pause");
        addElementToPageElements("replay");
        addElementToPageElements("controlbar");
        addElementToPageElements("fullscreen");
        addElementToPageElements("adPodEnd");
        addElementToPageElements("startscreen");
    }

    public boolean validate(String element, int timeout) throws Exception {

        if (!loadingSpinner()) {
            extentTest.log(LogStatus.FAIL, "Loading spinner seems to be there for a really long time.");
            return false;
        }

        try {
            if (driver.getCurrentUrl().contains("AnalyticsQEPlugin")) {
                if (element.toLowerCase().contains("ad") && !element.toLowerCase().contains("nonlinear")) {
                    if (element.toLowerCase().contains("willplay")) {
                        if (!(waitOnElement(By.id("analytics_ad_break_started_1"), timeout)
                                && waitOnElement(By.id("analytics_ad_started_1"), timeout))) {
                            logger.error("analytics_ad_started_1 or analytics_ad_break_started_1 analytics element is not present for ad start");
                            extentTest.log(LogStatus.FAIL, "analytics_ad_started_1 or analytics_ad_break_started_1 analytics element is not present for ad start");
                            return false;
                        }
                        logger.info("analytics_ad_started_1 and analytics_ad_break_started_1 analytics elements are present for ad start");
                        extentTest.log(LogStatus.PASS, "analytics_ad_started_1 and analytics_ad_break_started_1 analytics elements are present for ad start");
                    }
                    if (element.toLowerCase().contains("played")) {
                        if (!(waitOnElement(By.id("analytics_ad_break_ended_1"), timeout)
                                && waitOnElement(By.id("analytics_ad_ended_1"), timeout))) {
                            logger.error("analytics_ad_ended_1 or analytics_ad_break_ended_1 analytics element is not present for ad start");
                            extentTest.log(LogStatus.FAIL, "analytics_ad_ended_1 or analytics_ad_break_ended_1 analytics element is not present for ad start");
                            return false;
                        }
                        logger.info("analytics_ad_ended_1 and analytics_ad_break_ended_1 analytics elements are present for ad start");
                        extentTest.log(LogStatus.PASS, "analytics_ad_ended_1 and analytics_ad_break_ended_1 analytics elements are present for ad start");
                    }
                }
                if (element.toLowerCase().contains("buffering")) {
                    if (!waitOnElement(By.id("analytics_video_buffering_started_1"), timeout)) {
                        logger.error("analytics_video_buffering_started_1 element is not presesnt");
                        extentTest.log(LogStatus.FAIL, "analytics_video_buffering_started_1 element is not presesnt");
                        return false;
                    } else {
                        logger.info("analytics_video_buffering_started_1 element is presesnt");
                        extentTest.log(LogStatus.PASS, "analytics_video_buffering_started_1 element is presesnt");
                        return true;
                    }
                }
                if (element.toLowerCase().contains("seek")) {
                    if (!waitOnElement(By.id("analytics_video_seek_completed_1"), timeout)) {
                        logger.error("analytics_video_seek_completed_1 element is not presesnt");
                        extentTest.log(LogStatus.FAIL, "analytics_video_seek_completed_1 element is not presesnt");
                        return false;
                    } else {
                        logger.info("analytics_video_seek_completed_1 element is presesnt");
                        extentTest.log(LogStatus.PASS, "analytics_video_seek_completed_1 element is presesnt");
                        return true;
                    }
                }
                if (element.toLowerCase().contains("volume")) {
                    if (!waitOnElement(By.id("analytics_volume_changed_2"), timeout)) {
                        logger.error("analytics_volume_changed_2 element is not presesnt");
                        extentTest.log(LogStatus.FAIL, "analytics_volume_changed_2 element is not presesnt");
                        return false;
                    } else {
                        logger.info("analytics_volume_changed_2 element is presesnt");
                        extentTest.log(LogStatus.PASS, "analytics_volume_changed_2 element is presesnt");
                        return true;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (waitOnElement(By.id(element), timeout)) {
            ScrubberValidator scrubberValidator = new PlayBackFactory(driver, extentTest).getScrubberValidator();
            logger.info("element found : " + element);
            extentTest.log(LogStatus.PASS, "Wait on element : " + element);
            if (element.startsWith("played")) {
                return scrubberValidator.validate("", 1000);
            }
            if (element.startsWith("seeked")) {
                ((JavascriptExecutor) driver).executeScript("return pp.pause();");
                boolean flag = scrubberValidator.validate("", 1000);
                ((JavascriptExecutor) driver).executeScript("return pp.play();");
                return flag;
            }
            return true;
        }
        logger.error("Wait on element : " + element + " failed after " + timeout + " ms");
        extentTest.log(LogStatus.FAIL, "Wait on element : " + element + " failed after " + timeout + " ms");
        return false;
    }

    public boolean eventAction(String element) throws Exception {
        if (!getBrowser().equalsIgnoreCase("safari")) {
            return clickOnIndependentElement(element);
        } else
            return clickOnHiddenElement(element);
    }

    public boolean validateElement(String element, int timeout) throws Exception {
        if (!waitOnElement(By.id(element), timeout)) {
            logger.error("Wait on element failed : " + element);
            extentTest.log(LogStatus.FAIL, "Wait on element : " + element);
            return false;
        }
        logger.info("Wait on element Success : " + element);
        extentTest.log(LogStatus.PASS, "Wait on element : " + element);
        return true;
    }

    public boolean validateElementPresence(String element) throws Exception {
        if (isElementPresent(By.id(element))) {
            return true;
        }
        return false;
    }

    public boolean playVideoForSometime(int secs) {
        int count = 0;
        double playTime = Double
                .parseDouble(((JavascriptExecutor) driver).executeScript("return pp.getPlayheadTime();").toString());
        while (playTime <= secs) {
            playTime = Double.parseDouble(
                    ((JavascriptExecutor) driver).executeScript("return pp.getPlayheadTime();").toString());
            if (count == 120) {
                logger.error("Looks like the video did not play after waiting for 2 mins.");
                extentTest.log(LogStatus.FAIL, "Looks like the video did not play after waiting for 2 mins.");
                return false;
            }
            if (!loadingSpinner()) {
                logger.error("Loading spinner seems to be there for a really long time.");
                extentTest.log(LogStatus.FAIL, "Loading spinner seems to be there for a really long time.");
                return false;
            }
            count++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
        return true;
    }

    public boolean isVideoPluginPresent(String videoPlugin) throws Exception {
        Map<String, String> map = parseURL();
        if (map != null && map.get("video_plugins") != null && map.get("video_plugins").contains(videoPlugin)) {
            return true;
        }
        return false;
    }

    public boolean validatePlaybackReadyEvent(int timeout) {
        boolean result = false;
        logger.info("Inside validatePlaybackReadyEvent method");
        String consoleOutput = driver.executeScript("return OO.DEBUG.consoleOutput[0].toString()").toString();
        logger.info(consoleOutput);
        if (consoleOutput.contains("playbackReady")) {
            logger.info("playbackReady event found in consoleOutput");
            extentTest.log(LogStatus.PASS, "playbackReady event found in consoleOutput");
            result = true;
        } else {
            extentTest.log(LogStatus.FAIL, "playbackReady event not found in consoleOutput");
            logger.error("playbackReady event not found in consoleOutput");
        }
        return result;
    }

    public boolean validateAutoPlay() {
        boolean autoplay = false;

        autoplay = Boolean.parseBoolean(driver.executeScript("return pp.parameters.autoPlay").toString());

        if (!autoplay) {
            logger.info("Autoplay not set for this video");
            extentTest.log(LogStatus.INFO, "Autoplay not set for this video");
        }
        return autoplay;
    }

    public boolean checkIsAdPlaying() {
        IsAdPlayingValidator adPlaying = new PlayBackFactory(driver, extentTest).isAdPlaying();
        boolean isAdPlaying=false;
        for (int i = 0; i < 5; i++) {
            try {
                isAdPlaying = adPlaying.validate("", 1000);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return isAdPlaying;
    }

    public boolean validateVideoElementOccuredCount(int timeout){
        int counter = 0;
        boolean result = true;
        try {
            int noOfPoddedAds = parseInt(
                    (((JavascriptExecutor) driver).executeScript("return document.getElementById('adPodStarted_1').textContent")).toString());

        for (int i = 1 + counter; i <= noOfPoddedAds; i++) {
                result = result && validate("videoCreatedForAds_" + i + "", 20000);
            }
            }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
}