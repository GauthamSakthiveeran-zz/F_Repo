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
 * Created by soundarya on 11/17/16.
 */
public class PlaybackVerticalVideoTests extends PlaybackWebTest {

    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;
    private PlayAction playAction;
    private EventValidator eventValidator;
    private AspectRatioValidator aspectRatioValidator;

    public PlaybackVerticalVideoTests() throws OoyalaException {
        super();
    }

	@Test(groups = "alice", dataProvider = "testUrls")
	public void testVerticalVideo(String testName, String url)
			throws OoyalaException {

        boolean result = false;
        /*PlayValidator play = pageFactory.getPlayValidator();
        PauseValidator pause = pageFactory.getPauseValidator();
        SeekValidator seek = pageFactory.getSeekValidator();
        PlayAction playAction = pageFactory.getPlayAction();
        EventValidator eventValidator = pageFactory.getEventValidator();
        AspectRatioValidator aspectRatioValidator = pageFactory.getAspectRatioValidator();*/

		try {
			driver.get(url);

			play.waitForPage();

			injectScript("http://10.11.66.55:8080/alice.js");

			play.validate("playing_1", 60);

			logger.info("video playing");

			sleep(2000);

			aspectRatioValidator.verticalVideoValidate("assetDimension_1", 60);

			pause.validate("paused_1", 60);

			logger.info("video paused");

			playAction.startAction();

			seek.validate("seeked_1", 60);

			logger.info("video seeked");

			aspectRatioValidator.verticalVideoValidate("assetDimension_1", 60);

			logger.info("validated vertical video dimention");

			eventValidator.validate("videoPlayed_1", 60);

			logger.info("video played");

		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertTrue(result, "Vertical Video tests failed");

	}

}