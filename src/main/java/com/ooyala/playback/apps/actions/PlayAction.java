package com.ooyala.playback.apps.actions;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

public class PlayAction extends PlaybackApps implements Actions {

	private static Logger logger = Logger.getLogger(PlayAction.class);
	private PauseAction tapActions;

	public PlayAction(AppiumDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("playpause");
		tapActions = new PlayBackFactory(driver, extentTest).getPauseAction();
	}

	@Override
	public boolean startAction(String element) throws Exception {
		if (getPlatform().equalsIgnoreCase("ios"))
			return ios(element);
		return android(element);
	}

	private boolean ios(String element) throws Exception {
		if (isV4) {
			if (!waitOnElement(element, 5000)) {
				logger.error("Unable to get Play Element");
				extentTest.log(LogStatus.FAIL, "Unable to get the Play element");
				return false;
			}
			if (!getPlayPause(element)) {
				logger.error("Unable to get the element");
				extentTest.log(LogStatus.FAIL, "Unable to get the element");
				return false;
			}
		} else {
			try {
				if (getPlatformVersion().startsWith("11")) {
					element = element + "_IOS11";
				}
				if(!waitOnElement(element, 1000))
					tapActions.tapScreen();
				if (!clickOnIndependentElement(element)) {
					if(!waitOnElement(element, 1000))
						tapActions.tapScreen();
					if (!clickOnIndependentElement(element)) {
						logger.error("Unable to click on play pause.");
						extentTest.log(LogStatus.FAIL, "Unable to click on play pause.");
						return false;
					}
				}
			} catch (Exception e) {
				logger.info("Play button not found. Tapping screen and retrying..");
				if (!tapActions.tapScreen()) {
					extentTest.log(LogStatus.FAIL, "tapActions.tapScreen failed.");
					return false;
				}
				if (!clickOnIndependentElement(element)) {
					extentTest.log(LogStatus.FAIL, "Unable to click on play pause.");
					logger.error("Unable to click on play pause.");
					return false;
				}
			}
		}
		return true;
	}

	private boolean android(String element) throws Exception {
		if(waitOnElement(element, 15000)) {
			logger.info("play button is visible on player");
			extentTest.log(LogStatus.PASS, "play button is visible on player");
			
		} else {	
			logger.error("play button is not visibe");
			extentTest.log(LogStatus.FAIL, "play button is not visible on player");
			return false;
		}
		try {
			if (!getPlayPause(element)) {
				extentTest.log(LogStatus.FAIL, "Unable to get the element");
				logger.error("Unable to get the element");
				return false;
			}
		} catch (Exception e) {
			logger.info("Play button not found. Tapping screen and retrying..");
			tapOnScreen();
			extentTest.log(LogStatus.INFO, "Play button not found. Tapping screen and retrying..");
			if (!getPlayPause(element)) {
				extentTest.log(LogStatus.FAIL, "Unable to get the element");
				logger.error("Unable to click on play pause.");
				return false;
			}
		}
		return true;
	}
	
	public boolean createVideo(String element,int timeout) {
		if(waitOnElement(element, timeout)) {
			clickOnIndependentElement("CREATE_VIDEO");
			logger.info("clicked on create video button");
			extentTest.log(LogStatus.INFO, "clicked on create video button");
			if(waitOnElement("PLAY_PAUSE_ANDROID", 150000)) {
				logger.info("play button is visible..");
				extentTest.log(LogStatus.INFO, "play button is visible");
				return true;
			}
			
		} else {
			logger.error("create video button is not visible");
			extentTest.log(LogStatus.INFO, "create video button is not visible");
		}
		return false;
	}
	
	public boolean playPauseAd(String element) throws Exception  {
		Thread.sleep(2000);
		logger.info("tapping on  ad screen");
		tapOnScreen();	
		logger.info("verify if the ad control bar is shown");
		if(waitOnElement(element, 5000)) {
			logger.info("ad control bar is shown");
			clickOnElement(element);
			return true;
		} else {
			logger.info("ad control bar is not shown..tapping screen and retrying");
			tapOnScreen();
			if(waitOnElement(element, 5000)) {
				logger.info("ad control bar is shown");
				clickOnElement(element);
				return true;
			}
			return false;
		}

	}
	

}
