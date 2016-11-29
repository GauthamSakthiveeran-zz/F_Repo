package com.ooyala.playback.alice;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.ReplayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/17/16.
 */
public class PlaybackReplayVideoTests extends PlaybackWebTest {

	private PlayValidator play;
	private SeekValidator seek;
	private EventValidator eventValidator;
	private ReplayValidator replayValidator;

	public PlaybackReplayVideoTests() throws OoyalaException {
		super();
	}

	@Test(groups = "Player", dataProvider = "testUrls")
	public void testVideoReplay(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url);

            result = result && play.waitForPage();

			injectScript();

            result = result && play.validate("playing_1", 60);

			logger.info("video is playing");

			Thread.sleep(2000);

            result = result && seek.validate("seeked_1", 60);

			logger.info("video seeked");

            result = result && eventValidator.validate("played_1", 200);

			logger.info("video played");

            result = result && replayValidator.validate("replay_1", 60);

			logger.info("video replayed");

		} catch (Exception e) {
			e.printStackTrace();
            result = false;
		}
		Assert.assertTrue(result, "Playback Replay tests failed");
	}
}
