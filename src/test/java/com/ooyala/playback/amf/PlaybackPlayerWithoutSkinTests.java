package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Thread.sleep;
import static org.testng.Assert.assertEquals;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPlayerWithoutSkinTests extends PlaybackWebTest{

	public PlaybackPlayerWithoutSkinTests() throws OoyalaException {
		super();
	}
	private EventValidator event;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private PoddedAdValidator poddedAdValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPlayerWithoutskin(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {
			url = removeSkin(url);
			driver.get(url);
			
			injectScript();
			
			

			
		}catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified");
	}

}
