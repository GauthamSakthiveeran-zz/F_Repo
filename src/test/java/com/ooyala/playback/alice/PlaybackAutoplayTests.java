package com.ooyala.playback.alice;

import static java.lang.Thread.sleep;

import org.apache.log4j.Logger;
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

	private static Logger logger = Logger.getLogger(PlaybackAutoplayTests.class);
	private EventValidator eventValidator;
	private PlayValidator play;
	private SeekValidator seek;
	private AutoplayAction autoplayAction;

	public PlaybackAutoplayTests() throws OoyalaException {
		super();
	}

	@Test(groups = "alice", dataProvider = "testUrls")
	public void testAutoPlay(String testName, String url)
			throws OoyalaException {
		boolean result = true;

		if (getPlatform().equalsIgnoreCase("Android")) {
			throw new SkipException("Test PlaybackAutoplayTests Is Skipped");
		} else {
			try {
				driver.get(url);
                result = result &&	play.waitForPage();

				injectScript();

				autoplayAction.startAction();
				try {
					eventValidator.validate("singleAdPlayed_1", 50);
				} catch (Exception e) {
					logger.info("No Preroll ad present in this autoplay video");
				}
                result = result && play.validate("playing_1", 60000);

				sleep(500);

                result = result && seek.validate("seeked_1", 60000);

                result = result && eventValidator.validate("played_1", 60000);

				logger.info("Verified that video is played");

			} catch (Exception e) {
				e.printStackTrace();
                result = false;

			}
			Assert.assertTrue(result, "Playback Autoplay tests failed");
		}
	}
}
