package com.ooyala.playback.apps.actions;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;

import io.appium.java_client.AppiumDriver;

public class CCAction extends PlaybackApps implements Actions {

	private static Logger logger = Logger.getLogger(CCAction.class);

	public CCAction(AppiumDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("cc");
	}

	public boolean tapCC(String element) {

		try {
			if (!clickOnIndependentElement(element)) {
				logger.error("Unable to click on CC" + element);
				return false;
			}
		} catch (Exception e) {
			logger.info("CC button not found. Tapping screen and retrying..");
			tapScreenIfRequired();
			if (!clickOnIndependentElement(element)) {
				logger.error("Unable to click on CC" + element);
				return false;
			}
		}
		return true;
	}

	public boolean tapCC() {
		return tapCC("CC");
	}

	public boolean verifyLanguagePage(String elementKey) {
		if (!waitOnElement(elementKey, 20000)) {
			logger.error("Unable open LanguagePage:" + elementKey);
			return false;
		}
		return true;
	}

	public boolean verifyLanguagePage() {
		return verifyLanguagePage("Language");
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
	
	public boolean selectEnglishLanguage(){
		return selectLanguage("ENGLISH_LANGUAGE");
	}
	
	public boolean enableCC(String ccElement, String page,String languageKey){
		if(!tapCC(ccElement))
			return false;
		if(!verifyLanguagePage(page))
			return false;
		if(!selectLanguage(languageKey))
			return false;
		return true;
	}
	
	public boolean enableCC(){
		return enableCC("CC", "Language","ENGLISH_LANGUAGE");
		
	}

	@Override
	public boolean startAction(String element) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
