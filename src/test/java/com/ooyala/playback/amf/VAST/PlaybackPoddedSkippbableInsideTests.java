package com.ooyala.playback.amf.VAST;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdSkipButtonValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPoddedSkippbableInsideTests extends PlaybackWebTest {

	public PlaybackPoddedSkippbableInsideTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private AdSkipButtonValidator skipValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPoddedInsideSkipButton(String testName, String url)
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

	        // verify first podded preroll played
	        event.validate("willPlaySingleAd_1", 150);
	        event.validate("singleAdPlayed_1", 150);
	        loadingSpinner();

	        //second preroll podded starts
	        event.validate("willPlaySingleAd_2", 150);

	        //skip buttons shows for second preroll podded

	        skipValidator.validate("", 120);
	        
	        loadingSpinner();

	        // verify third ad played
	        event.validate("willPlaySingleAd_3", 150);

	        event.validate("singleAdPlayed_3", 150);

	        extentTest.log(PASS, "Played Preroll podded Ads");
	        sleep(3000);
	        event.validate("playing_1", 190);
	        sleep(500);


	        seekValidator.validate("seeked_1", 190);
	        event.validate("played_1", 190);
	        extentTest.log(PASS, "Main Video played successfully");
	        extentTest.log(PASS, "Verified PlaybackPoddedSkippbableInsideTests");

			result = true;

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}


}
