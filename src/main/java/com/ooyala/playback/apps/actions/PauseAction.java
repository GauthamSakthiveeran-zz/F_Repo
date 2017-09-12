package com.ooyala.playback.apps.actions;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;

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
		try {
			if(!clickOnIndependentElement("PLAY_PAUSE_BUTTON")) {
				logger.error("Unable to click on play pause.");
				return false;
			}
		} catch (Exception e) {
			logger.info("Play button not found. Tapping screen and retrying..");
			tapScreenIfRequired();
			if(!clickOnIndependentElement("PLAY_PAUSE_BUTTON")) {
				logger.error("Unable to click on play pause.");
				return false;
			}
		}
		return true;
	}

}
