package com.ooyala.playback.page;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class AdClickThroughValidator extends PlayBackPage implements
		PlaybackValidator {

	public static Logger log = Logger.getLogger(AdClickThroughValidator.class);

	public AdClickThroughValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("adclicks");
	}

	public boolean validate(String element, int timeout) throws Exception {

		Map<String, String> data = parseURL();

		if (data == null) {
			throw new Exception("Map is null");
		}

		String value = data.get("ad_plugin");
		String video_plugin = data.get("video_plugins");

		String baseWindowHdl = driver.getWindowHandle();
		if (value != null) {

			if (!getPlatform().equalsIgnoreCase("Android")) {

				boolean flag = true;

				if (!value.contains("freewheel")) {
					if (value.contains("vast")) {
						if (!clickOnIndependentElement("AD_SCREEN_PANEL"))
							return false;
					}

					else if (value.contains("ima")
							&& video_plugin.contains("bit")
							&& isStreamingProtocolPrioritized("hls")
							&& !getBrowser().contains("safari")
							&& !getBrowser().contains("internet explorer")) {
						if (!clickOnIndependentElement("AD_PANEL_1"))
							return false;
						if (!waitOnElement(By.id("adsClickThroughOpened"),
								10000))
							return false;
						flag = false;
					}

					else {
						if (!clickOnIndependentElement("AD_PANEL"))
							return false;
					}
					if (flag) {
						if (!waitOnElement(By.id("adsClicked_1"), 10000))
							return false;
						if (!waitOnElement(By.id("adsClicked_videoWindow"),
								10000))
							return false;
					}
					extentTest.log(PASS,
							"AdsClicked by clicking on the ad screen");
				}
			}
			if (!value.contains("ima")) {

				if (!clickOnIndependentElement("LEARN_MORE"))
					return false;
				if (!waitOnElement(By.id("adsClicked_learnMoreButton"), 5000))
					return false;

			}
			extentTest.log(PASS, "AdsClicked by clicking on the learn more button");

			sleep(2000);
			java.util.Set<java.lang.String> windowHandles = driver
					.getWindowHandles();
			int count = windowHandles.size();
			log.info("Window handles : " + count);

			for (String winHandle : driver.getWindowHandles()) {
				if (!winHandle.equals(baseWindowHdl)) {
					driver.switchTo().window(winHandle);
					driver.close();
					driver.switchTo().window(baseWindowHdl);
				}
			}

			boolean isAd = isAdPlaying();
			if (isAd) {
				((JavascriptExecutor) driver).executeScript("pp.play()");
			}
			return true;

		} else {
			throw new Exception("Ad plugin not present in test url");
		}

	}

	public boolean isAdPlaying() {
		Boolean isAdplaying = (Boolean) (((JavascriptExecutor) driver)
				.executeScript("return pp.isAdPlaying()"));
		return isAdplaying;
	}
}
