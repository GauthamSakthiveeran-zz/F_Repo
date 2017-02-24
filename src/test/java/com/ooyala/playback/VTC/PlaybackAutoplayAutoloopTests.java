package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.AutoplayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by snehal on 25/11/16.
 */
public class PlaybackAutoplayAutoloopTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackAutoplayAutoloopTests.class);
	private PlayValidator play;
	private EventValidator eventValidator;
	private SeekValidator seekValidator;
	private ReplayValidator replayValidator;
	private IsAdPlayingValidator isAdPlayingValidator;
	private AutoplayAction autoplayAction;

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

			boolean autoplay = false;

			autoplay = Boolean.parseBoolean(driver.executeScript(
					"return pp.parameters.autoPlay").toString());

			if(!autoplay){
				logger.error("Autoplay not set for this video");
				result = false;
			}

			result = result && eventValidator.validate("adsPlayed_1", 30000);

			result = result && eventValidator.loadingSpinner();

			result = result && eventValidator.validate("playing_1", 60000);

			result = result && seekValidator.validate("seeked_1", 60000);

			result = result && eventValidator.validate("replay_1", 60000);

			result = result && eventValidator.validate("willPlaySingleAd_2", 30000);

			result = result && eventValidator.validate("adsPlayed_2", 30000);

		} catch (Exception e) {
			logger.error(e);
			result = false;
			extentTest.log(LogStatus.FAIL, "Playback Autoplay Autoloop test failed", e);
		}
		Assert.assertTrue(result, "Playback Autoplay Autoloop test failed");
	}
}
