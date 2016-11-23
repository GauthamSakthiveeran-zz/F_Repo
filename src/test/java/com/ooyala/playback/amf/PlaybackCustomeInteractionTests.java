package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdSkipButtonValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.VolumeValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackCustomeInteractionTests extends PlaybackWebTest{

	public PlaybackCustomeInteractionTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private VolumeValidator volumeValidator;
	private AdSkipButtonValidator adSkipButtonValidator;
	
	static int index =0;
	
	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyCustomeInteractionAd(String testName, String url) throws Exception {
		
		boolean result = false;
		
		try {
			
			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			playValidator.waitForPage();
			Thread.sleep(10000);
			
			injectScript();
			
			playAction.startAction();
			
			event.validate("willPlaySingleAd_1", 190);
			
			extentTest.log(PASS, "Preroll Ad started");

			event.validate("showAdSkipButton_1", 60);

			extentTest.log(PASS, "Skip button for Ads shown");
	        sleep(5000);

	        volumeValidator.validate("", 60);

	        extentTest.log(PASS, "Clicked on Skip button");

	        adSkipButtonValidator.validate("", 60);
	        
	        event.validate("singleAdPlayed_1", 190);

	        extentTest.log(PASS, "Preroll Ad Completed");

	        event.validate("playing_1", 60);

	        extentTest.log(PASS, "Main video started to play");
	        sleep(500);
	        
	        seekAction.seekPlayback();

	        event.validate("seeked_1", 180);

	        event.validate("played_1", 200);

	        extentTest.log(PASS, "Video completed playing");
			
			result = true;
			
		}catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback CC Enabled MidRoll Ads tests failed");
		
	}
	
}
