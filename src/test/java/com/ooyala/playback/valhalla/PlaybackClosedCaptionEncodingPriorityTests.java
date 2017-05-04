package com.ooyala.playback.valhalla;


import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.CCValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.StreamValidator;
import com.ooyala.playback.page.VideoPluginValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackClosedCaptionEncodingPriorityTests extends PlaybackWebTest {
	private PlayValidator play;
	private PauseValidator pause;
	private SeekValidator seek;
	private EventValidator eventValidator;
	private CCValidator ccValidator;
	private StreamValidator streamTypeValidator;
	private VideoPluginValidator videoPluginValidator;

	public PlaybackClosedCaptionEncodingPriorityTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testClosedCaption(String testName, UrlObject url)
			throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();
			
			videoPluginValidator.getConsoleLogs();

			result = result && play.validate("playing_1", 60000);
			
			result = result && eventValidator.playVideoForSometime(3);
			
			result = result && eventValidator.validate("videoPlayingurl", 10000);
			
			if(!url.getStreamType().contains("mp4"))
			result = result && streamTypeValidator.setStreamType(url.getStreamType()).validate("videoPlayingurl", 1000);
			
//			result = result && videoPluginValidator.setUrlObject(url).validate("", 1000);

			result = result && pause.validate("paused_1", 60000);

			result = result && play.validate("playing_2", 60000);

			result = result && ccValidator.validate("cclanguage", 60000);

			result = result && seek.validate("seeked_1", 60000);

			result = result && eventValidator.validate("played_1", 60000);

		} catch (Exception e) {
			result = false;
			extentTest.log(LogStatus.FAIL, "Playback Closed Caption tests failed", e);

		}
		Assert.assertTrue(result, "Closed Caption tests failed");
	}
}
