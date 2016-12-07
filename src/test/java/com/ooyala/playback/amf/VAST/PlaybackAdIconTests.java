package com.ooyala.playback.amf.VAST;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackAdIconTests extends PlaybackWebTest {

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;

	public PlaybackAdIconTests() throws OoyalaException {
		super();
	}

	@Test(groups = {"amf","adicon"}, dataProvider = "testUrls", enabled = false)
	public void verifyADIcon(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();
			Thread.sleep(10000);

			injectScript();

			result = result && playAction.startAction();

			loadingSpinner();
			// Wait for ad start
			result = result && event.validate("willPlaySingleAd_1", 60000);

			// verify ad icon TODO

			result = result && event.validate("singleAdPlayed_1", 160000);

			result = result && playValidator.validate("playing_1", 190000);

			result = result && seekValidator.validate("seeked_1", 190000);

			result = result && event.validate("played_1", 190000);

			extentTest.log(LogStatus.PASS, "Main Video played successfully");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback Ad Icon tests failed");

	}

}
