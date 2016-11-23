package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackMidRollAdsTests extends PlaybackWebTest{

	public PlaybackMidRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	
	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyMidRoll(String testName, String url)
			throws OoyalaException {
		
		boolean result = false;
		
		try {
			
			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			playValidator.waitForPage();
			Thread.sleep(2000);
			
			injectScript();
			
			playValidator.validate("playing_1", 60);
	        extentTest.log(PASS, "Video started playing");
	        Thread.sleep(5000);

	        // TODO - working on implementing the commented code.
	        
	        // As there is problem for pulse asset that if we seek the video then ads get skip therefore adding below condition
//	        if(!(Description.equalsIgnoreCase("Midroll_Bitmovin_Pulse")||Description.equalsIgnoreCase("Midroll_HTML5_Pulse")||Description.equalsIgnoreCase("BitmovinMidroll_IMA")))
//	            ((JavascriptExecutor) webDriver).executeScript("pp.seek(pp.getDuration()-15);");

	        loadingSpinner();
	        event.validate("videoPlaying_1", 90);
	        event.validate("MidRoll_willPlaySingleAd_1", 120);
	        extentTest.log(PASS, "Midroll Ad started to play");
	        event.validate("singleAdPlayed_1", 160);
	        extentTest.log(PASS, "Midroll Ad ended");

	        // Seek the video for Bitmovin Pulse
//	        if(Description.equalsIgnoreCase("Midroll_Bitmovin_Pulse")||Description.equalsIgnoreCase("Midroll_HTML5_Pulse"))
//	        {
//	            event.validate("singleAdPlayed_2", 160);
//	            Thread.sleep(2000);
//	            ((JavascriptExecutor) webDriver).executeScript("pp.seek(pp.getDuration()-10);");
//	        }

	        // Seek the video for Bitmovin IMA
//	        if (Description.equalsIgnoreCase("BitmovinMidroll_IMA")){
//	            ((JavascriptExecutor) webDriver).executeScript("pp.seek(pp.getDuration()-10);");
//	        }

	        event.validate("videoPlayed_1", 160);
	        event.validate("played_1", 160);
	        extentTest.log(PASS, "Video Played");
	        extentTest.log(PASS, "Verified MidrollAdsTest");
			
			result = true;
			
		}catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified");
	}
}
