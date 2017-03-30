package com.ooyala.playback.contentProtection;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.ErrorDescriptionValidator;
import com.ooyala.playback.page.FlightTimeValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by suraj on 3/23/17.
 */
public class PlaybackFlightTimeTests extends PlaybackWebTest{

    private static Logger logger = Logger
            .getLogger(PlaybackGeoRestrictionTests.class);

    private FlightTimeValidator flight;
    private ErrorDescriptionValidator error;

    PlaybackFlightTimeTests() throws OoyalaException{
        super();
    }

    @Test(groups = "syndicationRules", dataProvider = "testUrls")
    public void testFlightTime(String testName, UrlObject url){
        boolean result = true;
        try{
            driver.get(url.getUrl());

            result = result && flight.isPageLoaded();

            injectScript();

            result = result && error.expectedErrorCode("past").expectedErrorDesc("This video is no longer available").validate("",60000);

        }catch(Exception e){
            logger.error("Error while checking flight time syndication" + e);
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }

        Assert.assertTrue(result,"flight time syndication test failed");
    }
}
