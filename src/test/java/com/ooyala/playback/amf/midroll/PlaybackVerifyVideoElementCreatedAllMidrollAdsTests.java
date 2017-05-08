package com.ooyala.playback.amf.midroll;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DifferentElementValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 2/3/17.
 */
public class PlaybackVerifyVideoElementCreatedAllMidrollAdsTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackVerifyVideoElementCreatedAllMidrollAdsTests.class);
	private EventValidator eventValidator;
	private PlayValidator play;
	private PlayAction playAction;
	private DifferentElementValidator differentElementValidator;
	private SeekValidator seekValidator;

	PlaybackVerifyVideoElementCreatedAllMidrollAdsTests() throws OoyalaException {
		super();
	}

	@Test(groups = { "videoCreated", "amf" }, dataProvider = "testUrls")
	public void testVideoElementCreated(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;
		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && eventValidator.validate("playing_1", 5000);

			result = result && seekValidator.validate("seeked_1", 60000);

			result = result && eventValidator.validate("MidRoll_willPlayAds", 60000);

			result = result && (eventValidator.isAdPluginPresent("freewheel")
					? eventValidator.validate("adsPlayed_2", 40000) : eventValidator.validate("adsPlayed_1", 40000));

			result = result && differentElementValidator.validateMainVideoElementId("VIDEO_ELEMENT", 20000);

			if (!eventValidator.isAdPluginPresent("ima")) {
				result = result && differentElementValidator.validateAdElementId("AD_ELEMENT", 5000);
			}

		} catch (Exception e) {
			logger.error("Exception while checking Video Element tests  " + e.getMessage());
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}
		Assert.assertTrue(result, "Playback Video Element test failed");

	}
}
