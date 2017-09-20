package com.ooyala.playback.apps.actions;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;

public class PlayAction extends PlaybackApps implements Actions {

	private static Logger logger = Logger.getLogger(PlayAction.class);

	public PlayAction(AppiumDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("playpause");
	}

	@Override
	public boolean startAction(String element) throws Exception {
		if (getPlatform().equalsIgnoreCase("ios"))
			return ios(element);
		return android(element);
	}

	private boolean ios(String element) throws Exception {
		try {
			if (!tapScreenIfRequired()) {
				extentTest.log(LogStatus.FAIL, "tapScreenIfRequired failed.");
				return false;
			}
			if (!clickOnIndependentElement(element)) {
				logger.error("Unable to click on play pause.");
				extentTest.log(LogStatus.FAIL, "Unable to click on play pause.");
				return false;
			}
		} catch (Exception e) {
			logger.info("Play button not found. Tapping screen and retrying..");
			if (!tapScreenIfRequired()) {
				extentTest.log(LogStatus.FAIL, "tapScreenIfRequired failed.");
				return false;
			}
			if (!clickOnIndependentElement(element)) {
				extentTest.log(LogStatus.FAIL, "Unable to click on play pause.");
				logger.error("Unable to click on play pause.");
				return false;
			}
		}
		return true;
	}

	private boolean android(String element) throws Exception {
		try {
			if (!getPlayPause(element)) {
				extentTest.log(LogStatus.FAIL, "Unable to get the element");
				logger.error("Unable to get the element");
				return false;
			}
		} catch (Exception e) {
			logger.info("Play button not found. Tapping screen and retrying..");
			extentTest.log(LogStatus.INFO, "Play button not found. Tapping screen and retrying..");
			if (!getPlayPause(element)) {
				extentTest.log(LogStatus.FAIL, "Unable to get the element");
				logger.error("Unable to click on play pause.");
				return false;
			}
		}
		return true;
	}

}
