package com.ooyala.playback.amf.midroll;

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
public class PlaybackAutoplayAutoloopMidrollPoddedAdTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackAutoplayAutoloopMidrollPoddedAdTests.class);
	private EventValidator eventValidator;
	private SeekAction seek;
	private PlayerAPIAction playerAPI;

	public PlaybackAutoplayAutoloopMidrollPoddedAdTests() throws OoyalaException {
		super();
	}

	@Test(groups = { "amf", "autoplay", "midroll" }, dataProvider = "testUrls")
	public void testAutoplayAutoloop(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && eventValidator.isPageLoaded();

			injectScript();

			result = result && eventValidator.validateAutoPlay();

			result = result && eventValidator.validate("playing_1", 60000);

			result = result && seek.seek(15, true);

			result = result && eventValidator.validate("MidRoll_willPlayAds", 60000);

			result = result && eventValidator.validate("adsPlayed_1", 200000);

			result = result && eventValidator.validate("countPoddedAds_1", 60000);

			int noOfAds = Integer.parseInt(playerAPI.getTextContent("countPoddedAds_1"));

			for (int i = 1; i <= noOfAds; i++) {
				result = result && eventValidator.validate("willPlaySingleAd_" + i + "", 45000);
				result = result && eventValidator.validate("singleAdPlayed_" + i + "", 45000);
			}

			result = result && seek.seek(15, true);

			result = result && eventValidator.validate("replay_1", 60000);

			result = result && seek.seek(15, true);

			result = result && eventValidator.validate("countPoddedAds_2", 60000);

			int noOfAdsOnReplay = Integer.parseInt(playerAPI.getTextContent("countPoddedAds_2"));

			for (int i = noOfAds + 1; i <= noOfAdsOnReplay; i++) {
				result = result && eventValidator.validate("willPlaySingleAd_" + i + "", 45000);
				result = result && eventValidator.validate("singleAdPlayed_" + i + "", 45000);
			}

		} catch (Exception e) {
			logger.error(e);
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}
		Assert.assertTrue(result, "Playback Autoplay Autoloop test failed for " + testName + "");
	}
}