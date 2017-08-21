package com.ooyala.playback.amf;

import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdClickThroughValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.MidrollAdValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackPreMidPostRollAdsPoddedTests extends PlaybackWebTest {

	public PlaybackPreMidPostRollAdsPoddedTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private PoddedAdValidator poddedAdValidator;
	private SeekAction seekAction;
	private MidrollAdValidator adStartTimeValidator;
	private AdClickThroughValidator clickthrough;

	@Test(groups = { "amf", "preroll", "midroll", "postroll", "podded" }, dataProvider = "testUrls")
	public void verifyPreMidPostrollPodded(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;
		boolean clickThrough = clickthrough.ignoreClickThrough(url);

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlayAds", 60000);

			if (result && clickThrough) {
				s_assert.assertTrue(clickthrough.validateClickThroughForPoddedAds("preroll"), "PreMidPost Podded");
			}

			result = result && event.validate("adsPlayed_1", 600000);

			result = result && poddedAdValidator.setPosition("PreRoll").validate("countPoddedAds_1", 60000);

			result = result && event.validate("playing_1", 90000);
			
			result = result && adStartTimeValidator.setTime(url.getAdStartTime())
					.validateAdStartTime("MidRoll_willPlayAds");
			
			if (result && clickThrough) {
				s_assert.assertTrue(clickthrough.validateClickThroughForPoddedAds("midroll"), "PreMidPost Podded");
			}

			result = result && event.validate("adsPlayed_2", 600000);

			result = result && poddedAdValidator.setPosition("MidRoll").validate("countPoddedAds_2", 600000);

			result = result && seekAction.seekTillEnd().startAction();

			result = result && event.validate("PostRoll_willPlayAds", 200000);

			if (result && clickThrough) {
				s_assert.assertTrue(clickthrough.validateClickThroughForPoddedAds("postroll"), "PreMidPost Podded");
			}

			result = result && event.validate("adsPlayed_3", 600000);

			result = result && poddedAdValidator.setPosition("PostRoll").validate("countPoddedAds_3", 600000);

			result = result && event.validate("played_1", 180000);

		} catch (Exception e) {
			logger.error(e.getMessage());
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}

		s_assert.assertTrue(result, "Test failed");
		s_assert.assertAll();
	}
	
}
