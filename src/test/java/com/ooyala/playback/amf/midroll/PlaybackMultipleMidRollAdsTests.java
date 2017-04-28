package com.ooyala.playback.amf.midroll;

import com.ooyala.playback.page.MidrollAdValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackMultipleMidRollAdsTests extends PlaybackWebTest {

	public PlaybackMultipleMidRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seek;
	private MidrollAdValidator adStartTimeValidator;

	@Test(groups = { "amf", "midroll" }, dataProvider = "testUrls")
	public void verifyMultipleMidroll(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {
			
			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 90000);

            if (url.getAdStartTime()!=null && !url.getAdStartTime().isEmpty()) {
                result = result && adStartTimeValidator.validateMultipleMidrollAdStartTime(url.getAdStartTime());
                result = result && event.validate("singleAdPlayed_1", 200000);
                result = result && event.validate("singleAdPlayed_2", 200000);
            } else {
                result = result && event.validate("MidRoll_willPlayAds", 200000);
                result = result && event.validate("MidRoll_willPlaySingleAd_1", 200000);
                result = result && event.validate("singleAdPlayed_1", 200000);
                result = result && event.validate("MidRoll_willPlaySingleAd_2", 200000);
                result = result && event.validate("singleAdPlayed_2", 200000);
            }

			if (event.isAdPluginPresent("pulse")) {

				result = result && event.validate("MidRoll_willPlaySingleAd_3", 200000);
				result = result && event.validate("singleAdPlayed_3", 200000);

				result = result && event.validate("MidRoll_willPlaySingleAd_4", 200000);
				result = result && event.validate("singleAdPlayed_4", 200000);
			}

			result = result && seek.validate("seeked_1", 10000);

			result = result && event.validate("played_1", 200000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified Multiple MidRoll Ads");

	}
}
