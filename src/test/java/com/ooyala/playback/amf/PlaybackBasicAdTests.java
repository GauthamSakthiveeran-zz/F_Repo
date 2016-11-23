package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

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

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyBasicAd(String testName, String url) throws Exception {

		boolean result = false;

		try {

			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			playValidator.waitForPage();
			Thread.sleep(10000);

			injectScript();

			playAction.startAction();

			loadingSpinner();

			event.validate("willPlaySingleAd_1", 150);

			extentTest.log(PASS, "Preroll Ad started");

			// String adurl = (((JavascriptExecutor)
			// driver).executeScript("return adplayingurl_1.textContent")).toString();

			event.validate("singleAdPlayed_1", 150);

			extentTest.log(PASS, "Preroll Ad Completed");

			event.validate("playing_1", 120);

			extentTest.log(PASS, "Main video started to play");

			sleep(500);

			seekValidator.validate("seeked_1", 190);

			event.validate("played_1", 190);

			extentTest.log(PASS, "Video completed palying");

			result = true;

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback Ad Wrapper tests failed");

	}

}
