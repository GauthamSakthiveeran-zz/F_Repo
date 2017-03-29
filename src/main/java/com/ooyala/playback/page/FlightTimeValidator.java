package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by suraj on 3/23/17.
 */
public class FlightTimeValidator extends PlayBackPage implements PlaybackValidator {

	private static Logger logger = Logger.getLogger(FlightTimeValidator.class);

	public FlightTimeValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("play");
	}

	public boolean validate(String element, int timeout) throws Exception {
		//TODO
		return true;
	}

}
