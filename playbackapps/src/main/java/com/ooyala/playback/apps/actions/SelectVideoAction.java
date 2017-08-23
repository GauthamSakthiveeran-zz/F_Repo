package com.ooyala.playback.apps.actions;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;

import io.appium.java_client.AppiumDriver;

public class SelectVideoAction extends PlaybackApps implements Actions {
	
	private static Logger logger = Logger.getLogger(SelectVideoAction.class);
	
	public SelectVideoAction(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("selectvideo");
    }

	@Override
	public boolean startAction(String element) throws Exception {
		return clickOnIndependentElement(element);
	}

}
