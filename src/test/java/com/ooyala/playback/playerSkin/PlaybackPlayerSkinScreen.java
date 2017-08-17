package com.ooyala.playback.playerSkin;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by Gautham
 */
public class PlaybackPlayerSkinScreen extends PlaybackWebTest {
	private PlayValidator play;
	private PlayerSkinScrubberValidator skinValidator;
	private EventValidator eventValidator;
	private SeekAction seekAction;
    private static Logger logger = Logger
            .getLogger(PlaybackPlayerSkinScreen.class);


    public PlaybackPlayerSkinScreen() throws OoyalaException {
        super();
    }

    @Test(groups = "PlayerSkin", dataProvider = "testUrls")
    public void testFCCClosedcaption(String testName, UrlObject url) throws OoyalaException {

        boolean result = true;
        try {
        	
        	String urlLink = url.getUrl();

            driver.get(urlLink);
            
            s_assert.assertTrue(play.waitForPage(),"Page Loading so long");
            
            injectScript();
            
            s_assert.assertTrue(skinValidator.verifyWebElementCSSColor("PLAY_BUTTON","color","red"), "cssProperty Check Failed For Play Button color ");
            
            play.clickOnIndependentElement("PLAY_BUTTON");
            
            eventValidator.playVideoForSometime(5);
            
            s_assert.assertTrue(skinValidator.verifyWebElementCSSColor("SCRUBBER_BAR","background-color","red"), "cssProperty Check Failed For SCRUBBER_BAR background color ");

            s_assert.assertTrue(skinValidator.moveToWebElementCSSColor("SHARE_BTN", "color","green"), "cssProperty Check Failed For Share Button color ");

            s_assert.assertTrue(skinValidator.moveToWebElementCSSColor("PAUSE_BUTTON", "color","green"), "cssProperty Check Failed For Pause Button color ");

            s_assert.assertTrue(skinValidator.moveToWebElementCSSColor("VOLUME_MAX", "color","green"), "cssProperty Check Failed For Volume Button color ");

            s_assert.assertTrue(skinValidator.moveToWebElementCSSColor("BITRATE", "color","green"), "cssProperty Check Failed For Bitrate button color ");

            s_assert.assertTrue(skinValidator.moveToWebElementCSSColor("FULLSCREEN_BTN", "color","green"), "cssProperty Check Failed For FullScreen button color ");
            
            s_assert.assertTrue(skinValidator.verifyWebElementCSSColor("PLAYED","background-color","yellow"), "cssProperty Check Failed For  Played SCRUBBER_BAR background color ");
 
            s_assert.assertTrue(skinValidator.verifyWebElementCSSColor("BUFFERED","background-color","green"), "cssProperty Check Failed For Buffered SCRUBBER_BAR background color ");

            seekAction.setTime(100).startAction();
            
            s_assert.assertTrue(eventValidator.validate("played_1", 120000),"Video not seeked Properly");

            s_assert.assertTrue(skinValidator.verifyWebElementCSSColor("SCRUBBER_BAR","background-color","red"), "cssProperty Check Failed For SCRUBBER_BAR background color ");

            s_assert.assertAll();
        } catch (Exception e) {
            logger.error(e);
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback FCC CC tests failed :"+testName);
    }
}
