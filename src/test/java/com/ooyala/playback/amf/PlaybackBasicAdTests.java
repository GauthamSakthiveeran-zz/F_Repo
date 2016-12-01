package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

import org.junit.Ignore;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackBasicAdTests extends PlaybackWebTest {

	public PlaybackBasicAdTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;

	@Test(groups = "amf", dataProvider = "testUrls", enabled= false)
	public void verifyBasicAd(String testName, String url) throws Exception {

		boolean result = true;

		try {

			driver.get(url);

            result = result &&	playValidator.waitForPage();
			Thread.sleep(10000);

			injectScript();

            result = result && playAction.startAction();

			loadingSpinner();

            result = result && event.validate("willPlaySingleAd_1", 150000);

			extentTest.log(PASS, "Preroll Ad started");

			// String adurl = (((JavascriptExecutor)
			// driver).executeScript("return adplayingurl_1.textContent")).toString();

            result = result && event.validate("singleAdPlayed_1", 150000);

			extentTest.log(PASS, "Preroll Ad Completed");

            result = result && event.validate("playing_1", 120000);

			extentTest.log(PASS, "Main video started to play");

			sleep(500);

            result = result && 	seekValidator.validate("seeked_1", 190000);

            result = result &&	event.validate("played_1", 190000);

			extentTest.log(PASS, "Video completed palying");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback Ad Wrapper tests failed");

	}

}
