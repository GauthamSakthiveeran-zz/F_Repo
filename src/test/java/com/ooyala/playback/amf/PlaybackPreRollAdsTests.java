package com.ooyala.playback.amf;

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

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPreroll(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			if ((event.isVideoPluginPresent("main") && event.isAdPluginPresent("freewheel"))
					|| event.isVideoPluginPresent("osmf") && event.isAdPluginPresent("ima")) {
				result = result && event.validate("PreRoll_willPlayAds", 60000);
				result = result && event.validate("adsPlayed_1", 160000);
			} else {
				result = result && event.validate("willPlaySingleAd_1", 60000);
				result = result && event.validate("singleAdPlayed_1", 160000);
			}

			result = result && event.validate("playing_1", 190000);

			result = result && seekValidator.validate("seeked_1", 190000);

			result = result && event.validate("played_1", 190000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");

	}

}
