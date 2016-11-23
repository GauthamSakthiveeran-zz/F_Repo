package com.ooyala.playback.amf.VAST;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.FullScreenValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.VolumeValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackLiveRailVastManagerTests extends PlaybackWebTest {

	public PlaybackLiveRailVastManagerTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private PauseValidator pauseValidator;
	private FullScreenValidator fullScreenValidator;
	private VolumeValidator volumeValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPlaybackLiveRailVastTests(String testName, String url)
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

			extentTest.log(PASS, "Main video started to play");
			sleep(500);

			// verify play pause
			pauseValidator.validate("paused_1", 60);
			sleep(2000);
			playValidator.validate("playing_1", 60);

			// Verify fullscreen
			fullScreenValidator.validate("", 60);

			// verify volume
			volumeValidator.validate("", 60);

			// verify seek
			seekValidator.validate("seeked_1", 60);

			event.validate("seeked_1", 190);
			event.validate("played_1", 190);
			extentTest.log(PASS, "Video completed palying");
			extentTest.log(PASS, "verified LiveRail with Vast Manager");

			result = true;

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}

}
