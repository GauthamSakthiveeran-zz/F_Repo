package com.ooyala.playback.playerfeatures;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import static java.lang.Thread.sleep;

/**
 * Created by jitendra on 29/12/16.
 */
public class OoyalaAPITests extends PlaybackWebTest {
    private static Logger logger = Logger
            .getLogger(OoyalaAPITests.class);

    private EventValidator eventValidator;
    private OoyalaAPIValidator ooyalaAPIValidator;


    public OoyalaAPITests() throws OoyalaException {
        super();
    }

    @Test(groups = "api", dataProvider = "testUrls")
    public void testOoyalaAPI(String testName, String url)
            throws OoyalaException {
        boolean result = true;

        try {
            driver.get(url);
            result = result && eventValidator.loadingSpinner();
            injectScript();
            result = result && ooyalaAPIValidator.validate("", 30000);
            result = result && eventValidator.validate("played_1",60000);

        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        Assert.assertTrue(result, "Ooyala API test failed");

    }
}
