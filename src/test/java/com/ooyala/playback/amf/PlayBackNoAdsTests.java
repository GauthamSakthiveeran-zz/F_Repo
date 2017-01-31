package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlayBackNoAdsTests extends PlaybackWebTest {

	public PlayBackNoAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;

	@Test(groups = {"amf"}, dataProvider = "testUrls")
	public void verifyNoAds(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();
			injectScript();

			result = result && playValidator.validate("playing_1", 10000);
			
			result = result && event.validate("played_1", 240000); // waiting for the whole video to complete playing 
			
			result = result && !event.validate("willPlaySingleAd_1", 6000);

			
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Test failed");

	}

}