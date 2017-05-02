package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.url.UrlObject;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 10/4/17.
 */
public class MidrollAdValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(MidrollAdValidator.class);

	public MidrollAdValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
	}

	private int time;

	public boolean isOverlayPlayTimePresent(UrlObject url) {
		if (url.getOverlayPlayTime() != null && !url.getOverlayPlayTime().isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean isAdPlayTimePresent(UrlObject url) {
		if (url.getAdStartTime() != null && !url.getAdStartTime().isEmpty()) {
			return true;
		}
		return false;
	}

	public MidrollAdValidator setTime(String time) {
		this.time = Integer.parseInt(time);
		return this;
	}

	public MidrollAdValidator setTime(int time) {
		this.time = time;
		return this;
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		return false;
	}

	public boolean validateMidrollAd(UrlObject url) throws Exception {

		if (isAdPlayTimePresent(url)) {
			time = Integer.parseInt(url.getAdStartTime());
		}
		if (url.getVideoPlugins().toLowerCase().contains("akamai")) {

			if (url.getAdPlugins().toLowerCase().contains("freewheel")) {

				boolean flag = isAdPlayTimePresent(url) ? validateAdStartTime("MidRoll_willPlaySingleAd_2")
						: waitOnElement(By.id("MidRoll_willPlayAds_2"), 120000);
				return flag && waitOnElement(By.id("adsPlayed_2"), 60000);

			} else {

				boolean flag = isAdPlayTimePresent(url) ? validateAdStartTime("MidRoll_willPlaySingleAd_1")
						: waitOnElement(By.id("MidRoll_willPlayAds_1"), 120000);
				return flag && waitOnElement(By.id("adsPlayed_1"), 60000);
			}

		} else {

			boolean flag = isAdPlayTimePresent(url) ? validateAdStartTime("MidRoll_willPlaySingleAd_1")
					: waitOnElement(By.id("MidRoll_willPlaySingleAd_1"), 120000);

			if (url.getAdPlugins().toLowerCase().contains("pulse"))
				return flag && waitOnElement(By.id("singleAdPlayed_2"), 60000);
			else
				return flag && waitOnElement(By.id("singleAdPlayed_1"), 60000);
		}

	}

	public boolean validateAdStartTime(String adEventLocator) {
		if (!loadingSpinner()) {
			extentTest.log(LogStatus.FAIL, "Loading spinner for a really long time");
			return false;
		}

		int count = 0;
		double playTime = getPlayAheadTime();
		while (playTime <= time) {
			playTime = getPlayAheadTime();
			if (count == 120 && playTime <= 0) {
				extentTest.log(LogStatus.FAIL, "Looks like the video did not play.");
				return false;
			}
			if (!loadingSpinner()) {
				extentTest.log(LogStatus.FAIL, "Loading spinner seems to be there for a really long time.");
				return false;
			}
			count++;
		}

		if (!waitOnElement(By.id(adEventLocator), 10000)) {
			logger.error(adEventLocator + "element is not found or midroll ad is not playing at " + time);
			extentTest.log(LogStatus.FAIL,
					adEventLocator + "element is not found or midroll ad is not playing at " + time);
			return false;
		}

		logger.info(adEventLocator + " is present and midroll ad played at " + time);
		extentTest.log(LogStatus.PASS, adEventLocator + " is present and midroll ad played at " + time);
		return true;
	}

	public boolean validateNonLinearAdStartTime(String element) {

		if (!loadingSpinner()) {
			extentTest.log(LogStatus.FAIL, "Loading spinner seems to be there for a really long time.");
			return false;
		}

		if (!waitOnElement(By.id(element), 20000)) {
			logger.error(element + "is not present");
			extentTest.log(LogStatus.FAIL, element + "is not present");
			return false;
		}

		if (!waitOnElement(By.id("overlay-ad-position"), 20000)) {
			logger.error("overlay-ad-position element id is not present");
			extentTest.log(LogStatus.FAIL, "overlay-ad-position element id is not present");
			return false;
		}

		if (!waitOnElement(By.id("play-overaly-ad"), 20000)) {
			logger.error("play-overaly-ad element id is not present");
			extentTest.log(LogStatus.FAIL, "play-overaly-ad element id is not present");
			return false;
		}

		logger.info("overlay position is at " + time + "th second");

		int overlayPlayingAt = Integer.parseInt(driver.findElement(By.id("play-overaly-ad")).getText());

		logger.info("overlay is playing at " + overlayPlayingAt + "th second");

		// in following if statement, we are giving offset of 3 sec so that test
		// won't get fail
		if (overlayPlayingAt >= time && overlayPlayingAt <= (time + 3)) {
			logger.info("Overlay is playing at expected position i.e : " + overlayPlayingAt);
			extentTest.log(LogStatus.PASS, "Overlay is playing at expected position i.e : " + overlayPlayingAt);
			return true;
		}

		logger.error("Overlay is not playing at expected position i.e : " + overlayPlayingAt);
		extentTest.log(LogStatus.FAIL, "Overlay is not playing at expected position i.e : " + overlayPlayingAt);
		return false;
	}

	public boolean validateMultipleMidrollAdStartTime(UrlObject url, String testName) throws Exception {
		boolean result = true;
		if (isAdPlayTimePresent(url)) {
			String[] midrollAdStartTimes = null;
			int timeForFirstMidrollAd;
			int timeForSecondMidrollAd;
			String adStartTime = url.getAdStartTime();

			if (adStartTime.contains(",")) {
				midrollAdStartTimes = adStartTime.split(",");
			}

			timeForFirstMidrollAd = Integer.parseInt(midrollAdStartTimes[0]);
			timeForSecondMidrollAd = Integer.parseInt(midrollAdStartTimes[1]);
			
			if(testName.contains("VAST:Main Vast3.0 Multimidroll")){
				result = result && setTime(timeForFirstMidrollAd).validateAdStartTime("MidRoll_willPlaySingleAd_1")
						&& waitOnElement(By.id("singleAdPlayed_2"), 200000);
				result = result && setTime(timeForSecondMidrollAd).validateAdStartTime("MidRoll_willPlaySingleAd_3")
						&& waitOnElement(By.id("singleAdPlayed_4"), 200000);
			} else{
				result = result && setTime(timeForFirstMidrollAd).validateAdStartTime("MidRoll_willPlaySingleAd_1")
						&& waitOnElement(By.id("singleAdPlayed_1"), 200000);
				result = result && setTime(timeForSecondMidrollAd).validateAdStartTime("MidRoll_willPlaySingleAd_2")
						&& waitOnElement(By.id("singleAdPlayed_2"), 200000);
			}
			
		} else {
			result = result && waitOnElement(By.id("MidRoll_willPlayAds"), 200000)
					&& waitOnElement(By.id("MidRoll_willPlaySingleAd_1"), 200000)
					&& waitOnElement(By.id("singleAdPlayed_1"), 200000)
					&& waitOnElement(By.id("MidRoll_willPlaySingleAd_2"), 200000)
					&& waitOnElement(By.id("singleAdPlayed_2"), 200000);
		}

		if (isAdPluginPresent("pulse")) {

			result = result && waitOnElement(By.id("MidRoll_willPlaySingleAd_3"), 200000)
					&& waitOnElement(By.id("singleAdPlayed_3"), 200000)
					&& waitOnElement(By.id("MidRoll_willPlaySingleAd_4"), 200000)
					&& waitOnElement(By.id("singleAdPlayed_4"), 200000);
		}
		return result;
	}
}