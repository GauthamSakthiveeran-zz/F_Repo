package com.ooyala.playback.VTC;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.UpNextValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by jitendra on 24/11/16.
 */
public class PlaybackDiscoverTests extends PlaybackWebTest {

	private EventValidator eventValidator;
	private PlayValidator play;
	private UpNextValidator discoveryUpNext;
	private DiscoveryValidator discoveryValidator;
	private PlayAction playAction;
	private SeekAction seekAction;
	private SeekValidator seek;

	PlaybackDiscoverTests() throws OoyalaException {
		super();
	}

	@Test(groups = "Playback", dataProvider = "testUrls")
	public void testDiscoveryVTC(String testName, String url)
			throws OoyalaException {

		boolean result = true;
		try {
			driver.get(url);

			result = result && play.waitForPage();

			Thread.sleep(10000);

			injectScript();

			result = result && play.validate("playing_1", 60000);

			logger.info("Verifed that video is getting playing");

			result = result
					&& discoveryValidator.validate("reportDiscoveryClick_1",
							60000);

			logger.info("verified discovery");

			result = result && play.waitForPage();

			playAction.startAction();

			loadingSpinner();

			result = result && eventValidator.validate("playing_2", 60000);

			logger.info("Verified that 2nd video is playing");

			((JavascriptExecutor) driver)
					.executeScript("pp.seek(pp.getDuration()-15)");

			result = result && eventValidator.validate("seeked_1",20000);


    		result = result && discoveryUpNext.validate("UPNEXT_CONTENT", 60000);

			logger.info("Verified UpNext content");

			result = result && eventValidator.validate("played_1", 60000);

			logger.info("Verified that video is played");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		Assert.assertTrue(result, "Playback Discovery tests failed");
	}

}
