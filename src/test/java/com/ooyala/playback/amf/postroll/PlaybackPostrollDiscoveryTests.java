package com.ooyala.playback.amf.postroll;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.UpNextValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPostrollDiscoveryTests extends PlaybackWebTest {

	public PlaybackPostrollDiscoveryTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private DiscoveryValidator discoveryValidator;
	private SeekAction seekAction;
	private UpNextValidator upNextValidator;

	@Test(groups = {"amf","postroll","discovery","upnext"}, dataProvider = "testUrls")
	public void verifyPostrollDiscovery(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 150000);
			
			if (event.isAdPluginPresent("pulse"))
				result = result && seekAction.fromLast().setTime(15).startAction();
			else
				result = result && seekAction.fromLast().setTime(30).startAction();
			
			result = result && upNextValidator.validate("", 60000);
			
			result = result && event.validate("PostRoll_willPlaySingleAd_1", 90000);

			if (event.isAdPluginPresent("pulse"))
				result = result && event.validate("singleAdPlayed_2", 60000);
			else
				result = result && event.validate("singleAdPlayed_1", 60000);
			
			result = result && discoveryValidator.validateDiscoveryToaster();

			result = result && discoveryValidator.validateLeftRightButton();

			result = result && discoveryValidator.clickOnDiscoveryCloseButton("DISCOVERY_CLOSE_BTN", 20000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");

	}

}
