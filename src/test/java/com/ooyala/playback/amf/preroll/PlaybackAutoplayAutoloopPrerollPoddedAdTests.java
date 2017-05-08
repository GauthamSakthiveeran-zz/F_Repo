package com.ooyala.playback.amf.preroll;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 15/03/17.
 */
public class PlaybackAutoplayAutoloopPrerollPoddedAdTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackAutoplayAutoloopPrerollPoddedAdTests.class);
	private EventValidator eventValidator;
	private SeekValidator seekValidator;

	public PlaybackAutoplayAutoloopPrerollPoddedAdTests() throws OoyalaException {
		super();
	}

	@Test(groups = { "amf", "autoplay" }, dataProvider = "testUrls")
	public void testAutoplayAutoloop(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && eventValidator.waitTillAdPlays();
			
			injectScript();

			result = result && eventValidator.validateAutoPlay();

			result = result && eventValidator.validate("adsPlayed_1", 180000);

			result = result && eventValidator.validate("countPoddedAds_1", 1000);
			
			int noOfAds = Integer.parseInt(driver.executeScript("return countPoddedAds_1.textContent").toString());
			
			for (int i = 1; i <= noOfAds; i++) {
				result = result && eventValidator.validate("singleAdPlayed_" + i + "", 1000);
			}

			result = result && eventValidator.validate("playing_1", 10000);

			result = result && seekValidator.validate("seeked_1", 60000);
			result = result && eventValidator.validate("replay_1", 60000);

			result = result && eventValidator.validate("adsPlayed_2", 180000);

			result = result && eventValidator.validate("countPoddedAds_2", 60000);
			
			int noOfAdsOnReplay = Integer
					.parseInt(driver.executeScript("return countPoddedAds_2.textContent").toString());
			
			for (int i = noOfAds + 1; i <= noOfAdsOnReplay; i++) {
				result = result && eventValidator.validate("singleAdPlayed_" + i + "", 1000);
			}
			
		} catch (Exception e) {
			logger.error(e);
			result = false;
			extentTest.log(LogStatus.FAIL, "Playback Autoplay Autoloop test failed for " + testName + "", e);
		}
		Assert.assertTrue(result, "Playback Autoplay Autoloop test failed for " + testName + "");
	}
}
