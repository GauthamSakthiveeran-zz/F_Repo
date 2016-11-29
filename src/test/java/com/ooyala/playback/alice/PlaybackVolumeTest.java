package com.ooyala.playback.alice;

import static java.lang.Thread.sleep;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.VolumeValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackVolumeTest extends PlaybackWebTest {

	private PlayValidator play;
	private SeekValidator seek;
	private PlayAction playAction;
	private EventValidator eventValidator;
	private VolumeValidator volumeValidator;

	public PlaybackVolumeTest() throws OoyalaException {
		super();
	}

	@Test(groups = "PlayerSkin", dataProvider = "testUrls")
	public void testVolume(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

            result = result && play.waitForPage();

			injectScript();

            result = result && playAction.startAction();

			Boolean isAdplaying = (Boolean) (((JavascriptExecutor) driver)
					.executeScript("return pp.isAdPlaying()"));
			if (isAdplaying) {
				volumeValidator.validate("VOLUME_MAX", 60);
				logger.info("validated ad volume at full range");
				eventValidator.validate("adPodEnded_1", 200);
				logger.info("Ad played");
			}

            Thread.sleep(2000);

            result = result && volumeValidator.validate("VOLUME_MAX", 60);

			logger.info("validated video volume at full range");

            result = result && eventValidator.validate("playing_10 ", 60);

            logger.info("video is playing");
            sleep(4000);

            result = result && seek.validate("seeked_1", 60);

			logger.info("video seeked");

            result = result && eventValidator.validate("played_1", 60);

			logger.info("video played");

		} catch (Exception e) {
			e.printStackTrace();
            result = false;
		}
		Assert.assertTrue(result, "Playback Volume tests failed");
	}

}
