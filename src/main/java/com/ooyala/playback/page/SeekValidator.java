package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;

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
		
		return
				PlayBackFactory.getInstance(driver).getSeekAction().seekTillEnd().startAction()
				&& waitOnElement(By.id(element), timeout);
	}

}
