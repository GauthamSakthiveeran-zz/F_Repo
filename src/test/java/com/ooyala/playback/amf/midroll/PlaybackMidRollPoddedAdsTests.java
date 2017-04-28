package com.ooyala.playback.amf.midroll;

import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackMidRollPoddedAdsTests extends PlaybackWebTest {

    public PlaybackMidRollPoddedAdsTests() throws OoyalaException {
        super();
    }

    private EventValidator event;
    private PlayValidator playValidator;
    private SeekAction seek;
    private SeekValidator seekValidator;
    private PoddedAdValidator poddedAdValidator;
    private SetEmbedCodeValidator setEmbedCodeValidator;
    private MidrollAdValidator adStartTimeValidator;

    @Test(groups = {"amf", "podded", "midroll"}, dataProvider = "testUrls")
    public void verifyMidrollPodded(String testName, UrlObject url) throws OoyalaException {

        boolean result = true;

        try {
            driver.get(url.getUrl());
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playValidator.validate("playing_1", 60000);

            if (event.isAdPluginPresent("ima") || event.isAdPluginPresent("vast")) {
                result = result && seek.seek("8");
            } else
                result = result && seek.seek("18");

            if (adStartTimeValidator.isAdPlayTimePresent(url)) {
                result = result && adStartTimeValidator.validateAdStartTime("MidRoll_willPlayAds");
            } else
                result = result && event.validate("MidRoll_willPlayAds", 60000);

            result = result && seek.fromLast().setTime(20).startAction();

            if (event.isAdPluginPresent("freewheel") || (event.isAdPluginPresent("ima") && testName.contains("MAIN"))) {
                result = result && event.validate("adsPlayed_2", 200000); // TODO
                result = result && poddedAdValidator.setPosition("MidRoll").validate("countPoddedAds_2", 120000);
            } else {
                result = result && event.validate("adsPlayed_1", 250000);
                result = result && poddedAdValidator.setPosition("MidRoll").validate("countPoddedAds_1", 120000);
            }

            if (testName.contains("SetEmbedCode")) {
                result = result && setEmbedCodeValidator.validate("setEmbedmbedCode", 6000);
            } else {
                result = result && seekValidator.validate("seeked_1", 60000);
                result = result && event.validate("played_1", 200000);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e);
            result = false;
        }
        Assert.assertTrue(result, "Tests failed");
    }
}
