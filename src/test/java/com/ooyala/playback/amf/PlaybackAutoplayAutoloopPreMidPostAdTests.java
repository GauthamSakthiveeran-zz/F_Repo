package com.ooyala.playback.amf;

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
public class PlaybackAutoplayAutoloopPreMidPostAdTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackAutoplayAutoloopPreMidPostAdTests.class);
	private EventValidator eventValidator;
	private SeekValidator seekValidator;

	public PlaybackAutoplayAutoloopPreMidPostAdTests() throws OoyalaException {
		super();
	}

	@Test(groups = {"amf","autoplay","vtc"}, dataProvider = "testUrls")
	public void testAutoplayAutoloop(String testName, UrlObject url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && eventValidator.isPageLoaded();
			
			injectScript();

			result = result && eventValidator.validateAutoPlay();

			result = result && eventValidator.validate("adsPlayed_1", 45000);
			result = result && eventValidator.validate("playing_1", 60000);
			result = result && seekValidator.validate("seeked_1", 60000);
			result = result && eventValidator.validate("willPlaySingleAd_2", 45000);
			result = result && eventValidator.validate("adsPlayed_2", 45000);
			result = result && eventValidator.validate("willPlaySingleAd_3", 45000);
			result = result && eventValidator.validate("adsPlayed_3", 45000);
			
			result = result && eventValidator.validate("replay_1", 60000);
//			result = result && eventValidator.validate("willPlayAdOnReplay_1", 45000);
			result = result && eventValidator.validate("adsPlayed_4", 45000);
			result = result && seekValidator.validate("seeked_1", 60000);
//			result = result && eventValidator.validate("willPlayAdOnReplay_2", 45000);
			result = result && eventValidator.validate("adsPlayed_5", 45000);
//			result = result && eventValidator.validate("willPlayAdOnReplay_3", 45000);
			result = result && eventValidator.validate("adsPlayed_6", 45000);

		} catch (Exception e) {
			logger.error(e);
			result = false;
			extentTest.log(LogStatus.FAIL, "Playback Autoplay Autoloop test failed for "+testName+"", e);
		}
		Assert.assertTrue(result, "Playback Autoplay Autoloop test failed for "+testName+"");
	}
}
