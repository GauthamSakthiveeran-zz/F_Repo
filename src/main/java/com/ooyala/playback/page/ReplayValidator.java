package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/17/16.
 */
public class ReplayValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(ReplayValidator.class);

	public ReplayValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("replay");
	}

	public boolean validate(String element, int timeout) throws Exception {

		if (waitOnElement("END_SCREEN", 60000) && waitOnElement("REPLAY", 60000) && clickOnIndependentElement("REPLAY")
				&& waitOnElement(By.id(element), timeout)) {
			extentTest.log(LogStatus.PASS, "Replay Successful");
			return true;
		}
		return false;

	}
}