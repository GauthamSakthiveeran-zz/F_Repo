package com.ooyala.playback.amf;

import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackPreRollAdsTests extends PlaybackWebTest {

	public PlaybackPreRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPreroll(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

            result = result && playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

            result = result && playAction.startAction();

			Thread.sleep(2000);

            result = result && event.validate("willPlaySingleAd_1", 60000);

			extentTest.log(LogStatus.INFO, "Preroll Ad started");

            result = result && event.validate("singleAdPlayed_1", 160000);

			extentTest.log(LogStatus.INFO, "Preroll Ad Completed");

            result = result && playValidator.validate("playing_1", 190000);

			extentTest.log(LogStatus.INFO, "Main video started to play");

			sleep(2000);

            result = result && seekValidator.validate("seeked_1", 190000);

			sleep(3000);

            result = result && event.validate("played_1", 190000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}

}
