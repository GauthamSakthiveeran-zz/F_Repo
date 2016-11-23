package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdClickThroughValidator;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackDiscoveryUpnextTests extends PlaybackWebTest {

	public PlaybackDiscoveryUpnextTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private AdClickThroughValidator adClickThroughValidator;
    private DiscoveryValidator discoveryValidator;

	static int index =0;
	
	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyDiscoveryUpNext(String testName, String url) throws Exception {
		
		boolean result = false;
		
		try {
			
			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			playValidator.waitForPage();
			Thread.sleep(10000);
			
			injectScript();
			
			playAction.startAction();
			
	        if(adClickThroughValidator.isAdPlaying())
	            event.validate("singleAdPlayed_1", 90);
	        
	        event.validate( "playing_1", 90);
	        extentTest.log(PASS, "Video starting");
	        sleep(2000);
	        
	        seekAction.seek(10, true);

	        event.validate( "seeked_1", 180);

			discoveryValidator.validate("reportDiscoveryClick_1", 60);
	        extentTest.log(PASS, "Clicked video loaded");
	        extentTest.log(PASS, "Verified DiscoveryUpNext tests");
			
			result = true;
			
		}catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback CC Enabled MidRoll Ads tests failed");
		
	}
	
}
