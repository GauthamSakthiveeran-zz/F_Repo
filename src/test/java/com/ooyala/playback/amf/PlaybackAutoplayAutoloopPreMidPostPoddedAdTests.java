package com.ooyala.playback.amf;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 15/03/17.
 */
public class PlaybackAutoplayAutoloopPreMidPostPoddedAdTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackAutoplayAutoloopPreMidPostPoddedAdTests.class);
	private EventValidator eventValidator;
	private SeekAction seek;

	public PlaybackAutoplayAutoloopPreMidPostPoddedAdTests() throws OoyalaException {
		super();
	}

	@Test(groups = "amf,autoplay", dataProvider = "testUrls")
	public void testAutoplayAutoloop(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			result = result && eventValidator.isPageLoaded();
			
			injectScript();

			boolean autoplay = false;

			autoplay = Boolean.parseBoolean(driver.executeScript("return pp.parameters.autoPlay").toString());

			if (!autoplay) {
				logger.error("Autoplay not set for this video");
				result = false;
			}

			result = result && eventValidator.validate("adsPlayed_1", 45000);
			result = result && eventValidator.validate("countPoddedAds_1", 60000);
			int noOfPrerollAds = Integer
					.parseInt(driver.executeScript("return countPoddedAds_1.textContent").toString());
			for (int i = 1; i <= noOfPrerollAds; i++) {
				result = result && eventValidator.validate("willPlaySingleAd_" + i + "", 45000);
				result = result && eventValidator.validate("singleAdPlayed_" + i + "", 45000);
			}

			if (!eventValidator.isAdPluginPresent("freewheel")) {
				result = result && seek.seek(15, true);
			}
			result = result && eventValidator.validate("countPoddedAds_2", 60000);
			int noOfMidrollAds = Integer
					.parseInt(driver.executeScript("return countPoddedAds_2.textContent").toString());
			for (int i = noOfPrerollAds + 1; i <= noOfMidrollAds; i++) {
				result = result && eventValidator.validate("willPlaySingleAd_" + i + "", 45000);
				result = result && eventValidator.validate("singleAdPlayed_" + i + "", 45000);
			}

			if (eventValidator.isAdPluginPresent("freewheel")) {
				result = result && seek.seek(15, true);
			}

			result = result && eventValidator.validate("countPoddedAds_3", 60000);
			int noOfPostrollAds = Integer
					.parseInt(driver.executeScript("return countPoddedAds_3.textContent").toString());
			for (int i = noOfMidrollAds + 1; i <= noOfPostrollAds; i++) {
				result = result && eventValidator.validate("willPlaySingleAd_" + i + "", 45000);
				result = result && eventValidator.validate("singleAdPlayed_" + i + "", 45000);
			}

			result = result && eventValidator.validate("replay_1", 60000);

			result = result && eventValidator.validate("countPoddedAds_4", 60000);
			int noOfPrerollAdsOnReplay = Integer
					.parseInt(driver.executeScript("return countPoddedAds_4.textContent").toString());
			for (int i = noOfPostrollAds + 1; i <= noOfPrerollAdsOnReplay; i++) {
				result = result && eventValidator.validate("willPlaySingleAd_" + i + "", 45000);
				result = result && eventValidator.validate("singleAdPlayed_" + i + "", 45000);
			}

			if (!eventValidator.isAdPluginPresent("freewheel")) {
				result = result && seek.seek(15, true);
			}

			result = result && eventValidator.validate("countPoddedAds_5", 60000);
			int noOfMidrollAdsOnReplay = Integer
					.parseInt(driver.executeScript("return countPoddedAds_5.textContent").toString());
			for (int i = noOfPrerollAdsOnReplay + 1; i <= noOfMidrollAdsOnReplay; i++) {
				result = result && eventValidator.validate("willPlaySingleAd_" + i + "", 45000);
				result = result && eventValidator.validate("singleAdPlayed_" + i + "", 45000);
			}

			if (eventValidator.isAdPluginPresent("freewheel")) {
				result = result && seek.seek(15, true);
			}

			result = result && eventValidator.validate("countPoddedAds_6", 60000);
			int noOfPostrollAdsOnReplay = Integer
					.parseInt(driver.executeScript("return countPoddedAds_6.textContent").toString());
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
