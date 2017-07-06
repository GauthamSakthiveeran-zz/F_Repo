package com.ooyala.playback.platformParameter;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.BitmovinTechnologyValidator;
import com.ooyala.playback.page.ControlBarValidator;
import com.ooyala.playback.page.ErrorDescriptionValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.FullScreenValidator;
import com.ooyala.playback.page.LiveValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.StreamValidator;
import com.ooyala.playback.page.action.LiveAction;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by Gautham on 11/16/16.
 */
public class PlatformParameterLiveTest extends PlaybackWebTest {

	private PlayValidator play;
	private PauseValidator pause;
	private ControlBarValidator controlBarValidator;
	private FullScreenValidator fullScreenValidator;
	private LiveAction liveAction;
	private PauseAction pauseAction;
	private PlayAction playAction;
	private LiveValidator live;
	private ErrorDescriptionValidator error;
	private StreamValidator streamTypeValidator;
	private BitmovinTechnologyValidator bitmovinvalidator;
	private EventValidator event;

	public PlatformParameterLiveTest() throws OoyalaException {
		super();
	}

	@Test(groups = "streams", dataProvider = "testUrls")
	public void testLive(String testName, UrlObject url) throws OoyalaException {
		boolean isChannelIdPresent = false;
		boolean result = true;

		try {

			isChannelIdPresent = live.isChannelIdPresent(url);
			
			System.out.println("Channel ID : " + url.getChannelId());


		if (isChannelIdPresent) {
				liveChannel.startChannel(url.getChannelId(), url.getProvider());
		}
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();
			
			bitmovinvalidator.getConsoleLogs();

			result = result && play.validate("playing_1", 60000);

			result = result && pause.validate("paused_1", 60000);
			
			result = result && bitmovinvalidator.setStream(url.getStreamType()).validate("bitmovin_technology", 6000);
			

			if (url.getStreamType() != null && !url.getStreamType().isEmpty()) {
                result = result && event.validate("videoPlayingurl", 40000);
                result = result && streamTypeValidator.setStreamType(url.getStreamType()).validate("videoPlayingurl", 1000);
            }

			result = result && controlBarValidator.validate("", 60000);
			// to-do add ooyala logo to the test page

			result = result && fullScreenValidator.validate("FULLSCREEN_BTN_1", 60000);

			result = result && pauseAction.startAction();

			result = result && liveAction.startAction();

			if (isChannelIdPresent) 
				liveChannel.stopChannels();

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}
		Assert.assertTrue(result, "Playback Live tests passed");

	}
}
