package com.ooyala.playback.page;

import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

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
	boolean flag = false;

	public boolean validate(String element, int timeout) throws Exception {
		if(flag) return true;
		return waitOnElement(By.id(element), timeout);
	}

	public boolean eventAction(String element) throws Exception {
		 return clickOnIndependentElement(element);
	}

	public void validateElement(String element, int timeout) throws Exception {
		waitOnElement(element, timeout);
	}

	public boolean validateElementPresence(String element) throws Exception {
		return isElementPresent(By.id(element));
	}
	
	public boolean validateForSpecificPlugins(String element, int timeOut, String adPlugin) throws Exception{
		Map<String,String> map = parseURL();
		
		if (map!=null && map.get("ad_plugin")!=null && map.get("ad_plugin").contains(adPlugin)) {
			flag = true;
			return validate(element, timeOut);
		}
		return true;
		
	}

}
