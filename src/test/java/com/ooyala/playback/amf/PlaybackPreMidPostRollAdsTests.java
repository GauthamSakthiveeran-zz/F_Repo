package com.ooyala.playback.amf;

import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdClickThroughValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.MidrollAdValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackPreMidPostRollAdsTests extends PlaybackWebTest {

	public PlaybackPreMidPostRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private MidrollAdValidator adStartTimeValidator;
	private AdClickThroughValidator adClickThroughValidator;

	@Test(groups = { "amf", "preroll", "midroll", "postroll" }, dataProvider = "testUrls")
	public void verifyPreMidPostroll(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			boolean isPulse = event.isAdPluginPresent("pulse");

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlaySingleAd_1", 30000);

			result = result && event.validate("videoPlayingAd_1", 60000);

			s_assert.assertTrue(adClickThroughValidator.setUrlObject(url).validate("videoPausedAds_1", 20000),
					"PreMidPost failed");

			executeScript("pp.skipAd()");

			result = result && event.validate("adsPlayed_1", 200000);

			result = result && event.validate("playing_1", 150000);

			result = result
					&& adStartTimeValidator.setTime(url.getAdStartTime()).validateAdStartTime("MidRoll_willPlayAds");

			result = result && event.validate("videoPlayingAd_2", 60000);

			s_assert.assertTrue(adClickThroughValidator.setUrlObject(url).validate("videoPausedAds_2", 20000),
					"PreMidPost");

			executeScript("pp.skipAd()");

			result = result && event.validate("adsPlayed_2", 150000);

			result = result && seekAction.seekTillEnd().startAction();

			result = result && event.validate("willPlayPostrollAd_1", 900000);

			result = result && event.validate("videoPlayingAd_3", 60000);

			s_assert.assertTrue(adClickThroughValidator.setUrlObject(url).validate("videoPausedAds_3", 20000),
					"PreMidPost");

			executeScript("pp.skipAd()");

			result = result && isPulse ? event.validate("singleAdPlayed_6", 60000) && event.validate("seeked_1", 60000)
					: event.validate("adsPlayed_3", 60000);

			result = result && event.skipScrubberValidation().validate("played_1", 200000);

		} catch (Exception e) {
			logger.error(e.getMessage());
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}
		s_assert.assertTrue(result, "Pre Mid Post Roll Ads failed.");
		s_assert.assertAll();
	}
}
