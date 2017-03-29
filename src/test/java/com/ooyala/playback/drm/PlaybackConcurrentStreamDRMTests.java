package com.ooyala.playback.drm;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackConcurrentStreamDRMTests extends PlaybackWebTest {


	public PlaybackConcurrentStreamDRMTests() throws OoyalaException {
		super();
	}

	@Test(groups = "drm", dataProvider = "testUrls")
	public void testPlaybackDRM(String testName, UrlObject url) throws OoyalaException {
		boolean result = true;

		try {
			driver.get(url.getUrl());
			Thread.sleep(10000);
			injectScript();
			result = !driver.executeScript("return pp.getErrorCode()").toString()
					.equalsIgnoreCase("concurrent_streams");
			getWebdriver(browser).get(url.getUrl());
			Thread.sleep(10000);
			injectScript();
			result = !driver.executeScript("return pp.getErrorCode()").toString()
					.equalsIgnoreCase("concurrent_streams");
			getWebdriver(browser).close();

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "DRM tests failed : " + testName);
	}
}