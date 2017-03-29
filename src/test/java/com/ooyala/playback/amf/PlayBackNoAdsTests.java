package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.IsAdPlayingValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlayBackNoAdsTests extends PlaybackWebTest {

	public PlayBackNoAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seek;
	private IsAdPlayingValidator isAdPlaying;

	@Test(groups = {"amf"}, dataProvider = "testUrls")
	public void verifyNoAds(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();
			injectScript();
			
			result = result && !isAdPlaying.validate("",50000);

			result = result && playValidator.validate("playing_1", 10000);
			
			Thread.sleep(10000);
			
			result = result && !isAdPlaying.validate("",50000);
			
			result = result && seek.validate("seeked_1", 19000);
			
			result = result && !isAdPlaying.validate("",50000);
			
			result = result && event.validate("played_1", 240000);
			
			result = result && !isAdPlaying.validate("",50000);
			
			result = result && !event.validateElementPresence("willPlaySingleAd_1");

			
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Test failed");

	}

}