package com.ooyala.playback.amf.postroll;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 15/03/17.
 */
public class PlaybackAutoplayAutoloopPostrollAdTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackAutoplayAutoloopPostrollAdTests.class);
	private EventValidator eventValidator;
	private SeekValidator seek;
	private SeekAction seekAction;

	public PlaybackAutoplayAutoloopPostrollAdTests() throws OoyalaException {
		super();
	}

	@Test(groups = { "amf", "autoplay" }, dataProvider = "testUrls")
	public void testAutoplayAutoloop(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());
			
			result = result && eventValidator.isPageLoaded();

			injectScript();
			
			result = result && eventValidator.validateAutoPlay();
			result = result && eventValidator.validate("playing_1", 60000);
			result = result && seek.validate("seeked_1", 60000);
			result = result && eventValidator.validate("played_1", 60000);
			result = result && eventValidator.validate("PostRoll_willPlaySingleAd_1", 5000);
			result = result && eventValidator.validate("adsPlayed_1", 45000);
			result = result && eventValidator.validate("replay_1", 60000);
			result = result && eventValidator.validate("playing_2", 60000);
			result = result && seekAction.seekTillEnd().startAction();
			result = result && eventValidator.validate("played_2", 60000);
			result = result && eventValidator.validate("willPlayAds_OnReplay", 5000);
			result = result && eventValidator.validate("adsPlayed_2", 45000);

		} catch (Exception e) {
			logger.error(e);
			result = false;
			extentTest.log(LogStatus.FAIL, "Playback Autoplay Autoloop test failed for " + testName + "", e);
		}
		Assert.assertTrue(result, "Playback Autoplay Autoloop test failed for " + testName + "");
	}
}
