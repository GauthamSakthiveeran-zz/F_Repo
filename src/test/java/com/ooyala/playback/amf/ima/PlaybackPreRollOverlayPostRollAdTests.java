package com.ooyala.playback.amf.ima;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPreRollOverlayPostRollAdTests extends PlaybackWebTest {

	public PlaybackPreRollOverlayPostRollAdTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;

	@Test(groups = { "amf", "preroll", "overlay", "postroll" }, dataProvider = "testUrls")
	public void verifyPrerollOverlayPostrollAd(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			if (!event.isAdPluginPresent("ima")) {
				result = result && event.validate("willPlayNonlinearAd_1", 1000);
			}

			result = result && event.validate("videoPlaying_1", 90000);

			if (!getBrowser().equalsIgnoreCase("MicrosoftEdge"))
				result = result && seekValidator.validate("seeked_1", 6000);

			result = result && event.validate("videoPlayed_1", 160000);
			result = result && event.validate("PostRoll_willPlaySingleAd_1", 90000);

			result = result && event.validate("singleAdPlayed_1", 190000);

			result = result && event.validate("played_1", 200000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Test failed");

	}

}
