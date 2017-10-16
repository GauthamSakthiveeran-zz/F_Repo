package com.ooyala.playback.apps.actions;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;
import com.ooyala.playback.factory.PlayBackFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

public class CCAction extends PlaybackApps implements Actions {

	private static Logger logger = Logger.getLogger(CCAction.class);

	private String platform;
	private PauseAction tapActions;
	
	public CCAction(AppiumDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("cc");
		platform = getPlatform();
		tapActions = new PlayBackFactory(driver, extentTest).getPauseAction();
	}

	public boolean tapCC(String element) {

		if(!tapActions.tapScreenIfRequired()){
			return false;	
		}
		if (isV4) {
			try {
				if (!tapActions.clickOnIndependentElement("MORE_OPTIONS_V4_IOS"))
					return false;
//				TouchAction tapV4 = new TouchAction(driver);
//				WebElement el = getWebElement(element);
//				tapV4.tap(el).perform();
				if (!waitOnElement(element,5000)) {
					logger.error("Unable to find element CC" + element);
					return false;
				}
				if (!clickOnIndependentElement(element)) {
					logger.error("Unable to click on CC" + element);
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			if (!clickOnIndependentElement(element)) {
				tapActions.tapScreenIfRequired();
				if (!clickOnIndependentElement(element)) {
					logger.error("Unable to click on CC" + element);
					return false;
				}
			}
		}
		return true;
	}

	public boolean tapCC() {
		if(platform.equalsIgnoreCase("ios"))
			return tapCC("CC");
		return tapCC("CC_ANDROID");
	}

	public boolean verifyLanguagePage(String elementKey) {
		if (!waitOnElement(elementKey, 20000)) {
			logger.error("Unable open LanguagePage:" + elementKey);
			return false;
		}
		return true;
	}

	public boolean verifyLanguagePage() {
		if(platform.equalsIgnoreCase("ios"))
			return verifyLanguagePage("LANGUAGE");
		return verifyLanguagePage("LANGUAGE_ANDROID");
	}

	public boolean selectLanguage(String elementKey) {
		if(!waitOnElement(elementKey, 5000)){
			logger.error("Unable fine Element:" + elementKey);
			return false;
		}
		if (!clickOnIndependentElement(elementKey)) {
			logger.error("Unable Select Language:" + elementKey);
			return false;
		}
		String done="DONE";
		if(isV4)
			done="DONE_V4";	
		if (!waitOnElement(done,5000)) {
			logger.error("Unable to find element  Done/Dismiss" + done);
			return false;
		}
		if (!clickOnIndependentElement(done)) {
			logger.error("Unable to click on Done/Dismiss");
			return false;
		}
		if(isV4){
			if (!waitOnElement(done,5000)) {
				logger.error("Unable to find element  Done/Dismiss" + done);
				return false;
			}
			if (!clickOnIndependentElement(done)) {
				logger.error("Unable to click on Done/Dismiss");
				return false;
			}
		}
		return true;

	}

	public boolean selectEnglishLanguage() {
		if(platform.equalsIgnoreCase("ios"))
			return selectLanguage("ENGLISH_LANGUAGE");
		return selectLanguage("ENGLISH_LANGUAGE_ANDROID");
	}

	public boolean enableCC(String ccElement, String page, String languageKey) {
		if (!tapCC(ccElement))
			return false;
		if (!verifyLanguagePage(page))
			return false;
		if (!selectLanguage(languageKey))
			return false;
		return true;
	}

	public boolean enableCC() {
		if(isV4)
			return enableCC("CC_V4", "CC_V4", "CC_SWITCH");
		return enableCC("CC", "LANGUAGE", "ENGLISH_LANGUAGE");
	}

	@Override
	public boolean startAction(String element) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
