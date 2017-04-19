package com.ooyala.playback.valhalla;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.StreamValidator;
import com.ooyala.playback.page.VideoPluginValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackStreamsTests extends PlaybackWebTest {

	private EventValidator eventValidator;
	private PlayValidator play;
	private PauseValidator pause;
	private SeekValidator seek;
	private StreamValidator streamTypeValidator;
	private VideoPluginValidator videoPluginValidator;

	public PlaybackStreamsTests() throws OoyalaException {
		super();
	}

	@Test(groups = "streams", dataProvider = "testUrls") // TODO for other plugins
	public void testBasicPlaybackStreams(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			Thread.sleep(5000);

			result = result && eventValidator.validate("videoPlayingurl", 40000);
			
			result = result && streamTypeValidator.setStreamType(url.getStreamType()).validate("", 1000);
			
			result = result && videoPluginValidator.setUrlObject(url).validate("", 1000);

			result = result && pause.validate("paused_1", 60000);

			result = result && play.validate("playing_2", 60000);

			result = result && seek.validate("seeked_1", 60000);

			result = result && eventValidator.validate("played_1", 120000);

		} catch (Exception e) {
			logger.error("Exception while checking basic playback " + e.getMessage());
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}
		Assert.assertTrue(result, "Basic playback tests failed" + testName);
	}

}
