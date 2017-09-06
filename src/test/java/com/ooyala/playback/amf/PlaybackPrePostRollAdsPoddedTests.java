package com.ooyala.playback.amf;

import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdSkipButtonValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackPrePostRollAdsPoddedTests extends PlaybackWebTest {

	public PlaybackPrePostRollAdsPoddedTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private PoddedAdValidator poddedAdValidator;
	private SeekAction seekAction;
	private AdSkipButtonValidator skipButtonValidator;

	@Test(groups = {"amf","preroll","postroll","podded"}, dataProvider = "testUrls")
	public void verifyPreMidPostrollPodded(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlayAds", 60000);
			
			result = result && event.validate("PreRoll_willPlaySingleAd_1", 10000);
			result = result && skipButtonValidator.validate("", 120000);
			
			result = result && event.validate("PreRoll_willPlaySingleAd_2", 10000);
			result = result && skipButtonValidator.validate("", 120000);
			
			result = result && event.validate("PreRoll_willPlaySingleAd_3", 10000);
			result = result && skipButtonValidator.validate("", 120000);

			result = result && event.validate("adsPlayed_1", 600000);

			result = result && poddedAdValidator.setPosition("PreRoll").validate("countPoddedAds_1", 60000);

			result = result && event.validate("playing_1", 90000);
            
    		result = result && seekAction.seekTillEnd().startAction();
			
			result = result && event.validate("PostRoll_willPlayAds", 200000);
			
			result = result && event.validate("PostRoll_willPlaySingleAd_4", 10000);
			result = result && skipButtonValidator.validate("", 120000);
			
			result = result && event.validate("PostRoll_willPlaySingleAd_5", 10000);
			result = result && skipButtonValidator.validate("", 120000);
			
			result = result && event.validate("PostRoll_willPlaySingleAd_6", 10000);
			result = result && skipButtonValidator.validate("", 120000);

			result = result && event.validate("adsPlayed_2", 10000);

			result = result && poddedAdValidator.setPosition("PostRoll").validate("countPoddedAds_2", 10000);

			result = result && event.skipScrubberValidation().validate("played_1", 10000);

		} catch (Exception e) {
			logger.error(e.getMessage());
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}

		s_assert.assertTrue(result, "Test failed");
		s_assert.assertAll();
	}
}