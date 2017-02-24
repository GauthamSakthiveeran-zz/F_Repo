package com.ooyala.playback.VTC;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.IsAdPlayingValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by snehal on 25/11/16.
 */
public class PlaybackAutoplayAutoloopTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackAutoplayAutoloopTests.class);
	private EventValidator eventValidator;
	private SeekValidator seekValidator;
	private IsAdPlayingValidator isAdPlayingValidator;

	public PlaybackAutoplayAutoloopTests() throws OoyalaException {
		super();
	}

	@Test(groups = "Playback", dataProvider = "testUrls")
	public void testAutoplayAutoloop(String testName, String url)
			throws OoyalaException {

		boolean result = true;
		try {

			driver.get(url);

			injectScript();

			Thread.sleep(3000);
			String autoplay="";

			try {
				autoplay = ((JavascriptExecutor) driver).executeScript(
						"return pp.parameters.autoPlay").toString();
				logger.info("Autoplay is set for this video");
				Assert.assertEquals(autoplay, "true", "verified autoplay");

			}catch(Exception e) {
				logger.error("Autoplay not set for this video");
			}

			Assert.assertEquals(autoplay, "true", "verified autoplay");
			result = result && eventValidator.validate("adsPlayed_1", 30000);

			result = result && eventValidator.loadingSpinner();

			result = result && eventValidator.validate("playing_1", 60000);
			logger.info("Autoplayed the asset.");

			result = result && seekValidator.validate("seeked_1", 60000);

			result = result && eventValidator.validate("replay_1", 60000);

			result = result && eventValidator.validate("willPlaySingleAd_2", 30000);

			boolean isAdplaying = isAdPlayingValidator.validate(
					"CheckAdPlaying", 60000);

			if(!isAdplaying){
				result = result && eventValidator.validate("adsPlayed_2", 30000);
			}

			logger.info("Video Played");

		} catch (Exception e) {
			logger.error(e);
			result = false;
		}
		Assert.assertTrue(result, "Playback Autoplay Autoloop test failed");
	}
}
