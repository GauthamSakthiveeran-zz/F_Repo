package com.ooyala.playback.playerfeatures;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackLocalizationTests extends PlaybackWebTest {

	private static Logger logger = Logger
			.getLogger(PlaybackLocalizationTests.class);

	private PlayValidator play;
	private PauseValidator pause;
	private SeekValidator seek;
	private PlayAction playAction;
	private EventValidator eventValidator;
	private ShareTabValidator shareTabValidator;
	private FullScreenValidator fullScreenValidator;

	public PlaybackLocalizationTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testPlaybackLocalization(String testName, UrlObject url)
			throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			result = result && pause.validate("paused_1", 60000);

			result = result && shareTabValidator.validate("", 60000);

			if (!(getBrowser().equalsIgnoreCase("safari") || getPlatform().equalsIgnoreCase("Android") || getBrowser().equalsIgnoreCase("MicrosoftEdge"))) {
				result = result && fullScreenValidator.getFullScreen();

				result = result && shareTabValidator.validate("", 60000);

				result = result && fullScreenValidator.getNormalScreen();
			}

			result = result && playAction.startAction();

			result = result && seek.validate("seeked_1", 60000);

			result = result && eventValidator.validate("played_1", 60000);

		} catch (Exception e) {
			logger.error(e);
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Playback Localization tests failed");
	}
}
