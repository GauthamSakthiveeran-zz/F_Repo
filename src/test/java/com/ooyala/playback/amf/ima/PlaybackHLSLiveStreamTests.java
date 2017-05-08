package com.ooyala.playback.amf.ima;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.ControlBarValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackHLSLiveStreamTests extends PlaybackWebTest {

	public PlaybackHLSLiveStreamTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private ControlBarValidator controlBar;
	private PauseAction pause;

	@Test(groups = {"amf","preroll","live"}, dataProvider = "testUrls")
	public void verifyHLSLiveStream(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();
			injectScript();

			result = result && playValidator.validate("playing_1", 10000);
			
			result = result && event.validate("PreRoll_willPlaySingleAd_1", 6000);
			result = result && event.validate("singleAdPlayed_1", 20000);
			
			result = result && pause.startAction();
			
			result = result && controlBar.live().validate("", 60000);
			
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}

		Assert.assertTrue(result, "Test failed");

	}

}