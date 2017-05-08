package com.ooyala.playback.amf.ima;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackOnlyMp4Tests extends PlaybackWebTest {

	public PlaybackOnlyMp4Tests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seek;

	@Test(groups = { "amf" }, dataProvider = "testUrls", enabled = false)
	public void verifyOnlyMp4(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();
			injectScript();

			result = result && playValidator.validate("playing_1", 10000);

			result = result && event.validate("PreRoll_willPlaySingleAd_1", 6000);
			result = result && event.validate("singleAdPlayed_1", 20000);

			result = result && seek.validate("seeked_1", 1000);

			result = result && event.validate("played_1", 20000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}

		Assert.assertTrue(result, "Test failed");

	}
}