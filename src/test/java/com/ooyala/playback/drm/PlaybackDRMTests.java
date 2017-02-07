package com.ooyala.playback.drm;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PlaybackDRMTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackDRMTests.class);
	private EventValidator eventValidator;
	private PlayValidator play;
	private PauseValidator pause;
	private SeekValidator seek;
	private PlayAction playAction;

	public PlaybackDRMTests() throws OoyalaException {
		super();
	}

	@Test(groups = "drm", dataProvider = "testUrls")
	public void testPlaybackDRM(String testName, String url)
			throws OoyalaException {
		boolean result = true;

		logger.info("Test Description :\n"
				+ testName.split(":")[1].toLowerCase());

		try {
			driver.get(url);

			// need to add logic for verifying description
			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			result = result && eventValidator.loadingSpinner();

			Thread.sleep(2000);

			result = result && pause.validate("paused_1", 60000);

			result = result && play.validate("playing_2", 60000);

			if (!(testName.split(":")[1]
					.equalsIgnoreCase(" elemental fairplay fairplay hls + opt")))
				result = result && seek.validate("seeked_1", 60000);
			else
				((JavascriptExecutor) driver)
						.executeScript("pp.seek(pp.getDuration()-2);");

			result = result && eventValidator.validate("played_1", 60000);

			logger.info("Verified that video is played");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "DRM tests failed : " + testName);
	}
}