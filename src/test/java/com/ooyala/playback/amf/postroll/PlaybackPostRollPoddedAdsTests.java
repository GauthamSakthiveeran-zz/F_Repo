package com.ooyala.playback.amf.postroll;

import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdClickThroughValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackPostRollPoddedAdsTests extends PlaybackWebTest {

	public PlaybackPostRollPoddedAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private PoddedAdValidator poddedAdValidator;
	private AdClickThroughValidator clickthroughValidator;

	@Test(groups = { "amf", "postroll", "podded" }, dataProvider = "testUrls")
	public void verifyPostrollPodded(String testName, UrlObject url) throws OoyalaException {
		boolean result = true;
		boolean clickthrough = !clickthroughValidator.ignoreClickThrough(url);
		try {
			driver.get(url.getUrl());
			result = result && playValidator.waitForPage();
			injectScript();
			result = result && playValidator.validate("playing_1", 60000);
			result = result && seekValidator.skipScrubberValidation().validate("seeked_1", 60000);
			result = result && event.validate("willPlayPostrollAd_1", 25000);
			if (result && clickthrough) {
				s_assert.assertTrue(clickthroughValidator.validateClickThroughForPoddedAds("postroll"),
						"Postroll Podded");
			}
			result = result && seekValidator.validate("videoPlayed_1", 60000);
			result = result && poddedAdValidator.setPosition("PostRoll").validate("countPoddedAds", 120000);
			result = result && event.validate("played_1", 60000);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}
		s_assert.assertTrue(result, "Tests failed");
		s_assert.assertAll();
	}
}
