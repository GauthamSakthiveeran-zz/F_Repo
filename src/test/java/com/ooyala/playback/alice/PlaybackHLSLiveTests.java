package com.ooyala.playback.alice;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.ControlBarValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.FullScreenValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.LiveAction;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackHLSLiveTests extends PlaybackWebTest {

	public PlaybackHLSLiveTests() throws OoyalaException {
		super();
	}

	@Test(groups = "alice", dataProvider = "testUrls")
	public void testHLSLive(String testName, String url) throws OoyalaException {

		boolean result = false;
		PlayValidator play = pageFactory.getPlayValidator();
		PauseValidator pause = pageFactory.getPauseValidator();
		SeekValidator seek = pageFactory.getSeekValidator();
		EventValidator eventValidator = pageFactory.getEventValidator();
		ControlBarValidator controlBarValidator = pageFactory
				.getControlBarValidator();
		FullScreenValidator fullScreenValidator = pageFactory
				.getFullScreenValidator();
		LiveAction liveAction = pageFactory.getLiveAction();

		if (getBrowser().equalsIgnoreCase("safari")) {
			try {
				driver.get(url);
				if (!getPlatform().equalsIgnoreCase("android")) {
					driver.manage().window().maximize();
				}

				play.waitForPage();

				injectScript("http://10.11.66.55:8080/alice.js");

				play.validate("playing_1", 60);

				logger.info("video is playing");

				pause.validate("paused_1", 60);

				logger.info("video paused");

				controlBarValidator.validate("", 60);
				// to-do add ooyala logo to the test page

				fullScreenValidator.validate("FULLSCREEN_BTN_1", 60);

				logger.info("playing video in full screen");

				pause.validate("paused_2", 60);

				logger.info("video paused in fullscreen");

				play.validate("playing_2", 60);

				logger.info("video playing in fullscreen");

				liveAction.startAction();

				eventValidator.validate("played_1", 60);

				logger.info("video played");
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			Assert.assertTrue(result, "Alice basic playback tests failed");

		} else {

			throw new SkipException("Test PlaybackHLSLive Is Skipped");
		}
	}
}
