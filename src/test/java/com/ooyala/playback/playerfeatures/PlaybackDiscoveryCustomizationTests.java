package com.ooyala.playback.playerfeatures;

import com.ooyala.playback.page.*;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.action.ClickDiscoveryButtonAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.PlayPauseAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/17/16.
 */
public class PlaybackDiscoveryCustomizationTests extends PlaybackWebTest {

	private static Logger logger = Logger
			.getLogger(PlaybackDiscoveryCustomizationTests.class);
	private PlayValidator play;
	private UpNextValidator discoveryUpNext;
	private EventValidator eventValidator;
	private DiscoveryValidator discoveryValidator;
	private ClickDiscoveryButtonAction clickDiscoveryButtonAction;
	private PlayAction playAction;
	private PlayPauseAction playPauseAction;
	private SeekAction seekAction;
	private FullScreenValidator fullScreenValidator;

	public PlaybackDiscoveryCustomizationTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testDiscoveryUpNext(String testName, UrlObject url)
			throws OoyalaException {
		boolean result = true;

		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			result = result && clickDiscoveryButtonAction.startAction();

			result = result && discoveryValidator.verifyDiscoveryEnabled("On_discovery_click",true); // verify discovery is disabled on discovery

			result = result && eventValidator.eventAction("DISCOVERY_CLOSE_BTN");

			result = result && play.validate("playing_2", 60000);

			result = result && eventValidator.eventAction("FULLSCREEN_BTN");

            result = result && eventValidator.eventAction("PAUSE_BUTTON");

			result = result && discoveryValidator.verifyDiscoveryEnabled("On_pause_FullScreen",true);

			result = result && eventValidator.eventAction("DISCOVERY_CLOSE_BTN");

			result = result && playPauseAction.startAction();

			result = result && clickDiscoveryButtonAction.startAction();

			result = result && discoveryValidator.verifyDiscoveryEnabled("On_discoveryclick_fullScreen",true);

			result = result && eventValidator.eventAction("DISCOVERY_CLOSE_BTN");

			result = result && fullScreenValidator.getNormalScreen();

			result = result && playAction.startAction();

			result = result && seekAction.setTime(20).fromLast().startAction();

			result = result && eventValidator.loadingSpinner();

			result = result && discoveryUpNext.validate("", 60000);

            result = result && discoveryValidator.clickOnDiscoveryCloseButton();

            result =result && discoveryValidator
						.verifyDiscoveryEnabled("On_endScreen",false);
		} catch (Exception e) {
            logger.error("Discovery customization tests failed" + e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Discovery customization tests failed");
	}

}
