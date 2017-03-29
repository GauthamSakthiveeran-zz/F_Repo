package com.ooyala.playback.playerfeatures;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackDiscoveryTests extends PlaybackWebTest {

	private static Logger logger = Logger
			.getLogger(PlaybackDiscoveryTests.class);

	private PlayValidator play;
	private DiscoveryValidator discoveryValidator;
	private PlayAction playAction;
	private EventValidator eventValidator;

	public PlaybackDiscoveryTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testDiscovery(String testName, UrlObject url)
			throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			result = result && discoveryValidator.validate("reportDiscoveryClick_1", 60000);

			// uncommenting the playAction as video is playing automatically.
			//result = result && playAction.startAction();


			result = result && discoveryValidator.clickOnDiscoveryButton();

			result = result && discoveryValidator.clickOnDiscoveryCloseButton();

			result = result && playAction.startAction();

			result = result && eventValidator.validate("played_1", 60000);

		} catch (Exception e) {
			logger.error("Playback Discovery tests failed" + e.getMessage());
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Playback Discovery tests failed");
	}
}
