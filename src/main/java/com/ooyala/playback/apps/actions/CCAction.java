package com.ooyala.playback.apps.actions;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;
import com.ooyala.playback.factory.PlayBackFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

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
		
		if(platform.equalsIgnoreCase("android"))
		{
			Boolean result = true;
			try {
				if (waitOnElement("MOREOPTIONS_BUTTON_ANDROID", 3000)) {
					result = result && clickOnIndependentElement("MOREOPTIONS_BUTTON_ANDROID");
					Thread.sleep(2000);
				} else {
					tapinMiddleOfScreen();
					result = result && clickOnIndependentElement("MOREOPTIONS_BUTTON_ANDROID");
					Thread.sleep(2000);
				}
				result = result && clickOnIndependentElement("CC_ANDROID");
				
				if (waitOnElement("ENABLE_CC_ANDROID", 3000)) 
				{
				result = result && clickOnIndependentElement("ENABLE_CC_ANDROID");
				}
				else
				{
					logger.error("Element Not Found -  CC element");
					return false;
				}
				
				result = result && seletCCLanguageAndroid("en");
				
				result = result && clickOnIndependentElement("CCSCREEN_CLOSEBUTTON_ANDROID");
				
				Thread.sleep(1000);
				
				result = result && clickOnIndependentElement("MOREOPTIONSSCREEN_CLOSEBUTTON_ANDROID");
				

				return result;
				
		
			}
			catch(Exception e)
			{
				logger.error("Exception while clicking CC element");
				return false;
			}
		}
		else
		{
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
	}

	public boolean seletCCLanguageAndroid(String selectedLanguage) {
		TouchAction swipe = new TouchAction((AndroidDriver) driver);
		boolean islanguageFound = false;
		List<WebElement> allCaptions;
		while (!islanguageFound) {
			allCaptions = getWebElementsList("CC_LANGUAGE_LIST_ANDROID");

			if (allCaptions.size() != 0) {
				for (WebElement language : allCaptions)
					if (language.getText().equalsIgnoreCase(selectedLanguage)) {
						language.click();
						islanguageFound = true;
						return islanguageFound;
					}

				if (!islanguageFound && waitOnElement("CLOSED_CAPTION_PREVIEW_TEXT_ANDROID", 3000)) {
					logger.info("Language not found");
					return islanguageFound;
				} else
					swipe.longPress(allCaptions.get(allCaptions.size() - 1)).moveTo(allCaptions.get(1)).release()
							.perform();
			}

			else {
				logger.info("Caption Language List is empty");
				return islanguageFound;
			}
		
		}
		return islanguageFound;
		

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
			
		return false;

}
}
