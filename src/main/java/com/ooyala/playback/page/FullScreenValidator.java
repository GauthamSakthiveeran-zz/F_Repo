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

	public FullScreenValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("fullscreen");
		addElementToPageElements("pause");

	}

	public void validate(String element, int timeout) throws Exception {

		PlayBackFactory.getInstance(driver).getFullScreenAction().startAction();

		Thread.sleep(3000);
		clickOnIndependentElement("PAUSE_BUTTON");
		sleep(2000);
		clickOnIndependentElement("NORMAL_SCREEN");

		// PBW-5165 we are not verifying fullscreen change event for safari and
		// firefox browser as fullscreen is not working in safari in automation
		if (!(getBrowser().equalsIgnoreCase("safari")
				|| getBrowser().equalsIgnoreCase("firefox")
				|| getBrowser().equalsIgnoreCase("internet explorer") || getPlatform()
				.equalsIgnoreCase("Android"))) {
			waitOnElement(By.id("fullscreenChangedfalse"), 60);
			logger.info("Video changed to normal size");
		}
	}
}
