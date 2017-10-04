package com.ooyala.playback.amf.preroll;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.VolumeValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.PlayerAPIAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 28/2/17.
 */
public class PlaybackPrerollInitialVolumeTests extends PlaybackWebTest {
	private EventValidator eventValidator;
	private PlayValidator play;
	private VolumeValidator volumeValidator;
	private PlayAction playAction;
	private SeekValidator seekValidator;
	private PlayerAPIAction playerAPI;

	PlaybackPrerollInitialVolumeTests() throws OoyalaException {
		super();
	}

	@Test(groups = "Playback", dataProvider = "testUrls")
	public void testInitialVolumeVTC(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;
		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && eventValidator.validate("videoPlayingAd_1", 10000);

			result = result && eventValidator.validate("adPodStarted_2", 10000);

			int noOfAds = Integer.parseInt(playerAPI.getTextContent("adPodStarted_2"));

			for (int i = 1; i <= noOfAds; i++) {
				if (eventValidator.isAdPlaying()) {
					logger.info("Checking initial volume for PrerollPodded Ad");
					result = result && eventValidator.validate("willPlaySingleAd_" + i + "", 50000);
					result = result && volumeValidator.validateInitialVolume("ad");
				} else {
					extentTest.log(LogStatus.FAIL, "Preroll Ads are not playing.");
				}
			}

			result = result && eventValidator.validate("playing_1", 60000);

			result = result && volumeValidator.validateInitialVolume("video");

			result = result && seekValidator.validate("seeked_1", 60000);

			result = result && eventValidator.validate("played_1", 60000);

		} catch (Exception e) {
			logger.error("Exception while checking  initial Volume tests " + e.getMessage());
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Playback initial Volume tests failed");
	}
}
