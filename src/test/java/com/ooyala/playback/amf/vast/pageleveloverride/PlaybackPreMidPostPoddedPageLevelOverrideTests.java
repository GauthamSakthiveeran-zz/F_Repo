package com.ooyala.playback.amf.vast.pageleveloverride;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.VastPageLevelOverridingValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.annotations.Test;

/**
 * Created by Jitendra on 8/23/17.
 */

public class PlaybackPreMidPostPoddedPageLevelOverrideTests extends PlaybackWebTest{
    public PlaybackPreMidPostPoddedPageLevelOverrideTests() throws OoyalaException {
        super();
    }
    private PlayValidator playValidator;
    private VastPageLevelOverridingValidator pageOverride;
    private PlayAction playAction;
    private EventValidator event;
    private PoddedAdValidator poddedAdValidator;
    private SeekAction seekAction;

    @Test(dataProvider = "testUrls",groups = "pageLevelOverriding")
    public void prerollPageLevelOverride(String testName, UrlObject url){
        boolean result= true;
        try {
            driver.get(url.getUrl());
            result = result && playValidator.waitForPage();
            injectScript();
            pageOverride.getTotalDuration();
            result = result && playAction.startAction();
            result = result && event.validate("PreRoll_willPlayAds", 60000);
            result = result && event.validate("adsPlayed_1", 600000);
            result = result && poddedAdValidator.setPosition("PreRoll").validate("countPoddedAds_1", 60000);
            result = result && event.validate("playing_1", 90000);
            result = result && pageOverride.isMoreThanOneAd(true).setAdType("preroll").validate("",20000);
            result = result && poddedAdValidator.setPosition("MidRoll").validate("countPoddedAds_2", 600000);
            result = result && event.validate("playing_2", 90000);
            result = result && pageOverride.isMoreThanOneAd(true).setAdType("midroll").validate("",20000);
            result = result && seekAction.seekTillEnd().startAction();
            result = result && event.validate("PostRoll_willPlayAds", 200000);
            result = result && event.validate("adsPlayed_3", 600000);
            result = result && poddedAdValidator.setPosition("PostRoll").validate("countPoddedAds_3", 600000);
            result = result && pageOverride.isMoreThanOneAd(true).setAdType("postroll").validate("",20000);
            result = result && event.validate("played_1", 180000);

        }catch (Exception e){
            extentTest.log(LogStatus.FAIL,e.getMessage());
            result = false;
        }

        org.testng.Assert.assertTrue(result, "Preroll Page Level Overriding tests failed");
    }
}
