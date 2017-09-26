package com.ooyala.playback.playerParameter;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.OverlayValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.VolumeValidator;
import com.ooyala.playback.page.action.AutoplayAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackOverlayAdsWithInitialTime extends PlaybackWebTest {

	public static Logger logger = Logger.getLogger(PlaybackOverlayAdsWithInitialTime.class);

	public PlaybackOverlayAdsWithInitialTime() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private OverlayValidator overlayValidator;
	private SeekValidator seekValidator;
	private AutoplayAction autoplayAction;
	private VolumeValidator volumeValidator;

	@Test(groups = { "autoplay" }, dataProvider = "testUrls")
	public void verifyOverlayWithInitialTime(String testDescription, UrlObject url) throws OoyalaException {
		boolean result = true;
		try {
			driver.get(url.getUrl());

			result = result && event.isPageLoaded();	
			

			injectScript();

			result = result && event.loadingSpinner();
			result = result && autoplayAction.startAction();

		//result = result && event.validate("willPlayNonlinearAd_1", 10000);
			result = result && playValidator.getPlayAheadTime()>=10.0;
			result = result && overlayValidator.validateClickThrough("paused_1", 7000, url.getAdPlugins());
		
		    result = result && overlayValidator.validateOverlayRenderingEvent(6000);
			result = result && volumeValidator.validateInitialVolume(0.5);
			
			
			result = result && overlayValidator.validate("nonlinearAdPlayed_1", 160000);
			
			result = result && event.validate("played_1", 190000);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "PlaybackOverlayAdsWithInitialTime  Testsfailed");
	}
}
