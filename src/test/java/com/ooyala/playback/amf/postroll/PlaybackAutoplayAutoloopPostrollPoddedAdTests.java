package com.ooyala.playback.amf.postroll;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 15/03/17.
 */
public class PlaybackAutoplayAutoloopPostrollPoddedAdTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackAutoplayAutoloopPostrollPoddedAdTests.class);
	private EventValidator eventValidator;
	private SeekValidator seekValidator;
    private SeekAction seekAction;
    private PoddedAdValidator podded;

	public PlaybackAutoplayAutoloopPostrollPoddedAdTests() throws OoyalaException {
		super();
	}

	@Test(groups = { "amf", "autoplay" }, dataProvider = "testUrls")
	public void testAutoplayAutoloop(String testName, UrlObject url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());
			
			result = result && eventValidator.isPageLoaded();
			
			injectScript();
			
			result = result && eventValidator.validateAutoPlay();
			
			result = result && eventValidator.playVideoForSometime(6);

			result = result && seekValidator.validate("seeked_1", 60000);
			
			
			if(eventValidator.isAdPluginPresent("freewheel")){
				result = result && podded.setPosition("PostRoll").validate("countPoddedAds_2", 60000);
			} else{
				result = result && podded.setPosition("PostRoll").validate("countPoddedAds_1", 60000);
			}
			
			result = result && eventValidator.validate("replay_1", 60000);

			result = result && seekAction.seekTillEnd().startAction();
			
			if(eventValidator.isAdPluginPresent("freewheel")){
				result = result && eventValidator.validate("countPoddedAds_4", 60000);
//				result = result && podded.setPosition("PostRoll").validate("countPoddedAds_4", 60000); TODO - willplayads is not working
			} else{
				result = result && podded.setPosition("PostRoll").validate("countPoddedAds_2", 60000);
			}

		} catch (Exception e) {
			logger.error(e);
			result = false;
			extentTest.log(LogStatus.FAIL, "Playback Autoplay Autoloop test failed for "+testName+"", e);
		}
		Assert.assertTrue(result, "Playback Autoplay Autoloop test failed for "+testName+"");
	}
}
