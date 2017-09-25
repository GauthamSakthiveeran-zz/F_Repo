package com.ooyala.playback.platformParameter;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.BitmovinTechnologyValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.StreamValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by Gautham on 20-6-2017
 */
public class PlatformParameterTest extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlatformParameterTest.class);
	private EventValidator eventValidator;
	private PlayValidator play;
	private PauseValidator pause;
	private StreamValidator streamTypeValidator;
	private SeekAction seekAction;
	private BitmovinTechnologyValidator bitmovinvalidator;

	public PlatformParameterTest() throws OoyalaException {
		super();
	}

	@Test(groups = "platformParameterTests", dataProvider = "testUrls")
	public void testPlatformbStreams(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();
			bitmovinvalidator.getConsoleLogs();

			result = result && play.validate("playing_1", 60000);

			result = result && eventValidator.playVideoForSometime(3);

			result = result && pause.validate("paused_1", 60000);

			result = result && eventValidator.validate("videoPlayingurl", 40000);
			result = result
					&& streamTypeValidator.setStreamType(url.getStreamType()).validate("videoPlayingurl", 1000);

			result = result && bitmovinvalidator.setStream(url.getStreamType()).validate("bitmovin_technology", 6000);

			result = result && play.validate("playing_2", 60000);

			if (!testName.contains("Live")) {
				result = result && seekAction.setTime(100).startAction();
				result = result && eventValidator.validate("seeked_1", 60000);
			}

		} catch (Exception e) {
			logger.error("Exception while checking Platform Parameters " + e.getMessage());
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}
		Assert.assertTrue(result, "Platform Tests failed" + testName);
	}
}
