package com.ooyala.playback.amf.vast.pageleveloverride;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.VastPageLevelOverridingValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by suraj on 8/28/17.
 */
public class PlaybackPreMidPostPageLevelOverrideTests extends PlaybackWebTest {
    public PlaybackPreMidPostPageLevelOverrideTests() throws OoyalaException {
    }
    private PlayValidator playValidator;
    private VastPageLevelOverridingValidator pageOverride;
    private PlayAction playAction;
    private EventValidator event;
    private static Logger logger = Logger.getLogger(PlaybackPreMidPostPageLevelOverrideTests.class);

    @Test(dataProvider = "testUrls",groups = "pageLevelOverriding")
    public void premidpostPageLevelOverride(String testName, UrlObject url){
        boolean result= true;
        try {
            driver.get(url.getUrl());
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playAction.startAction();
            result = result && event.validate("PreRoll_willPlaySingleAd_1", 60000);
            result = result && event.validate("singleAdPlayed_1", 120000);
            result = result && event.validate("playing_1",10000);
            result = result && pageOverride.setAdType("Preroll").validate("",10000);
            result = result && event.validate("playing_2",10000);
            result = result && pageOverride.setAdType("midroll").validate("",300000);
            result = result && event.validate("playing_3",10000);
            result = result && pageOverride.setAdType("postroll").validate("",300000);
            result = result && event.validate("played_1",10000);
        }catch (Exception e){
            extentTest.log(LogStatus.FAIL,e.getMessage());
            logger.error(e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Preroll Page Level Overriding tests failed");
    }
}
