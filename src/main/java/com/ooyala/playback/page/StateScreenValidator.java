package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

public class StateScreenValidator extends PlayBackPage implements PlaybackValidator{
	
	public StateScreenValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("pause");
	}

	private static Logger logger = Logger.getLogger(StateScreenValidator.class);
	
	
	public boolean validateTitle(String title) {
		if(isElementPresent("STATE_SCREEN_TITLE")) {
			if(!getWebElement("STATE_SCREEN_TITLE").getText().equals(title)) {
				extentTest.log(LogStatus.FAIL, "Title is incorrect");
				return false;
			}
			return true;
		}
		extentTest.log(LogStatus.FAIL, "State screen title not found");
		return false;
	}
	
	public boolean validateDescription(String desc) {
		if(isElementPresent("STATE_SCREEN_DESCRIPTION")) {
			if(!getWebElement("STATE_SCREEN_DESCRIPTION").getText().equals(desc)) {
				extentTest.log(LogStatus.FAIL, "Desc is incorrect");
				return false;
			}
			return true;
		}
		extentTest.log(LogStatus.FAIL, "State screen desc not found");
		return false;
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		
		return false;
	}

}
