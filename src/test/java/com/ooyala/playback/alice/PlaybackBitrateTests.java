package com.ooyala.playback.alice;

import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.Bitratevalidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/17/16.
 */
public class PlaybackBitrateTests extends PlaybackWebTest {

	public PlaybackBitrateTests() throws OoyalaException {
		super();
	}

	@Test(groups = "alice", dataProvider = "testUrls")
	public void testBitrate(String testName, String url) throws OoyalaException {

		boolean result = false;
		PlayValidator play = pageFactory.getPlayValidator();
		PauseValidator pause = pageFactory.getPauseValidator();
		SeekValidator seek = pageFactory.getSeekValidator();
		EventValidator eventValidator = pageFactory.getEventValidator();
		Bitratevalidator bitratevalidator = pageFactory.getBitratevalidator();

		try {
			driver.get(url);
			if (!driver.getCapabilities().getPlatform().toString()
					.equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			play.waitForPage();

			injectScript("http://10.11.66.55:8080/alice.js");

			play.validate("playing_1", 60);
			logger.info("Verifed that video is getting playing");
			sleep(2000);

			pause.validate("paused_1", 60);
			logger.info("Verified that video is getting pause");

			bitratevalidator.validate("", 60);

			sleep(1000);

			seek.validate("seeked_1", 60);
			logger.info("Verified that video is seeked");

			eventValidator.validate("videoPlayed_1", 60);
			logger.info("Verified that video is played");

		} catch (Exception e) {
			e.printStackTrace();

		}
		Assert.assertTrue(result, "Aspect ratio tests failed");

	}

}
