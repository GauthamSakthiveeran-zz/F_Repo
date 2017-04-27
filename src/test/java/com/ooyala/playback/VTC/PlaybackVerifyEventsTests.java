package com.ooyala.playback.VTC;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 25/11/16.
 */
public class PlaybackVerifyEventsTests extends PlaybackWebTest {

	private static Logger logger = Logger
			.getLogger(PlaybackVerifyEventsTests.class);
	private PlayValidator play;
	private EventValidator eventValidator;
	private SeekValidator seekValidator;

	public PlaybackVerifyEventsTests() throws OoyalaException {
		super();
	}

	@Test(groups = "Playback", dataProvider = "testUrls")
	public void testVerifyEvents(String testName, UrlObject url) {

		boolean result = true;

		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			executeScript("pp.play();");

			result = result && eventValidator.validate("playing_1",120000);

			result = result
					&& eventValidator.validate("videoSetInitialTime_1", 60000);

			result = result && eventValidator.validate("videoPlay_1", 60000);

			result = result
					&& eventValidator.validate("videoWillPlay_1", 60000);

			result = result && eventValidator.validate("videoPlaying_1", 60000);

			executeScript("pp.pause();");

			result = result && eventValidator.validate("videoPause_1",40000);

            executeScript("pp.play();");

            result = result && seekValidator.validate("seeked_1", 60000);

			result = result && eventValidator.validate("videoPaused_1", 60000);

            result = result && eventValidator.validate("videoPlayed_1",240000);

		} catch (Exception e) {
			logger.error("Exception while checking Controller Event test  "+e.getMessage());
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}

		Assert.assertTrue(result, "Playback Video Controller Event test failed");

	}
}
