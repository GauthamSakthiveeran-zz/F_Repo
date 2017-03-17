package com.ooyala.playback.amf.preroll;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EncodingValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
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
	private PlayValidator play;
	private PlayAction playAction;
	private SeekValidator seek;
	private EventValidator event;
	private EncodingValidator encode;

	public PlaybackOverrideEncodingPriorityPrerollPoddedTests() throws OoyalaException {
		super();
	}

	@Test(groups = "EncodingPriority", dataProvider = "testUrls")
	public void testOverrideEncodingPriorities(String testName, String url) {

		boolean result = true;
		String param = "";

		try {

			driver.get(url);

			result = result && play.waitForPage();

			injectScript();

			encode.setTestUrl(url);

			result = result && encode.validate("validate_default_encoding", 20000);

            result = result && playAction.startAction();

			result = result && event.validate("adsPlayed_1", 50000);

			result = result && seek.validate("seeked_1", 60000);

			result = result && event.validate("videoPlayed_1", 60000);

			if (event.isAdPluginPresent("freewheel")) {
				param = "{\"encodingPriority\":[\"hls\",\"webm\",\"mp4\",\"dash\"],\"freewheel-ads-manager\":{\"fw_video_asset_id\":\"Fua2tmcjqpz9g6tBUEHukAOLuXaVZ7Lw\",\"html5_ad_server\":\"http://g1.v.fwmrm.net\",\"html5_player_profile\":\"90750:ooyala_html5\",\"showInAdControlBar\":true},\"initialTime\":0,\"autoplay\":false}";
			} else {
				param = "{\"encodingPriority\":[\"hls\",\"webm\",\"mp4\",\"dash\"],\"showInAdControlBar\":true}";
			}

			encode.setTestUrl(encode.getNewUrl(param, browser));

			injectScript();

			result = result && encode.validate("Override", 60000);

			result = result && playAction.startAction();

			result = result && event.validate("adsPlayed_1", 60000);

			result = result && seek.validate("seeked_1", 60000);

			result = result && event.validate("videoPlayed_1", 60000);

		} catch (Exception e) {
			logger.error("Exception while checking OverrideEncoding Priority test  "+e.getMessage());
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}

		Assert.assertTrue(result, "OverrideEncoding Priority test failed");
	}
}
