package com.ooyala.playback.playerfeatures;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.qe.common.exception.OoyalaException;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by Snehal on 13/01/2017.
 */
public class PlaybackThumbnailCarouselTests extends PlaybackWebTest {
    private static Logger logger = Logger
            .getLogger(PlaybackThumbnailCarouselTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;
    private ThumbnailCarouselValidator thumbnailCarouselValidator;

    public PlaybackThumbnailCarouselTests() throws OoyalaException {
        super();
    }

    @Test(groups = "playerFeatures", dataProvider = "testUrls")
    public void testThumbnailCarousel(String testName, String url)
            throws OoyalaException {

        boolean result = true;

        try {

            driver.get(url);

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1", 60000);

            result = result && pause.validate("paused_1", 60000);

            result = result && thumbnailCarouselValidator.validate("", 60000);

            Thread.sleep(3000);

            result = result && play.validate("playing_2", 60000);

            result = result && seek.validate("seeked_1", 60000);

            result = result && eventValidator.validate("played_1", 60000);

        } catch (InterruptedException e) {
            logger.error("Thread interruption for wait method");
            result = false;
        } catch (Exception ex){
            logger.error("**** Failed ***** : Thumbnail Carousel is failing");
            result = false;
        }

        Assert.assertTrue(result, "Thumbnail Carousel test failed");
    }
}


