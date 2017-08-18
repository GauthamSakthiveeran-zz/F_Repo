package com.ooyala.playback.playerfeatures;

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
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
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
	public void testPlayerMetadataStates(String testName, UrlObject url)
			throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			result = result && startScreenValidator.validateMetadata(url);

			injectScript();

			result = result && play.validate("playing_1", 60000);
			
			result = result && eventValidator.playVideoForSometime(2);

			result = result && pause.validate("paused_1", 60000);

			result = result && play.validate("playing_2", 60000);

			result = result && seek.validate("seeked_1", 60000);

			result = result && eventValidator.validate("played_1", 60000);

			result = result && endScreenValidator.validate("END_SCREEN", 60000);

			result = result && eventValidator.eventAction("FULLSCREEN_BTN_1");

			result = result
					&& endScreenValidator.validate("fullscreenChangedtrue", 10000);

			result = result && endScreenValidator.validate("", 60000);
			result = result && eventValidator.eventAction("FULLSCREEN_BTN_1");

			result = result && eventValidator.validatePlaybackReadyEvent(2000);

			result = result && eventValidator.validate("buffering_1", 1000);

		} catch (Exception e) {
			logger.error(e.getMessage());
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Playback MetadataStates tests failed");
	}
}
