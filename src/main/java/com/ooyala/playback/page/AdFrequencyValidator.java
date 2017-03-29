package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.relevantcodes.extentreports.LogStatus;

public class AdFrequencyValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger log = Logger.getLogger(AdFrequencyValidator.class);

	public AdFrequencyValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
	}

	int firstAdPlay = 0;
	int adFrequency = 0;

	public AdFrequencyValidator split(UrlObject url) {

		if (url != null && url.getAdFirstPlay() != null && url.getAdFrequency() != null
				&& !url.getAdFirstPlay().isEmpty() && !url.getAdFrequency().isEmpty()) {
			firstAdPlay = Integer.parseInt(url.getAdFirstPlay());
			adFrequency = Integer.parseInt(url.getAdFrequency());
		} else {
			extentTest.log(LogStatus.FAIL, "Insufficient data");
			assert false;
		}
		return this;
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {

		if (firstAdPlay == 0 || adFrequency == 0) {
			return false;
		}
		
		PlayBackFactory factory = new PlayBackFactory(driver, extentTest);
		
		PlayAction play = factory.getPlayAction();
		EventValidator event = factory.getEventValidator();
		SeekAction seek = factory.getSeekAction();
		ReplayValidator replay = factory.getReplayValidator();

		int i = 1;
		play.startAction();
		while (i < firstAdPlay) {
			event.validate("playing_1", 10000);
			extentTest.log(LogStatus.INFO, "Play #" + i);
			if (adPlaying(true)) {
				extentTest.log(LogStatus.FAIL, "Ad should not be playing right now.");
				return false;
			}
			if (!seek.seekTillEnd().startAction()) {
				extentTest.log(LogStatus.FAIL, i + " Seek failed.");
				return false;
			}

			if (!replay.validate("replay_" + i, 5000)) {
				extentTest.log(LogStatus.FAIL, i + " Replay failed.");
				return false;
			}
			i++;
		}

		if (!adPlaying(true)) {
			extentTest.log(LogStatus.FAIL, "Ad should be playing right now.");
			return false;
		}

		adPlaying(false);

		extentTest.log(LogStatus.PASS, "Validated if ad played.");
		
		if (!seek.seekTillEnd().startAction()) {
			extentTest.log(LogStatus.FAIL, i + " Seek failed.");
			return false;
		}
		if (!replay.validate("replay_" + i, 5000)) {
			extentTest.log(LogStatus.FAIL, i + " Replay failed.");
			return false;
		}

		extentTest.log(LogStatus.PASS, "Ad played for the first time");

		int j = 1;
		while (j < adFrequency * 2) {
			extentTest.log(LogStatus.INFO, "Play #" + i);
			if (j % adFrequency == 0) {
				if (!adPlaying(true)) {
					extentTest.log(LogStatus.FAIL, "Ad should be playing right now.");
					return false;
				}
				adPlaying(false);
			} else{
				if (adPlaying(true)) {
					extentTest.log(LogStatus.FAIL, "Ad should not be playing right now.");
					return false;
				}
			}
			if (!seek.seekTillEnd().startAction()) {
				extentTest.log(LogStatus.FAIL, i + " Seek failed.");
				return false;
			}
			if (!replay.validate("replay_" + i, 5000)) {
				extentTest.log(LogStatus.FAIL, i + " Replay failed.");
				return false;
			}
			j++;
			i++;
		}

		return true;
	}

}
