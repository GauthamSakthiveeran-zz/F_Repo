package com.ooyala.playback.alice;

import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AspectRatioValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackAspectRatioTests extends PlaybackWebTest {
    private EventValidator eventValidator;
    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;
    private PlayAction playAction;
    private AspectRatioValidator aspectRatioValidator;


	public PlaybackAspectRatioTests() throws OoyalaException {
		super();
	}


	@Test(groups = "alice", dataProvider = "testUrls")
	public void testAspectRation(String testName, String url)
			throws OoyalaException {
		boolean result = false;

		try {
			driver.get(url);
			/*PlayValidator play = pageFactory.getPlayValidator();
			PauseValidator pause = pageFactory.getPauseValidator();
			SeekValidator seek = pageFactory.getSeekValidator();
			PlayAction playAction = pageFactory.getPlayAction();
			EventValidator eventValidator = pageFactory.getEventValidator();
			AspectRatioValidator aspectRatioValidator = pageFactory.getAspectRatioValidator();*/
			play.waitForPage();


			injectScript("http://10.11.66.55:8080/alice.js");

			play.validate("playing_1", 60);

			logger.info("Verified that video is playing");
			sleep(2000);

			aspectRatioValidator.validate("assetDimension_1", 60);

			pause.validate("paused_1", 60);

			logger.info("Verirfied that video is getting paused");

			playAction.startAction();
			// add fullscreen functionality

			seek.validate("seeked_1", 60);

			logger.info("Verified that video is seeked");

			aspectRatioValidator.validate("assetDimension_1", 60);

			eventValidator.validate("videoPlayed_1", 60);

			logger.info("Verified that video is played");

		} catch (Exception e) {
			e.printStackTrace();

		}
		Assert.assertTrue(result, "Aspect ratio tests failed");

	}

}
