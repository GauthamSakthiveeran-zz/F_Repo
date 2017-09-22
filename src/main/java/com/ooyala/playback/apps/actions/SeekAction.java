package com.ooyala.playback.apps.actions;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.utils.CommandLineParameters;

import io.appium.java_client.AppiumDriver;

public class SeekAction extends PlaybackApps implements Actions {

    private static Logger logger = Logger.getLogger(SeekAction.class);

    public SeekAction(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("seekbar");
    }
    
    private String slider;
    boolean seekFrwd;
    
    public SeekAction setSlider(String slider) {
    	this.slider = slider;
    	return this;
    }
    
    public SeekAction seekforward() {
    	seekFrwd= true;
    	return this;
    }

	@Override
	public boolean startAction(String seek) throws Exception {
		if (System.getProperty(CommandLineParameters.PLATFORM).equalsIgnoreCase("ios")) {
			if (seekFrwd) {
				// seekFrwd = false;
				tapScreenIfRequired();
				if (!seekVideoForward(slider, seek)) {
					logger.error("Unable to seek forward video.");
					tapScreenIfRequired();
					if (!seekVideoForward(slider, seek)) {
						logger.error("Unable to seek forward video");
						return false;
					}
				}
			} else {
				tapScreenIfRequired();
				if (!seekVideoBack(slider, seek)) {
					logger.error("Unable to seek forward video.");
					tapScreenIfRequired();
					if (!seekVideoBack(slider, seek)) {
						logger.error("Unable to seek forward video");
						return false;
					}
				}
			}
		} else {
			try {
				if (!seekVideo(seek)) {
					logger.error("Unable to seek Video, Seekbar was not present");
					return false;
				}
			} catch (Exception e) {
				logger.info("Seekbar is not present , let's tap on screen");
				tapOnScreen();
				if (!seekVideo(seek)) {
					logger.error("Unable to click on play pause.");
					return false;
				}
			}
		}
		return true;

	}

}
