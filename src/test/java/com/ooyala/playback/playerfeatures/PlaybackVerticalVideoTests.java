package com.ooyala.playback.playerfeatures;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AspectRatioValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackVerticalVideoTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackVerticalVideoTests.class);
	private PlayValidator play;
	private SeekAction seek;
	private EventValidator eventValidator;
	private AspectRatioValidator aspectRatioValidator;

	public PlaybackVerticalVideoTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testVerticalVideo(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;
		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();
			
			executeScript("pp.play();");

			result = result && eventValidator.validate("playing_1", 60000);

			result = result && aspectRatioValidator.setVerticalVideo().validate("assetDimension_1", 60000);

			result = result && seek.seekTillEnd().startAction();

			result = result && aspectRatioValidator.setVerticalVideo().validate("assetDimension_1", 60000);

			result = result && eventValidator.validate("videoPlayed_1", 60000);

		} catch (Exception e) {
			logger.error(e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Vertical Video tests failed");
	}

}