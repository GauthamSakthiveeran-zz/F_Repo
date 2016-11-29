package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdSkipButtonValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.VolumeValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackCustomeInteractionTests extends PlaybackWebTest {

	public PlaybackCustomeInteractionTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private VolumeValidator volumeValidator;
	private AdSkipButtonValidator adSkipButtonValidator;

	static int index = 0;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyCustomeInteractionAd(String testName, String url)
			throws Exception {

		boolean result = true;

		try {

			driver.get(url);

            result = result && playValidator.waitForPage();
			Thread.sleep(10000);

			injectScript();

            result = result && playAction.startAction();

            result = result && event.validate("willPlaySingleAd_1", 190000);

			extentTest.log(PASS, "Preroll Ad started");

            result = result && event.validate("showAdSkipButton_1", 60000);

			extentTest.log(PASS, "Skip button for Ads shown");
			sleep(5000);

            result = result && volumeValidator.validate("", 60000);

			extentTest.log(PASS, "Clicked on Skip button");

            result = result && adSkipButtonValidator.validate("", 60000);

            result = result && event.validate("singleAdPlayed_1", 190000);

			extentTest.log(PASS, "Preroll Ad Completed");

            result = result && event.validate("playing_1", 60000);

			extentTest.log(PASS, "Main video started to play");
			sleep(500);

            result = result && seekAction.seekTillEnd().startAction();

            result = result && event.validate("seeked_1", 180000);

            result = result && event.validate("played_1", 200000);

			extentTest.log(PASS, "Video completed playing");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result,
				"Playback CC Enabled MidRoll Ads tests failed");

	}

}
