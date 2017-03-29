package com.ooyala.playback.playerfeatures;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.UpNextValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/11/16.
 */

public class DiscoveryUpNextTests extends PlaybackWebTest {

	private EventValidator eventValidator;
	private PlayValidator play;
	private UpNextValidator discoveryUpNext;
	private SeekAction seek;

	public DiscoveryUpNextTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testDiscoveryUpNext(String testName, UrlObject url)
			throws OoyalaException {
		boolean result = true;

		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			result = result && seek.setTime(31).fromLast().startAction();

			result = result && discoveryUpNext.validate("", 60000);

			result = result && eventValidator.validate("played_1", 60000);

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Discovery up next tests failed");

	}
}