package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.page.action.FullScreenAction;
import com.relevantcodes.extentreports.LogStatus;

public class OverlayValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(OverlayValidator.class);

	public OverlayValidator(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("adoverlay");
		addElementToPageElements("fullscreen");
	}
	
	private boolean checkInFullScreen(String element, int timeout) throws Exception{
		
		logger.info("No close button for Overlay");
		extentTest.log(LogStatus.INFO, "No close button seen in normal screen on Overlay....trying in Fullscreen.");
		
		FullScreenAction fullScreenAction = PlayBackFactory.getInstance(driver).getFullScreenAction();
		
		if (!fullScreenAction.startAction())
			return false;

		if (!getBrowser().equalsIgnoreCase("safari") && !getPlatform().equalsIgnoreCase("Android")) {
			if (!waitOnElement("OVERLAY_CLOSE_BTN", 40000))
				return false;
			if (!clickOnIndependentElement("OVERLAY_CLOSE_BTN"))
				return false;
			logger.info("Clicked on overlay close button in fullscreen screen \n");
			logger.info("Overlay gets closed");
		}

		if (!waitOnElement(By.id(element), timeout))
			return false;

		return true;
	}

	public boolean validate(String element, int timeout) throws Exception {
		
		if (!isElementPresent("OVERLAY_CLOSE_BTN")) {
			return checkInFullScreen(element, timeout);
		}

		if (!waitOnElement("OVERLAY_CLOSE_BTN", 20000))
			return false;
		
		if (!clickOnIndependentElement("OVERLAY_CLOSE_BTN"))
			return false;
		
		if (!waitOnElement(By.id(element), timeout))
			return false;
		
		return true;

	}

}
