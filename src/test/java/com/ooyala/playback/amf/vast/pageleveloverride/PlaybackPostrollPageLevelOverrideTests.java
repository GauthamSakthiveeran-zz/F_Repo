package com.ooyala.playback.amf.vast.pageleveloverride;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.VastPageLevelOverridingValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.annotations.Test;

/**
 * Created by Jitendra on 8/23/17.
 */

public class PlaybackPostrollPageLevelOverrideTests extends PlaybackWebTest{
    public PlaybackPostrollPageLevelOverrideTests() throws OoyalaException {
        super();
    }
    private PlayValidator playValidator;
    private VastPageLevelOverridingValidator pageOverride;
    private PlayAction playAction;
    private EventValidator event;
    private SeekValidator seek;

    @Test(dataProvider = "testUrls",groups = "pageLevelOverriding")
    public void postrollPageLevelOverride(String testName, UrlObject url){
        boolean result= true;
        try {
            driver.get(url.getUrl());
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playAction.startAction();
            result = result && event.validate("playing_1", 60000);
            result = result && event.playVideoForSometime(3);
            driver.executeScript("pp.seek(pp.getDuration()-10)");
            result = result && event.validate("PostRoll_willPlaySingleAd_1", 60000);
            result = result && event.validate("singleAdPlayed_1", 120000);
            result = result && event.validate("played_1", 10000);
            result = result && pageOverride.setAdType("Postroll").validate("",1000);

        }catch (Exception e){
            extentTest.log(LogStatus.FAIL,e.getMessage());
            result = false;
        }

        org.testng.Assert.assertTrue(result, "Postroll Page Level Overriding tests failed");
    }
}
