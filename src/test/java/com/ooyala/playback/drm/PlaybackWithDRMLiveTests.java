package com.ooyala.playback.drm;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.BitmovinTechnologyValidator;
import com.ooyala.playback.page.DRMValidator;
import com.ooyala.playback.page.ErrorDescriptionValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.LiveValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackWithDRMLiveTests extends PlaybackWebTest {

	private EventValidator eventValidator;
	private PlayValidator play;
	private PauseValidator pause;
	private DRMValidator drm;
    private LiveValidator live;
    private ErrorDescriptionValidator error;
    private BitmovinTechnologyValidator tech;

	public PlaybackWithDRMLiveTests() throws OoyalaException {
		super();
	}

	@Test(groups = "drm", dataProvider = "testUrls")
	public void testPlaybackWithDRMLive(String testName, UrlObject url)
			throws OoyalaException {

        boolean isChannelIdPresent = false;
		boolean result = true;

		try {

            isChannelIdPresent = live.isChannelIdPresent(url);
            if (isChannelIdPresent) {
                result = result && liveChannel.startChannel(url.getChannelId(), url.getProvider());
            }

			driver.get(url.getUrl());

			result = result && drm.isPageLoaded();

			injectScript();

            tech.getConsoleLogs();

            result = result && drm.validate("drm_tag", 5000);
			
			result = result && play.waitForPage();
			
			result = result && play.validate("playing_1", 60000);
			
			result = result && pause.validate("paused_1", 60000);

			result = result && play.validate("playing_2", 60000);


            if (isChannelIdPresent) {

                result = result && liveChannel.stopChannels();

                driver.get(url.getUrl());

                result = result && error.expectedErrorCode("network_error")
						.expectedErrorDesc("Network connection temporarily lost").validate("", 30000);
            }

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "DRM tests failed : " + testName);
	}
}