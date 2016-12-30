package com.ooyala.playback.page;

import static java.lang.Thread.sleep;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;

public class FullScreenValidator extends PlayBackPage implements
		PlaybackValidator {

	public static Logger logger = Logger.getLogger(FullScreenValidator.class);
	private FCCValidator fccValidator;

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

		if (!PlayBackFactory.getInstance(driver).getFullScreenAction()
				.startAction())
			return false;

		Thread.sleep(3000);
		// if(!clickOnIndependentElement("PAUSE_BUTTON")) return false;
		sleep(2000);
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

	public boolean getFullscreen() {
		try {
			// PBW-5165 we are not verifying fullscreen change event for safari and
			// firefox browser as fullscreen is not working in safari in automation
			if (!(getBrowser().equalsIgnoreCase("safari") || getBrowser().equalsIgnoreCase("internet explorer")
					|| (getBrowser().equalsIgnoreCase("firefox") && getPlatform().equalsIgnoreCase("mac")))){
				if (!clickOnIndependentElement("FULLSCREEN_BTN_1")) {
					return false;
				}
				Thread.sleep(2000);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean getNormalscreen() {
		try{
			// PBW-5165 we are not verifying fullscreen change event for safari and
			// firefox browser as fullscreen is not working in safari in automation
			if (!(getBrowser().equalsIgnoreCase("safari") || getBrowser().equalsIgnoreCase("internet explorer")
					|| (getBrowser().equalsIgnoreCase("firefox") && getPlatform().equalsIgnoreCase("mac")))){
				if (!clickOnIndependentElement("NORMAL_SCREEN"))
					return false;
			}
			return true;
		} catch (Exception e){
			return false;
		}
	}



}
