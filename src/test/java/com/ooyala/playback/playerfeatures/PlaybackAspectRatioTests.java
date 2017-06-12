package com.ooyala.playback.playerfeatures;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AspectRatioValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackAspectRatioTests extends PlaybackWebTest {

	private static Logger logger = Logger
			.getLogger(PlaybackAspectRatioTests.class);

	private EventValidator eventValidator;
	private PlayValidator play;
	private PauseValidator pause;
	private SeekValidator seek;
	private PlayAction playAction;
	private AspectRatioValidator aspectRatioValidator;

	public PlaybackAspectRatioTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testAspectRation(String testName, UrlObject url)
			throws OoyalaException {
		boolean result = true;

		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			result = result && eventValidator.playVideoForSometime(2);

    		result = result && pause.validate("paused_1", 60000);

			result = result && playAction.startAction();

			result = result && seek.validate("seeked_1", 60000);

			result = result && aspectRatioValidator.validate("assetDimension_1", 60000);

			result = result && eventValidator.validate("played_1", 60000);

		} catch (Exception e) {
			logger.error(e.getMessage());
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Aspect ratio tests failed");
	}
}
