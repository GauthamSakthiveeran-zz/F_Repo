package com.ooyala.playback.amf;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPlayerWithoutSkinTests extends PlaybackWebTest {

	public PlaybackPlayerWithoutSkinTests() throws OoyalaException {
		super();
	}

	private EventValidator event;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPlayerWithoutskin(String testName, String url) // TODO
			throws OoyalaException {

		boolean result = true;

		try {
			url = removeSkin(url);
			driver.get(url);

			injectScript();

			result = result
					&& !Boolean.parseBoolean(executeScript(
							"return pp.parameters.autoplay").toString());

			executeScript("pp.play();");

			result = result && event.validate("willPlayPrerollAd", 60000);

			result = result && event.validate("adIsPlaying", 60000);

			executeScript("pp.skipAd()");

			result = result && event.validate("adPlayed", 60000);

			double initialtimeset = Double
					.parseDouble((executeScript("return VideoInitialTime.textContent"))
							.toString());

			result = result && event.validate("InitialTime_0", 60000);

			result = result && event.validate("videoPlaying", 60000);

			result = result
					&& parseBoolean((executeScript("return pp.isFullscreen()"))
							.toString());

			executeScript("pp.pause()");

			result = result && event.validate("paused_1", 60000);

			executeScript("pp.setVolume(0.5)");

			double getvol = parseDouble(executeScript("return pp.getVolume()")
					.toString());

			result = result && (getvol == 0.5);

			executeScript("pp.seek(20)");

			result = result && event.validate("seeked_1", 60000);

			executeScript("pp.seek(10)");

			result = result && event.validate("seeked_2", 60000);

			executeScript("pp.play()");

			executeScript("pp.seek(pp.getDuration()-7);");

			result = result && event.validate("videoPlaying_1", 190000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");
	}

}
