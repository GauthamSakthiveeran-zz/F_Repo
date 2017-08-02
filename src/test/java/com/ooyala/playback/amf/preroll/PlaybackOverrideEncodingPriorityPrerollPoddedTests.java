package com.ooyala.playback.amf.preroll;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EncodingValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 28/11/16.
 */
public class PlaybackOverrideEncodingPriorityPrerollPoddedTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackOverrideEncodingPriorityPrerollPoddedTests.class);
	private PlayValidator playValidator;
	private PlayAction playAction;
	private SeekValidator seek;
	private EventValidator event;
	private EncodingValidator encode;

	public PlaybackOverrideEncodingPriorityPrerollPoddedTests() throws OoyalaException {
		super();
	}

	@Test(groups = { "encodingPriority", "preroll" }, dataProvider = "testUrls")
	public void testOverrideEncodingPriorities(String testName, UrlObject url) {

		boolean result = true;
		String param = "";
		boolean isDRM = testName.toLowerCase().contains("drm");

		try {
			driver.get(url.getUrl());
			result = result && playValidator.waitForPage();
			injectScript();
			result = result && playAction.startAction();
            result = result && event.validate("adsPlayed_1", 50000);
			result = result && event.loadingSpinner();
			if (result && isDRM){
				result = result && encode.validateDRM();
			}else
				result = result && encode.getStreamType(url).verifyEncodingPriority(url);

			param = event.isAdPluginPresent("freewheel")
					? "{\"encodingPriority\":[\"hls\",\"webm\",\"mp4\",\"dash\"],\"freewheel-ads-manager\":{\"fw_video_asset_id\":\"Fua2tmcjqpz9g6tBUEHukAOLuXaVZ7Lw\",\"html5_ad_server\":\"http://g1.v.fwmrm.net\",\"html5_player_profile\":\"90750:ooyala_html5\",\"showInAdControlBar\":true},\"initialTime\":0,\"autoplay\":false}"
					: "{\"encodingPriority\":[\"hls\",\"webm\",\"mp4\",\"dash\"],\"showInAdControlBar\":true}";

			encode.getNewUrl(param, browser);
			injectScript();
			result = result && playAction.startAction();
            result = result && event.validate("adsPlayed_1", 50000);
			result = result && event.loadingSpinner();
			if (result && isDRM){
				result = result && encode.validateDRM();
			}else
				result = result && encode.getStreamType(url).verifyEncodingPriority(url);

		} catch (Exception e) {
			logger.error("Exception while checking OverrideEncoding Priority test  " + e.getMessage());
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "OverrideEncoding Priority test failed");
	}
}
