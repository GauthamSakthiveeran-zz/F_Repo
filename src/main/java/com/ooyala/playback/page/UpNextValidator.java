package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.page.action.PlayerAPIAction;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/1/16.
 */
public class UpNextValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(UpNextValidator.class);

	public UpNextValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("upnext");
	}

	public boolean validate(String element, int timeout) throws Exception {

		if (!loadingSpinner()) {
			extentTest.log(LogStatus.FAIL, "loading spinner appears for long time");
			return false;
		}

		try {
			// inc the timeout because of pulse ads.
			logger.info("checking up next");
			if (!waitOnElement("UPNEXT_CONTENT", 60000)) {
				logger.error("upnext is not visible");
				extentTest.log(LogStatus.FAIL, "upnext is not visible");
				return false;
			}

			if (!waitOnElement("CONTENT_METADATA", 10000)) {
				logger.error("content metadata is not visible");
				extentTest.log(LogStatus.FAIL, "content metadata is not visible");
				return false;
			}

			return clickOnIndependentElement("UPNEXT_CLOSE_BTN");
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("UpNext is not showing");
			extentTest.log(LogStatus.FAIL, "No Upnext panel");
			return false;
		}

	}

	public boolean autoPlayUpNextVideo() {
		PlayerAPIAction playerAPI = new PlayBackFactory(driver, extentTest).getPlayerAPIAction();
		int waitTime = 0;
		try {
			String playerState = playerAPI.getState();
			while ((playerState.equalsIgnoreCase("loading") || playerState.equalsIgnoreCase("paused"))
					&& waitTime < 10) {
				Thread.sleep(1000);
				waitTime++;
			}
			return playerAPI.isPlaying();
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, e.getMessage());
			logger.error(e.getMessage());
			e.printStackTrace();
			return false;
		}

	}
}
