package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackMidRollPoddedAdsTests extends PlaybackWebTest{

	public PlaybackMidRollPoddedAdsTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private PoddedAdValidator poddedAdValidator;
	
	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyMidrollPodded(String testName, String url)
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
			
			seekValidator.validate("seeked_1", 60);
			
			event.validate("videoPlayed_1", 200);
			
			poddedAdValidator.validate("countPoddedAds", 120);
			
			event.validate("seeked_1", 60);
			event.validate("played_1", 200);
	        extentTest.log(PASS, "Verified MidrollPodded Ads Tests");
			
			result = true;
			
		}catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified");
	}

}
