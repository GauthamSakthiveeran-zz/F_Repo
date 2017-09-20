package com.ooyala.playback.apps.actions;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;

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
		try {
			waitAndTap();
			if(!clickOnIndependentElement(element)) {
				logger.error("Unable to click on play pause.");
				return false;
			}
		} catch (Exception e) {
			logger.info("Play button not found. Tapping screen and retrying..");
			waitAndTap();
			if(!clickOnIndependentElement(element)) {
				logger.error("Unable to click on play pause.");
				return false;
			}
		}
		return true;
	}

    public boolean startAction_Android(String element) throws Exception {
        try {
            if(!getPlayPause(element)) {
                logger.error("Unable to get the element");
            }
        } catch (Exception e) {
            logger.info("Play button not found. Tapping screen and retrying..");
            if(!getPlayPause(element)) {
                logger.error("Unable to click on play pause.");
                return false;
            }
        }
        return true;
    }

}
