package com.ooyala.playback.playlist;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PlaylistValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by snehal on 13/01/17.
 */
public class PlaybackPlaylistTests extends PlaybackWebTest {

	private PlaylistValidator playlist;
	private PlayValidator play;

	public PlaybackPlaylistTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playlist", dataProvider = "testUrls")
	public void testPlaylistTests(String testName, UrlObject url) throws OoyalaException {

		String[] parts = testName.split(":")[1].trim().split("-");

		String tcName = parts[0].trim();
		String tcValue = "";
		if (parts.length > 1)
			tcValue = parts[1].trim();

		boolean result = true;
		try {

			driver.get(url.getUrl());
			if (!(testName.contains("true") || testName.contains("Menustyle-tabs"))) {
				result = result && play.waitForPage();
				injectScript();
			} else {
				result = result && playlist.isPageLoaded();
			}

			result = result && playlist.playlistValidator(tcName, tcValue);

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, e.getMessage());
			e.printStackTrace();
			result = false;
		}
		Assert.assertTrue(result, "Playback Playlist tests failed" + testName);
	}
}
