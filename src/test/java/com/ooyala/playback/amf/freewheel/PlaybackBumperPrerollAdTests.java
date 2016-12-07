package com.ooyala.playback.amf.freewheel;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.ReplayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

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
	public void verifyBumperPrerollPlayback(String testName, String url) throws Exception {
		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();
			result = result && event.validate("BumperAd", 60000);

			result = result && event.validate("playing_FirstTime", 30000);

			result = result && seekAction.seekTillEnd().startAction();

			result = result && replayValidator.validate("replay_1", 30000);

			result = result && event.validate("BumperAdOnReplay", 30000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback Bumper Preroll Ad tests failed");

	}
}
