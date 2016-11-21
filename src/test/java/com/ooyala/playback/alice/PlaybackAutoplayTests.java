package com.ooyala.playback.alice;

import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.AutoplayAction;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackAutoplayTests extends PlaybackWebTest {

	public PlaybackAutoplayTests() throws OoyalaException {
		super();
	}

	@Test(groups = "alice", dataProvider = "testUrls")
	public void testAutoPlay(String testName, String url)
			throws OoyalaException {

		boolean result = false;
		PlayValidator play = pageFactory.getPlayValidator();
		SeekValidator seek = pageFactory.getSeekValidator();
		AutoplayAction autoplayAction = pageFactory.getAutoplay();
		EventValidator eventValidator = pageFactory.getEventValidator();

		if (getPlatform().equalsIgnoreCase("Android")) {
			throw new SkipException("Test PlaybackAutoplayTests Is Skipped");
		} else {
			try {
				driver.get(url);
				driver.manage().window().maximize();

				play.waitForPage();

				injectScript("http://10.11.66.55:8080/alice.js");

				autoplayAction.startAction();
				try {
					eventValidator.validate("singleAdPlayed_1", 50);
				} catch (Exception e) {
					logger.info("No Preroll ad present in this autoplay video");
				}
				play.validate("playing_1", 60);

				logger.info("Verifed that video is getting playing");

				sleep(500);

				seek.validate("seeked_1", 60);

				logger.info("Verified that video is seeked");

				eventValidator.validate("played_1", 60);

				logger.info("Verified that video is played");

				result = true;
			} catch (Exception e) {
				e.printStackTrace();

			}
			Assert.assertTrue(result, "Alice basic playback tests failed");
		}
	}
}
