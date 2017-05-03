package com.ooyala.playback.page;

import com.ooyala.playback.factory.PlayBackFactory;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class FullScreenValidator extends PlayBackPage implements
		PlaybackValidator {

	public static Logger logger = Logger.getLogger(FullScreenValidator.class);

	public FullScreenValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("fullscreen");
		addElementToPageElements("pause");

	}

	public boolean validate(String element, int timeout) throws Exception {

		if (!(new PlayBackFactory(driver,extentTest)).getFullScreenAction().startAction())
			return false;

		// if(!clickOnIndependentElement("PAUSE_BUTTON")) return false;
		if (!clickOnIndependentElement("NORMAL_SCREEN"))
			return false;

		// PBW-5165 we are not verifying fullscreen change event for safari and
		// firefox browser as fullscreen is not working in safari in automation
		if (!(getBrowser().equalsIgnoreCase("safari")
				|| getBrowser().equalsIgnoreCase("firefox")
				|| getBrowser().equalsIgnoreCase("internet explorer") || getPlatform()
				.equalsIgnoreCase("Android"))) {
			return waitOnElement(By.id("fullscreenChanged_false"), 60000);
		} else {
			return true;
		}
	}

	public boolean getFullScreen() {
		if (!clickOnIndependentElement("FULLSCREEN_BTN_1")) {
			return false;
		}
			return true;
	}

	public boolean getNormalScreen() {
		if (!isElementPresent("FULLSCREEN_BTN_1")) {
		    waitOnElement("NORMAL_SCREEN",10000);
			if (!clickOnIndependentElement("NORMAL_SCREEN")) {
				return false;
			}
		}else {
			logger.info("clicked on fullscreen button but it does not go into fullscreen mode");
		}
		return true;
	}
}
