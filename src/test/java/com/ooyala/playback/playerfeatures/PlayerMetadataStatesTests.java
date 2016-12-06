package com.ooyala.playback.playerfeatures;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EndScreenValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.StartScreenValidator;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlayerMetadataStatesTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlayerMetadataStatesTests.class);
	private PlayValidator play;
	private SeekValidator seek;
	private EventValidator eventValidator;
	private PauseValidator pause;
	private EndScreenValidator endScreenValidator;
	private StartScreenValidator startScreenValidator;

	public PlayerMetadataStatesTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testPlayerMetadataStates(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url);

            result = result && play.waitForPage();

            result = result && startScreenValidator.validate("", 60000);

			injectScript();

			// playAction.startAction();

            result = result && play.validate("playing_1", 60000);
			logger.info("video is playing");
			Thread.sleep(2000);

            result = result && pause.validate("paused_1", 60000);
			logger.info("video is paused");

            result = result && play.validate("playing_2", 60000);
			logger.info("video is playing again");

            result = result && seek.validate("seeked_1", 60000);
			logger.info("video seeked");

            result = result && eventValidator.validate("played_1", 60000);
			logger.info("video played");

            result = result && endScreenValidator.validate("END_SCREEN", 60000);

            result = result && eventValidator.eventAction("FULLSCREEN_BTN_1");

            result = result && endScreenValidator.validate("fullscreenChangedtrue", 50);
			logger.info("checked fullscreen");

            result = result && endScreenValidator.validate("", 60000);
            result = result && eventValidator.eventAction("FULLSCREEN_BTN_1");

		} catch (Exception e) {
			e.printStackTrace();
            result = false;
		}
		Assert.assertTrue(result, "Playback MetadataStates tests failed");
	}
}
