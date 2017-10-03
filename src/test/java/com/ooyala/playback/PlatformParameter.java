package com.ooyala.playback;

import com.ooyala.playback.page.BitmovinTechnologyValidator;
import com.ooyala.playback.page.EncodingValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlatformParameter extends PlaybackWebTest {

	public PlatformParameter() throws OoyalaException {
		super();
	}

	protected boolean validate(String testName, boolean result, UrlObject url, EncodingValidator encodingValidator,
			BitmovinTechnologyValidator tech, PlayValidator play, SeekValidator seek, EventValidator eventValidator) throws Exception {

		String[] parameters = { "{\"platform\":\"html5\"}", "{\"platform\":\"flash\"}" };
		String playerParameters = url.getPlayerParameter();
		if (testName.contains("Playlist")) {
			if (url.getPlayerParameter().contains("html5")) {
				logger.info("verifying platform flash");
				playerParameters = playerParameters.replace("html5", "flash");
				driver.get(encodingValidator.getNewUrl(playerParameters, browser));
				result = result && play.waitForPage();

				injectScript();

				tech.getConsoleLogs();

				result = result && play.validate("playing_1", 60000);

				result = result && tech.setStream(url.getStreamType()).validate("bitmovin_technology", 6000);
			}

		} else {
			// verifying html5 and flash platform
			for (int i = 0; i < parameters.length; i++) {

				driver.get(encodingValidator.getNewUrl(parameters[i], browser));

				result = result && play.waitForPage();

				injectScript();

				tech.getConsoleLogs();

				result = result && play.validate("playing_1", 60000);

				result = result && tech.setStream(url.getStreamType()).validate("bitmovin_technology", 6000);

				result = result && seek.validate("seeked_1", 60000);

				result = result && eventValidator.skipScrubberValidation().validate("played_1", 120000);
			}
		}
		return result;
	}

}
