package com.ooyala.playback.amf.vast.pageleveloverride;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.VastPageLevelOverridingValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by suraj on 8/29/17.
 */
public class PlaybackOverlayPageLevelOverrideTests extends PlaybackWebTest {

    public PlaybackOverlayPageLevelOverrideTests() throws OoyalaException {
    }

    private PlayValidator playValidator;
    private VastPageLevelOverridingValidator pageOverride;
    private PlayAction playAction;
    private EventValidator event;
    private SeekAction seekAction;
    private static Logger logger = Logger.getLogger(PlaybackOverlayPageLevelOverrideTests.class);

    @Test(dataProvider = "testUrls",groups = "pageLevelOverriding")
    public void prerollOverlayPageLevelOverrideTest(String description, UrlObject url){
        boolean result= true;
        String overlayType = description.split("and")[1].trim().toLowerCase();
        try {
            driver.get(url.getUrl());
            result = result && playValidator.waitForPage();
            injectScript();
            pageOverride.getTotalDuration();
            result = result && playAction.startAction();
            result = result && event.validate("willPlayNonlinearAd_1", 50000);
            result = result && event.validate("nonLinearAdDisplayed_1",10000);
            result = result && pageOverride.setAdType(overlayType).validate("nonLinearAdDisplayed_1",10000);
            result = result && event.validate("playing_1",10000);

        }catch (Exception e){
            extentTest.log(LogStatus.FAIL,e.getMessage());
            logger.error(e.getMessage());
            result = false;
        }

        Assert.assertTrue(result, "Preroll Page Level Overriding tests failed");
    }
}
