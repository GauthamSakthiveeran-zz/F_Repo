package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class BasicPlaybackTests extends PlaybackWebTest{

	public BasicPlaybackTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	
	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyBasicPlayback(String testName, String url)
			throws OoyalaException {
		
		boolean result = false;
		
		try {
			
			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			playValidator.waitForPage();
			Thread.sleep(2000);
			
			injectScript();
			
			playAction.startAction();
			
			Thread.sleep(3000);
	
			playValidator.validate("playing_1", 190);
			
			extentTest.log(PASS, "Main video started to play");
			
			sleep(2000);
			
			seekValidator.validate("seeked_1", 190);
			
			extentTest.log(PASS, "Seek successfull");
			
			sleep(3000);
			
			event.validate("played_1", 190);
			
			extentTest.log(LogStatus.PASS, "Main Video played successfully");
			
			result = true;
			
		}catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified");
		
	}
}
