package com.ooyala.playback.apps.actions;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;

import io.appium.java_client.AppiumDriver;

public class CCAction extends PlaybackApps implements Actions {

	private static Logger logger = Logger.getLogger(CCAction.class);

	private String platform;
	
	public CCAction(AppiumDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("cc");
		platform = getPlatform();
	}

	public boolean tapCC(String element) {

		tapScreenIfRequired();
		if (!clickOnIndependentElement(element)) {
			tapScreenIfRequired();
			if (!clickOnIndependentElement(element)) {
				logger.error("Unable to click on CC" + element);
				return false;
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
		if (!clickOnIndependentElement(elementKey)) {
			logger.error("Unable Select Language:" + elementKey);
			return false;
		}
		if (!clickOnIndependentElement("DONE")) {
			logger.error("Unable to click on CC");
			return false;
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
		return enableCC("CC", "LANGUAGE", "ENGLISH_LANGUAGE");
	}

	@Override
	public boolean startAction(String element) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
