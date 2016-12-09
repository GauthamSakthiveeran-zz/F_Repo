package com.ooyala.playback.VTC;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by snehal on 25/11/16.
 */
public class PlaybackNewStreamTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackNewStreamTests.class);
	private PlayValidator play;
	private PauseValidator pause;
	private DiscoveryValidator discoveryValidator;
	private EventValidator eventValidator;
	private PlayAction playAction;

	public PlaybackNewStreamTests() throws OoyalaException {
		super();
	}

	@Test(groups = "Playback", dataProvider = "testUrls")
	public void testLoadingNewStream(String testName, String url)
			throws OoyalaException {
		boolean result = true;
		try {
			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			play.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && eventValidator.validate("playing_1", 60000);
			logger.info("Video starts playing");

			((JavascriptExecutor) driver)
					.executeScript("pp.setEmbedCode('Vmd2VmeDq6-92C-kPkkZGoOkTCeSZq4e')");

			result = result && eventValidator.validate("setEmbedCode_1", 15000);
			logger.info("Loaded New Asset");

			((JavascriptExecutor) driver).executeScript("pp.play()");

			result = result && eventValidator.validate("playing_2", 60000);
			logger.info("Played new asset");

			result = result
					&& discoveryValidator.validate("reportDiscoveryClick_1",
							6000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		Assert.assertTrue(result, "Playback new stream tests failed");
	}
}
