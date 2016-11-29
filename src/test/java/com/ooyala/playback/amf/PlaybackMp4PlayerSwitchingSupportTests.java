package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackMp4PlayerSwitchingSupportTests extends PlaybackWebTest {

	public PlaybackMp4PlayerSwitchingSupportTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private PauseAction pauseAction;
	
	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPlaybackOfOSMFMp4(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);
            result = result && playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

            result = result && playAction.startAction();
			loadingSpinner();
			
	        event.validate("willPlaySingleAd_1", 50000);
	        extentTest.log(PASS, "Ad starts to play");
	        event.validate("singleAdPlayed_1", 190000);
	        extentTest.log(PASS, "Ad Played completely");
	        event.validate("playing_1", 120000);
	        sleep(10000);

	        pauseAction.startActionOnScreen();
	        sleep(5000);
	        playAction.startActionOnScreen();

	        extentTest.log(PASS, "Verified Play Pause Functionality");

	        sleep(5000);

            result = result && seekValidator.validate("seeked_1", 190000);
	        extentTest.log(PASS, "Seek successful");

            result = result &&  event.validate("played_1", 190000);
	        extentTest.log(PASS, "Video played");

	        extentTest.log(PASS, "verified Playback of OSMF MP4 Asset");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified");

	}


}
