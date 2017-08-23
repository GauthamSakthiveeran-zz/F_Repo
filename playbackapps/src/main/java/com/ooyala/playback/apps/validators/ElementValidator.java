package com.ooyala.playback.apps.validators;

import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;

import io.appium.java_client.AppiumDriver;

public class ElementValidator extends PlaybackApps implements Validators {

	public ElementValidator(AppiumDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("elements");
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		return waitOnElement(element, timeout);
	}

}
