package com.ooyala.playback.drm;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.BitmovinTechnologyValidator;
import com.ooyala.playback.page.DRMValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.StreamValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackDRMTests extends PlaybackWebTest {

	private EventValidator eventValidator;
	private PlayValidator play;
	private PauseValidator pause;
	private SeekValidator seek;
	private SeekAction seekAction;
	private DRMValidator drm;
	private BitmovinTechnologyValidator tech;
    private StreamValidator stream;

	public PlaybackDRMTests() throws OoyalaException {
		super();
	}

	@Test(groups = "drm", dataProvider = "testUrls")
	public void testPlaybackDRM(String testName, UrlObject url)
			throws OoyalaException {
		boolean result = true;
		
		try {
			driver.get(url.getUrl());

            result = result && drm.isPageLoaded();
            
            tech.getConsoleLogs();
            
			result = result && play.waitForPage();

            injectScript();

            result = result && drm.validate("drm_tag", 5000);
			
			result = result && play.validate("playing_1", 60000);
			
			result = result && eventValidator.playVideoForSometime(3);

            result = result && pause.validate("paused_1", 60000);

			if (url.getStreamType()!=null && !url.getStreamType().isEmpty()){
				result = result && tech.setStream(url.getStreamType()).validate("bitmovin_technology", 15000);
                result = result && stream.verifyStreamType(url.getStreamType());
			}

			result = result && play.validate("playing_2", 60000);

			if (!(testName.split("-")[1].trim()
					.equalsIgnoreCase("elemental fairplay fairplay hls + opt")))
				result = result && seek.validate("seeked_1", 60000);
			else{
				result = result && seekAction.fromLast().setTime(2).startAction();
				result = result && eventValidator.validate("seeked_1", 60000);
			}

			result = result && eventValidator.validate("played_1", 60000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "DRM tests failed : " + testName);
	}
}