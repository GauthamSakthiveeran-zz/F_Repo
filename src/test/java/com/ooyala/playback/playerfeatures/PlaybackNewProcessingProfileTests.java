package com.ooyala.playback.playerfeatures;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PlaybackNewProcessingProfileTests extends PlaybackWebTest {

    private static Logger logger = Logger
            .getLogger(PlaybackNewProcessingProfileTests.class);

    private PlayValidator play;
    private SeekValidator seek;
    private PauseValidator pause;
    private ControlBarValidator control;
    private FullScreenValidator fullScreen;

    PlaybackNewProcessingProfileTests() throws OoyalaException{
        super();
    }

    @Test(groups = "playerFeatures", dataProvider = "testUrls")
    public void testNewProcessingProfile(String testName, String url){
        boolean result = true;
        try{
            driver.get(url);

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1",30000);

            result = result && pause.validate("paused_1", 30000);

            result = result && control.validate("",60000);

            result = result && fullScreen.getFullScreen();

            result = result &&fullScreen.getNormalScreen();

            result = result && play.validate("playing_2",30000);

            result = result && seek.validate("",60000);

        }catch(Exception e)
        {
            logger.error(e);
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }

        Assert.assertTrue(result, "New Processing Profile Playback Tests failed"+testName);
    }

}
