package com.ooyala.playback;

import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PlayerAPIValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class APIValidationsTest extends PlaybackWebTest {

	public APIValidationsTest() throws OoyalaException {
		super();
	}

	protected void validateAPI(String testName, boolean result, PlayerAPIValidator ooyalaAPIValidator,
			PlayValidator playValidator) throws Exception {
		boolean playPauseSeek = testName.contains("Play Pause Seek");
		boolean volume = testName.contains("Volume");
		boolean destroy = testName.contains("Destroy");
		boolean fullscreen = testName.contains("FullScreen");
		boolean durationAtEnd = testName.contains("DurationAtEnd");
		boolean cc = testName.contains("CC");
		if (result && playPauseSeek) {
			s_assert.assertTrue(ooyalaAPIValidator.validatePlayPauseSeekAPI(), "Play Pause Seek");
		}

		if (result && volume) {
			s_assert.assertTrue(ooyalaAPIValidator.validateVolumeAPI(), "Volume");
		}

		if (result && fullscreen) {
			s_assert.assertTrue(ooyalaAPIValidator.validateFullScreenAPI(), "FullScreen");
		}

		if (result && durationAtEnd) {
			s_assert.assertTrue(ooyalaAPIValidator.validateDurationAtEndScreen(), "DurationAtEnd");
		}

		if (result && destroy) {
			driver.navigate().refresh();
			result = result && playValidator.waitForPage();
			injectScript();
			s_assert.assertTrue(ooyalaAPIValidator.validateDestroyAPI(), "Destroy");
		}

		if (result && cc) {
			s_assert.assertTrue(ooyalaAPIValidator.validateCloseCaptionAPI(), "CC");
		}
	}

}
