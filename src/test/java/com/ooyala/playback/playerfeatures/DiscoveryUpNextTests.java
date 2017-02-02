package com.ooyala.playback.playerfeatures;

import org.apache.log4j.Logger;
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

	private static Logger logger = Logger.getLogger(DiscoveryUpNextTests.class);
	private EventValidator eventValidator;
	private PlayValidator play;
	private UpNextValidator discoveryUpNext;

	public DiscoveryUpNextTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testDiscoveryUpNext(String testName, String url)
			throws OoyalaException {
		boolean result = true;

		try {
			driver.get(url);

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			result = result
					&& pageFactory.getSeekAction().setTime(31).fromLast()
							.startAction();// seek(25, true);

			result = result
					&& discoveryUpNext.validate("UPNEXT_CONTENT", 60000);

			result = result && eventValidator.validate("played_1", 60000);

			logger.info("Verified that video is played");
		} catch (Exception e) {
			logger.error("Exception while checking discovery up next "+e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Discovery up next tests failed");

	}
}