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
import com.ooyala.playback.page.action.LiveAction;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackLiveTests extends PlaybackWebTest {

	private PlayValidator play;
	private PauseValidator pause;
	private EventValidator eventValidator;
	private ControlBarValidator controlBarValidator;
	private FullScreenValidator fullScreenValidator;
	private LiveAction liveAction;

	public PlaybackLiveTests() throws OoyalaException {
		super();
	}

	@Test(groups = "Playback", dataProvider = "testUrls")
	public void testHLSLive(String testName, String url) throws OoyalaException {

		boolean result = true;

		if (getBrowser().equalsIgnoreCase("safari")) {
			try {
				driver.get(url);
				if (!getPlatform().equalsIgnoreCase("android")) {
					driver.manage().window().maximize();
				}

                result = result && play.waitForPage();

				injectScript();

                result = result && play.validate("playing_1", 60);

				logger.info("video is playing");

                result = result && pause.validate("paused_1", 60);

				logger.info("video paused");

                result = result && controlBarValidator.validate("", 60);
				// to-do add ooyala logo to the test page

                result = result && fullScreenValidator.validate("FULLSCREEN_BTN_1", 60);

				logger.info("playing video in full screen");

                result = result && pause.validate("paused_2", 60);

				logger.info("video paused in fullscreen");

                result = result && play.validate("playing_2", 60);

				logger.info("video playing in fullscreen");

                result = result && liveAction.startAction();

                result = result && eventValidator.validate("played_1", 60);

				logger.info("video played");
			} catch (Exception e) {
				e.printStackTrace();
                result = false;
			}
			Assert.assertTrue(result, "Playback HLSLive tests failed");
		} else {
			throw new SkipException("Test PlaybackHLSLive Is Skipped");
		}
	}
}
