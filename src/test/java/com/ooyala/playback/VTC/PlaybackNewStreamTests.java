package com.ooyala.playback.VTC;

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
 * Created by snehal on 25/11/16.
 */
public class PlaybackNewStreamTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackNewStreamTests.class);
	private PlayValidator play;
	private DiscoveryValidator discoveryValidator;
	private EventValidator eventValidator;
	private PlayAction playAction;

	public PlaybackNewStreamTests() throws OoyalaException {
		super();
	}

	@Test(groups = "Playback", dataProvider = "testUrls")
	public void testLoadingNewStream(String testName, UrlObject url)
			throws OoyalaException {
		boolean result = true;
		try {
			driver.get(url.getUrl());

			play.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && eventValidator.validate("playing_1", 60000);

			driver.executeScript("pp.setEmbedCode('Vmd2VmeDq6-92C-kPkkZGoOkTCeSZq4e')");

			result = result && eventValidator.validate("setEmbedCode_1", 15000);

			driver.executeScript("pp.play()");

			result = result && eventValidator.validate("playing_2", 60000);

			result = result
					&& discoveryValidator.validate("reportDiscoveryClick_1",
							6000);

		} catch (Exception e) {
			logger.error("Exception while checking stream tests  "+e.getMessage());
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Playback new stream tests failed");
	}
}
