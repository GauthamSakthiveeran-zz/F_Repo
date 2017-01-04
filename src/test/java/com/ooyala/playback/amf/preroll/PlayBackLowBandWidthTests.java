package com.ooyala.playback.amf.preroll;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.facile.proxy.browsermob.BrowserMobProxyHelper;
import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

import net.lightbody.bmp.proxy.ProxyServer;

@SuppressWarnings("deprecation")
public class PlayBackLowBandWidthTests extends PlaybackWebTest {

	public PlayBackLowBandWidthTests() throws OoyalaException {
		super();
	}

	private PlayAction playAction;
	private PlayValidator playValidator;
	private EventValidator event;
	private SeekValidator seekValidator;

	@Test(groups = { "amf", "lowbandwidth", "preroll" }, dataProvider = "testUrls")
	public void verifyLowBandwidth(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {

			if (isBrowserMobProxyEnabled() && System.getProperty("mode").equalsIgnoreCase("local")) {

				ProxyServer proxy = BrowserMobProxyHelper.getBrowserMobProxyServer();
				proxy.setDownstreamKbps(750);

			} else {
				extentTest.log(LogStatus.SKIP, "Browsermob proxy should be enabled and mode should be local.");
				return;
			}

			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			if ((event.isVideoPluginPresent("main") && event.isAdPluginPresent("freewheel"))
					|| event.isVideoPluginPresent("osmf") && event.isAdPluginPresent("ima")) {
				result = result && event.validate("PreRoll_willPlayAds", 1000);
				result = result && event.validate("adsPlayed_1", 160000);
			} else {
				result = result && event.validate("PreRoll_willPlaySingleAd_1", 1000);
				result = result && event.validate("singleAdPlayed_1", 160000);
			}

			result = result && event.validate("playing_1", 10000);

			result = result && seekValidator.validate("seeked_1", 190000);

			result = result && event.validate("played_1", 190000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");

	}

}
