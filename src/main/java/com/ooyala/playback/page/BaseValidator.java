package com.ooyala.playback.page;

import org.openqa.selenium.WebDriver;

public abstract class BaseValidator extends PlayBackPage implements
		PlaybackValidator {

	public BaseValidator(WebDriver webDriver) {
		super(webDriver);
	}

}
