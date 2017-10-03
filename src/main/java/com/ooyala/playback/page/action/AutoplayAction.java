package com.ooyala.playback.page.action;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.page.PlayBackPage;

/**
 * Created by soundarya on 11/16/16.
 */
public class AutoplayAction extends PlayBackPage implements PlayerAction {

	private static Logger logger = Logger.getLogger(AutoplayAction.class);

	public AutoplayAction(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("play");
	}

	@Override
	public boolean startAction() {
		Boolean autoplay = false;
		try {
			// Adding below piece of code specifically to handle safari failure,
			// As immidiately after playing the video sometimes javascript
			// executor won't work.
			if (getBrowser().equalsIgnoreCase("safari"))
				waitOnElement("playing_1", 10000);

			autoplay = new PlayBackFactory(driver, extentTest).getPlayerAPIAction().isAutoPlay();
			logger.info("auto-play is set for video");
		} catch (Exception e) {
			logger.error("Autoplay not set for this video : " + e.getMessage());
		}
		if (!autoplay) {

			if (!new PlayBackFactory(driver, extentTest).getPlayValidator().waitForPage())
				return false;

			if (waitOnElement("PLAY_BUTTON", 60000)) {
				if (clickOnIndependentElement("PLAY_BUTTON")) {
					return true;
				}
			}
			return false;
		}
		return autoplay;
	}
}
