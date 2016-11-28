package com.ooyala.playback.amf.VAST;

import static com.relevantcodes.extentreports.LogStatus.PASS;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackVastPreIMAMidlAdsTests extends PlaybackWebTest{

	public PlaybackVastPreIMAMidlAdsTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	
	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPrerollOverlay(String testName, String url)
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
			
			playAction.startAction();
	        loadingSpinner();
	        event.validate("PreRoll_willPlayAds", 120);

	        event.validate("adsPlayed_1", 200);

	        event.validate("adPodEnd_vast_2_1", 180);

	        extentTest.log(PASS, "Played Vast Preroll Ads");

	        event.validate("playing_1", 90);

	        seekAction.seekTillEnd().startAction();

	        event.validate("MidRoll_willPlayAds", 100);

	        event.validate("adsPlayed_2", 200);

	        event.validate("AD_POD_END_IMA", 20);

	        extentTest.log(PASS, "Played IMA Midroll Ads");

	        event.validate("played_1", 200);

	        extentTest.log(PASS, "Verified VastPreIMAMidlAdsTests Ads Test");
			
			result = true;

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}

}
