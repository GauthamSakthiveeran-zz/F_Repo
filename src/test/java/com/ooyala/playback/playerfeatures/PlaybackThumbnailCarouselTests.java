package com.ooyala.playback.playerfeatures;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.ThumbnailCarouselValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by Snehal on 13/01/2017.
 */
public class PlaybackThumbnailCarouselTests extends PlaybackWebTest {
    private EventValidator eventValidator;
    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;
    private ThumbnailCarouselValidator thumbnailCarouselValidator;

    public PlaybackThumbnailCarouselTests() throws OoyalaException {
        super();
    }

    @Test(groups = "playerFeatures", dataProvider = "testUrls")
    public void testThumbnailCarousel(String testName, UrlObject url)
            throws OoyalaException {

        boolean result = true;

        try {

            driver.get(url.getUrl());

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1", 60000);

            result = result && pause.validate("paused_1", 60000);

            result = result && thumbnailCarouselValidator.validate("", 60000);

            result = result && play.validate("playing_2", 60000);

            result = result && seek.validate("seeked_1", 60000);

            result = result && eventValidator.validate("played_1", 60000);

        } catch (InterruptedException e) {
            extentTest.log(LogStatus.FAIL,"Thread interruption for wait method");
            result = false;
        } catch (Exception ex){
        	extentTest.log(LogStatus.FAIL,"**** Failed ***** : Thumbnail Carousel is failing");
            result = false;
        }

        Assert.assertTrue(result, "Thumbnail Carousel test failed");
    }
}


