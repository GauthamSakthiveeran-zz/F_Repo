package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 10/27/16.
 */
public class SeekValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(SeekValidator.class);

	public SeekValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("play");
	}

	public boolean validate(String element, int timeout) throws Exception {

		PlayBackFactory.getInstance(driver).getSeekAction().seekTillEnd().startAction();

		if (waitOnElement(By.id(element), timeout)) {
			extentTest.log(LogStatus.PASS, "Seek successfull.");
			return true;
		}
		extentTest.log(LogStatus.FAIL, "Wait on " + element + " failed after "+ timeout + " ms");
		return false;
	}

}
