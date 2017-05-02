package com.ooyala.playback.streams;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.ControlBarValidator;
import com.ooyala.playback.page.ErrorDescriptionValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.FullScreenValidator;
import com.ooyala.playback.page.LiveValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.LiveAction;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackLiveTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackLiveTests.class);
	private PlayValidator play;
	private PauseValidator pause;
	private ControlBarValidator controlBarValidator;
	private FullScreenValidator fullScreenValidator;
	private LiveAction liveAction;
	private PauseAction pauseAction;
	private PlayAction playAction;
	private LiveValidator live;
	private ErrorDescriptionValidator error;
	private EventValidator event;

	public PlaybackLiveTests() throws OoyalaException {
		super();
	}

	@Test(groups = "streams", dataProvider = "testUrls")
	public void testLive(String testName, UrlObject url) throws OoyalaException {
		boolean isChannelIdPresent = false;
		boolean result = true;

		try {

			isChannelIdPresent = live.isChannelIdPresent(url);

			if (isChannelIdPresent) {
				liveChannel.startChannel(url.getChannelId(), url.getProvider());
			}

			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);
			result = result && event.playVideoForSometime(3);

			result = result && pause.validate("paused_1", 60000);

			result = result && controlBarValidator.validate("", 60000);
			// to-do add ooyala logo to the test page

			result = result && fullScreenValidator.validate("FULLSCREEN_BTN_1", 60000);

			result = result && pauseAction.startAction();

			result = result && liveAction.startAction();

			result = result && playAction.startAction();

			if (isChannelIdPresent) {

				liveChannel.stopChannels();

				driver.get(url.getUrl());
				
				result = result && play.waitForPage();
				
				result = result && playAction.startAction();

				result = result && error.expectedErrorCode("network_error")
						.expectedErrorDesc("Network connection temporarily lost").validate("", 30000);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}
		Assert.assertTrue(result, "Playback Live tests failed");

	}
}
