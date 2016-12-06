package com.ooyala.playback.VTC;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DifferentElementValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.IsAdPlayingValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by snehal on 28/11/16.
 */
public class PlaybackAdVideoSamePluginDiffElementTests extends PlaybackWebTest {
	private PlayValidator play;
	private EventValidator eventValidator;
	private PlayAction playAction;
	private SeekValidator seekValidator;
	private DifferentElementValidator elementValidator;
	private IsAdPlayingValidator isAdPlayingValidator;

	PlaybackAdVideoSamePluginDiffElementTests() throws OoyalaException {
		super();
	}

	@Test(groups = "Playback", dataProvider = "testUrls")
	public void testSamePluginsDiffElementTests(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}
			Thread.sleep(2000);

			injectScript();

			result = result && playAction.startAction();

			result = result && eventValidator.validate("adsPlayed_1", 30000);
			logger.info("Ad played");

			result = result && elementValidator.validate("VIDEO_PATH", 30000);
			logger.info("Two different elements created for ad and main video");

			Thread.sleep(2000);

			result = result && eventValidator.validate("playing_1", 60000);
			logger.info("Video starts playing");

			result = result && seekValidator.validate("seeked_1", 60000);

			result = result && eventValidator.validate("played_1", 60000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		Assert.assertTrue(result,
				"Playback Same plugins Different Element test failed");
	}

}
