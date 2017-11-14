package com.ooyala.playback.apps.actions;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

public class PauseAction extends PlaybackApps implements Actions {

	private static Logger logger = Logger.getLogger(PauseAction.class);

	public PauseAction(AppiumDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("playpause");
	}

	@Override
	public boolean startAction(String element) throws Exception {
		if (getPlatform().equalsIgnoreCase("android")) {
			try {
				if (!getPause(element)) {
					logger.error("Unable to get the element");
					extentTest.log(LogStatus.FAIL, "Unable to get the element");
					return false;
				}
			} catch (Exception e) {
				logger.info("Play button not found. Tapping screen and retrying..");
				tapOnScreen();
				if (!getPause(element)) {
					logger.error("Unable to click>>>>>>>>>>>> on pause.");
					extentTest.log(LogStatus.FAIL, "Unable to click on pause.");
					return false;
				}
			}
			return true;
		} else {
			if (isV4)
				return performPlayPauseV4(element);
			return performPlayPauseV3iOS(element);
		}
	}
	
	private boolean performPlayPauseV4(String element) {
		logger.info("Perform Play/Pause action on V4 skin");
		if (!waitOnElement(element, 1000)) {
			logger.error("Unable to get the Play/Pause element");
			extentTest.log(LogStatus.FAIL, "Unable to get the Play/Pause element");
			return false;
		}
		WebElement button = getWebElement(element);
		if(playCoordinates[0] ==null)
			playCoordinates[0]= button.getLocation().getX() + button.getSize().getWidth() / 2;
		if(playCoordinates[1] ==null)
			playCoordinates[1]= button.getLocation().getY() + button.getSize().getHeight() / 2;
		logger.info("Tap position : X >> " + playCoordinates[0] + " Y >> : " + playCoordinates[1]);
		TouchAction action = new TouchAction(driver);
		if(!isElementPresent("MORE_OPTIONS_V4_IOS"))
			action.tap(button).perform();
		action.tap(playCoordinates[0], playCoordinates[1]).perform();
		return true;
	}
	
	private boolean performPlayPauseV3iOS(String element) {
		try {
			if(getPlatformVersion().startsWith("11"))
				element += "_IOS11";
			if(!isElementPresent(element))
				tapScreen();
			if (!clickOnIndependentElement(element)) {
				logger.error("Unable to click on play/pause.");
				extentTest.log(LogStatus.FAIL, "Unable to click on play/pause.");
				return false;
			}
		} catch (Exception e) {
			logger.info("Play button not found. Tapping screen and retrying..");
			tapScreenIfRequired();
			if (!clickOnIndependentElement(element)) {
				logger.error("Unable to click on play/pause.");
				extentTest.log(LogStatus.FAIL, "Unable to click on play/pause.");
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean tapScreenIfRequired() {
		if (isV4) {
			if (!isElementPresent("MORE_OPTIONS_V4_IOS")) {
				return tapScreen();
			}
		} else {
			if (!isElementPresent("IOS_TOOLBAR_V3")) {
				return tapScreen();
			}
		}
		return true;
	}

	@Override
	public boolean tapScreen() {
		try {
			if (isV4){
				TouchAction tapV4 = new TouchAction(driver);
				WebElement el = getWebElement("PLAY_PAUSE_BUTTON_V4_IOS");
				tapV4.tap(el).perform();
			}else{
				if (!clickOnIndependentElement("IOS_TAP_V3")) {
					logger.error("Unable to click on play/pause.");
					extentTest.log(LogStatus.FAIL, "Unable to click on play/pause.");
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to tap the element");
			extentTest.log(LogStatus.FAIL, "Unable to tap the element");
			return false;
		}
		return true;
	}

	@Override
    public boolean waitAndTap() throws InterruptedException {
        Thread.sleep(5000);
        return tapScreen();
    }
}
