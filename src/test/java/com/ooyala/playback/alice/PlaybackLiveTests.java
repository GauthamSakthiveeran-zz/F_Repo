package com.ooyala.playback.alice;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.ControlBarValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.FullScreenValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.LiveAction;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackLiveTests extends PlaybackWebTest {

	private PlayValidator play;
	private PauseValidator pause;
	private EventValidator eventValidator;
	private ControlBarValidator controlBarValidator;
	private FullScreenValidator fullScreenValidator;
	private LiveAction liveAction;

	public PlaybackLiveTests() throws OoyalaException {
		super();
	}

	@Test(groups = "Playback", dataProvider = "testUrls")
	public void testLive(String testName, String url) throws OoyalaException {

		boolean result = true;

        if((testName.split(":")[1].toLowerCase()).contains("HLS".toLowerCase())&& !(getBrowser().equalsIgnoreCase("safari")) ){
            throw new SkipException("HLS tests run only on Safari browser - Test Skipped");
        }

			try {
				driver.get(url);

                result = result && play.waitForPage();

				injectScript();

                result = result && play.validate("playing_1", 60000);

                result = result && pause.validate("paused_1", 60000);

                result = result && controlBarValidator.validate("", 60000);
				// to-do add ooyala logo to the test page

                result = result && fullScreenValidator.validate("FULLSCREEN_BTN_1", 60000);

                result = result && pause.validate("paused_2", 60000);

                result = result && play.validate("playing_2", 60000);

                result = result && liveAction.startAction();

                result = result && eventValidator.validate("played_1", 60000);

				logger.info("video played");
			} catch (Exception e) {
				e.printStackTrace();
                result = false;
			}
			Assert.assertTrue(result, "Playback Live tests failed");

	}
}
