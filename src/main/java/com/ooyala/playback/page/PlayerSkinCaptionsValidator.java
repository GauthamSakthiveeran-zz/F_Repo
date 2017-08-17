package com.ooyala.playback.page;

import java.io.FileInputStream;

/**
 * Created by Gautham
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import com.ooyala.qe.common.util.PropertyReader;

import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;

public class PlayerSkinCaptionsValidator extends PlayBackPage{

	private static Logger logger = Logger.getLogger(PlayValidator.class);

	public PlayerSkinCaptionsValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("cc");
		addElementToPageElements("fcc");

	}


	//Function to Verify the Css Property
	public boolean verifyWebElementCSSProperty(String element, String cssStyleProperty , String value)
	{
		PropertyReader properties = null;
		try
		{
			properties = PropertyReader.getInstance("cssProperty.properties");
			if((getWebElement(element).getCssValue(cssStyleProperty)).equalsIgnoreCase(properties.getProperty(value)))
				{
				logger.info("css Property Check Passed");
				extentTest.log(LogStatus.PASS, "Color of Element matched." + element + "-" + value);
				return true;
				}
			else
			{
				logger.info("css Property Check Failed");
				extentTest.log(LogStatus.FAIL, "Color of Element not matched." + element + "-" + value);
				return false;
				
			}
				
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		

	}


	
	
	
}