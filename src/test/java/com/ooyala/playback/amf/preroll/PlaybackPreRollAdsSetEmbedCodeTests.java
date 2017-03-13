package com.ooyala.playback.amf.preroll;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SetEmbedCodeValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPreRollAdsSetEmbedCodeTests extends PlaybackWebTest {

	public PlaybackPreRollAdsSetEmbedCodeTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SetEmbedCodeValidator setEmbedCodeValidator;

	@Test(groups = { "amf", "preroll","setEmbedCode" }, dataProvider = "testUrls", enabled =false)
	public void verifyPrerollSetEmbedCode(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {
			
			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlaySingleAd_1", 5000);
			result = result && event.validate("singleAdPlayed_1", 160000);
			
			result = result && event.validate("playing_1", 20000);
			
			Thread.sleep(2000);
			
			result = result && setEmbedCodeValidator.validate("setEmbedmbedCode",6000);
			
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");

	}

}
