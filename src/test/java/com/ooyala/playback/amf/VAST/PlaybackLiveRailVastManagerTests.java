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

		boolean result = true;

		try {

			driver.get(url);

            result = result && playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

            result = result && playValidator.validate("playing_1", 60000);

			extentTest.log(PASS, "Main video started to play");
			sleep(500);

			// verify play pause
            result = result && pauseValidator.validate("paused_1", 60000);
			sleep(2000);
            result = result && playValidator.validate("playing_1", 60000);

			// Verify fullscreen
            result = result && fullScreenValidator.validate("", 60000);

			// verify volume
            result = result && volumeValidator.validate("", 60000);

			// verify seek
            result = result && seekValidator.validate("seeked_1", 60000);

            result = result && event.validate("played_1", 190000);
			extentTest.log(PASS, "Video completed palying");
			extentTest.log(PASS, "verified LiveRail with Vast Manager");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}

}
