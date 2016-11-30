package com.ooyala.playback.amf.VAST;

import static com.relevantcodes.extentreports.LogStatus.PASS;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.OverlayValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPoddedwithOverlayStandaloneTests  extends PlaybackWebTest{

	public PlaybackPoddedwithOverlayStandaloneTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private OverlayValidator overlayValidator;
	
	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPoddedStandaloneOverlay(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			result = result && playValidator.waitForPage();

			injectScript();

			//play video
			result = result && playAction.startAction();


	        // verify podded preroll
	        result = result && event.validate("willPlaySingleAd_1", 60000);

	        result = result && event.validate("singleAdPlayed_1", 150000);


	        result = result && event.validate("willPlaySingleAd_2", 160000);

	        result = result && event.validate("singleAdPlayed_2", 160000);

	        extentTest.log(PASS, "Played Preroll podded Ads");

	        result = result && event.validate("playing_1", 160000);

	        result = result && overlayValidator.validate("nonlinearAdPlayed_1", 90000);

	        extentTest.log(PASS, "Overlay Played");

	        result = result && seekValidator.validate("seeked_1", 14000);

	        result = result && event.validate("played_1", 20000);

	        extentTest.log(PASS, "Main Video played successfully");


		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}


}
