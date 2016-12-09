package com.ooyala.playback.page.action;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.page.PlayBackPage;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlayPauseAction extends PlayBackPage implements PlayerAction {

	private Logger logger = Logger.getLogger(PlayPauseAction.class);

	public PlayPauseAction(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("play");
		addElementToPageElements("pause");

	}

	@Override
	public boolean startAction() {
		boolean ispause = isElementPresent("PLAY_BUTTON");

		logger.info("Video Paused" + ispause);
		if (ispause) {
			try {
				if (waitOnElement("PLAY_BUTTON", 10000)) {
					return clickOnIndependentElement("PLAY_BUTTON");
				} else {
					if (!waitOnElement("PAUSE_SCREEN", 10000))
						return false;
					return clickOnIndependentElement("PAUSE_SCREEN");
				}

			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return false;
	}
}