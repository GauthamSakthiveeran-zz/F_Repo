package com.ooyala.playback.playerfeatures;

import static java.lang.Thread.sleep;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.UpNextValidator;
import com.ooyala.playback.page.action.ClickDiscoveryButtonAction;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.PlayPauseAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/17/16.
 */
public class PlaybackDiscoveryCustomizationTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackDiscoveryCustomizationTests.class);
	private PlayValidator play;
	private UpNextValidator discoveryUpNext;
	private EventValidator eventValidator;
	private DiscoveryValidator discoveryValidator;
	private ClickDiscoveryButtonAction clickDiscoveryButtonAction;
	private PauseAction pauseAction;
	private PlayAction playAction;
	private PlayPauseAction playPauseAction;
	private SeekAction seekAction;

	public PlaybackDiscoveryCustomizationTests() throws OoyalaException {
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

            result = result &&	play.validate("playing_1", 60000);

			logger.info("verified video playing");

			Thread.sleep(3000);

			try {
                result = result && clickDiscoveryButtonAction.startAction();

				discoveryValidator.verifyDiscoveryEnabled("On_discovery_click", true); // verify discovery is disabled on discovery

                result = result && eventValidator.eventAction("DISCOVERY_CLOSE_BTN");
				logger.info("verified discovery close button is present or not");
				sleep(2000);
                result = result && play.validate("playing_2", 60000);
				logger.info("verified video playing again after discovery check");

                /*result = result &&	pauseAction.startAction();
				discoveryValidator.verifyDiscoveryEnabled("On_pauseScreen", true); // verify discovery is disabled on pause screen

				Thread.sleep(10000);

                result = result &&	playAction.startAction();*/

                result = result &&	eventValidator.eventAction("FULLSCREEN_BTN");
				logger.info("verified fullscreen");
				try {
					eventValidator.eventAction("PAUSE_BUTTON");
				} catch (Exception e) {
					eventValidator.eventAction("VIDEO");
				}
				discoveryValidator.verifyDiscoveryEnabled(
						"On_pause_FullScreen", true);
				sleep(1000);
                result = result && eventValidator.eventAction("DISCOVERY_CLOSE_BTN");

                result = result && playPauseAction.startAction();

                result = result &&	clickDiscoveryButtonAction.startAction();

				sleep(2000);
				discoveryValidator.verifyDiscoveryEnabled("On_discoveryclick_fullScreen", true);
                result = result && eventValidator.eventAction("DISCOVERY_CLOSE_BTN");
				logger.info("verified discovery in full screen");
                eventValidator.eventAction("NORMAL_SCREEN");

				sleep(2000);

                result = result && playAction.startAction();

                result = result && seekAction.setTime(20).fromLast().startAction();//seek(20, true);

				loadingSpinner();
				discoveryUpNext.validate("", 60000);
				try {
                    discoveryValidator.clickOnDiscoveryCloseButton("DISCOVERY_CLOSE_BTN",20000);
				} catch (Exception e) {
					playAction.startAction();
					seekAction.setTime(20).fromLast().startAction();//seek(20, true);
					eventValidator.validateElement("END_SCREEN", 60000);
                }
				discoveryValidator.verifyDiscoveryEnabled("On_endScreen", false);
			} catch (Exception e) {
				logger.info("Exception " + e);
			}
		} catch (Exception e) {
			e.printStackTrace();
            result = false;
		}
		Assert.assertTrue(result, "Discovery customization tests failed");
	}

}
