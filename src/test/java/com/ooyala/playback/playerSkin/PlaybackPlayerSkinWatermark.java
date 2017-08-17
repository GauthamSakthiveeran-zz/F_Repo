package com.ooyala.playback.playerSkin;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Created by Gautham
 */
public class PlaybackPlayerSkinWatermark extends PlaybackWebTest {
	private PlayValidator play;
	private WaterMarkValidator watermarkValidator;

    private static Logger logger = Logger
            .getLogger(PlaybackPlayerSkinWatermark.class);


    public PlaybackPlayerSkinWatermark() throws OoyalaException {
        super();
    }

 
    @Test(groups = "PlayerSkin", dataProvider = "testUrls")
    public void testWatermark(String testName, UrlObject url) throws OoyalaException {

        boolean result = true;
        try {
        	
        	String urlLink = url.getUrl();
        	
        	driver.get(urlLink);    
            
            result = result && play.waitForPage();
            
            injectScript();
    
            result = result && play.validate("playing_1", 60000);
            
            result = result &&  watermarkValidator.validate("", 60000);
            

        } catch (Exception e) {
            logger.error(e);
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "PlayerSkin Localization tests failed :"+testName);
    }
}
