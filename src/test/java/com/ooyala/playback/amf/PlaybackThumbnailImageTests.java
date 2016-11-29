package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.ThumbnailValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.page.action.StateScreenAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackThumbnailImageTests extends PlaybackWebTest{

	public PlaybackThumbnailImageTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private ThumbnailValidator thumbNail;
	private StateScreenAction stateScreenAction;
	
	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPrerollOverlay(String testName, String url)
			throws OoyalaException {

		boolean result = true;
		
		if(getBrowser().equals("safari")){
			extentTest.log(LogStatus.SKIP, "Test Thumbnail Image Is Skipped as hovering is not supportted in Safari");
			return;
		}

		try {

			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

            result = result && playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

            result = result && playValidator.validate("playing_1", 50);
            Thread.sleep(2000);

            //Seek the Video
            seekAction.setTime(35).startAction();
            result = result &&   event.validate("seeked_1", 50);

            //Hovering on scrubber bar
            Thread.sleep(5000);
            stateScreenAction.startAction();

            result = result && thumbNail.validate("", 120);

            thumbNail.validateThumbNailImage("9qaHdodTqmllcEnthP1AgrCTjf19HD4i"); // TODO, hardcoding for now

            seekAction.seekTillEnd().startAction();
            result = result && event.validate("videoPlayed_1", 200);
            extentTest.log(LogStatus.PASS,"video played completely");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}

}
