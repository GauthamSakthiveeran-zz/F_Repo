package com.ooyala.playback.amf;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.PlayerAPIAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 28/2/17.
 */
public class PlaybackPreMidPostInitialVolumeTests extends PlaybackWebTest {
	private static Logger logger = Logger.getLogger(PlaybackPreMidPostInitialVolumeTests.class);
	private EventValidator eventValidator;
	private PlayValidator play;
	private VolumeValidator volumeValidator;
	private PlayAction playAction;
	private SeekValidator seekValidator;
	private SeekAction seekAction;
	private PlayerAPIAction playerAPI;

	PlaybackPreMidPostInitialVolumeTests() throws OoyalaException {
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

			result = result && eventValidator.validate("willPlaySingleAd_1", 60000);

			result = result && eventValidator.validate("adPodStarted_2", 10000);

			int noOfAds = Integer.parseInt(playerAPI.getTextContent("adPodStarted_2"));

			for (int i = 1; i <= noOfAds; i++) {
				if (eventValidator.checkIsAdPlaying()) {
					result = result && eventValidator.validate("willPlaySingleAd_" + i + "", 50000);
					result = result && volumeValidator.checkInitialVolume("ad");
				}
			}

			result = result && eventValidator.validate("playing_1", 60000);

			result = result && volumeValidator.checkInitialVolume("video");

			result = result && seekAction.seek(20, true);

			result = result && eventValidator.validate("willPlaySingleAd_1", 60000);

			result = result && eventValidator.validate("adPodStarted_3", 60000);

			int noOfAdsmid = Integer.parseInt(playerAPI.getTextContent("adPodStarted_3")) + noOfAds;

			for (int i = 1 + noOfAds; i <= noOfAdsmid; i++) {
				if (eventValidator.checkIsAdPlaying()) {
					result = result && eventValidator.validate("willPlaySingleAd_" + i + "", 50000);
					result = result && volumeValidator.checkInitialVolume("ad");
				}
			}

			result = result && seekValidator.validate("seeked_1", 60000);

			result = result && eventValidator.validate("adPodStarted_4", 60000);

			int noOfAdsPost = Integer.parseInt(playerAPI.getTextContent("adPodStarted_4")) + noOfAdsmid;
			logger.info("Total Number of ads are : " + noOfAdsPost);

			for (int i = 1 + noOfAdsmid; i <= noOfAdsPost; i++) {
				if (eventValidator.checkIsAdPlaying()) {
					result = result && eventValidator.validate("willPlaySingleAd_" + i + "", 50000);
					result = result && volumeValidator.checkInitialVolume("ad");
				}
			}

			result = result && eventValidator.skipScrubberValidation().validate("played_1", 60000);

		} catch (Exception e) {
			logger.error("Exception while checking  initial Volume tests " + e.getMessage());
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}
		Assert.assertTrue(result, "Playback initial Volume tests failed");
	}
}
