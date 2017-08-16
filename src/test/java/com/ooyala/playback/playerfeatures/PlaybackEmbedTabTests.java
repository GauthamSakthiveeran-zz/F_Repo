package com.ooyala.playback.playerfeatures;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EmbedTabValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackEmbedTabTests extends PlaybackWebTest {

	private PlayValidator play;
	private EmbedTabValidator embedTab;
	private EventValidator event;

	public PlaybackEmbedTabTests() throws OoyalaException {
        super();
    }

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testEmbedTab(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;
		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			result = result && event.playVideoForSometime(2);

			result = result && embedTab.setUrlObject(url).validate("", 60000);

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}
		s_assert.assertAll();
		Assert.assertTrue(result, "Social Media tests failed");
	}

}
