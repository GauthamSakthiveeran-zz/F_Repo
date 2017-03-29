package com.ooyala.playback.amf.ima;

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

	@Test(groups = {"amf","customInteraction"}, dataProvider = "testUrls")
	public void verifyCustomeInteractionAd(String testName, UrlObject url)
			throws Exception {

		try {

			driver.get(url.getUrl());

			playValidator.waitForPage();

			injectScript();

			playAction.startAction();

			event.validate("willPlaySingleAd_1", 190000);

			adSkipButtonValidator.validate("showAdSkipButton_1", 60000);

			event.validate("singleAdPlayed_1", 190000);

			event.validate("playing_1", 60000);

			volumeValidator.validate("", 60000);

			seekAction.seekTillEnd().startAction();

			event.validate("seeked_1", 180000);

			event.validate("played_1", 200000);

		} catch (Exception e) {
			e.printStackTrace();
			extentTest.log(LogStatus.ERROR, e.getMessage());
		}

	}

}
