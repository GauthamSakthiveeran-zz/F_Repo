package com.ooyala.playback.apps.android.pulseadsampleapp;

import com.ooyala.playback.apps.actions.*;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;
import com.relevantcodes.extentreports.LogStatus;

import java.util.List;

public class PulseSampleAppNoAdsTests extends PlaybackAppsTest {

    private SelectVideoAction selectVideo;
    private ElementValidator elementValidator;
    private PlayAction playAction;
    private SeekAction seekAction;
    private NotificationEventValidator notificationEventValidator;
    private AndroidKeyCodeAction androidAction;
    private AllowAction allowAction;
    private DiscoveryAction clickDiscoveryAction;


    @Test(groups = "pulsesampleapp", dataProvider = "testData")
    public void testBasicPlayer(String testName, TestParameters test) throws Exception {

        boolean result = true;

        try {
            result = result && selectVideo.startAction(test.getAsset());
            result = result && allowAction.startAction("ALLOW");
            result = result && androidAction.startAction("BACK");
            result = result && selectVideo.startAction(test.getAsset());
            result = result && clickDiscoveryAction.clickPlayButton();
            if(test.getAsset().equals("PULSE_PREROLL_AD_404")) {
                result = result && eventValidator404();
                result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
            }
//            if(test.getAsset().equals("PULSE_PREROLL_AD_INVALID")) {
//                result = result && notificationEventValidator.validateEvent(Events.AD_ERROR, 10000);
//                result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
//            }
            if((test.getAsset().equals("PULSE_PREROLL")) || test.getAsset().equals("PULSE_PRE_MID_POSTROLL") || test.getAsset().equals("PULSE_PRE_MID_POSTROLL_SKIPPABLE")) {
                result = result && adEventValidator();
                result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 10000);
            }
            if(test.getAsset().equals("PULSE_MIDROLL") || test.getAsset().equals("PULSE_PRE_MID_POSTROLL") || test.getAsset().equals("PULSE_PRE_MID_POSTROLL_SKIPPABLE")) {
                result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 10000);
                result = result && elementValidator.letVideoPlayForSec(15);
                result = result && adEventValidator();
                result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_RESUMED_ANDRD, 35000);
                result = result && elementValidator.letVideoPlayForSec(15);
                result = result && adEventValidator();
                result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_RESUMED_ANDRD, 90000);
            }
            result = result && elementValidator.letVideoPlayForSec(3);
            result = result && clickDiscoveryAction.clickPauseButton();
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
            result = result && clickDiscoveryAction.seekToEnd("SEEKBAR_ANDROID");
            result = result && elementValidator.handleLoadingSpinner();
            result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 90000);
            result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 90000);
            result = result && clickDiscoveryAction.clickPlayButton();
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_RESUMED_ANDRD, 90000);
            if((test.getAsset().equals("PULSE_POSTROLL")) || test.getAsset().equals("PULSE_PRE_MID_POSTROLL") || test.getAsset().equals("PULSE_PRE_MID_POSTROLL_SKIPPABLE")) {
                result = result && adEventValidator();
            }
            result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_COMPLETED, 95000);

        } catch (Exception ex) {
            extentTest.log(LogStatus.FAIL, ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:" + test.getApp() + "->Asset:" + test.getAsset());
    }

    private boolean adEventValidator() {
        boolean result = true;
        result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
        result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
        result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
        result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
        return result;
    }

    private boolean eventValidator404() {
        boolean result = false;
        List<LogEntry> logEntriesList = ((AndroidDriver) driver).manage().logs().get("logcat").getAll();
        for (int i = 0; i < logEntriesList.size(); i++) {
            String test = logEntriesList.get(i).toString();
            String test1[] = test.split("\\s+");
            for (int j = 0; j < test1.length; j++)
                if (test1[j].toLowerCase().contains(("Request:404").toLowerCase())) {
                if(test1[j-1].contains("[TRACKER/REQUEST_FAILED]"))
                    logger.info("404 Error request found");
                    result = true;
                }
        }
        return result;
    }
}