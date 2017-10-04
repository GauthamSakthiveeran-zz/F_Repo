package com.ooyala.playback.apps.validators;


import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.utils.CommandLineParameters;
import com.relevantcodes.extentreports.LogStatus;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

/**
 * Created by Gautham
 */
public class ShareValidator extends PlaybackApps implements Validators {

	private static Logger logger = Logger.getLogger(ShareValidator.class);

	public ShareValidator(AppiumDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("share");
	}

	int eventVerificationCount = 0;

	// Function to verify the Share screen and various Social Media Shares

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		Boolean result = true;
		if (getPlatform().equalsIgnoreCase("android")) {
			try {

				result = result && isElementFoundinShareScreen("SHARETEXT_ANDROID");
				if (((AndroidDriver) driver).currentActivity().equals("com.android.internal.app.ChooserActivity")) {
					result = result && validateSocialMediaFaceBook("FACEBOOK_BUTTON_ANDROID");
					result = result && validateSocialMediaTwitter("TWITTER_BUTTON_ANDROID");
					result = result && validateSocialMediaGooglePlus("GOOGLEPLUS_BUTTON_ANDROID");
					return result;
				} else {
					logger.info("Not in Share Screen");
					extentTest.log(LogStatus.FAIL, "Not in Share Screen");
					return false;
				}

			} catch (Exception e) {
				logger.info("Exception while validating Share");
				extentTest.log(LogStatus.FAIL, "Exception while validating Share");
				return false;
			}
		} else {
			// Code for IOS
			return result;
		}

	}

	public boolean validateSocialMediaFaceBook(String element) {
		Boolean result = true;
		try {

			result = result && waitForElementAndClick(element);

			Thread.sleep(2000);

			if (((AndroidDriver) driver).currentActivity()
					.equals("com.facebook.katana.dbl.activity.FacebookLoginActivity")) {

				gotoBackScreenAndroid();

				logger.info("Facebook Login Screen Displayed Properly");
				extentTest.log(LogStatus.PASS, "Facebook Login Screen Displayed Properly");


			}

			else if (((AndroidDriver) driver).currentActivity()
					.equals("com.facebook.platform.composer.composer.PlatformComposerActivity"))

			{
				gotoBackScreenAndroid();

				result = result && waitForElementAndClick("FACEBOOKPOST_DISCARD_ANDROID");

				logger.info("Facebook Share Post Screen Displayed Properly");
				extentTest.log(LogStatus.PASS, "Facebook Share Post Screen Displayed Properly");

				Thread.sleep(2000);

			} else {
				logger.info("Error While verifying Facebook Share");
				extentTest.log(LogStatus.FAIL, "Error While verifying Facebook Share");

				return false;

			}
		} catch (Exception e) {
			logger.info("Exception while validating facebook share");
			extentTest.log(LogStatus.FAIL, "Exception while validating facebook share");
			return false;
		}

		return result;
	}

	public boolean postOnSocialMediaFaceBook(String element) {
		Boolean result = true;
		try {
			result = result && waitForElementAndClick(element);

			Thread.sleep(2000);

			if (((AndroidDriver) driver).currentActivity()
					.equals("com.facebook.platform.composer.composer.PlatformComposerActivity"))

			{

				result = result && waitForElementAndClick("FACEBOOKPOST_BUTTON_ANDROID");

				logger.info("Facebook Share Post Screen Displayed Properly");
				extentTest.log(LogStatus.PASS, "Facebook Share Post Screen Displayed Properly");

				Thread.sleep(2000);

			} else {
				logger.info("Error While verifying Facebook Share");
				extentTest.log(LogStatus.FAIL, "Error While verifying Facebook Share");

			}
		} catch (Exception e) {
			logger.info("Exception while posting video in facebook");
			extentTest.log(LogStatus.FAIL, "Exception while posting video in facebook");
			return false;
		}

		return result;
	}

	public boolean validateSocialMediaTwitter(String element) {
		Boolean result = true;
		try {

			result = result && waitForElementAndClick("SHAREBUTTON_ANDROID");

			Thread.sleep(2000);

			result = result && waitForElementAndClick(element);

			Thread.sleep(2000);

			if (((AndroidDriver) driver).currentActivity()
					.equals("com.twitter.app.onboarding.signup.SignUpSplashActivity")) {

				gotoBackScreenAndroid();

				logger.info("Twitter Login Screen Displayed Properly");
				extentTest.log(LogStatus.PASS, "Twitter Login Screen Displayed Properly");


			}

			else if (((AndroidDriver) driver).currentActivity().equals("com.twitter.composer.ComposerActivity"))

			{

				result = result && waitForElementAndClick("TWITTERPOST_CLOSEBUTTON_ANDROID");

				result = result && waitForElementAndClick("TWITTERPOST_DISCARD_ANDROID");

				logger.info("Twitter Share Post Screen Displayed Properly");
				extentTest.log(LogStatus.PASS, "Twitter Share Post Screen Displayed Properly");

				Thread.sleep(2000);

			} else {
				logger.info("Error While verifying Twitter Share");
				extentTest.log(LogStatus.FAIL, "Error While verifying Twitter Share");

				return false;

			}
		} catch (Exception e) {
			logger.info("Exception while validating Twitter share");
			extentTest.log(LogStatus.FAIL, "Exception while validating Twitter share");
			return false;
		}

		return result;
	}

	public boolean postOnSocialMediaTwitter(String element) {
		Boolean result = true;
		try {
			result = result && waitForElementAndClick(element);

			Thread.sleep(2000);

			if (((AndroidDriver) driver).currentActivity().equals("com.twitter.composer.ComposerActivity"))

			{

				result = result && waitForElementAndClick("TWIITERTWEET_BUTTON_ANDROID");

				logger.info("Twitter Share Post Screen Displayed Properly");
				extentTest.log(LogStatus.PASS, "Twitter Share Post Screen Displayed Properly");

				Thread.sleep(2000);

			} else {
				logger.info("Error While verifying Twitter Share");
				extentTest.log(LogStatus.FAIL, "Error While verifying Twitter Share");

			}
		} catch (Exception e) {
			logger.info("Exception while posting video in twitter");
			extentTest.log(LogStatus.FAIL, "Exception while posting video in twitter");
			return false;
		}

		return result;
	}

	public boolean validateSocialMediaGooglePlus(String element) {
		Boolean result = true;
		try {

			result = result && waitForElementAndClick("SHAREBUTTON_ANDROID");

			Thread.sleep(2000);

			result = result && waitForElementAndClick(element);

			Thread.sleep(2000);

			if (((AndroidDriver) driver).currentActivity()
					.equals("com.google.android.gms.plus.oob.UpgradeAccountActivity"))

			{

				gotoBackScreenAndroid();

				logger.info("Google+ Share Post Screen Displayed Properly");
				extentTest.log(LogStatus.PASS, "Google+ Share Post Screen Displayed Properly");

				Thread.sleep(2000);

			} else {
				logger.info("Error While verifying Google+ Share");
				extentTest.log(LogStatus.FAIL, "Error While verifying Google+ Share");

			}
		} catch (Exception e) {
			logger.info("Exception while posting video in Google+");
			extentTest.log(LogStatus.FAIL, "Exception while posting video in Google+");
			return false;
		}

		return result;
	}

	// Function to check if an element is present in share screen

	public Boolean isElementFoundinShareScreen(String element) {
		try {

			if (waitOnElement(element, 5000)) {
				WebElement elementToFind = getWebElement(element);
				if (elementToFind.isEnabled()) {
					extentTest.log(LogStatus.PASS, element + " is present in Share Screen");
					logger.info(element + " is present in Share Screen");
					return true;
				} else {
					extentTest.log(LogStatus.FAIL, element + " not present Share Screen");
					logger.info("Not in share Screen");
					return false;

				}
			} else {
				extentTest.log(LogStatus.FAIL, element + " not present Share Screen");
				logger.info("Not in share Screen");
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
					extentTest.log(LogStatus.INFO, "Element Found");
					logger.info("Element Found");
					result = true;
				} else {
					extentTest.log(LogStatus.FAIL, "Element Not Found");
					logger.info("Element Not Found");
					result = false;

				}
			} else {
				extentTest.log(LogStatus.FAIL, "Exception while trying to locate the element");
				logger.info("Exception while trying to locate the element");
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

	public boolean waitForElementAndClick(String element) {
		try {

			if (waitOnElement(element, 5000)) {
				WebElement elementToFind = getWebElement(element);
				elementToFind.click();
				logger.info("Clicked Element");
				extentTest.log(LogStatus.INFO, "Clicked Element");
				return true;
			} else {
				logger.info("Failed to Click Element");
				extentTest.log(LogStatus.FAIL, "Element not found");
				return true;

			}

		} catch (Exception e) {

			e.printStackTrace();
			logger.info("Failed to Click Element");
			extentTest.log(LogStatus.FAIL, "Failed to Click Element");
			return false;

		}
	}

	// To Swipe based on WebElements - UpDown (OR) RightLeft
	public void swipeBasedOnWebElements(WebElement start, WebElement end) {
		TouchAction a2 = new TouchAction((AppiumDriver) driver);
		a2.longPress(start).moveTo(end).release().perform();
	}

}
