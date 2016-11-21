package com.ooyala.playback.alice;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.ShareTabValidator;
import com.ooyala.playback.page.action.PlayPauseAction;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackLocalizationTests extends PlaybackWebTest {

	public static Logger logger = Logger
			.getLogger(PlaybackLocalizationTests.class);

	public PlaybackLocalizationTests() throws OoyalaException {
		super();
	}

	@Test(groups = "alice", dataProvider = "testUrls")
	public void testPlaybackLocalization(String testName, String url)
			throws OoyalaException {

		boolean result = false;
		PlayValidator play = pageFactory.getPlayValidator();
		PauseValidator pause = pageFactory.getPauseValidator();
		SeekValidator seek = pageFactory.getSeekValidator();
		PlayPauseAction playPauseAction = pageFactory.getPlayPauseAction();
		EventValidator eventValidator = pageFactory.getEventValidator();
		ShareTabValidator shareTabValidator = pageFactory
				.getShareTabValidator();

		try {
			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			play.waitForPage();

			injectScript("http://10.11.66.55:8080/alice.js");

			play.validate("playing_1", 60);

			logger.info("video playing");

			pause.validate("paused_1", 60);

			logger.info("video paused");

			play.validate("playing_2", 60);

			logger.info("video paying again");

			shareTabValidator.validate("", 60);

			eventValidator.eventAction("FULLSCREEN_BTN");

			logger.info("checked fullscreen");

			shareTabValidator.validate("", 60);

			eventValidator.eventAction("NORMAL_SCREEN");

			playPauseAction.startAction();

			seek.validate("seeked_1", 60);

			logger.info("video seeked");

			eventValidator.validate("played_1", 60);

			logger.info("video played");

			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertTrue(result, "Playback Localization tests failed");
	}
}
