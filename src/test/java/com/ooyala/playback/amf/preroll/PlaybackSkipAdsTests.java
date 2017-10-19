package com.ooyala.playback.amf.preroll;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdSkipButtonValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackSkipAdsTests extends PlaybackWebTest {

	public PlaybackSkipAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private AdSkipButtonValidator skipButtonValidator;

	@Test(groups = { "amf", "skipads" }, dataProvider = "testUrls")
	public void verifySkipButton(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();
			result = result && event.validate("willPlaySingleAd_1", 150000);

			// Unable to click skip ad
			// button for IMA
			result = result && !event.isAdPluginPresent("ima") ? skipButtonValidator.validate("", 120000)
					: event.validate("showAdSkipButton_1", 150000);

			 result = result && event.validateSingleAdPlayedEvent(1);

			result = result && event.validate("playing_1", 150000);

			result = result && seekValidator.validate("seeked_1", 150000);
			result = result && event.validate("played_1", 200000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Test failed");

	}

}
