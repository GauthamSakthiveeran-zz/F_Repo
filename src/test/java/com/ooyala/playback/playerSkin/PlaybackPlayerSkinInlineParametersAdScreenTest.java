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

//Function to test AdMarque and AdControl bar
public class PlaybackPlayerSkinInlineParametersAdScreenTest extends PlaybackWebTest {
	private PlayValidator play;
	private PlayerSkinLocalizationValidator skinLocalizationValidator;
	private PlayAction playAction;
	private EventValidator event;
	private PlayerSkinScrubberValidator adScrubberValidator;

    private static Logger logger = Logger
            .getLogger(PlaybackPlayerSkinInlineParametersAdScreenTest.class);


    public PlaybackPlayerSkinInlineParametersAdScreenTest() throws OoyalaException {
        super();
    }

 
    @Test(groups = "PlayerSkin", dataProvider = "testUrls")
    public void testInlineParameters(String testName, UrlObject url) throws OoyalaException {

        boolean result = true;
        try {
        	
        	String urlLink = url.getUrl();

            driver.get(urlLink);    
            
            injectScript();
            
            result = result && play.waitForPage();
            

			result = result && playAction.startAction();
			
			Thread.sleep(4000);
			
			result = result && adScrubberValidator.isElementNotPresentinAdScreen();

			result = result && event.validate("singleAdPlayed_1", 60000);

			result = result && event.loadingSpinner();
			
		    result = result && event.playVideoForSometime(3);
    
			result = result && event.validate("playing_1", 60000);


            

        } catch (Exception e) {
            logger.error(e);
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "PlayerSkin Localization tests failed :"+testName);
    }
}
