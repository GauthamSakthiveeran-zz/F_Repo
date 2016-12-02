package com.ooyala.playback.alice;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPlayerDestroyTests extends PlaybackWebTest{

	private static Logger logger = Logger.getLogger(PlaybackPlayerDestroyTests.class);
	
	private PlayValidator play;
	private EventValidator eventValidator;
	
	public PlaybackPlayerDestroyTests() throws OoyalaException {
		super();
	}

	
	@Test(groups = "alice", dataProvider = "testUrls")
	public void testVideoReplay(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url);

            result = result && play.waitForPage();
            
            injectScript();

            result = result && play.validate("playing_1", 60000);
            
            executeScript("pp.destroy()");

            result = result && play.validate("destroy_1", 50);
            
            result = result && eventValidator.validateElementPresence("STATE_SCREEN_SELECTABLE");
           
		} catch (Exception e) {
			e.printStackTrace();
            result = false;
		}
		Assert.assertTrue(result, "Player Destroy tests failed");
	}
}
