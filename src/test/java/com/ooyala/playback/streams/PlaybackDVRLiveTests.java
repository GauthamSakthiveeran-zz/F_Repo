package com.ooyala.playback.streams;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.LiveAction;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PlaybackDVRLiveTests extends PlaybackWebTest {

	private PlayValidator play;
	private PauseValidator pause;
	private DVRLiveValidator dvrLiveValidator;
	private VolumeValidator volumeValidator;
	private Bitratevalidator bitratevalidator;

    public PlaybackDVRLiveTests() throws OoyalaException {
		super();
	}

	@Test(groups = "live,dvr", dataProvider = "testUrls")
	public void testDVRLive(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;
		boolean seek = testName.contains("DVR Seek");
		boolean playerControl = testName.contains("DVR Player Control");
		boolean volume = testName.contains("Volume");
		boolean bitrate = testName.contains("Bitrate");
		boolean playerTime = testName.contains("DVR Player Time");

		try {
			driver.get(url.getUrl());
			result = result && play.waitForPage();
			injectScript();
			result = result && play.validate("playing_1", 60000);

            if (result && playerControl){
                s_assert.assertTrue(dvrLiveValidator.validatePlayerControl(),"DVR Player Control");
            }

			if (result && seek){
                s_assert.assertTrue(dvrLiveValidator.validateSeek(),"DVR Seek");
            }

            if (result && volume){
                s_assert.assertTrue(volumeValidator.validate("",20000),"Volume");
            }

            if (result && playerTime){
                s_assert.assertTrue(dvrLiveValidator.validatePlayerTime(),"DVR Player Time");
            }

            if (result && bitrate){
                s_assert.assertTrue(bitratevalidator.validate("",20000),"Bitrate");
            }

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}

		s_assert.assertTrue(result, "Playback Live tests failed");
		s_assert.assertAll();
	}
}
