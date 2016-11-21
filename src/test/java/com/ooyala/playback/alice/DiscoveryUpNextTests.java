package com.ooyala.playback.alice;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.UpNextValidator;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/11/16.
 */
public class DiscoveryUpNextTests extends PlaybackWebTest {

	public DiscoveryUpNextTests() throws OoyalaException {
		super();
	}

	@Test(groups = "alice", dataProvider = "testUrls")
	public void testDiscoveryUpNext(String testName, String url)
			throws OoyalaException {
		boolean result = false;
		PlayValidator play = pageFactory.getPlayValidator();
		UpNextValidator discoveryUpNext = pageFactory.getUpNextValidator();
		EventValidator eventValidator = pageFactory.getEventValidator();

		try {
			driver.get(url);
			if (!driver.getCapabilities().getPlatform().toString()
					.equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			play.waitForPage();

			logger.info("Verified that video is seeked");

			injectScript("http://10.11.66.55:8080/alice.js");

			play.validate("playing_1", 60);

			logger.info("Verifed that video is getting playing");

			pageFactory.getSeekValidator().seek(25, true);

			discoveryUpNext.validate("UPNEXT_CONTENT", 60);

			logger.info("Verified Unpnext content");

			eventValidator.validate("played_1", 60);

			logger.info("Verified that video is played");

			result = true;
		} catch (Exception e) {
			e.printStackTrace();

		}
		Assert.assertTrue(result, "Discovery up next tests failed");

	}
}
