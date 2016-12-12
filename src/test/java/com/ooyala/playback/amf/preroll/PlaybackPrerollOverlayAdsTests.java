package com.ooyala.playback.amf.preroll;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.OverlayValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPrerollOverlayAdsTests extends PlaybackWebTest {

	public PlaybackPrerollOverlayAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private OverlayValidator overLayValidator;
	private SeekValidator seekValidator;

	@Test(groups = {"amf","preroll","overlay"}, dataProvider = "testUrls")
	public void verifyPrerollOverlay(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			// added condition for IMA OVerlay as overlay is showing
			// intermittently PBI-1825
			if (!(event.isAdPluginPresent("ima") || event.isAdPluginPresent("freewheel")))
				result = result && overLayValidator.validate("nonlinearAdPlayed_1", 6000);
			else if (!event.isAdPluginPresent("ima"))
				result = result && event.validate("nonlinearAdPlayed_1", 6000);

			result = result && event.validate("videoPlaying_1", 9000);

			result = result && seekValidator.validate("seeked_1", 6000);

			result = result && event.validate("videoPlaying_1", 9000);

			result = result && seekValidator.validate("seeked_1", 6000);

			result = result && event.validate("played_1", 5000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Test failed");

	}

}
