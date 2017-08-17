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

	private void errorDescription() {
		if (isElementPresent("ERROR_SCREEN")) {
			String text = getWebElement("ERROR_DESCRIPTION").getText();
			extentTest.log(LogStatus.FAIL, text);
		} else {
			extentTest.log(LogStatus.FAIL, "Player did not load.");
		}

	}

	@Override
	public boolean waitForPage() {
		boolean errorScreen = false;
		try {

			if (!loadingSpinner()) {
				extentTest.log(LogStatus.FAIL, "In loading spinner for a very long time.");
				return false;
			}

			if (!waitOnElement("PLAY_BUTTON", 90000)) {
				errorDescription();
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			driver.navigate().refresh();
			if (!waitOnElement("INNER_WRAPPER", 60000))
				return false;
			errorScreen = isElementPresent("ERROR_SCREEN");
			if (errorScreen)
				driver.navigate().refresh();
			if (!waitOnElement("PLAY_BUTTON", 60000)) {
				errorDescription();
				return false;
			}
		}
		logger.info("Page is loaded completely");
		return true;
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
		boolean flag = false;
		if(!waitOnElement(element, 10000)) {
			logger.info(element+"  button is not visible on control bar");
			extentTest.log(LogStatus.INFO, element+" button is not visible on control bar");
			flag = false;
		}
		else {
			logger.info(element+" button is visible on control bar");
			extentTest.log(LogStatus.INFO, element+" button is visible on control bar");
			flag = true;			
		}
			
		return flag;
		
	}
    public boolean playButtonVisibleOnStartScreen() {
      	boolean flag = true;
		if(!waitOnElement("PLAY_BUTTON", 10000)) {
			logger.info("Play button is not visible on  start screen");
			extentTest.log(LogStatus.INFO, "PLAY button is not visible on start screen");
			flag = false;
		}
		else {
			logger.info("Play button is visible on  screen");
			extentTest.log(LogStatus.INFO, "PLAY button is visible on start screen");
			flag = true;
		}
		return flag;
	}
	
	public boolean pauseButtonVisibleOnPauseScreen() {
		boolean flag = false;
		if(!waitOnElement("PAUSE_BUTTON", 10000)) {
			logger.info("Pause button is not visible on  pause screen");
			extentTest.log(LogStatus.INFO, "PAUSE button is not visible on pause screen");
			flag = false;
		}
		else {
			logger.info("Pause button is visible on  screen");
			extentTest.log(LogStatus.INFO, "PAUSE button is visible on pause screen");
			flag = true;		
		}
		return flag;
	}
	
	
	public boolean replayButtonVisibleOnEndScreen() {
		boolean flag = true;
		if(!waitOnElement("REPLAY", 10000)) {
			logger.info("Replay  button is not visible on end screen");
			extentTest.log(LogStatus.INFO, "REPLAY button is not visible on end screen");
			flag=false;
		}
		else {
			logger.info("Replay  button is visible on end screen");
			extentTest.log(LogStatus.INFO, "REPLAY button is visible on end screen");
			flag=true;
		}
		return flag;
	}

	
	
	
}