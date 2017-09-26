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

public class DiscoveryAutoplayPostroll extends PlaybackWebTest {

	public DiscoveryAutoplayPostroll() throws OoyalaException {
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
		boolean result = true;

		try {

			driver.get(url.getUrl());
			String autoPlay = getAutoPlayFlag();
            String upNextFlag = getAutoplayUpNextDiscoveryVideosFlag();


			if(autoPlay.equalsIgnoreCase("true"))
				result = result && event.isPageLoaded();
			else
				result = result && playValidator.waitForPage();

			injectScript();
			
			result = result && event.loadingSpinner();
			if(autoPlay.equalsIgnoreCase("true"))
				result = result && autoplayAction.startAction();
			else
				result = result && playValidator.clickOnIndependentElement("PLAY_BUTTON");	
			result = result && seekAction.fromLast().setTime(30).startAction();
			result = result && volumeValidator.validateInitialVolume(0.5);

			result = result && seekAction.seek(10, true);
			result = result && event.waitOnElement(By.id("played_1"), 30000);
			result = result && event.validate("singleAdPlayed_1", 150000);
			if(upNextFlag.equalsIgnoreCase("true"))
				result = result && upNextValidator.autoPlayUpNextVideo();
			else
				result = result && !upNextValidator.autoPlayUpNextVideo();

		} catch (Exception e) {
			logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL,e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "DiscoveryAutoplayPostroll Test failed");
	}
}
