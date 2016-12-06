package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
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
	private DiscoveryValidator discoveryValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyDiscoveryUpNext(String testName, String url)
			throws Exception {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

            result = result && playAction.startAction();


			result = result && event.validate("playing_1", 90000);

			result = result && seekAction.setTime(10).fromLast().startAction();

			result = result && event.validate("seeked_1", 180000);

			result = result
					&& discoveryValidator.validate("reportDiscoveryClick_1",
							60000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "DiscoveryUpNext tests failed");

	}

}
