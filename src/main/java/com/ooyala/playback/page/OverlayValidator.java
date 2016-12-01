package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
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

	public boolean validate(String element, int timeout) throws Exception {
		try {
			if(!waitOnElement("OVERLAY_CLOSE_BTN", 40000)) return false;
			extentTest.log(LogStatus.PASS, "Overlay Shown");
			if(!clickOnIndependentElement("OVERLAY_CLOSE_BTN")) return false;
			if(!waitOnElement(By.id(element), timeout)) return false;

		} catch (Exception e) {
			logger.info("No close button for Overlay");
			logger.info("No close button seen in normal screen on Overlay....trying in Fullscreen \n");
			FullScreenAction fullScreenAction = PlayBackFactory.getInstance(
					driver).getFullScreenAction();
			if(!fullScreenAction.startAction()) return false;

			if (!getBrowser().equalsIgnoreCase("safari")
					&& !getPlatform().equalsIgnoreCase("Android")) {
				if(!waitOnElement("OVERLAY_CLOSE_BTN", 40000)) return false;
				if(!clickOnIndependentElement("OVERLAY_CLOSE_BTN")) return false;
				logger.info("Clicked on overlay close button in fullscreen screen \n");
				logger.info("Overlay gets closed");
			}

			if(!waitOnElement(By.id(element), timeout)) return false;

			if (getBrowser().equalsIgnoreCase("safari")) {
				if(!clickOnIndependentElement("NORMAL_SCREEN")) return false;
			} else {
				try {
					
					if(!PlayBackFactory.getInstance(driver).getSeekAction().setTime(15).startAction()) return false;
					
				} catch (Exception e1) {
					if(!clickOnHiddenElement("NORMAL_SCREEN")) return false;
				}
			}

		}
		return true;

	}

}
