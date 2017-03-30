package com.ooyala.playback.streams;

import com.ooyala.playback.page.*;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.action.LiveAction;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

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

	public PlaybackLiveTests() throws OoyalaException {
		super();
	}

	@Test(groups = "streams", dataProvider = "testUrls")
	public void testLive(String testName, UrlObject url) throws OoyalaException {

        String description = testName.split("-")[1].trim();
        boolean isChannelIdPresent = false;
		boolean result = true;

		try {

            isChannelIdPresent = live.isChannelIdPresent(url);

            if (isChannelIdPresent) {
                result = result && url.getLiveChannel().startChannel(url.getChannelId(),url.getProvider());
            }

			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			result = result && pause.validate("paused_1", 60000);

			result = result && controlBarValidator.validate("", 60000);
			// to-do add ooyala logo to the test page

			result = result
					&& fullScreenValidator.validate("FULLSCREEN_BTN_1", 60000);

			result = result && pauseAction.startAction();

			result = result && liveAction.startAction();

			result = result && playAction.startAction();


            if (isChannelIdPresent) {

                url.getLiveChannel().stopChannels();

                driver.get(url.getUrl());

                result = result && error.expectedErrorCode("sas")
                        .expectedErrorDesc("Invalid Authorization Response").validate("",30000);
            }


		} catch (Exception e) {
            logger.error(e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Playback Live tests failed");

	}
}
