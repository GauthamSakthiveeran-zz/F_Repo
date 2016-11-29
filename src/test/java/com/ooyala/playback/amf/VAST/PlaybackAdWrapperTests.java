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
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackAdWrapperTests extends PlaybackWebTest {

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private FullScreenAction fullScreenAction;
	private VolumeValidator volumeValidator;
	private PauseAction pauseAction;

	public PlaybackAdWrapperTests() throws OoyalaException {
		super();
	}

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPlaybackAdWrapper(String testName, String url)
			throws Exception {

		boolean result = false;

		try {

			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}
			Thread.sleep(10000);
			playValidator.waitForPage();

			injectScript();

			playValidator.validate("playing_1", 190);
			sleep(2000);

			pauseAction.startAction();
			fullScreenAction.startAction();

			volumeValidator.validate("", 60);
			seekValidator.validate("seeked_1", 190);
			event.validate("played_1", 190);

			result = true;

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback Ad Wrapper tests failed");

	}
}
