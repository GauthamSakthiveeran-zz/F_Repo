package com.ooyala.playback.core;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.BitmovinTechnologyValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class HTML5FirstPlaybackTests extends PlaybackWebTest{

	public HTML5FirstPlaybackTests() throws OoyalaException {
		super();
	}
	
	private PlayValidator play;
	private BitmovinTechnologyValidator tech;
	private SeekValidator seek;
	private EventValidator eventValidator;
	
	@Test(groups = "html5", dataProvider = "testUrls")
	public void testHTML5FirstPlayback(String testName, UrlObject url) throws OoyalaException {
		boolean result = true;
		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();
			injectLogs();
			
			result = result && play.validate("playing_1", 60000);

			result = result && tech.setStream(url.getStreamType()).validate("bitmovin_technology", 6000);
			
			result = result && seek.validate("seeked_1", 60000);
			
			result = result && eventValidator.validate("played_1", 120000);

		} catch (Exception e) {
			logger.error("Exception while checking basic playback " + e.getMessage());
			extentTest.log(LogStatus.FAIL, "Exception while checking basic playback " + e.getMessage());
			e.printStackTrace();
			result = false;
		}
		Assert.assertTrue(result, "Basic playback tests failed" + testName);
		
	}
}
