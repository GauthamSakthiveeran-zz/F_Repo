package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.relevantcodes.extentreports.LogStatus;

public class AdFrequencyValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger log = Logger.getLogger(AdFrequencyValidator.class);

	public AdFrequencyValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
	}

	String urlData = "";
	String url = "";

	int firstAdPlay = 0;
	int adFrequency = 0;

	public AdFrequencyValidator split(String url) {

		if (url == null || url.isEmpty()) {
			extentTest.log(LogStatus.FAIL, "Insufficient data");
			assert false;
		}

		if (url.contains(" ")) {
			String splits[] = url.split(" ");
			if (splits.length != 3) {
				extentTest.log(LogStatus.FAIL, "Insufficient data");
			} else {
				this.url = splits[0];
				firstAdPlay = Integer.parseInt(splits[1]);
				adFrequency = Integer.parseInt(splits[2]);
			}
		} else {
			extentTest.log(LogStatus.FAIL, "Insufficient data");
			assert false;
		}
		return this;
	}

	public String getUrl() {
		return this.url;
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

	public boolean adPlaying(boolean checkOnce) throws Exception {
		int time = 0;
		boolean flag;

		IsAdPlayingValidator adPlaying = new PlayBackFactory(driver,extentTest).isAdPlaying();

		if (checkOnce){
			Thread.sleep(1000);
			return adPlaying.validate("", 1000);
		}

		while (true) {
			if (time <= 120) {
				try {
					flag = adPlaying.validate("", 1000);
					if (!flag) {
						flag = true;
						break;
					}
					Thread.sleep(1000);
					time++;
				} catch (Exception e) {
					return true;
				}
			} else {
				flag = false;
				break;
			}

		}
		if (!flag) {
			extentTest.log(LogStatus.FAIL, "Ad is playing for a really long time. Some issue.");
			assert false;
		}
		return flag;

	}

}
