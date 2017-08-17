package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

public class PlayerSkinButtonsValidator extends PlayBackPage{

	private static Logger logger = Logger.getLogger(PlayValidator.class);

	public PlayerSkinButtonsValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("play");
		addElementToPageElements("pause");
		addElementToPageElements("controlbar");
		addElementToPageElements("replay");
	}
		

	
	public boolean validateControlbarButtonsNotPresent() throws Exception {
		// if(!PlayBackFactory.getInstance(driver).getPlayAction().startAction())
		// return false;
		boolean flag = true;
		flag = flag && !verifyButtonVisibleOnControlBar("PLAY_PAUSE");
		flag = flag && !verifyButtonVisibleOnControlBar("VOLUME_BUTTON");
		flag = flag && !verifyButtonVisibleOnControlBar("DISCOVERY_BTN");
		flag = flag && !verifyButtonVisibleOnControlBar("CC_BTN");
		flag = flag && !verifyButtonVisibleOnControlBar("QUALITY_BTN");
		flag = flag && !verifyButtonVisibleOnControlBar("SHARE_BTN");
		flag = flag && !verifyButtonVisibleOnControlBar("FULLSCREEN_BTN");
		return flag;

	}
	
	public boolean verifyButtonVisibleOnControlBar(String element) {
		if(!waitOnElement(element, 10000)) {
			logger.info(element+"  button is not visible on control bar");
			extentTest.log(LogStatus.INFO, element+" button is not visible on control bar");
			return false;
		}
		else {
			logger.info(element+" button is visible on control bar");
			extentTest.log(LogStatus.INFO, element+" button is visible on control bar");
			return true;		
		}	
	}
	
    public boolean playButtonVisibleOnStartScreen() {
		if(!waitOnElement("PLAY_BUTTON", 10000)) {
			logger.info("Play button is not visible on  start screen");
			extentTest.log(LogStatus.INFO, "PLAY button is not visible on start screen");
			return false;
		}
		else {
			logger.info("Play button is visible on  screen");
			extentTest.log(LogStatus.INFO, "PLAY button is visible on start screen");
			return true;
		}
	}
	
	public boolean pauseButtonVisibleOnPauseScreen() {		
		if(!waitOnElement("PAUSE_BUTTON", 10000)) {
			logger.info("Pause button is not visible on  pause screen");
			extentTest.log(LogStatus.INFO, "PAUSE button is not visible on pause screen");
			return false;
		}
		else {
			logger.info("Pause button is visible on  screen");
			extentTest.log(LogStatus.INFO, "PAUSE button is visible on pause screen");
			return true;	
		}	
	}
	
	
	public boolean replayButtonVisibleOnEndScreen() {
		if(!waitOnElement("REPLAY", 10000)) {
			logger.info("Replay  button is not visible on end screen");
			extentTest.log(LogStatus.INFO, "REPLAY button is not visible on end screen");
			return false;
		}
		else {
			logger.info("Replay  button is visible on end screen");
			extentTest.log(LogStatus.INFO, "REPLAY button is visible on end screen");
			return true;
		}
	}
	
	public boolean verifyButtonVisibleInMoreOptions(String element) {
		boolean flag = false;
		if(!waitOnElement(element, 3000)) {
			logger.info("clicking on more options button");
			clickOnIndependentElement("MORE_OPTION");
			if(waitOnElement(element, 5000)) {
				logger.info(element+" is visible in more options screen");
				extentTest.log(LogStatus.PASS, element+" is visible in more options screen");
				flag = true;
			}
			else {
				logger.info(element+" is not visible in more option screen");
				extentTest.log(LogStatus.FAIL, element+" is not visible in more options screen");
			}
			//close the more options screen
			clickOnIndependentElement("CC_PANEL_CLOSE");
			
		}
		
		else {
			logger.info(element+" is visible in control bar");
			extentTest.log(LogStatus.FAIL, element+" is visible in control bar");
			flag = false;
		}
		
		return flag;
	}

	public boolean clickButtonInMoreOptions(String element) {
		if(waitOnElement("MORE_OPTION", 5000)) {
		    clickOnIndependentElement("MORE_OPTION");
		}
		else {
			return false;
		}
		if(waitOnElement(element, 5000)) {
			logger.info(element+" is visible in more options screen");
			extentTest.log(LogStatus.PASS, element+" is visible in more options screen");
			return clickOnIndependentElement(element);
		}
		else {
			logger.info(element+" is not visible in more option screen");
			extentTest.log(LogStatus.FAIL, element+" is not visible in more options screen");
			return false;
		}
	}
	
	
	
}