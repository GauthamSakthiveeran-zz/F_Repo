package com.ooyala.playback.adhoc;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 14/8/17.
 */
public class BitrateOptionsBeforeAndAfterMidrollTests extends PlaybackWebTest{
     BitrateOptionsBeforeAndAfterMidrollTests() throws OoyalaException{
         super();
     }

    private static Logger logger = Logger.getLogger(BitrateOptionsBeforeAndAfterMidrollTests.class);
    private PlayValidator playValidator;
    private EventValidator eventValidator;
    private Bitratevalidator bitrate;

    @Test(dataProvider = "testUrls",groups = "adhoc")
    public void BitrateOptions(String testName, UrlObject url){
        boolean result = true;
        try {
            driver.get(url.getUrl());
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playValidator.validate("playing_1",10000);
            result = result && eventValidator.playVideoForSometime(2);
            driver.executeScript("pp.pause()");
            result = result && bitrate.totalBitrateCountBeforeAdPlayback();
            driver.executeScript("pp.play()");
            result = result && eventValidator.validate("MidRoll_willPlaySingleAd_1",30000);
            driver.executeScript("pp.skipAd()");
            result = result && eventValidator.validate("midrollAdPlayed_1",100000);
            driver.executeScript("pp.pause()");
            result = result && bitrate.totalBitrateCountAfterAdPlayback();
            driver.executeScript("pp.play()");
            result = result && bitrate.isBirateOptionsVarying();

        } catch (Exception e) {
            logger.error(e);
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Bitrate Options :"+testName);
    }
}
