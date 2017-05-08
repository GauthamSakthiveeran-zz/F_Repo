package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackMp4PlayerSwitchingSupportTests extends PlaybackWebTest {

	public PlaybackMp4PlayerSwitchingSupportTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private PauseAction pauseAction;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPlaybackOfOSMFMp4(String testName, UrlObject url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());
			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("willPlaySingleAd_1", 50000);
			result = result && event.validate("singleAdPlayed_1", 190000);
			result = result && event.validate("playing_1", 120000);

			result = result && pauseAction.onScreen().startAction();

			result = result && playAction.onScreen().startAction();

			result = result && seekValidator.validate("seeked_1", 190000);

			result = result && event.validate("played_1", 190000);

		} catch (Exception e) {
			e.printStackTrace();
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");

	}

}
