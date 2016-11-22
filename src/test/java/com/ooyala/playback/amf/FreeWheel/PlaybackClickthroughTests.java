package com.ooyala.playback.amf.FreeWheel;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdClickThroughValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackClickthroughTests extends PlaybackWebTest{

	public PlaybackClickthroughTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private AdClickThroughValidator clickThrough;
	static int index =0;
	
	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyClickthrough(String testName, String url) throws Exception {
		
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
			
			event.validate("willPlaySingleAd_1", 60);
			
			extentTest.log(PASS, "Ad started to play");
			
			clickThrough.clickThroughAds(testData,index++);
			
			event.validate("singleAdPlayed_1", 190);
			
			extentTest.log(PASS, "Ad completed");

	        loadingSpinner();

	        event.validate("playing_1", 160);

	        extentTest.log(PASS, "Video started");
	        sleep(5000);

	        seekAction.seekPlayback();
	        
	        event.validate("played_1", 160);
	        
	        extentTest.log(PASS, "Video ended");

	        extentTest.log(PASS, "Verified Clickthrough functionality");
			
			result = true;
			
		}catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback CC Enabled MidRoll Ads tests failed");
		
	}
	

}
