package com.ooyala.playback.amf.preroll;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdClickThroughValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackClickthroughTests extends PlaybackWebTest {

	public PlaybackClickthroughTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private AdClickThroughValidator clickThrough;

	@Test(groups = {"amf","clickThrough","sequential","preroll"}, dataProvider = "testUrls")
	public void verifyClickthrough(String testName, String url)
			throws Exception {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlaySingleAd_1", 10000);

			result = result && clickThrough.validate("", 120000);

			result = result && event.validate("singleAdPlayed_1", 190000);

			result = result && event.validate("playing_1", 160000);

			result = result && seekAction.seekTillEnd().startAction();

			result = result && event.validate("seeked_1", 10000);
			result = result && event.validate("played_1", 200000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		Assert.assertTrue(result, "Clickthrough functionality tests failed");

	}

}
