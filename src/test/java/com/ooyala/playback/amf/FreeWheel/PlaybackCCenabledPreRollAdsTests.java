package com.ooyala.playback.amf.FreeWheel;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.CCValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackCCenabledPreRollAdsTests extends PlaybackWebTest{

	public PlaybackCCenabledPreRollAdsTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private PauseAction pauseAction;
	private CCValidator ccValidator;
	
	@DataProvider(name = "testUrls")
	public Object[][] getTestData() {

		return UrlGenerator.parseXmlDataProvider(getClass().getSimpleName(),
				nodeList);
	}
	
	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyCCenabledPreroll(String testName, String url) throws Exception {
		boolean result = false;
		event = pageFactory.getEventValidator();
		playAction = pageFactory.getPlayAction();
		playValidator = pageFactory.getPlayValidator();
		seekAction = pageFactory.getSeekAction();
		pauseAction = pageFactory.getPauseAction();
		ccValidator = pageFactory.getCCValidator();
		
		try {
			
			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			playValidator.waitForPage();
			Thread.sleep(10000);
			
			//TODO
			injectScript("http://192.168.0.43:8080/common.js");
			injectScript("http://192.168.0.43:8080/amf/amf.js");
			
			playAction.startAction();
			
			//TODO
			
			/*if (Description.equalsIgnoreCase("Preroll_Bitmovin_Pulse_CC")){
	            waitForElement(webDriver, "singleAdPlayed_2", 190);
	            loadingSpinner(webDriver);
	        }
	        else {
	            waitForElement(webDriver, "singleAdPlayed_1", 190);
	            loadingSpinner(webDriver);
	        }*/
			
			event.validate("singleAdPlayed_1", 190);
			
			extentTest.log(PASS, "Preroll Ad Completed");
			
			event.validate("playing_1", 120);
			
			extentTest.log(PASS, "Main video started to play");

	        sleep(2000);
	        
	        pauseAction.startAction();
	        
	        ccValidator.validate("cclanguage",60);

	        sleep(2000);
	        seekAction.seek(10, true);
	        
	        /*if(Description.equalsIgnoreCase("BitmovinCCenabledpreroll_IMA")){
	            ((JavascriptExecutor) webDriver).executeScript("pp.seek(pp.getDuration()-28);");
	        }else {
	            ((JavascriptExecutor) webDriver).executeScript("pp.seek(pp.getDuration()-10);");
	        }*/
	        
	        Thread.sleep(5000);
	        
	        event.validate("seeked_1", 190);
	        event.validate("played_1", 190);

	        boolean isccCueshowing = event.validateElementPresence("ccshowing_1");
	        Assert.assertEquals(isccCueshowing, true, "ClosedCaption Cue is not changing");

	        extentTest.log(PASS, "Video completed palying");

	        extentTest.log(PASS, "Verified PreRoll Ads test");
			
		}catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback CC Enabled PreRoll Ad tests failed");
		
	}

}
