package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

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

		boolean result = false;

		try {

			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

			//play video
			playAction.startAction();

	        loadingSpinner();

	        // verify podded preroll
	        event.validate("willPlaySingleAd_1", 60);

	        event.validate("singleAdPlayed_1", 150);

	        loadingSpinner();

	        event.validate("willPlaySingleAd_2", 160);

	        event.validate("singleAdPlayed_2", 160);

	        extentTest.log(PASS, "Played Preroll podded Ads");
	        sleep(3000);

	        event.validate("playing_1", 160);

	        sleep(500);

	        // Verify overlay
	        
	        overlayValidator.validate("nonlinearAdPlayed_1", 90);

	        extentTest.log(PASS, "Overlay Played");

	        seekValidator.validate("seeked_1", 140);

	        event.validate("played_1", 200);

	        extentTest.log(PASS, "Main Video played successfully");

			result = true;

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}


}
