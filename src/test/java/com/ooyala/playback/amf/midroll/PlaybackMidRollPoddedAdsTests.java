package com.ooyala.playback.amf.midroll;

import com.ooyala.playback.page.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackMidRollPoddedAdsTests extends PlaybackWebTest {

	public PlaybackMidRollPoddedAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private PoddedAdValidator poddedAdValidator;
	private SetEmbedCodeValidator setEmbedCodeValidator;

	@Test(groups = {"amf","podded","midroll"}, dataProvider = "testUrls")
	public void verifyMidrollPodded(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 60000);

			result = result && seekValidator.validate("seeked_1", 60000);
			
			result = result && event.validate("MidRoll_willPlayAds", 60000);

			result = result && event.validate("adsPlayed_1", 200000);

			result = result && poddedAdValidator.setPosition("MidRoll").validate("countPoddedAds_1", 120000);

			if(testName.contains("SetEmbedCode")){
				result = result && setEmbedCodeValidator.validate("setEmbedmbedCode",6000);
			}else{
				result = result && event.validate("seeked_1", 60000);
				result = result && event.validate("played_1", 200000);
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");
	}

}
