package com.ooyala.playback.VTC;

import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.SeekAction;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by jitendra on 24/11/16.
 */
public class PlaybackDiscoverTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackDiscoverTests.class);
	private EventValidator eventValidator;
	private PlayValidator play;
	private UpNextValidator discoveryUpNext;
	private DiscoveryValidator discoveryValidator;
	private PlayAction playAction;
	private SeekAction seekAction;

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

			injectScript();

			result = result && play.validate("playing_1", 60000);

			result = result
					&& discoveryValidator.validate("reportDiscoveryClick_1",
							60000);

			result = result && eventValidator.validate("playing_2", 60000);

			result = result && seekAction.seek(15,true);

			result = result && eventValidator.validate("seeked_1",20000);

    		result = result && discoveryUpNext.validate("", 60000);

			result = result && eventValidator.validate("played_1", 60000);

		} catch (Exception e) {
			logger.error(e);
			result = false;
			extentTest.log(LogStatus.FAIL, "Playback Discovery tests failed", e);
		}
		Assert.assertTrue(result, "Playback Discovery tests failed");
	}

}
