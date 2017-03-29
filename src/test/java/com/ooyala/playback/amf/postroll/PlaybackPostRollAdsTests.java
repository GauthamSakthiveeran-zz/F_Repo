package com.ooyala.playback.amf.postroll;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.SetEmbedCodeValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPostRollAdsTests extends PlaybackWebTest {

	public PlaybackPostRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private SetEmbedCodeValidator setEmbedCodeValidator;

	@Test(groups = { "amf", "postroll" }, dataProvider = "testUrls")
	public void verifyPostroll(String testName, UrlObject url) {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 90000);
			
			result = result && seekValidator.validate("seeked_1", 10000);

			result = result && event.validate("played_1", 200000);
			
			result = result && event.validate("PostRoll_willPlaySingleAd_1", 90000);

			result = result && event.validate("singleAdPlayed_1", 190000);

			if(testName.contains("SetEmbedCode")){
				result = result && setEmbedCodeValidator.validate("setEmbedmbedCode",6000);
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");

	}

}
