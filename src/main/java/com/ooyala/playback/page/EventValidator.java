package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;
import java.util.Map;

/**
 * Created by soundarya on 11/14/16.
 */
public class EventValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(EventValidator.class);

	public EventValidator(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("discovery");
		addElementToPageElements("play");
		addElementToPageElements("pause");
		addElementToPageElements("replay");
		addElementToPageElements("controlbar");
		addElementToPageElements("fullscreen");
		addElementToPageElements("adPodEnd");
		addElementToPageElements("startscreen");
	}

	public boolean validate(String element, int timeout) throws Exception {

		if (!loadingSpinner()) {
			extentTest.log(LogStatus.FAIL, "Loading spinner seems to be there for a really long time.");
			return false;
		}

		if (waitOnElement(By.id(element), timeout)) {
			logger.info("element found : " + element);

			if (waitOnElement(By.id(element), timeout)) {
				extentTest.log(LogStatus.PASS, "Wait on element : " + element);
				if (element.startsWith("played")) {
					return new PlayBackFactory(driver, extentTest).getScrubberValidator().validate("", 1000);
				}
				if (element.startsWith("seeked")) {
					((JavascriptExecutor) driver).executeScript("return pp.pause();");
					boolean flag = new PlayBackFactory(driver, extentTest).getScrubberValidator().validate("", 1000);
					((JavascriptExecutor) driver).executeScript("return pp.play();");
					return flag;
				}
				return true;
			}
		}
		extentTest.log(LogStatus.FAIL, "Wait on element : " + element + " failed after " + timeout + " ms");
		return false;
	}

	public boolean eventAction(String element) throws Exception {
	if(!getBrowser().equalsIgnoreCase("safari")) {
			return clickOnIndependentElement(element);
		}else return clickOnHiddenElement(element);
	}

	public void validateElement(String element, int timeout) throws Exception {
		if (waitOnElement(element, timeout)) {
			extentTest.log(LogStatus.PASS, "Wait on element : " + element);
		} else {
			extentTest.log(LogStatus.FAIL, "Wait on element : " + element + " failed after " + timeout + " ms");
		}

	}

	public boolean validateElementPresence(String element) throws Exception {
		if (isElementPresent(By.id(element))) {
			return true;
		}
		return false;
	}

	public boolean playVideoForSometime(int secs) {
		int count = 0;
		double playTime = Double
				.parseDouble(((JavascriptExecutor) driver).executeScript("return pp.getPlayheadTime();").toString());
		while (playTime <= secs) {
			playTime = Double.parseDouble(
					((JavascriptExecutor) driver).executeScript("return pp.getPlayheadTime();").toString());
			if (count == 120) {
				extentTest.log(LogStatus.FAIL, "Looks like the video did not play.");
				return false;
			}
			if (!loadingSpinner()) {
				extentTest.log(LogStatus.FAIL, "Loading spinner seems to be there for a really long time.");
				return false;
			}
			count++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public boolean isVideoPluginPresent(String videoPlugin) throws Exception {
		Map<String, String> map = parseURL();
		if (map != null && map.get("video_plugins") != null && map.get("video_plugins").contains(videoPlugin)) {
			return true;
		}
		return false;
	}

	public boolean validatePlaybackReadyEvent(int timeout) {
		boolean result = false;
		logger.info("Inside validatePlaybackReadyEvent method");
		String consoleOutput = driver.executeScript("return OO.DEBUG.consoleOutput[0].toString()").toString();
		logger.info(consoleOutput);
		if (consoleOutput.contains("playbackReady")) {
			logger.info("playbackReady event found in consoleOutput");
			extentTest.log(LogStatus.PASS, "playbackReady event found in consoleOutput");
			result = true;
		} else {
			logger.error("playbackReady event not found in consoleOutput");
		}

		return result;
	}
	
	public boolean validateAutoPlay() {
		boolean autoplay = false;

		autoplay = Boolean.parseBoolean(driver.executeScript("return pp.parameters.autoPlay").toString());

		if (!autoplay) {
			extentTest.log(LogStatus.INFO, "Autoplay not set for this video");
		}
		return autoplay;
	}
}