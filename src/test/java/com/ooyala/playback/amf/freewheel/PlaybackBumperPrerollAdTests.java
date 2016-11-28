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
			event.validate("BumperAd", 60);

			extentTest.log(PASS, "verified Bumper ad is playing");

			event.validate("playing_FirstTime", 30);
			extentTest.log(PASS, "verified Players controls");

			seekAction.seekTillEnd().startAction();

			extentTest.log(PASS, "verified Seek functionality");

			event.validate("replay", 30);
			replayValidator.validate("replay_1", 30);

			extentTest.log(PASS, "verified replay of video");

			event.validate("BumperAdOnReplay", 30);

			extentTest.log(PASS, "verified Bumper ad is playing on replay");

			result = true;

			extentTest.log(LogStatus.PASS, "Main Video played successfully");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback Bumper Preroll Ad tests failed");

	}
}
