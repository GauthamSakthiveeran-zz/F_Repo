package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdClickThroughValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackClickthroughTests extends PlaybackWebTest {

	public PlaybackClickthroughTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private AdClickThroughValidator clickThrough;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyClickthrough(String testName, String url)
			throws Exception {

		boolean result = true;

		try {

			driver.get(url);

            result = result && playValidator.waitForPage();
			Thread.sleep(10000);

			injectScript();

            result = result && playAction.startAction();

            result = result && event.validate("willPlaySingleAd_1", 60000);

			extentTest.log(PASS, "Ad started to play");

            result = result && clickThrough.validate("", 120000);

            result = result && event.validate("singleAdPlayed_1", 190000);

			extentTest.log(PASS, "Ad completed");

			loadingSpinner();

            result = result && event.validate("playing_1", 160000);

			extentTest.log(PASS, "Video started");
			sleep(5000);

            result = result && seekAction.seekTillEnd().startAction();

            result = result &&event.validate("played_1", 160000);

			extentTest.log(PASS, "Video ended");

			extentTest.log(PASS, "Verified Clickthrough functionality");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		Assert.assertTrue(result, "Playback CC Enabled MidRoll Ads tests failed");

	}

}
