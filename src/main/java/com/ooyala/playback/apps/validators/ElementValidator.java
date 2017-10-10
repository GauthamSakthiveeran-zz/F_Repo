package com.ooyala.playback.apps.validators;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.actions.PauseAction;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;

public class ElementValidator extends PlaybackApps implements Validators {
	
	private static Logger logger = Logger.getLogger(PauseAction.class);

	public ElementValidator(AppiumDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("elements");
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		return waitOnElement(element, timeout);
	}
	
	public boolean clickOnElement(String element) throws Exception {
		return clickOnIndependentElement(element);
	}

	public boolean validateElementDisappeared(String element, int timeoutSec) throws Exception {
	    int count =0;
        while(isElementPresent(element)){
        	TimeUnit.SECONDS.sleep(1);
        	count++;
        	if(count>=timeoutSec){
        		logger.info("Element Did not Disappeared");
        		extentTest.log(LogStatus.FAIL, "Element Did not Disappeared");
        		return false;
        	}

        }
        logger.info("Element Disappeared");
        extentTest.log(LogStatus.PASS, "Element Disappeared");
		return true;
	}
}
