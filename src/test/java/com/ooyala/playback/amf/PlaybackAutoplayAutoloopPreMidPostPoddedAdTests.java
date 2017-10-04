package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.action.PlayerAPIAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 15/03/17.
 */
public class PlaybackAutoplayAutoloopPreMidPostPoddedAdTests extends PlaybackWebTest {

	private EventValidator eventValidator;
	private PlayerAPIAction playerAPI;
	private SeekAction seek;
	private PoddedAdValidator poddedAds;

	public PlaybackAutoplayAutoloopPreMidPostPoddedAdTests() throws OoyalaException {
		super();
	}

	@Test(groups = { "amf", "autoplay", "vtc" }, dataProvider = "testUrls")
	public void testAutoplayAutoloop(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && eventValidator.isPageLoaded();

			injectScript();

			result = result && eventValidator.validateAutoPlay();

			result = result && eventValidator.validate("adsPlayed_1", 45000);
			result = result && eventValidator.validate("countPoddedAds_1", 60000);

			int noOfPrerollAds = Integer.parseInt(playerAPI.getTextContent("countPoddedAds_1"));

			result = result && poddedAds.validatePoddedAds(0, noOfPrerollAds);

			result = result && !eventValidator.isAdPluginPresent("freewheel") ? seek.seek(15, true) : true;

			result = result && eventValidator.validate("countPoddedAds_2", 60000);
			int noOfMidrollAds = Integer.parseInt(playerAPI.getTextContent("countPoddedAds_2"));
			result = result && poddedAds.validatePoddedAds(noOfPrerollAds, noOfMidrollAds);

			result = result && !eventValidator.isAdPluginPresent("freewheel") ? seek.seek(15, true) : true;

			result = result && eventValidator.validate("countPoddedAds_3", 60000);
			int noOfPostrollAds = Integer.parseInt(playerAPI.getTextContent("countPoddedAds_3"));
			result = result && poddedAds.validatePoddedAds(noOfMidrollAds, noOfPostrollAds);

			result = result && eventValidator.validate("replay_1", 60000);

			result = result && eventValidator.validate("countPoddedAds_4", 60000);
			int noOfPrerollAdsOnReplay = Integer.parseInt(playerAPI.getTextContent("countPoddedAds_4"));
			result = result && poddedAds.validatePoddedAds(noOfPostrollAds, noOfPrerollAdsOnReplay);

			result = result && !eventValidator.isAdPluginPresent("freewheel") ? seek.seek(15, true) : true;

			result = result && eventValidator.validate("countPoddedAds_5", 60000);
			int noOfMidrollAdsOnReplay = Integer.parseInt(playerAPI.getTextContent("countPoddedAds_5"));
			result = result && poddedAds.validatePoddedAds(noOfPrerollAdsOnReplay, noOfMidrollAdsOnReplay);

			result = result && !eventValidator.isAdPluginPresent("freewheel") ? seek.seek(15, true) : true;

			result = result && eventValidator.validate("countPoddedAds_6", 60000);
			int noOfPostrollAdsOnReplay = Integer.parseInt(playerAPI.getTextContent("countPoddedAds_6"));
			result = result && poddedAds.validatePoddedAds(noOfMidrollAdsOnReplay, noOfPostrollAdsOnReplay);

		} catch (Exception e) {
			logger.error(e);
			result = false;
			extentTest.log(LogStatus.FAIL, "Playback Autoplay Autoloop test failed for " + testName + "", e);
		}
		Assert.assertTrue(result, "Playback Autoplay Autoloop test failed for " + testName + "");
	}

}
