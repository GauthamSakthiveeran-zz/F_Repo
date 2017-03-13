package com.ooyala.playback.amf.preroll;

import com.ooyala.playback.page.SetEmbedCodeValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPreRollAdsTests extends PlaybackWebTest {

	public PlaybackPreRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private SetEmbedCodeValidator setEmbedCodeValidator;

	@Test(groups = { "amf", "preroll" }, dataProvider = "testUrls")
	public void verifyPreroll(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			if ((event.isVideoPluginPresent("main") && event.isAdPluginPresent("freewheel"))
					|| event.isVideoPluginPresent("osmf") && event.isAdPluginPresent("ima")) {
				result = result && event.validate("PreRoll_willPlayAds", 1000);
				result = result && event.validate("adsPlayed_1", 160000);
			} else {
				result = result && event.validate("PreRoll_willPlaySingleAd_1", 1000);
				result = result && event.validate("singleAdPlayed_1", 160000);
			}

			if (testName.contains("OOYALA_ADS")) {
				result = result && event.validate("ooyalaAds", 1000);
				result = result && event.validate("playing_2", 20000);
			} else {
				result = result && event.validate("playing_1", 20000);
			}

			if(testName.contains("SetEmbedCode")){
				result = result && setEmbedCodeValidator.validate("setEmbedmbedCode",6000);
			}else{
				result = result && seekValidator.validate("seeked_1", 190000);

				result = result && event.validate("played_1", 200000);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");

	}

}
