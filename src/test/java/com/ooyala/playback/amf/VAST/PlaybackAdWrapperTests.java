package com.ooyala.playback.amf.VAST;

import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.VolumeValidator;
import com.ooyala.playback.page.action.FullScreenAction;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackAdWrapperTests extends PlaybackWebTest {

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private PauseAction pauseAction;
	private FullScreenAction fullScreenAction;
	private VolumeValidator volumeValidator;

	public PlaybackAdWrapperTests() throws OoyalaException {
		super();
	}

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPlaybackAdWrapper(String testName, String url)
			throws Exception {

		boolean result = true;

		try {
			driver.get(url);

            result = result && playValidator.waitForPage();
			Thread.sleep(10000);

			injectScript();

            result = result && playValidator.validate("playing_1", 190000);
			extentTest.log(LogStatus.PASS, "Main video started to play");
			sleep(500);

            result = result && pauseAction.startAction();
			fullScreenAction.startAction();

			sleep(2000);

			playAction.startAction();

            result = result && volumeValidator.validate("", 60000);
            result = result && seekValidator.validate("seeked_1", 190000);
            result = result && event.validate("played_1", 190000);

			extentTest.log(LogStatus.PASS, "Main Video played successfully");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback Ad Wrapper tests failed");

	}
}