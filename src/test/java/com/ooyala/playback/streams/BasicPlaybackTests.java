package com.ooyala.playback.streams;

import com.ooyala.playback.page.*;
import com.ooyala.playback.url.UrlObject;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/21/16.
 */
public class BasicPlaybackTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(BasicPlaybackTests.class);
	private EventValidator eventValidator;
	private PlayValidator play;
	private PauseValidator pause;
	private SeekValidator seek;
	private StreamValidator streamTypeValidator;

	public BasicPlaybackTests() throws OoyalaException {
		super();
	}

	@Test(groups = "streams", dataProvider = "testUrls")
	public void testBasicPlaybackStreams(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			Thread.sleep(5000);

			if (url.getStreamType() != null && !url.getStreamType().isEmpty()) {
				result = result && eventValidator.validate("videoPlayingurl", 40000);
				result = result
						&& streamTypeValidator.setStreamType(url.getStreamType()).validate("", 1000);
			}

			result = result && pause.validate("paused_1", 60000);

			// because of https://jira.corp.ooyala.com:8443/browse/PBW-6281
			if (!testName.contains("Streams:Bitmovin Elemental Delta DASH VOD (Clear Dash)")) {
				result = result && play.validate("playing_2", 60000);
				result = result && seek.validate("seeked_1", 60000);
				
				if (!testName.contains("Streams:Main Akamai HLS Remote Asset") && !testName.contains("Streams:Bitmovin Akamai HLS Remote Asset")) // live video
					result = result && eventValidator.validate("played_1", 120000);
			}

		} catch (Exception e) {
			logger.error("Exception while checking basic playback " + e.getMessage());
			extentTest.log(LogStatus.FAIL, "Exception while checking basic playback " + e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Basic playback tests failed" + testName);
	}
}
