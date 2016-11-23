package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.page.action.FullScreenAction;

public class OverlayValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(OverlayValidator.class);

	public OverlayValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("adoverlay");
		addElementToPageElements("fullscreen");
	}

	public void validate(String element, int timeout) throws Exception {
		try {
			waitOnElement("overlayCloseBtn", 40);
			clickOnIndependentElement("overlayCloseBtn");
			waitOnElement(By.id(element), timeout);

		} catch (Exception e) {
			logger.info("No close button for Overlay");
			logger.info("No close button seen in normal screen on Overlay....trying in Fullscreen \n");
			FullScreenAction fullScreenAction = PlayBackFactory.getInstance(
					driver).getFullScreenAction();
			fullScreenAction.startAction();

			if (!getBrowser().equalsIgnoreCase("safari")
					&& !getPlatform().equalsIgnoreCase("Android")) {
				waitOnElement("overlayCloseBtn", 40);
				clickOnIndependentElement("overlayCloseBtn");
				logger.info("Clicked on overlay close button in fullscreen screen \n");
				logger.info("Overlay gets closed");
			}
			Thread.sleep(1000);
			waitOnElement("nonlinearAdPlayed_1", 160);

			if (getBrowser().equalsIgnoreCase("safari")) {
				clickOnIndependentElement("normalScreen");
			} else {
				try {
					PlayBackFactory.getInstance(driver).getSeekAction()
							.seek("15");
				} catch (Exception e1) {
					clickOnHiddenElement("normalScreen");
				}
			}

		}

	}

}
