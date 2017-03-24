package com.ooyala.playback.playerfeatures;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.SeekAction;
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
    private SeekAction seekAction;
    private PauseValidator pause;
    private ControlBarValidator control;
    private FullScreenValidator fullScreen;
    private EventValidator eventValidator;
    private StreamTypeValidator streamTypeValidator;
    public String stream;

    PlaybackNewProcessingProfileTests() throws OoyalaException{
        super();
    }

    @Test(groups = "playerFeatures", dataProvider = "testUrls")
    public void testNewProcessingProfile(String testName, String url){

        boolean result = true;

        if(testName.contains("//")){
            stream = testName.split("//")[1].toLowerCase();
        }

        try{
            driver.get(url);

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1",30000);

            if(testName.contains("//")){

                result = result && eventValidator.validate("videoPlayingurl",40000);

                result = result && streamTypeValidator.validateStream("videoPlayingurl",stream);
            }

            result = result && pause.validate("paused_1", 30000);

            result = result && control.validate("",60000);

            result = result && play.validate("playing_2",30000);

            result = result && fullScreen.getFullScreen();

            result = result &&fullScreen.getNormalScreen();

            result = result && seekAction.seek(10,true);

        }catch(Exception e)
        {
            logger.error(e);
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }

        Assert.assertTrue(result, "New Processing Profile Playback Tests failed"+testName);
    }

}
