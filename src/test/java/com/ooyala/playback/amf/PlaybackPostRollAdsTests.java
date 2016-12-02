package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPostRollAdsTests extends PlaybackWebTest{

	public PlaybackPostRollAdsTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPostroll(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

            result = result && playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

			playValidator.validate("playing_1", 90000);
            result = result && seekValidator.validate("seeked_1", 90000);
            result = result && event.validate("videoPlayed_1", 120000);
            result = result && event.validate("willPlaySingleAd_1", 90000);
	        extentTest.log(PASS, "Postroll Ad started");
            result = result && event.validate("singleAdPlayed_1", 90000);
	        extentTest.log(PASS, "Postroll Ad completed");
            result = result && event.validate("played_1", 200000);
	        extentTest.log(PASS, "Verified PostRoll Ads test");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}

}