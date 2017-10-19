package com.ooyala.playback.ooyalaads;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PlaylistValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by suraj on 7/4/17.
 */
public class MovingFromNonOoyalaAdsToOyalaAdsViaPlaylistTests extends PlaybackWebTest {
    public MovingFromNonOoyalaAdsToOyalaAdsViaPlaylistTests() throws OoyalaException {
        super();
    }

    private static Logger logger = Logger.getLogger(MovingFromNonOoyalaAdsToOyalaAdsViaPlaylistTests.class);
    private PlayValidator playValidator;
    private PlayAction playAction;
    private EventValidator eventValidator;
    private PlaylistValidator playlist;

    @Test(groups = {"amf", "ooyalaads", "playlist"}, dataProvider = "testUrls")
    public void verifyOoyalaAdsViaPlaylist(String testName, UrlObject url) throws Exception {
        boolean result = true;

        try {

            driver.get(url.getUrl());

            result = result && playValidator.waitForPage();

            injectScript();

            result = result && playAction.startAction();

            //Ooyala Ad should not play
            result = result && !eventValidator.isAdPlaying();

            result = result && eventValidator.validate("playing_1",10000);

            result = result && playlist.scrollToEitherSide();

            //Give the name of the asset which you want to select and play
            result = result && playlist.selectAndClickonAssetFromPlaylist("Pulsar");

            //Ooyala Ad should play
            result = result && eventValidator.validate("singleAdPlayed_1",90000);
            result = result && eventValidator.validate("ooyalaAds_1",60000);

            result = result && eventValidator.validate("playing_2",10000);

            //Give the name of the asset which you want to select and play
            result = result && playlist.selectAndClickonAssetFromPlaylist("Baby");

            //Ooyala Ad should not play
            result = result && !eventValidator.isAdPlaying();

            result = result && eventValidator.validate("playing_3",10000);
        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL,e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Test failed");
    }
}
