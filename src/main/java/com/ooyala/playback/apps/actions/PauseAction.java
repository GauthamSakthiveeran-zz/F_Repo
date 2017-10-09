package com.ooyala.playback.apps.actions;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;

public class PauseAction extends PlaybackApps implements Actions {

	private static Logger logger = Logger.getLogger(PauseAction.class);

	public PauseAction(AppiumDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("playpause");
	}

	@Override
	public boolean startAction(String element) throws Exception {
		if (getPlatform().equalsIgnoreCase("android")) {
			try {
				if (!getPause(element)) {
					logger.error("Unable to get the element");
					extentTest.log(LogStatus.FAIL, "Unable to get the element");
					return false;
				}
			} catch (Exception e) {
				logger.info("Play button not found. Tapping screen and retrying..");
				tapOnScreen();
				if (!getPause(element)) {
					logger.error("Unable to click>>>>>>>>>>>> on pause.");
					extentTest.log(LogStatus.FAIL, "Unable to click on pause.");
					return false;
				}
			}
			return true;
		} else {
			try {
				if(!waitOnElement(element, 1000))
					tapScreen();
				if (!clickOnIndependentElement(element)) {
					logger.error("Unable to click on play pause.");
					return false;
				}
			} catch (Exception e) {
				logger.info("Play button not found. Tapping screen and retrying..");
				tapScreenIfRequired();
				if (!clickOnIndependentElement(element)) {
					logger.error("Unable to click on play pause.");
					return false;
				}
			}
			return true;
		}
	}
}
