package com.ooyala.playback.playerfeatures;

import static java.lang.Thread.sleep;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

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
	public void testDiscovery(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url);

			result = result && play.waitForPage();

			injectScript();
			result = result && play.validate("playing_1", 60000);

			logger.info("verified video is playing");

			result = result
					&& discoveryValidator.validate("reportDiscoveryClick_1",
							60000);
			logger.info("verified discovery");

			sleep(2000);

			result = result && playAction.startAction();

			sleep(2000);

			result = result && eventValidator.validate("played_1", 60000);
			logger.info("video played");
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		Assert.assertTrue(result, "Playback Discovery tests failed");
	}
}
