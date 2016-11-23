package com.ooyala.playback.alice;

import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.CCValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.FullScreenValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.ShareTabValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackHLSVodTests extends PlaybackWebTest {

    private PlayValidator play;
    private PauseValidator pause;
    private SeekAction seek;
    private EventValidator eventValidator;
    private FullScreenValidator fullScreenValidator;
    private CCValidator ccValidator ;
    private ShareTabValidator shareTabValidator;


    public PlaybackHLSVodTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testHLSVod(String testName, String url) throws OoyalaException {


        boolean result = false;


		if (getBrowser().equalsIgnoreCase("safari")) {
			try {
				driver.get(url);
				if (!getPlatform().equalsIgnoreCase("android")) {
					driver.manage().window().maximize();
				}

				play.waitForPage();

                injectScript();
				play.validate("playing_1", 60);

				logger.info("video is playing");

				pause.validate("paused_1", 60);

				logger.info("video paused");

				play.validate("playing_2", 60);

				logger.info("video is playing again");

				sleep(3000);

				fullScreenValidator.validate("", 60);

				ccValidator.validate("cclanguage", 60);

				logger.info("verified cc languages");

				shareTabValidator.validate("", 60);

				seek.seek("pp.getDuration()/2");

				eventValidator.validate("played_1", 60);

				logger.info("video played");

				result = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			Assert.assertTrue(result, "Playback HLS Vod tests failed");

		} else {
			throw new SkipException("Test PlaybackHLSVod Is Skipped");
		}
	}
}
