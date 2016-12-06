package com.ooyala.playback.playerfeatures;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.ControlBarValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.FullScreenValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPlayerControlsTests extends PlaybackWebTest {

	private static Logger logger = Logger
			.getLogger(PlaybackPlayerControlsTests.class);
	private EventValidator eventValidator;
	private PlayValidator play;
	private PauseValidator pause;
	private SeekValidator seek;
	private FullScreenValidator fullScreenValidator;
	private ControlBarValidator controlBarValidator;

	public PlaybackPlayerControlsTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testBasicPlaybackAlice(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			result = result && play.waitForPage();
			Thread.sleep(10000);

			injectScript();

			result = result && play.validate("playing_1", 60000);

			Thread.sleep(2000);

			result = result && pause.validate("paused_1", 60000);

			result = result && play.validate("playing_2", 60000);

			result = result && fullScreenValidator.validate("", 60000);

			result = result && controlBarValidator.validate("", 60000);

			result = result && seek.validate("seeked_1", 60000);

			logger.info("Verified that video is seeked");

			result = result && eventValidator.validate("played_1", 60000);

			logger.info("Verified that video is played");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		Assert.assertTrue(result, "Alice basic playback tests failed");
	}
}
