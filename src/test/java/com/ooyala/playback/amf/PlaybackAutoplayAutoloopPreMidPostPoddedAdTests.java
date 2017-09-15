package com.ooyala.playback.amf;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.action.PlayerAPIAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 15/03/17.
 */
public class PlaybackAutoplayAutoloopPreMidPostPoddedAdTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackAutoplayAutoloopPreMidPostPoddedAdTests.class);
	private EventValidator eventValidator;
	private SeekAction seek;
	private PlayerAPIAction playerAPI;

	public PlaybackAutoplayAutoloopPreMidPostPoddedAdTests() throws OoyalaException {
		super();
	}

	@Test(groups = { "amf", "autoplay", "vtc" }, dataProvider = "testUrls")
	public void testAutoplayAutoloop(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && eventValidator.isPageLoaded();

			injectScript();

			result = result && eventValidator.validateAutoPlay();

			result = result && eventValidator.validate("adsPlayed_1", 45000);
			result = result && eventValidator.validate("countPoddedAds_1", 60000);

			int noOfPrerollAds = Integer.parseInt(playerAPI.getTextContent("countPoddedAds_1"));
			for (int i = 1; i <= noOfPrerollAds; i++) {
				result = result && eventValidator.validate("willPlaySingleAd_" + i + "", 45000);
				result = result && eventValidator.validate("singleAdPlayed_" + i + "", 45000);
			}

			if (!eventValidator.isAdPluginPresent("freewheel")) {
				result = result && seek.seek(15, true);
			}
			result = result && eventValidator.validate("countPoddedAds_2", 60000);
			int noOfMidrollAds = Integer.parseInt(playerAPI.getTextContent("countPoddedAds_2"));
			for (int i = noOfPrerollAds + 1; i <= noOfMidrollAds; i++) {
				result = result && eventValidator.validate("willPlaySingleAd_" + i + "", 45000);
				result = result && eventValidator.validate("singleAdPlayed_" + i + "", 45000);
			}

			if (eventValidator.isAdPluginPresent("freewheel")) {
				result = result && seek.seek(15, true);
			}

			result = result && eventValidator.validate("countPoddedAds_3", 60000);
			int noOfPostrollAds = Integer.parseInt(playerAPI.getTextContent("countPoddedAds_3"));
			for (int i = noOfMidrollAds + 1; i <= noOfPostrollAds; i++) {
				result = result && eventValidator.validate("willPlaySingleAd_" + i + "", 45000);
				result = result && eventValidator.validate("singleAdPlayed_" + i + "", 45000);
			}

			result = result && eventValidator.validate("replay_1", 60000);

			result = result && eventValidator.validate("countPoddedAds_4", 60000);
			int noOfPrerollAdsOnReplay = Integer.parseInt(playerAPI.getTextContent("countPoddedAds_4"));
			for (int i = noOfPostrollAds + 1; i <= noOfPrerollAdsOnReplay; i++) {
				result = result && eventValidator.validate("willPlaySingleAd_" + i + "", 45000);
				result = result && eventValidator.validate("singleAdPlayed_" + i + "", 45000);
			}

			if (!eventValidator.isAdPluginPresent("freewheel")) {
				result = result && seek.seek(15, true);
			}

			result = result && eventValidator.validate("countPoddedAds_5", 60000);
			int noOfMidrollAdsOnReplay = Integer.parseInt(playerAPI.getTextContent("countPoddedAds_5"));
			for (int i = noOfPrerollAdsOnReplay + 1; i <= noOfMidrollAdsOnReplay; i++) {
				result = result && eventValidator.validate("willPlaySingleAd_" + i + "", 45000);
				result = result && eventValidator.validate("singleAdPlayed_" + i + "", 45000);
			}

			if (eventValidator.isAdPluginPresent("freewheel")) {
				result = result && seek.seek(15, true);
			}

			result = result && eventValidator.validate("countPoddedAds_6", 60000);
			int noOfPostrollAdsOnReplay = Integer.parseInt(playerAPI.getTextContent("countPoddedAds_6"));
			for (int i = noOfMidrollAdsOnReplay + 1; i <= noOfPostrollAdsOnReplay; i++) {
				result = result && eventValidator.validate("singleAdPlayed_" + i + "", 45000);
			}

		} catch (Exception e) {
			logger.error(e);
			result = false;
			extentTest.log(LogStatus.FAIL, "Playback Autoplay Autoloop test failed for " + testName + "", e);
		}
		Assert.assertTrue(result, "Playback Autoplay Autoloop test failed for " + testName + "");
	}
}
