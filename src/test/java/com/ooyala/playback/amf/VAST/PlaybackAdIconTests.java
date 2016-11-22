package com.ooyala.playback.amf.VAST;


import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackAdIconTests extends PlaybackWebTest {
	
	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;

	public PlaybackAdIconTests() throws OoyalaException {
		super();
	}
	
	
	
	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyADIcon(String testName, String url)
			throws OoyalaException {
		
		boolean result = false;
		
		
		
		try {
			
			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			playValidator.waitForPage();
			Thread.sleep(10000);
			
			//TODO
			injectScript("http://192.168.0.43:8080/common.js");
			injectScript("http://192.168.0.43:8080/amf/amf.js");
			
			playAction.startAction();
			
			loadingSpinner();
			
			event.validate("willPlaySingleAd_1", 60);
			event.validate("singleAdPlayed_1", 160);
			
			playValidator.validate("playing_1", 190);
			
			seekValidator.validate("seeked_1", 190);
			
			event.validate("played_1", 190);
			
			result = true;
			
			extentTest.log(LogStatus.PASS, "Main Video played successfully");
			
		}catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback Ad Icon tests failed");
		
	}

}
