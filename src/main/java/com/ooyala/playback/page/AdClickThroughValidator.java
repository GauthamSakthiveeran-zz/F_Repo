package com.ooyala.playback.page;

import static com.relevantcodes.extentreports.LogStatus.FAIL;
import static com.relevantcodes.extentreports.LogStatus.PASS;

import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.url.UrlObject;
import com.relevantcodes.extentreports.LogStatus;

public class AdClickThroughValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger log = Logger.getLogger(AdClickThroughValidator.class);

	public AdClickThroughValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("adclicks");
		addElementToPageElements("adoverlay");
	}

	boolean overlay = false;

	public AdClickThroughValidator overlay() {
		overlay = true;
		return this;
	}

	public boolean ignoreClickThrough(UrlObject url) {
		return url.getIgnoreClickThrough();
	}

	private boolean validateOverlayClickThrough() {
		if (isElementPresent("OVERLAY_IMAGE")) {
			/*if (!getBrowser().equalsIgnoreCase("safari"))*/
				if (clickOnIndependentElement("OVERLAY_IMAGE")) {
					if (!waitOnElement(By.id("adsClickThroughOpened"), 10000)) {
						extentTest.log(LogStatus.FAIL, "adsClickThroughOpened not found.");
					} else {
						extentTest.log(LogStatus.PASS, "adsClickThroughOpened found.");
					}
				} else {
					extentTest.log(LogStatus.FAIL, "OVERLAY_IMAGE not clickable.");
				}
		} else {
			extentTest.log(LogStatus.FAIL, "OVERLAY_IMAGE not found.");
		}
		return true;
	}

	public boolean validate(String element, int timeout) throws Exception {

		/*if (getBrowser().equalsIgnoreCase("safari"))
			return true;*/

		if (!loadingSpinner()) {
			extentTest.log(LogStatus.FAIL, "In Loading spinner for a really long time.");
			return false;
		}

		String baseWindowHdl = driver.getWindowHandle();

		if (overlay) {
			validateOverlayClickThrough();
			closeOtherWindows(baseWindowHdl);
			((JavascriptExecutor) driver).executeScript("pp.play()");
			return true;
		}

		return clickThroughOnAd(1);

	}

	private void validateNoOfTabsOpened() {
		int windowHandleCount = getWindowHandleCount();

		if (windowHandleCount <= 1) {
			log.info("New tab did not open on ad click.");
			extentTest.log(LogStatus.FAIL, "New tab did not open on ad click.");
		}

		if (windowHandleCount > 1) {
			log.info("No of tabs opened - " + windowHandleCount);
			// extentTest.log(LogStatus.FAIL, "Too many tabs opened - No of tabs
			// : " + windowHandleCount);
		}
	}

	public boolean isAdPlaying() {
		Boolean isAdplaying = (Boolean) (((JavascriptExecutor) driver).executeScript("return pp.isAdPlaying()"));
		return isAdplaying;
	}

	private boolean clickThroughOnAd(int counter) throws Exception {
		String baseWindowHdl = driver.getWindowHandle();
		Map<String, String> data = parseURL();
		boolean flag = true;
		if (data == null) {
			throw new Exception("Map is null");
		}

		String value = data.get("ad_plugin");
		String video_plugin = data.get("video_plugins");
		boolean isAdPlaying = isAdPlaying();
		if (isAdPlaying) {

			// check ad clickthrough
			if (isElementPresent("AD_SCREEN_PANEL")) {
				if (!clickOnIndependentElement("AD_SCREEN_PANEL")) {
					extentTest.log(LogStatus.FAIL, "Click on AD_SCREEN_PANEL failed.");
					return false;
				}

				if (!waitOnElement(By.id("videoPausedAds_" + counter + ""), 10000)) {
					log.info("unable to verify event videoPausedAds_" + counter);
					extentTest.log(LogStatus.FAIL, "unable to verify event videoPausedAds_" + counter);
					return false;
				}

			} else if (isElementPresent("AD_PANEL_1")) {
				if (!(clickOnIndependentElement("AD_PANEL_1") && waitOnElement(By.id("adsClickThroughOpened"), 2000))) {
					extentTest.log(LogStatus.FAIL,
							"Click on AD_PANEL_1 failed or wait on adsClickThroughOpened failed.");
					return false;
				}
				flag = false;
			} else {
				if (!clickOnIndependentElement("AD_PANEL")) {
					extentTest.log(LogStatus.FAIL, "Click on AD_PANEL failed.");
					return false;
				}
			}

			if (flag) {
				if (!waitOnElement(By.id("adsClicked_" + counter + ""), 2000)) {
					extentTest.log(FAIL, "adsClicked_" + counter + " event not found");
					return false;
				}
				if (!waitOnElement(By.id("adsClicked_videoWindow"), 2000)) {
					extentTest.log(FAIL, "adsClicked_videoWindow event not found");
					return false;
				}
			}

			validateNoOfTabsOpened();

			extentTest.log(PASS, "AdsClicked by clicking on the ad screen");
			log.info("AdsClicked by clicking on the ad screen");

			// check lean more clikcthrough
			if (!value.contains("ima")) {
				if (getBrowser().contains("internet explorer")) {
					if (value.contains("freewheel") && video_plugin.contains("main") && !video_plugin.contains("osmf")
							&& !video_plugin.contains("bit")) {
						if (!(waitOnElement("LEARN_MORE_IE", 10000) && clickOnIndependentElement("LEARN_MORE_IE"))) {
							extentTest.log(FAIL, "Issue with LEARN_MORE_IE");
							return false;
						}
					} else {
						if (!(waitOnElement("LEARN_MORE", 10000) && clickOnHiddenElement("LEARN_MORE"))) {
							extentTest.log(FAIL, "Issue with LEARN_MORE");
							return false;
						}
					}

				} else {
					if (!(waitOnElement("LEARN_MORE", 10000) && clickOnIndependentElement("LEARN_MORE"))) {
						extentTest.log(FAIL, "Issue with LEARN_MORE");
						return false;
					}
				}
				if (!waitOnElement(By.id("adsClicked_learnMoreButton"), 2000)) {
					extentTest.log(FAIL, "Wait on adsClicked_learnMoreButton failed.");
					return false;
				}

				if (!waitOnElement(By.id("videoPausedAds_1"), 20000)) {
					log.info("unable to verify event videoPausedAds_1");
					extentTest.log(LogStatus.FAIL, "unable to verify event videoPausedAds_1");
					return false;
				}

				validateNoOfTabsOpened();

			}
			extentTest.log(PASS, "AdsClicked by clicking on the learn more button");
			log.info("AdsClicked by clicking on the learn more button");

			closeOtherWindows(baseWindowHdl);

			((JavascriptExecutor) driver).executeScript("pp.play()");
			return true;
		} else {
			extentTest.log(FAIL, "Ad is not playing");
			return false;
		}
	}

	public boolean validateClickThroughForPoddedAds(String adType) throws Exception {
		int counter = 1;

		while (isAdPlaying()) {

			// verify if ad is playing or not based on ad type
			if (adType.equalsIgnoreCase("preroll")) {
				if (!waitOnElement(By.id("PreRoll_willPlaySingleAd_" + counter + ""), 10000)) {
					extentTest.log(FAIL, "PreRoll_willPlaySingleAd_" + counter + " element not found");
					return false;
				}
			} else if (adType.equalsIgnoreCase("midroll")) {
				if (!waitOnElement(By.id("willPlayMidrollAd_" + counter + ""), 10000)) {
					extentTest.log(FAIL, "willPlayMidrollAd_" + counter + " element not found");
					return false;
				}
			} else if (adType.equalsIgnoreCase("postroll")) {
				if (!waitOnElement(By.id("willPlayPostrollAd_" + counter + ""), 10000)) {
					extentTest.log(FAIL, "willPlayPostrollAd_" + counter + " element not found");
					return false;
				}
			}

			// verify clickthrough for ad and learn more if applicable
			if (!clickThroughOnAd(counter)) {
				extentTest.log(FAIL, "Clickthrough is not working");
				return false;
			}

			if (adType.equalsIgnoreCase("preroll")) {
				if (!waitOnElement(By.id("prerollAdPlayed_" + counter + ""), 10000)) {
					extentTest.log(FAIL, "prerollAdPlayed_" + counter + " element not found");
					return false;
				}
			} else if (adType.equalsIgnoreCase("midroll")) {
				if (!waitOnElement(By.id("midrollAdPlayed_" + counter + ""), 10000)) {
					extentTest.log(FAIL, "midrollAdPlayed_" + counter + " element not found");
					return false;
				}
			} else if (adType.equalsIgnoreCase("postroll")) {
				if (!waitOnElement(By.id("postrollAdPlayed_" + counter + ""), 10000)) {
					extentTest.log(FAIL, "postrollAdPlayed_" + counter + " element not found");
					return false;
				}
			}

			if (!loadingSpinner()) {
				extentTest.log(LogStatus.FAIL, "Video has been buffering for a really long time i.e it occured more that 2 minutes");
				return false;
			}

			// Increase counter by 1 if next ad is playing
			if (isAdPlaying()) {
				counter++;
			}
		}
		return true;
	}

}
