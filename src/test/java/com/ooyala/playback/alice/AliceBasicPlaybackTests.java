package com.ooyala.playback.alice;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AliceBasicPlaybackTests extends PlaybackWebTest {

	@DataProvider(name = "testUrls")
	public Object[][] getTestData() {

		return UrlGenerator.parseXmlDataProvider(getClass().getSimpleName(),
				nodeList);
	}

	public AliceBasicPlaybackTests() throws OoyalaException {
		super();
	}

	@Test(groups = "alice", dataProvider = "testUrls")
	public void testBasicPlaybackAlice(String testName, String url) throws OoyalaException {

		boolean result = false;
		PlayValidator play = pageFactory.getPlayValidator();
		PauseValidator pause = pageFactory.getPauseValidator();
		SeekValidator seek = pageFactory.getSeekValidator();
		PlayAction playAction = pageFactory.getPlayAction();
		PauseAction pauseAction = pageFactory.getPauseAction();
        EventValidator eventValidator = pageFactory.getEventValidator();
        FullScreenValidator fullScreenValidator = pageFactory.getFullScreenValidator();

		try {
			driver.get(url);
            if (!getPlatform().equalsIgnoreCase("android")) {
                driver.manage().window().maximize();
            }

			play.waitForPage();
            Thread.sleep(10000);

			injectScript("http://10.11.66.55:8080/alice.js");

			play.validate("playing_1", 60);

			logger.info("Verifed that video is getting playing");

            Thread.sleep(2000);

            pause.validate("paused_1", 60);

			logger.info("Verified that video is getting pause");

			play.validate("playing_2", 60);

           // fullScreenValidator.validate("",60);

			seek.validate("seeked_1", 60);

			logger.info("Verified that video is seeked");

            eventValidator.validate("played_1",60);

			logger.info("Verified that video is played");

			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertTrue(result, "Alice basic playback tests failed");
	}
}
