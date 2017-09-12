package com.ooyala.playback.apps.actions;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.ooyala.playback.apps.PlaybackApps;

import io.appium.java_client.AppiumDriver;

public class QAModeSwitchAction extends PlaybackApps implements Actions {

	public QAModeSwitchAction(AppiumDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("qamodeswitch");
	}

	@Override
	public boolean startAction(String element) throws Exception {
		if (!isQAModeEnabled())
    		return clickOnIndependentElement(element);
		return true;
	}
	
	private boolean isQAModeEnabled() {
    	return Boolean.parseBoolean(getWebElement("QA_MODE_SWITCH").getAttribute("value"));
    }

}
