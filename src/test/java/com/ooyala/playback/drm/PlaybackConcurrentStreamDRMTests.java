package com.ooyala.playback.drm;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.qe.common.exception.OoyalaException;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PlaybackConcurrentStreamDRMTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackConcurrentStreamDRMTests.class);
	private ConcurrentStreamValidator concurrentStream;
    private PlayValidator play;

	public PlaybackConcurrentStreamDRMTests() throws OoyalaException {
		super();
	}

	@Test(groups = "drm", dataProvider = "testUrls")
	public void testPlaybackDRM(String testName, String url)
			throws OoyalaException {
		boolean result = true;

		try {
            driver.get(url);
            Thread.sleep(10000);
            injectScript();
            result = !driver.executeScript("return pp.getErrorCode()").toString().equalsIgnoreCase("concurrent_streams");
            getWebdriver(browser).get(url);
            Thread.sleep(10000);
            injectScript();
            result = !driver.executeScript("return pp.getErrorCode()").toString().equalsIgnoreCase("concurrent_streams");
            getWebdriver(browser).close();

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "DRM tests failed : " + testName);
	}
}