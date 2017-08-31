package com.ooyala.playback.playerParameter;


import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.AutoplayAction;
import com.ooyala.playback.page.action.ChromeFlashUpdateAction;
import com.ooyala.playback.page.action.ClickDiscoveryButtonAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by Gautham
 */
public class PlayerParametersDiscoveryUpnextDisabledAutoPlay extends PlaybackWebTest {
	private static Logger logger = Logger
			.getLogger(PlayerParametersDiscoveryUpnextDisabledAutoPlay.class);

	private PlayValidator play;
	private PauseValidator pause;
	private SeekValidator seek;
	private EventValidator eventValidator;
	private CCValidator ccValidator;
	private AutoplayAction autoplayAction;
	private PlayAction playAction;
	private PlayerAPIValidator apiValidator;
	private ChromeFlashUpdateAction chromeValidator;
	private ClickDiscoveryButtonAction clickDiscoveryButtonAction;
	private DiscoveryValidator discoveryValidator;
	private SeekAction seekAction;

	public PlayerParametersDiscoveryUpnextDisabledAutoPlay() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testClosedCaption(String testName, UrlObject url)
			throws OoyalaException {

		boolean result = true;

		try {
			
			
			driver.get(url.getUrl());

			result = result && eventValidator.isPageLoaded();

			injectScript();
			
			result = result && eventValidator.loadingSpinner();

			result = result && (apiValidator.isPlayerState("playing"));
			
			result = result && eventValidator.playVideoForSometime(10);

			result = result && clickDiscoveryButtonAction.startAction();
			
			result = result && discoveryValidator.verifyDiscoveryEnabled("On_discovery_click",true);
			
			result = result && eventValidator.eventAction("DISCOVERY_CLOSE_BTN");
			
			result = result && playAction.startAction();
			
			result = result && eventValidator.eventAction("PAUSE_BUTTON");

			result = result && discoveryValidator.verifyDiscoveryEnabled("On_pause_FullScreen",true);

			result = result && eventValidator.eventAction("DISCOVERY_CLOSE_BTN");
			
			result = result && seekAction.setTime(20).startAction();
			
			result = result && playAction.startAction();
			
			result = result && eventValidator.validate("played_1", 60000);
			
			result = result && eventValidator.loadingSpinner();

			result = result && (apiValidator.isPlayerState("ready")) ;


			
		} catch (Exception e) {
			logger.error(e);
			result = false;
			extentTest.log(LogStatus.FAIL, "Playback Closed Caption tests failed", e);

		}
		Assert.assertTrue(result, "Closed Caption tests failed");
	}
}
