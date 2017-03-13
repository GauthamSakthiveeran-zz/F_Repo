package com.ooyala.playback.amf.postroll;

import com.ooyala.playback.page.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPostRollPoddedAdsTests extends PlaybackWebTest {

	public PlaybackPostRollPoddedAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private PoddedAdValidator poddedAdValidator;
	private SetEmbedCodeValidator setEmbedCodeValidator;

	@Test(groups = { "amf", "postroll", "podded" }, dataProvider = "testUrls")
	public void verifyPostrollPodded(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 60000);
			result = result && seekValidator.validate("seeked_1", 60000);
			result = result && event.validate("played_1", 60000);

			result = result && poddedAdValidator.setPosition("PostRoll").validate("countPoddedAds", 10000);

			if(testName.contains("SetEmbedCode")){
				result = result && setEmbedCodeValidator.validate("",6000);
			}


		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");

	}

}
