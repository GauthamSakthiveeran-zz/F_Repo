package com.ooyala.playback.amf.midroll;

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
 * Created by jitendra on 10/3/17.
 */
public class PlaybackOverrideEncodingPriorityMidrollPoddedAdTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackOverrideEncodingPriorityMidrollPoddedAdTests.class);
	private PlayValidator play;
	private PlayAction playAction;
	private SeekValidator seek;
	private EventValidator event;
	private EncodingValidator encode;

	public PlaybackOverrideEncodingPriorityMidrollPoddedAdTests() throws OoyalaException {
		super();
	}

	@Test(groups = "EncodingPriority", dataProvider = "testUrls")
	public void testOverrideEncodingPriorities(String testName, UrlObject url) {

		boolean result = true;
		String param = "";
		boolean isDRM = testName.toLowerCase().contains("drm");

		try {

			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("playing_1", 60000);

			result = result && seek.validate("seeked_1", 60000);

			result = result && (event.isAdPluginPresent("freewheel") ? event.validate("adsPlayed_2", 200000)
					: event.validate("adsPlayed_1", 200000));

			result = result && event.validate("videoPlayed_1", 60000);
			
			result = result && isDRM?encode.validateDRM():encode.getStreamType(url).verifyEncodingPriority(url);

			param = event.isAdPluginPresent("freewheel")
					? "{\"encodingPriority\":[\"hls\",\"webm\",\"mp4\",\"dash\"],\"freewheel-ads-manager\":{\"fw_video_asset_id\":\"Fsa2tmcjpvD21CN9WthbTbE4MIpWJoaK\",\"html5_ad_server\":\"http://g1.v.fwmrm.net\",\"html5_player_profile\":\"90750:ooyala_html5\",\"showInAdControlBar\":true},\"initialTime\":0,\"autoplay\":false}"
					: "{\"encodingPriority\":[\"hls\",\"webm\",\"mp4\",\"dash\"],\"showInAdControlBar\":true}";

			encode.getNewUrl(param, browser);

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("playing_1", 60000);

			result = result && seek.validate("seeked_1", 60000);

			result = result && (event.isAdPluginPresent("freewheel") ? event.validate("adsPlayed_2", 40000)
					: event.validate("adsPlayed_1", 40000));

			result = result && event.validate("videoPlayed_1", 60000);

			result = result && isDRM?encode.validateDRM():encode.getStreamType(url).verifyEncodingPriority(url);

		} catch (Exception e) {
			logger.error("Exception while checking OverrideEncoding Priority test  " + e.getMessage());
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "OverrideEncoding Priority test failed");
	}
}
