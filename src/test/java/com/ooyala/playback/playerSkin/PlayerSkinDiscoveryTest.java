package com.ooyala.playback.playerSkin;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PlayerSkinButtonsValidator;
import com.ooyala.playback.page.PlayerSkinScrubberValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.StreamValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.ooyala.qe.common.util.PropertyReader;
import com.relevantcodes.extentreports.LogStatus;


public class PlayerSkinDiscoveryTest extends PlaybackWebTest {

    private static Logger logger = Logger.getLogger(PlayerSkinControlbar.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;
    private StreamValidator streamTypeValidator;
    private SeekAction seekAction;
    private PlayerSkinButtonsValidator skinValidator;
    private PlayerSkinScrubberValidator colorValidator;

    public PlayerSkinDiscoveryTest() throws OoyalaException {
        super();
    }

    @Test(groups = "playerskin", dataProvider = "testUrls")
    public void testBasicPlaybackStreams(String testName, UrlObject url) throws OoyalaException {

        boolean result = true;

        try {

            driver.get(url.getUrl());
            
           	injectScript();
           	result = result && play.waitForPage();
           	result = result && play.clickOnHiddenElement("PLAY_BUTTON");
           	
           	s_assert.assertTrue(skinValidator.clickButtonInMoreOptions("DISCOVERY_BTN"),"Discovery button"
           			+ " is not visible in more options screen");
           	
           s_assert.assertTrue(colorValidator.verifyWebElementCSSColor("DISCOVERY_BTN","color","red"), "Discovery "
           		+ "button color is not matching with coor in json file");
            s_assert.assertAll();
            
            

        } catch (Exception e) {
            logger.error("Exception while checking for discovery button" + e.getMessage());
            extentTest.log(LogStatus.FAIL, e);
            result = false;
        }
        Assert.assertTrue(result, "discovery button tests failed" + testName);
    }
}