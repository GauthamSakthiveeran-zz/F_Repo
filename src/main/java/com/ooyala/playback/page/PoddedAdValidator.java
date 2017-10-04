package com.ooyala.playback.page;

import static java.lang.Integer.parseInt;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.LogStatus;

public class PoddedAdValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(PoddedAdValidator.class);

	public PoddedAdValidator(WebDriver webDriver) {
		super(webDriver);
		counter = 0;
	}

	private String position = "";

	public PoddedAdValidator setPosition(String position) {
		this.position = position;
		return this;
	}

	private int counter = 0;

	public boolean validate(String element, int timeout) throws Exception {

		try {
			if (!waitOnElement(By.id(element), timeout)) {
				logger.error(element + " not found after " + timeout + " ms");
				extentTest.log(LogStatus.FAIL, element + " not found after " + timeout + " ms");
				return false;
			}

			int result = parseInt(
					driver.executeScript("return document.getElementById('" + element + "').textContent").toString());
			logger.info("No of ads " + result);
			extentTest.log(LogStatus.INFO, "No of ads " + result);

			for (int i = 1 + counter; i <= result; i++) {
				boolean willPlaySingleAd = waitOnElement(By.id(position + "_willPlaySingleAd_" + i), 10000);
				if (driver.getCurrentUrl().contains("AnalyticsQEPlugin")) {
					// As analytics_ad_break_started_1 event gets triggered only
					// once
					if (i == 1) {
						if (!isAnalyticsElementPreset("analytics_ad_break_started_" + i)) {
							return false;
						}
					}
					if (!isAnalyticsElementPreset("analytics_ad_started_" + i)) {
						return false;
					}
				}

				boolean singleAdPlayed = waitOnElement(By.id("singleAdPlayed_" + i), 16000);

				if (driver.getCurrentUrl().contains("AnalyticsQEPlugin")) {
					// As analytics_ad_break_ended_1 event gets triggered only
					// once
					if (i == 1) {
						if (!isAnalyticsElementPreset("analytics_ad_break_ended_" + i)) {
							return false;
						}
					}
					if (!isAnalyticsElementPreset("analytics_ad_ended_" + i)) {
						return false;
					}
				}

				if (!(willPlaySingleAd && singleAdPlayed)) {
					logger.error("Ad started elements from injected scripts are not found");
					extentTest.log(LogStatus.FAIL, "Ad started elements from injected scripts are not found");
					return false;
				} else {
					logger.info("Found " + position + "_willPlaySingleAd_" + i + " and " + "singleAdPlayed_" + i);
					extentTest.log(LogStatus.PASS,
							"Found " + position + "_willPlaySingleAd_" + i + " and " + "singleAdPlayed_" + i);
				}
			}
			logger.info("Podded Ad Completed");
			extentTest.log(LogStatus.PASS, "Podded Ad Completed");
			counter += result;
			return true;
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, e.getMessage());
			logger.error(e.getMessage());
		}
		return false;
	}

	public boolean validatePoddedAds(int adsPlayed, int adsToPlay) throws Exception {
		boolean result = true;
		for (int i = 1 + adsPlayed; i <= adsToPlay; i++) {
			result = result && waitOnElement("willPlaySingleAd_" + i + "", 45000);
			result = result && waitOnElement("singleAdPlayed_" + i + "", 45000);
		}
		return result;
	}
}
