package com.ooyala.playback.page.action;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.page.PlayBackPage;

/**
 * Created by soundarya on 11/16/16.
 */
public class AutoplayAction extends PlayBackPage implements PlayerAction {

	public AutoplayAction(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("play");
	}

	@Override
	public void startAction() {
		Boolean autoplay = false;
		try {
			autoplay = Boolean.parseBoolean(((JavascriptExecutor) driver)
					.executeScript("return pp.parameters.autoplay").toString());
		} catch (Exception e) {
			logger.error("Autoplay not set for this video");
		}
		if (!autoplay) {
			waitOnElement("PLAY_BUTTON", 60);
			clickOnIndependentElement("PLAY_BUTTON");
		}
	}
}
