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
        addElementToPageElements("selectvideo_"+getPlatform());
        System.out.println("platform:"+getPlatform());
    }

	@Override
	public boolean startAction(String element) throws Exception {
		if(!waitOnElement(element,1000)){
			logger.info("Element Not Found");
			return false;
		}
		return clickOnIndependentElement(element);
	}

}
