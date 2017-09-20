package com.ooyala.playback.apps.validators;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;

import com.google.common.base.Function;
import com.ooyala.playback.apps.PlaybackApps;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

public class DiscoveryValidator extends PlaybackApps implements Validators {

	private static Logger logger = Logger.getLogger(DiscoveryValidator.class);

	public DiscoveryValidator(AppiumDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("discovery");
	}

	int eventVerificationCount = 0;

	// Function to verify the discovery screen and click an asset

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		Boolean result = true;

		try {
		    result = result && !isElementFoundinDiscoveryScreen("DISCOVERY_ERROR_SCREEN_ANDROID");
			result = result && isElementFoundinDiscoveryScreen("DISCOVERTEXT_ANDROID");
			result = result && isElementFoundinDiscoveryScreen("DISCOVERYICON_ANDROID");
			result = result && isElementFoundinDiscoveryScreen("DISCOVERYSCREEN_CLOSEBUTTON_ANDROID");
			result = result && isElementFoundinDiscoveryScreen("DISCOVERYSCROLLSCREEN_ANDROID");
			result = result && selectVideoFromDiscoveryScreen();

			return result;

		} catch (Exception e) {
			logger.info("Exception while validating discovery)");
			return false;
		}

	}

	// Function to select a video from discovery screen

	public Boolean selectVideoFromDiscoveryScreen() {
		Boolean result = true;
		try

		{

			List<WebElement> assets = getWebElementsList("DISCOVERYSCREENVIDEOS_ANDROID");

			if (assets.size() == 0) {
				logger.info("No Videos in Discovery Screen");
				result = false;
			} else {
				assets.get(0).click();

				Thread.sleep(3000);

				result = result && !isElementFoundinDiscoveryScreen("DISCOVERYSCREEN_CLOSEBUTTON_ANDROID");

			}
		}

		catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception while selecting a video from discovery screen");
			return result;
		}

		return result;
	}

	// Function to swipe the screen bottom to top and select a video from
	// discovery screen

	public Boolean swipeAndselectVideoFromDiscoveryScreen() {
		Boolean result = true;

		result = result && waitForElement("DISCOVERYSCROLLSCREEN_ANDROID");

		try

		{

			if (result) {

				List<WebElement> assets = getWebElementsList("DISCOVERYSCREENVIDEOS_ANDROID");

				swipeBasedOnWebElements(assets.get(assets.size() - 1), assets.get(1));

				assets = getWebElementsList("DISCOVERYSCREENVIDEOS_ANDROID");

				if (assets.size() == 0) {
					logger.info("No Videos in Discovery Screen");
					result = false;
				} else {
					assets.get(0).click();

					Thread.sleep(3000);

					result = result && !isElementFoundinDiscoveryScreen("DISCOVERYSCREEN_CLOSEBUTTON_ANDROID");

				}

			} else {

				logger.info("Discovery Scroll Screen Not Found");
				return result;
			}

		}

		catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception while selecting a video from discovery screen");
			return false;
		}

		return result;
	}

	// Function to check if an element is present in discovery screen

	public Boolean isElementFoundinDiscoveryScreen(String element) {
		try {

			if (waitOnElement(element, 5000)) {
				WebElement elementToFind = getWebElement(element);
				if (elementToFind.isEnabled()) {
					extentTest.log(LogStatus.PASS, element + " is present in Discovery Screen");
					logger.info(element + " is present in Discovery Screen");
					return true;
				} else {
					extentTest.log(LogStatus.FAIL, element + " not present Discovery Screen");
					logger.info("Asset List not  Found");
					return false;

				}
			} else {
				extentTest.log(LogStatus.FAIL, element + " not present Discovery Screen");
				logger.info("Asset List not  Found");
				return false;

			}

		} catch (Exception e) {

			e.printStackTrace();
			logger.info("Failed to locate Element");
			extentTest.log(LogStatus.FAIL, element + " Failed to locate Element");
			return false;

		}
	}

	public Boolean waitForElement(String element) {
		Boolean result = false;
		try {

			if (waitOnElement(element, 15000)) {
				WebElement elementToFind = getWebElement(element);
				if (elementToFind.isEnabled()) {
					extentTest.log(LogStatus.INFO, "Asset List Found");
					logger.info("Asset List Found");
					result = true;
				} else {
					extentTest.log(LogStatus.FAIL, "Asset List not Found");
					logger.info("Asset List not  Found");
					result = false;

				}
			} else {
				extentTest.log(LogStatus.FAIL, "Asset List not Found");
				logger.info("Asset List not  Found");
				result = false;
			}
		}

		catch (Exception e) {

			e.printStackTrace();
			extentTest.log(LogStatus.FAIL, "Asset List not Found");
			logger.info("Asset List not Found");
			result = false;

		}
		return result;
	}

	// To Swipe based on WebElements - UpDown (OR) RightLeft
	public void swipeBasedOnWebElements(WebElement start, WebElement end) {
		TouchAction a2 = new TouchAction((AppiumDriver) driver);
		a2.longPress(start).moveTo(end).release().perform();
	}

}
