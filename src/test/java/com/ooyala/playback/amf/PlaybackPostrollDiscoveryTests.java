package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.UpNextValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPostrollDiscoveryTests extends PlaybackWebTest{

	public PlaybackPostrollDiscoveryTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private PauseValidator pauseValidator;
	private DiscoveryValidator discoveryValidator;
	private SeekAction seekAction;
	private UpNextValidator upNextValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPostrollDiscovery(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

            result = result && playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

            result = result && playValidator.validate("playing_1", 150000);

            result = result && pauseValidator.validate("paused_1", 60000);

            result = result && discoveryValidator.validate("reportDiscoveryClick_1",60000);

            result = result && playAction.startActionOnScreen();
	        
	        seekAction.setTime(10).fromLast().startAction();

            result = result && upNextValidator.validate("", 60000);

            result = result && event.validate("willPlaySingleAd_1", 90000);
	        
            if(event.isAdPlugin("pulse"))
            	result = result && event.validate("singleAdPlayed_2", 60000);
            else
            	result = result && event.validate("singleAdPlayed_1", 60000);
	        
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");

	}

}
