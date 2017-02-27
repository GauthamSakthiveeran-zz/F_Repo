package com.ooyala.playback.VTC;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by jitendra on 25/11/16.
 */
public class PlaybackVerifyEventsTests extends PlaybackWebTest {

	private static Logger logger = Logger
			.getLogger(PlaybackVerifyEventsTests.class);
	private PlayValidator play;
	private PlayAction playAction;
	private EventValidator eventValidator;
	private PauseValidator pauseValidator;
	private SeekValidator seekValidator;

	public PlaybackVerifyEventsTests() throws OoyalaException {
		super();
	}

	@Test(groups = "Playback", dataProvider = "testUrls")
	public void testVerifyEvents(String testName, String url) {

		logger.info("Test url for " + testName + " is : " + url);

		boolean result = true;

		try {
			driver.get(url);

			result = result && play.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result
					&& eventValidator.validate("willPlaySingleAd_1", 10000);

			result = result && eventValidator.validate("adsPlayed_1", 20000);

			result = result && eventValidator.validate("playing_1", 60000);

			result = result
					&& eventValidator.validate("videoSetInitialTime_1", 60000);

			result = result && eventValidator.validate("videoPlay_1", 60000);

			result = result
					&& eventValidator.validate("videoWillPlay_1", 60000);

			result = result && eventValidator.validate("videoPlaying_1", 60000);

			result = result && pauseValidator.validate("videoPause_1", 60000);

			result = result && play.validate("playing_2", 10000);

			result = result && seekValidator.validate("seeked_1", 6000);

			result = result && eventValidator.validate("videoPaused_1", 60000);

			result = result && eventValidator.validate("videoPlayed_1",50000);

		} catch (Exception e) {
			logger.error("Exception while checking Controller Event test  "+e.getMessage());
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}

		Assert.assertTrue(result, "Playback Video Controller Event test failed");

	}
}
