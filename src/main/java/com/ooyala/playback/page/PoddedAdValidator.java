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
				extentTest.log(LogStatus.FAIL, element + " not found after " + timeout + " ms");
				return false;
			}

			int result = parseInt(driver.executeScript("return " + element + ".textContent").toString());
			extentTest.log(LogStatus.INFO, "No of ads " + result);

			for (int i = 1 + counter; i <= result; i++) {
				boolean willPlaySingleAd = waitOnElement(By.id(position + "_willPlaySingleAd_" + i), 10000);

				boolean singleAdPlayed = waitOnElement(By.id("singleAdPlayed_" + i), 16000);

				if (!(willPlaySingleAd && singleAdPlayed)) {
					extentTest.log(LogStatus.FAIL, "Ad started elements from injected scripts are not found");
					return false;
				} else {
					extentTest.log(LogStatus.PASS,
							"Found " + position + "_willPlaySingleAd_" + i + " and " + "singleAdPlayed_" + i);
				}
			}
			extentTest.log(LogStatus.PASS, "Podded Ad Completed");
			counter += result;
			return true;
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, e.getMessage());
			logger.error(e.getMessage());
		}
		return false;
	}
}
