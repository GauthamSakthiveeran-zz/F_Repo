package com.ooyala.playback.apps.validators;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;

public class ReplayValidator extends PlaybackApps implements Validators{
	
	public static Logger logger = Logger.getLogger(ReplayValidator.class);

	public ReplayValidator(AppiumDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("replay");
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		// TODO Auto-generated method stub
		if(waitOnElement("DISCOVERY",timeout)){
			//verify if the discovery screen is shown on End
			logger.info("Discovery screen is shown on End");
			extentTest.log(LogStatus.INFO, "Discovery screen is shown on End..closing it to click on replay");
			//TODO-closediscoveryscreen function
			//new PlayBackFactory(driver, extentTest).getClickDiscoveryButtonAction().closeDiscoveryScreen();
			return clickOnIndependentElement(element);
			
		} 
		else if(waitOnElement(element,timeout)) {
			logger.info("Replay button is found on screen");
			extentTest.log(LogStatus.INFO, "Replay button is found on screen");
			return clickOnIndependentElement(element);
		} else {
			logger.info("replay button is not visible on end screen");
			extentTest.log(LogStatus.INFO, "Replay button is not visible on end screen");
			return false;
			
		}
			
	}

}
