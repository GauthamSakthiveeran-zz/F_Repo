package com.ooyala.playback.amf.vast.pageleveloverride;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.VastPageLevelOverridingValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.annotations.Test;

/**
 * Created by Jitendra on 8/23/17.
 */

public class PlaybackMidrollPageLevelOverrideTests extends PlaybackWebTest{
    public PlaybackMidrollPageLevelOverrideTests() throws OoyalaException {
        super();
    }
    private PlayValidator playValidator;
    private VastPageLevelOverridingValidator pageOverride;
    private PlayAction playAction;
    private EventValidator event;

    @Test(dataProvider = "testUrls",groups = "pageLevelOverriding")
    public void midrollPageLevelOverride(String testName, UrlObject url){
        boolean result= true;
        try {
            driver.get(url.getUrl());
            result = result && playValidator.waitForPage();
            injectScript();
            pageOverride.getTotalDuration();
            result = result && playAction.startAction();
            result = result && event.validate("playing_1", 60000);
            result = result && event.validate("MidRoll_willPlaySingleAd_1", 150000);
            result = result && event.validate("singleAdPlayed_1", 120000);
            result = result && event.validate("playing_2",10000);
            result = result && pageOverride.setAdType("Midroll").validate("",1000);

        }catch (Exception e){
            extentTest.log(LogStatus.FAIL,e.getMessage());
            result = false;
        }

        org.testng.Assert.assertTrue(result, "Midroll Page Level Overriding tests failed");
    }
}
