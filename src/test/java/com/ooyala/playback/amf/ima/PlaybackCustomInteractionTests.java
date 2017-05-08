package com.ooyala.playback.amf.ima;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdSkipButtonValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.VolumeValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackCustomInteractionTests extends PlaybackWebTest {

	public PlaybackCustomInteractionTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private VolumeValidator volumeValidator;
	private AdSkipButtonValidator adSkipButtonValidator;

	@Test(groups = { "amf", "customInteraction" }, dataProvider = "testUrls")
	public void verifyCustomeInteractionAd(String testName, UrlObject url) throws Exception {
		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("willPlaySingleAd_1", 190000);

			result = result && adSkipButtonValidator.validate("showAdSkipButton_1", 60000);

			result = result && event.validate("singleAdPlayed_1", 190000);

			result = result && event.validate("playing_1", 60000);

			result = result && volumeValidator.validate("", 60000);

			result = result && seekAction.seekTillEnd().startAction();

			result = result && event.validate("seeked_1", 180000);

			result = result && event.validate("played_1", 200000);

		} catch (Exception e) {
			e.printStackTrace();
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}
		Assert.assertTrue(result, "PlaybackCustomInteractionTests failed.");

	}

}
