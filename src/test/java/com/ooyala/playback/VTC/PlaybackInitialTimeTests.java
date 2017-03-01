package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 1/3/17.
 */
public class PlaybackInitialTimeTests extends PlaybackWebTest{

    private static Logger logger = Logger.getLogger(PlaybackInitialTimeTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private PlayAction playAction;
    private PauseAction pauseAction;
    private SeekValidator seekValidator;
    private InitalTimeValidator initalTimeValidator;

    PlaybackInitialTimeTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testInitialTime(String testName, String url)
            throws OoyalaException {

        boolean result = true;
        try {
            driver.get(url);

            result = result && play.waitForPage();

            injectScript();

            result = result && playAction.startAction();

            result = result && eventValidator.validate("willPlaySingleAd_1",20000);

            result = result && eventValidator.validate("playing_1",20000);

            result = result && initalTimeValidator.validate("",30000);

            result = result && pauseAction.startAction();

            result = result && eventValidator.validate("paused_1",20000);

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
