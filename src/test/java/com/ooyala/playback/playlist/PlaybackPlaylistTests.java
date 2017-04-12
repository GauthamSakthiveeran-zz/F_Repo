package com.ooyala.playback.playlist;

import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.StreamTypeValidator;
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
	private StreamTypeValidator streamTypeValidator;
	private EventValidator eventValidator;

	public PlaybackPlaylistTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playlist", dataProvider = "testUrls")
	public void testPlaylistTests(String description, UrlObject url) throws OoyalaException {

		String[] parts = description.split(":")[1].trim().split("-");
		String[] descParts=description.split(" ");
        String videoPluginName = descParts[descParts.length-1];
		String tcName = parts[0].trim();
        if(tcName.contains(videoPluginName))
            tcName = tcName.replaceAll(videoPluginName,"").trim();
		String tcValue = "";
		if (parts.length > 1)
            tcValue = parts[1].replaceAll(videoPluginName,"").trim();

		boolean result = true;
		try {

			driver.get(url.getUrl());
			if (!(description.contains("true") || description.contains("Menustyle-tabs"))) {
				result = result && play.waitForPage();
				injectScript();
			} else {
				result = result && playlist.isPageLoaded();
                injectScript();
			}

			result = result && playlist.playlistValidator(tcName, tcValue, videoPluginName);

            if (url.getStreamType() != null && !url.getStreamType().isEmpty()) {
                if (description.contains("Autoplay-false"))
                    result = result && play.validate("playing_1",20000);
                result = result && eventValidator.validate("videoPlayingurl", 40000);
                result = result
                        && streamTypeValidator.setStreamType(url.getStreamType()).validate("videoPlayingurl", 1000);
            }

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, e.getMessage());
			e.printStackTrace();
			result = false;
		}
		Assert.assertTrue(result, "Playback Playlist tests failed" + description);
	}
}
