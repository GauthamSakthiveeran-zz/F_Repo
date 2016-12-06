package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.UpNextValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPostrollDiscoveryTests extends PlaybackWebTest {

	public PlaybackPostrollDiscoveryTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private DiscoveryValidator discoveryValidator;
	private SeekAction seekAction;
	private UpNextValidator upNextValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPostrollDiscovery(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 150000);

			Thread.sleep(3000);

			result = result && discoveryValidator.validateDiscoveryToaster();

			result = result && discoveryValidator.validateLeftRightButton();

			result = result && discoveryValidator.clickOnDiscoveryCloseButton("DISCOVERY_CLOSE_BTN", 20000);

			result = result && playAction.startAction();

			result = result && seekAction.fromLast().setTime(10).startAction();

			result = result && upNextValidator.validate("", 60000);

			result = result && upNextValidator.validate("", 60000);

			result = result && event.validate("willPlaySingleAd_1", 90000);

			if (event.isAdPluginPresent("pulse"))
				result = result && event.validate("singleAdPlayed_2", 60000);
			else
				result = result && event.validate("singleAdPlayed_1", 60000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");

	}

}
