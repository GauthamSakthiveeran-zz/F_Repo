package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by suraj on 6/22/17.
 */
public class UIControlValidator extends PlayBackPage implements PlaybackValidator {
	private static Logger logger = Logger.getLogger(UIControlValidator.class);

	public UIControlValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("volume");
		addElementToPageElements("play");
		addElementToPageElements("pause");
		addElementToPageElements("scrubber");
		addElementToPageElements("replay");
	}

	public boolean validate(String element, int timeout) throws Exception {
		if (!waitOnElement(By.id(element), timeout)) {
			logger.error(element + " Element not found");
			extentTest.log(LogStatus.FAIL, element + " Element not found");
			return false;
		}
		logger.info(element + " Element found");
		extentTest.log(LogStatus.PASS, element + " Element found");
		return true;
	}

	public boolean playAction(String element, int timeout) throws Exception {
		
		try {
			if(!clickOnIndependentElement("PLAY_BUTTON")) {
				extentTest.log(LogStatus.FAIL, "Click on PLAY_BUTTON failed.");
				return false;
			}
		} catch (Exception ex) {
			extentTest.log(LogStatus.FAIL, "Click on PLAY_BUTTON failed.", ex);
			return false;
		}
		
		return validate(element, timeout);
	}

	public boolean pauseAction(String element, int timeout) throws Exception {
		// WebElement pauseButton =
		// driver.findElement(By.className("oo-icon-pause-slick"));
		boolean flag;
		flag = switchToControlBar();
		if (flag) {
			try {
				if(!clickOnIndependentElement("PAUSE_BUTTON")) {
					extentTest.log(LogStatus.FAIL, "Click on PAUSE_BUTTON failed.");
					return false;
				}
			} catch (Exception ex) {
				extentTest.log(LogStatus.FAIL, "Click on PAUSE_BUTTON failed.", ex);
				return false;
			}
			return validate(element, timeout);
		}
		return flag;
	}

	public boolean validateVolumeMute(int expectedVolume) {
		try {
			if (!clickOnIndependentElement("VOLUME_MAX")) {
				extentTest.log(LogStatus.FAIL, "VOLUME_MAX not found");
				return false;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			extentTest.log(LogStatus.FAIL, "VOLUME_MAX not found", ex);
			return false;
		}
		return validateVolume(expectedVolume);
	}

	public boolean validateVolumeMax(int expectedVolume) {

		try {
			if (!clickOnIndependentElement("VOLUME_MUTE")) {
				extentTest.log(LogStatus.FAIL, "VOLUME_MUTE not found");
				return false;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			extentTest.log(LogStatus.FAIL, "VOLUME_MUTE not found", ex);
			return false;
		}

		return validateVolume(expectedVolume);
	}

	public boolean validateSeek(String element, int timeout) throws Exception {
		int seekPercent = 0;
		try {
			WebElement pointer = getWebElement("POINTER");
			seekPercent = (pointer.getSize().getWidth() * 2000 / 100);
			Actions action = new Actions(driver);
			action.dragAndDropBy(pointer, seekPercent, 0).build().perform();
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			extentTest.log(LogStatus.FAIL, ex);
			return false;
		}
		logger.info("Sucessfully seeked till :" + seekPercent);
		extentTest.log(LogStatus.INFO, "Sucessfully seeked till :" + seekPercent);
		return validate(element, timeout);
	}

	public boolean validateReplay(String element, int timeout) throws Exception {
		try {
			driver.executeScript("scroll(0, -250);");
			if(!clickOnIndependentElement("REPLAY")) {
				extentTest.log(LogStatus.FAIL, "Click REPLAY failed.");
				return false;
			}
		} catch (Exception ex) {
			extentTest.log(LogStatus.FAIL, "Click REPLAY failed.", ex);
			return false;
		}
		return validate(element, timeout);
	}

	private boolean validateVolume(int expectedVolume) {
		String volume = new PlayBackFactory(driver, extentTest).getPlayerAPIAction().getVolume();
		int volumeLevel = Integer.parseInt(volume);
		if (volumeLevel != expectedVolume) {
			logger.error("Actual Volume :" + volumeLevel + " and Expected Volume :" + expectedVolume + " do not match");
			extentTest.log(LogStatus.FAIL,
					"Actual Volume :" + volumeLevel + " and Expected Volume :" + expectedVolume + " do not match");
			return false;
		}
		logger.info("Actual Volume :" + volumeLevel + " and Expected Volume :" + expectedVolume + " do  match");
		extentTest.log(LogStatus.PASS,
				"Actual Volume :" + volumeLevel + " and Expected Volume :" + expectedVolume + " do match");
		return true;
	}
}
