package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 24/11/16.
 */
public class PlaybackVolumeTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackVolumeTests.class);
	private EventValidator eventValidator;
	private PlayValidator play;
	private VolumeValidator volumeValidator;
	private PlayAction playAction;
	private SeekValidator seekValidator;
	private IsAdPlayingValidator isAdPlayingValidator;

	PlaybackVolumeTests() throws OoyalaException {
		super();
	}

	@Test(groups = "Playback", dataProvider = "testUrls")
	public void testVolumeVTC(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;
		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && eventValidator.loadingSpinner();

			Boolean isAdplaying = isAdPlayingValidator.validate("", 2000);

			if (isAdplaying) {
				logger.info("Checking volume for Ad");
				result = result && volumeValidator.validate("VOLUME_MAX", 20000);
				result = result && eventValidator.validate("adPodEnded_1", 20000);
				logger.info("Ad played");
			}

			result = result && eventValidator.validate("playing_1", 60000);

			result = result && volumeValidator.validate("VOLUME_MAX", 60000);

			result = result && seekValidator.validate("seeked_1", 20000);

			result = result && eventValidator.validate("played_1", 60000);

		} catch (Exception e) {
			logger.error("Exception while checking  Volume tests  " + e.getMessage());
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Playback Volume tests failed");
	}
}
