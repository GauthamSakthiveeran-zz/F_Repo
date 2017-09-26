package com.ooyala.playback.apps.validators;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;

public class OverlayValidator extends PlaybackApps implements Validators {
	private static Logger logger = Logger.getLogger(OverlayValidator.class);

	public OverlayValidator(AppiumDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("overlay");
	}

	// TODO- Verify if the overlay is clickable in app
	/**
	 * 
	 * @param element
	 *            - overlay element to be validated
	 * @param timeout
	 *            - time to wait before we fail the test
	 * @param seconds
	 *            - number of seconds overlay has to be shown
	 * @return
	 * @throws Exception
	 */
	public boolean validateOverlay(String element, int timeout, long seconds) throws Exception {
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime <= 1000 * seconds) {
			if (waitOnElement(element, timeout)) {
				logger.info("overlay ad seen");
				return true;
			} else {
				logger.error("Overlay ad not seen");
				return false;
			}

		}

		if (waitOnElement(element, timeout) && seconds == 0) {
			logger.info("overlay ad seen");
			return true;
		} else if (seconds != 0 && waitOnElement(element, 2000)) {
			logger.error("overlay still seen even after " + seconds + " seconds");
			return false;
		} else {
			logger.error("overlay ad not seen");
			return false;
		}
	}

	public boolean valdiateOverlay(String element, int timeout) throws Exception {
		return validateOverlay(element, timeout, 0);
	}

	private TestParameters test;

	public OverlayValidator setTestParameters(TestParameters test) {
		this.test = test;
		return this;
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {

		boolean result = true;
		PlayBackFactory playBackFactory = new PlayBackFactory(driver, extentTest);
		NotificationEventValidator notificationEventValidator = playBackFactory.getNotificationEventValidator();
		PauseAction pauseAction = playBackFactory.getPauseAction();
		SeekAction seekAction = playBackFactory.getSeekAction();

		boolean iOS = getPlatform().equalsIgnoreCase("ios");

		if (!waitOnElement(element, timeout)) {
			extentTest.log(LogStatus.FAIL, "Overlay not present");
			return false;
		}

		if (test.getAsset().contains("PRE")) {
			result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
			result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
		}

		result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);

		result = result && notificationEventValidator.letVideoPlayForSec(3);

		if (test.getAsset().contains("PRE"))
			result = result && waitForOverlayToDisapper(element, 30000);

		if (test.getAsset().contains("MID")) {
			if (!waitOnElement(element, timeout)) {
				extentTest.log(LogStatus.FAIL, "Overlay not present");
				return false;
			}
			result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
			result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
			result = result && waitForOverlayToDisapper(element, 30000);
		}

		result = result && iOS ? pauseAction.startAction("PLAY_PAUSE_BUTTON")
				: pauseAction.startAction("PLAY_PAUSE_ANDROID");
		result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_PAUSED, 25000);
		result = result && iOS ? seekAction.setSlider("SLIDER").startAction("SEEK_BAR")
				: seekAction.startAction("SEEK_BAR_ANDROID");
		result = result && notificationEventValidator.verifyEvent(Events.SEEK_STARTED, 40000);
		result = result && notificationEventValidator.verifyEvent(Events.SEEK_COMPLETED, 40000);
		result = result && iOS ? pauseAction.startAction("PLAY_PAUSE_BUTTON")
				: pauseAction.startAction("PLAY_PAUSE_ANDROID");
		result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 30000);

		if (test.getAsset().contains("POST")) {
			if (!waitOnElement(element, timeout)) {
				extentTest.log(LogStatus.FAIL, "Overlay not present");
				return false;
			}
			result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
			result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
		}

		return result;

	}

	private boolean waitForOverlayToDisapper(String element, int timeout) {
		logger.info("Waiting for Overlay to disappear....");
		waitOnElement(element, timeout);
		if (isElementPresent(element)) {
			extentTest.log(LogStatus.FAIL, "Overlay is still present");
			return false;
		} else {
			extentTest.log(LogStatus.PASS, "Overlay has disappeared.");
			return true;
		}
	}

}
