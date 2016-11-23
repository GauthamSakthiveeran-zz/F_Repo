package com.ooyala.playback.alice;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class AliceBasicPlaybackTests extends PlaybackWebTest {

	private EventValidator eventValidator;
	private PlayValidator play;
	private PauseValidator pause;
	private SeekValidator seek;

	public AliceBasicPlaybackTests() throws OoyalaException {
		super();
		// eventValidator = pageFactory.getEventValidator();
		// play = pageFactory.getPlayValidator();
		// pause = pageFactory.getPauseValidator();
		// seek = pageFactory.getSeekValidator();

	}

	@Test(groups = "alice", dataProvider = "testUrls")
	public void testBasicPlaybackAlice(String testName, String url)
			throws OoyalaException {

		boolean result = false;

		try {
			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			play.waitForPage();
			Thread.sleep(10000);

			injectScript("http://192.168.1.102:8080/alice_full.js");
			
			play.validate("playing_1", 60);

			logger.info("Verifed that video is getting playing");

			Thread.sleep(2000);

			pause.validate("paused_1", 60);

			logger.info("Verified that video is getting pause");

			play.validate("playing_2", 60);

			// fullScreenValidator.validate("",60);

			seek.validate("seeked_1", 60);

			logger.info("Verified that video is seeked");

			eventValidator.validate("played_1", 60);

			logger.info("Verified that video is played");

			result = true;
			extentTest.log(LogStatus.PASS, "Alice basic playback tests Passed");
			
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Alice basic playback tests failed");
	}
}
