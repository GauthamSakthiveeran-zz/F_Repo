package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EncodingValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackEncodingPriorityTests extends PlaybackWebTest {

	public PlaybackEncodingPriorityTests() throws OoyalaException {
		super();
	}

	private EncodingValidator encode;
	private PlayValidator playValidator;
	private SeekValidator seek;
	private EventValidator event;
	private PlayAction playAction;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyEncodingPriority(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlaySingleAd_1", 5000);

			if (event.isAdPluginPresent("pulse"))
				result = result && event.validate("singleAdPlayed_2", 120000);
			else
				result = result && event.validate("singleAdPlayed_1", 120000);

			result = result && event.validate("playing_1", 60000);

			result = result && event.playVideoForSometime(3);

			result = result && encode.validate("", 6000);

			result = result && seek.validate("seeked_1", 19000);

			result = result && seek.validate("played_1", 60000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}

		Assert.assertTrue(result, "Tests failed");

	}

}
