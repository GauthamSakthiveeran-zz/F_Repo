package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.InitialTimeValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 10/4/17.
 */
public class PlaybackInitialTimeForLongAssetTests extends PlaybackWebTest{

    private static Logger logger = Logger.getLogger(PlaybackInitialTimeForLongAssetTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private PlayAction playAction;
    private PauseAction pauseAction;
    private SeekValidator seekValidator;
    private InitialTimeValidator initialTimeValidator;

    PlaybackInitialTimeForLongAssetTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testInitialTimeForLongAsset(String testName, UrlObject url)
            throws OoyalaException {

        boolean result = true;
        try {
            driver.get(url.getUrl());

            result = result && play.isPageLoaded();

            injectScript();

            result = result && initialTimeValidator.validatePlayHeadTime();

            result = result && pauseAction.startAction();

            result = result && eventValidator.validate("paused_1",20000);

            result = result && playAction.startAction();

            result = result && eventValidator.validate("playing_2",20000);

            result = result && seekValidator.validate("seeked_1",20000);

            result = result && eventValidator.validate("videoPlayed_1", 60000);


        } catch (Exception e) {
            logger.error("\n************************** \n"+e+"\n*********************************\n");
            result = false;
            extentTest.log(LogStatus.FAIL, "Playback Initial time tests failed", e);
        }
        Assert.assertTrue(result, "Playback Initial time tests failed");
    }
}
