package com.ooyala.playback.playerskin;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PlayerSkinButtonsValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.StreamValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.ooyala.qe.common.util.PropertyReader;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/21/16.
 */
public class PlayerSkinControlbar extends PlaybackWebTest {

    private static Logger logger = Logger.getLogger(PlayerSkinControlbar.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;
    private StreamValidator streamTypeValidator;
    private SeekAction seekAction;
    private PlayerSkinButtonsValidator skinValidator;

    public PlayerSkinControlbar() throws OoyalaException {
        super();
    }

    @Test(groups = "streams", dataProvider = "testUrls")
    public void testBasicPlaybackStreams(String testName, UrlObject url) throws OoyalaException {

        //boolean result = true;

        try {
        	
        
        	
        	     String URL = url.getUrl();
        	     System.out.println("url is "+URL);
            
           // URL = replaceSkin(URL,"playerskin/html5_buttonsDisabled.js", "playerskin/skin_buttonsDisabled.json", "playerskin/html5-skin.css");

            driver.get(URL);
            
           	injectScript();
            //play.waitForPage();

            s_assert.assertFalse(skinValidator.playButtonVisibleOnStartScreen(), "Play button is visible on start screen");
    
            play.clickOnHiddenElement("PLAY_BUTTON");
            s_assert.assertTrue(skinValidator.validateControlbarButtonsNotPresent(), "controlbar buttons present");
            s_assert.assertFalse(skinValidator.pauseButtonVisibleOnPauseScreen(), "Pause button is visible on screen");         
            eventValidator.validate("played_1", 120000);
            s_assert.assertFalse(skinValidator.replayButtonVisibleOnEndScreen(), "Replay button is visible on End screen");
        
            s_assert.assertAll();
            
            

        } catch (Exception e) {
            logger.error("Exception while checking basic playback " + e.getMessage());
            extentTest.log(LogStatus.FAIL, e);
            //result = false;
        }
        //s_assert.assertAll();
        //Assert.assertTrue(result, "Basic playback tests failed" + testName);
    }
}