package com.ooyala.playback.alice;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.annotations.Test;

import static java.lang.Thread.sleep;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackAspectRatioTests extends PlaybackWebTest {
	private EventValidator eventValidator;
	private PlayValidator play;
	private PauseValidator pause;
	private SeekValidator seek;
	private PlayAction playAction;
	private AspectRatioValidator aspectRatioValidator;

	public PlaybackAspectRatioTests() throws OoyalaException {
		super();
	}

	@Test(groups = "AspectRatio", dataProvider = "testUrls")
	public void testAspectRation(String testName, String url)
			throws OoyalaException {
		boolean result = true;

		try {
			driver.get(url);

            result = result && play.waitForPage();

			injectScript();

            result = result && play.validate("playing_1", 60000);

			sleep(2000);

            result = result && aspectRatioValidator.validate("assetDimension_1", 60000);

            if(!(getBrowser().equalsIgnoreCase("safari"))){
				result = result && pause.validate("paused_1", 60000);
				result = result && playAction.startAction();
			}

			// add fullscreen functionality

            result = result &&	seek.validate("seeked_1", 60000);

            result = result &&	aspectRatioValidator.validate("assetDimension_1", 60000);

            result = result && eventValidator.validate("videoPlayed_1", 60000);

			logger.info("Verified that video is played");

		} catch (Exception e) {
			e.printStackTrace();
            result = false;

		}
		Assert.assertTrue(result, "Aspect ratio tests failed");

	}

}
