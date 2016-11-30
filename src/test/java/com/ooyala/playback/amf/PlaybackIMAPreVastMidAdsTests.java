package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackIMAPreVastMidAdsTests extends PlaybackWebTest {

	public PlaybackIMAPreVastMidAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyIMAPreVastMidAds(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

            result = result && playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

            result = result && playAction.startAction();

			loadingSpinner();
			event.validate("PreRoll_willPlayAds", 120000);
            result = result && event.validate("adsPlayed_1", 200000);
			event.validate("adPodEnd_google-ima-ads-manager_0_1", 200000);
			extentTest.log(PASS, "Played IMA Preroll Ads");
            result = result && event.validate("playing_1", 90);
            result = result && seekValidator.validate("seeked_1", 190000);

            result = result && event.validate("MidRoll_willPlayAds", 100000);
			event.validate("adsPlayed_2", 200000);
			try {
				event.validate("adPodEnd_vast_2_2", 60000);
			} catch (Exception e) {
				event.validate("adPodEnd_vast_2_3", 180000);
			}
			extentTest.log(PASS, "Played Vast Midroll Ads");
			event.validate("played_1", 200000);
			extentTest.log(PASS, "Verified VastPreIMAMidlAdsTests Ads Test");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}

}
