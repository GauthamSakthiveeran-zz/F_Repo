package com.ooyala.playback.alice;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.ThumbnailValidator;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/22/16.
 */
public class PlaybackThumbnailTests extends PlaybackWebTest {

	private EventValidator eventValidator;
	private PlayValidator play;
	private PauseValidator pause;
	private SeekValidator seek;
	private ThumbnailValidator thumbnailValidator;

	public PlaybackThumbnailTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playback", dataProvider = "testUrls")
	public void testBasicPlaybackAlice(String testName, String url)
			throws OoyalaException {

		boolean result = false;

		try {
			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			play.waitForPage();

			Thread.sleep(10000);

			injectScript();

			play.validate("playing_1", 60);

			logger.info("Verifed that video is getting playing");

			Thread.sleep(5000);

			pause.validate("paused_1", 60);

			thumbnailValidator.validate("", 60);

			Thread.sleep(5000);

			play.validate("playing_2", 60);

			seek.validate("seeked_1", 60);

			eventValidator.validate("played_1", 60);

			logger.info("Verified that video is played");

			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertTrue(result, "Thumbnail test failed");
	}
}
