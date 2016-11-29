package com.ooyala.playback.amf.freewheel;

import static com.relevantcodes.extentreports.LogStatus.PASS;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.ReplayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackBumperPrerollAdTests extends PlaybackWebTest {

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private ReplayValidator replayValidator;

	public PlaybackBumperPrerollAdTests() throws OoyalaException {
		super();
	}

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyBumperPrerollPlayback(String testName, String url)
			throws Exception {
		boolean result = true;

		try {

			driver.get(url);

            result = result && playValidator.waitForPage();
			Thread.sleep(10000);

			injectScript();

            result = result && playAction.startAction();
            result = result && event.validate("BumperAd", 60000);

			extentTest.log(PASS, "verified Bumper ad is playing");

            result = result && event.validate("playing_FirstTime", 30000);
			extentTest.log(PASS, "verified Players controls");

            result = result && seekAction.seekTillEnd().startAction();

			extentTest.log(PASS, "verified Seek functionality");

            result = result && event.validate("replay", 30000);
            result = result && replayValidator.validate("replay_1", 30000);

			extentTest.log(PASS, "verified replay of video");

            result = result && event.validate("BumperAdOnReplay", 30000);

			extentTest.log(PASS, "verified Bumper ad is playing on replay");

			extentTest.log(LogStatus.PASS, "Main Video played successfully");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback Bumper Preroll Ad tests failed");

	}
}
