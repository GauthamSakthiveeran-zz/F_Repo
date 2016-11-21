package com.ooyala.playback.alice;

import static java.lang.Thread.sleep;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.CCValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.FullScreenValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackClosedCaptionTests extends PlaybackWebTest {
    public static Logger logger = Logger.getLogger(PlaybackClosedCaptionTests.class);

    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;
    private EventValidator eventValidator;
    private FullScreenValidator fullScreenValidator;
    private CCValidator ccValidator;

	public PlaybackClosedCaptionTests() throws OoyalaException {
		super();
	}

	@Test(groups = "alice", dataProvider = "testUrls")
	public void testClosedCaption(String testName, String url)
			throws OoyalaException {

        boolean result = false;
       /* PlayValidator play = pageFactory.getPlayValidator();
        PauseValidator pause = pageFactory.getPauseValidator();
        SeekValidator seek = pageFactory.getSeekValidator();
        EventValidator eventValidator = pageFactory.getEventValidator();
        FullScreenValidator fullScreenValidator = pageFactory.getFullScreenValidator();
        CCValidator ccValidator = pageFactory.getCCValidator();*/

		logger.info("Executing PlaybackClosedCaption test  ");
		try {
			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			play.waitForPage();

			injectScript("http://10.11.66.55:8080/alice.js");

			play.validate("playing_1", 60);

			logger.info("Verifed that video is getting playing");

			sleep(1000);

			pause.validate("paused_1", 60);

			logger.info("Verified that video is getting pause");

			play.validate("playing_2", 60);

			logger.info("Verifed that video is getting playing again after pause play");

			fullScreenValidator.validate("", 60);

			sleep(1000);

			ccValidator.validate("cclanguage", 60);

			logger.info("Verified cc languages");

			sleep(1000);

			seek.validate("seeked_1", 60);

			logger.info("Verirfied seeked functionality");

			eventValidator.validate("played_1", 60);

			logger.info("verified Video played");

			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertTrue(result, "Closed Caption tests failed");
	}
}
