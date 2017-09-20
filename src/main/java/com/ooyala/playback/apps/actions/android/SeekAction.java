package com.ooyala.playback.apps.actions.android;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.actions.Actions;

import io.appium.java_client.AppiumDriver;

public class SeekAction extends PlaybackApps implements Actions {

    private static Logger logger = Logger.getLogger(SeekAction.class);

    public SeekAction(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("seekbar");
    }

    @Override
    public boolean startAction(String element) throws Exception {
    	try {
            if(!seekVideo(element)) {
                logger.error("Unable to seek Video, Seekbar was not present");
                return false;
            }
        } catch (Exception e) {
            logger.info("Seekbar is not present , let's tap on screen");
            tapOnScreen();
            if(!seekVideo(element)) {
                logger.error("Unable to click on play pause.");
                return false;
            }
        }
        return true;
    }

}
