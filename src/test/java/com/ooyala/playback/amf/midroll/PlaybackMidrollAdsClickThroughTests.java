package com.ooyala.playback.amf.midroll;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdClickThroughValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackMidrollAdsClickThroughTests extends PlaybackWebTest {

	public PlaybackMidrollAdsClickThroughTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private AdClickThroughValidator clickThrough;

	@Test(groups = { "amf", "midroll", "sequential", "clickThrough" }, dataProvider = "testUrls")
	public void verifyMidrollClickthrough(String testName, UrlObject url) throws Exception {
		boolean result = true;

		try {
			driver.get(url.getUrl());
			result = result && playValidator.waitForPage();
			injectScript();
			result = result && playAction.startAction();
			result = result && event.validate("MidRoll_willPlaySingleAd_1", 60000);
			result = result && clickThrough.validate("", 120000);

			if (event.isAdPluginPresent("pulse"))
				result = result && event.validate("singleAdPlayed_2", 120000);
			else
				result = result && event.validate("singleAdPlayed_1", 120000);

			result = result && event.validate("playing_1", 35000);
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}
		Assert.assertTrue(result, "Midroll Clickthrough is failed");
	}
}
