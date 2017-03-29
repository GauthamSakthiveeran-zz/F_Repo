package com.ooyala.playback.playerfeatures;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.WaterMarkValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackWatermarkTests extends PlaybackWebTest {

	private static Logger logger = Logger
			.getLogger(PlaybackWatermarkTests.class);

	private PlayValidator play;
	private SeekValidator seek;
	private PlayAction playAction;
	private EventValidator eventValidator;
	private PauseValidator pause;
	private WaterMarkValidator waterMarkValidator;

	public PlaybackWatermarkTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testWatermarks(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;
		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			result = result && pause.validate("paused_1", 60000);

			result = result && waterMarkValidator.validate("", 60000);

			result = result && playAction.startAction();

			result = result && seek.validate("seeked_1", 60000);

			result = result && eventValidator.validate("videoPlayed_1", 60000);

		} catch (Exception e) {
			logger.error(e);
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}

		Assert.assertTrue(result, "Playback Watermark tests failed");
	}
}
