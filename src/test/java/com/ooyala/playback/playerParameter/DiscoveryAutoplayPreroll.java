package com.ooyala.playback.playerParameter;


import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.UpNextValidator;
import com.ooyala.playback.page.VolumeValidator;
import com.ooyala.playback.page.action.AutoplayAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class DiscoveryAutoplayPreroll extends PlaybackWebTest {

	public DiscoveryAutoplayPreroll() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private DiscoveryValidator discoveryValidator;
	private UpNextValidator upNextValidator;
	private SeekAction seekAction;
	private AutoplayAction autoplayAction;
	private VolumeValidator volumeValidator;

	@Test(groups = { "autoplay" }, dataProvider = "testUrls")
	public void verifyPrerollDiscovery(String testName, UrlObject url) throws OoyalaException {
        String[] parts = testName.split(":")[1].trim().split(",");
        String[] tcName = null;
        String[] autoplayValidator = null;
        String[] upNxtValidator = null;
        for(int i=0;i<parts.length-1;i++) {
    	      tcName=parts[i].split("-");
    	      if(tcName[i].contains("Auto") || tcName[i].contains("auto")) {
    	    	    autoplayValidator=tcName;
    	      } else {
    	      	upNxtValidator=tcName;
    	      }
       }
		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();
			
			result = result && event.loadingSpinner();
			if(autoplayValidator[1].equalsIgnoreCase("true"))
		    result = result && autoplayAction.startAction();
			else
		    result = result && playValidator.clickOnIndependentElement("PLAY_BUTTON");

			result = result && event.validate("singleAdPlayed_1", 150000);

			result = result && volumeValidator.validateInitialVolume(0.5);

			result = result && seekAction.seek(10, true);
			result = result && event.waitOnElement(By.id("played_1"), 30000);
			
			if(upNxtValidator[1].equalsIgnoreCase("true"))
				result = result && upNextValidator.autoPlayUpNextVideo();
			else
				result = result && !upNextValidator.autoPlayUpNextVideo();

		} catch (Exception e) {
			logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL,e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "DiscoveryAutoplayPreroll Test failed");
	}
}
