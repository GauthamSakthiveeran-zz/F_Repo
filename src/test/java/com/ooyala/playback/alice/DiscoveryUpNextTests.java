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

	private EventValidator eventValidator;
	private PlayValidator play;
	private UpNextValidator discoveryUpNext;

	public DiscoveryUpNextTests() throws OoyalaException {
		super();
	}

	@Test(groups = "Player", dataProvider = "testUrls")
	public void testDiscoveryUpNext(String testName, String url)
			throws OoyalaException {
		boolean result = false;

		try {
			driver.get(url);
			if (!driver.getCapabilities().getPlatform().toString()
					.equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			play.waitForPage();

			logger.info("Verified that video is seeked");

			injectScript();

			play.validate("playing_1", 60);

			logger.info("Verifed that video is getting playing");

			pageFactory.getSeekAction().seek(25, true);

			discoveryUpNext.validate("UPNEXT_CONTENT", 60);

			eventValidator.validate("played_1", 60);

			logger.info("Verified that video is played");

			result = true;
		} catch (Exception e) {
			e.printStackTrace();

		}
		Assert.assertTrue(result, "Discovery up next tests failed");

	}
}