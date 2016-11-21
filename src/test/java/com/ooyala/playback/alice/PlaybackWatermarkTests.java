package com.ooyala.playback.alice;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.VolumeValidator;
import com.ooyala.playback.page.WaterMarkValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackWatermarkTests extends PlaybackWebTest {

    private PlayValidator play;
    private SeekValidator seek;
    private PlayAction playAction;
    private EventValidator eventValidator;
    private VolumeValidator volumeValidator;
    private PauseValidator pause ;
    private WaterMarkValidator waterMarkValidator;

    public PlaybackWatermarkTests() throws OoyalaException {
        super();
    }


	@Test(groups = "alice", dataProvider = "testUrls")
	public void testWatermarks(String testName, String url)
			throws OoyalaException {

        boolean result = false;
        /*PlayValidator play = pageFactory.getPlayValidator();
        SeekValidator seek = pageFactory.getSeekValidator();
        PlayAction playAction = pageFactory.getPlayAction();
        EventValidator eventValidator = pageFactory.getEventValidator();
        VolumeValidator volumeValidator = pageFactory.getVolumeValidator();
        PauseValidator pause = pageFactory.getPauseValidator();
        WaterMarkValidator waterMarkValidator = pageFactory.getWaterMarkValidator();*/

		try {
			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			play.waitForPage();

			injectScript("http://10.11.66.55:8080/alice.js");

			playAction.startAction();

			Boolean isAdplaying = (Boolean) (((JavascriptExecutor) driver)
					.executeScript("return pp.isAdPlaying()"));
			if (isAdplaying) {
				volumeValidator.validate("VOLUME_MAX", 60);
				eventValidator.validate("adPodEnded_1", 200);
			}

			play.validate("playing_1", 60);
			logger.info("video is playing");
			Thread.sleep(3000);

			pause.validate("paused_1", 60);
			logger.info("video paused");

			waterMarkValidator.validate("WATERMARK_LOGO", 60);
			logger.info("checked watermark logo");

			playAction.startAction();

			seek.validate("seeked_1", 60);

			logger.info("video seeked");

			eventValidator.validate("videoPlayed_1", 60);

			logger.info("video played");

			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertTrue(result, "Alice basic playback tests failed");
	}
}
