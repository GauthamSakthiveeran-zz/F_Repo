package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.VolumeValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.PlayerAPIAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 28/2/17.
 */
public class PlaybackPreMidPostInitialVolumeTests extends PlaybackWebTest {
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
			
			result = result && volumeValidator.validateInitailVolumeForPoddedAds(0, noOfAds);

			result = result && eventValidator.validate("playing_1", 60000);

			result = result && volumeValidator.validateInitialVolume("video");

			result = result && seekAction.seek(20, true);

			result = result && eventValidator.validate("willPlaySingleAd_1", 60000);

			result = result && eventValidator.validate("adPodStarted_3", 60000);

			int noOfAdsmid = Integer.parseInt(playerAPI.getTextContent("adPodStarted_3")) + noOfAds;
			
			result = result && volumeValidator.validateInitailVolumeForPoddedAds(noOfAds, noOfAdsmid);

			result = result && seekValidator.validate("seeked_1", 60000);

			result = result && eventValidator.validate("adPodStarted_4", 60000);

			int noOfAdsPost = Integer.parseInt(playerAPI.getTextContent("adPodStarted_4")) + noOfAdsmid;
			
			result = result && volumeValidator.validateInitailVolumeForPoddedAds(noOfAdsmid, noOfAdsPost);

			result = result && eventValidator.skipScrubberValidation().validate("played_1", 60000);

		} catch (Exception e) {
			logger.error("Exception while checking  initial Volume tests " + e.getMessage());
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}
		Assert.assertTrue(result, "Playback initial Volume tests failed");
	}
}
