package com.ooyala.playback.amf.preroll;

import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdClickThroughValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.SetEmbedCodeValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPreRollPoddedAdsTests extends PlaybackWebTest {

	public PlaybackPreRollPoddedAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private PlayAction playAction;
	private SeekValidator seekValidator;
	private PoddedAdValidator poddedAdValidator;
	private SetEmbedCodeValidator setEmbedCodeValidator;
	private AdClickThroughValidator clickthrough;

	@Test(groups = {"amf","preroll","podded"}, dataProvider = "testUrls")
	public void verifyPrerollPodded(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();
			injectScript();

			result = result && playAction.startAction();
			
			result = result && event.validate("PreRoll_willPlayAds", 5000);
			if (result && !clickthrough.ignoreClickThrough(url)) {
                s_assert.assertTrue(clickthrough.validateClickThroughForPoddedAds("preroll"),"Preroll Podded");
            }
			result = result && event.validate("adsPlayed_1", 180000);

			result = result && poddedAdValidator.setPosition("PreRoll").validate("countPoddedAds_1", 120000);
			
			result = result && event.validate("playing_1", 10000);

			if(testName.contains("SetEmbedCode")){
				result = result && setEmbedCodeValidator.validate("setEmbedmbedCode",6000);
			} else {
				result = result && seekValidator.validate("seeked_1", 60000);
				result = result && event.validate("played_1", 90000);

			}

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		s_assert.assertTrue(result, "Test failed");
		s_assert.assertAll();

	}

}
