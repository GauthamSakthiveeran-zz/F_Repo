package com.ooyala.playback.platformParameter;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.BitmovinTechnologyValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.SetEmbedCodeValidator;
import com.ooyala.playback.page.StreamValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlatformParameterPostAdTest extends PlaybackWebTest {

    public PlatformParameterPostAdTest() throws OoyalaException {
        super();
    }

    private EventValidator event;
    private PlayValidator playValidator;
    private SeekValidator seekValidator;
    private PauseValidator pause;
    private PlayValidator play;
    private StreamValidator streamTypeValidator;
    private BitmovinTechnologyValidator bitmovinvalidator;
    @Test(groups = {"postroll"}, dataProvider = "testUrls")
    public void verifyPostroll(String testName, UrlObject url) {

        boolean result = true;

        try {

            driver.get(url.getUrl());

            result = result && playValidator.waitForPage();

            injectScript();
            
            bitmovinvalidator.getConsoleLogs();
            
            result = result && playValidator.validate("playing_1", 60000);
            
            result = result && event.playVideoForSometime(3);
            
            result = result && pause.validate("paused", 60000);
            
    		if (url.getStreamType() != null && !url.getStreamType().isEmpty()) {
                result = result && event.validate("videoPlayingurl", 40000);
                result = result && streamTypeValidator.setStreamType(url.getStreamType()).validate("videoPlayingurl", 1000);
            }
            
            result = result && play.validate("playing_2", 60000);
            
            result = result && seekValidator.validate("seeked_1", 10000);

            result = result && event.validate("played_1", 200000);
            
			result = result && bitmovinvalidator.setStream(url.getStreamType()).validate("bitmovin_technology", 6000);

            result = result && event.validate("PostRoll_willPlaySingleAd_1", 90000);

            result = result && event.validate("singleAdPlayed_1", 190000);

         
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Tests failed");
    }
}
