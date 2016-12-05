package com.ooyala.playback.page;

import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/14/16.
 */
public class EventValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(DiscoveryValidator.class);

	public EventValidator(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("discovery");
		addElementToPageElements("play");
		addElementToPageElements("pause");
		addElementToPageElements("replay");
		addElementToPageElements("controlbar");
		addElementToPageElements("fullscreen");
		addElementToPageElements("adPodEnd");
		addElementToPageElements("startscreen");
	}

	public boolean validate(String element, int timeout) throws Exception {
		return waitOnElement(By.id(element), timeout);
	}

	public boolean eventAction(String element) throws Exception {
		 return clickOnIndependentElement(element);
	}

	public void validateElement(String element, int timeout) throws Exception {
		waitOnElement(element, timeout);
	}

	public boolean validateElementPresence(String element) throws Exception {
		if(isElementPresent(By.id(element))){
			extentTest.log(LogStatus.PASS, element + " presence verified.");
			return true;
		}
		extentTest.log(LogStatus.FAIL, element + " not present.");
		return false;
	}
	
	public boolean isAdPlugin(String adPlugin) throws Exception{
		Map<String,String> map = parseURL();
		if (map!=null && map.get("ad_plugin")!=null && map.get("ad_plugin").contains(adPlugin)) {
			return true;
		}
		return false;
	}
	
	public boolean isVideoPlugin(String videoPlugin) throws Exception{
		Map<String,String> map = parseURL();
		if (map!=null && map.get("video_plugins")!=null && map.get("video_plugins").contains(videoPlugin)) {
			return true;
		}
		return false;
	}

}
