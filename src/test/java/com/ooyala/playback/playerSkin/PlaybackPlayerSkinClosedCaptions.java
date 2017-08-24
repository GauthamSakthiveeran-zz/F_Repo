package com.ooyala.playback.playerSkin;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by Gautham
 */
public class PlaybackPlayerSkinClosedCaptions extends PlaybackWebTest {
	private PlayValidator play;
	private PlayerSkinCaptionsValidator skinCCValidator;
	private EventValidator eventValidator;
	private PauseValidator pause;
	private static Logger logger = Logger.getLogger(PlaybackPlayerSkinClosedCaptions.class);

	public PlaybackPlayerSkinClosedCaptions() throws OoyalaException {
		super();
	}

	@Test(groups = "PlayerSkin", dataProvider = "testUrls")
	public void testFCCClosedcaption(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;
		try {

			String urlLink = url.getUrl();

			driver.get(urlLink);

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			result = result && eventValidator.playVideoForSometime(15);

			result = result && pause.validate("paused_1", 60000);

			result = result
					&& skinCCValidator.verifyWebElementCSSProperty("CC_PREVIEW_TEXT_BG", "background-color", "red");

			result = result && skinCCValidator.verifyWebElementCSSProperty("CC_PREVIEW_TEXT", "color", "yellow");

			// result = result &&
			// skinValidator.verifyWebElementCSSColor("CC_PREVIEW_TEXT","font-size","Large");

			// result = result &&
			// skinValidator.verifyWebElementCSSColor("CC_PREVIEW_TEXT","text-shadow","Depressed");

		} catch (Exception e) {
			logger.error(e);
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "PlayerSkin CC tests failed :" + testName);
	}
}
