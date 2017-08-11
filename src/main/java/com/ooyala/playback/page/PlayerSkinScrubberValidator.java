package com.ooyala.playback.page;
/**
 * Created by Gautham
 */

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import com.ooyala.qe.common.util.PropertyReader;
import com.relevantcodes.extentreports.LogStatus;

public class PlayerSkinScrubberValidator extends PlayBackPage{

	private static Logger logger = Logger.getLogger(PlayValidator.class);

	public PlayerSkinScrubberValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("play");
		addElementToPageElements("pause");
		addElementToPageElements("controlbar");
		addElementToPageElements("scrubber");
		addElementToPageElements("cc");
		addElementToPageElements("sharetab");
		addElementToPageElements("volume");
		addElementToPageElements("bitrate");
		addElementToPageElements("fullscreen");
		addElementToPageElements("replay");
		addElementToPageElements("fcc");
		

	}


	//Function to Verify the CSS Property
	public boolean verifyWebElementCSSColor(String element, String cssStyleProperty , String color)
	{
		PropertyReader properties = null;
		try
		{
			properties = PropertyReader.getInstance("cssProperty.properties");
			if((getWebElement(element).getCssValue(cssStyleProperty)).equalsIgnoreCase(properties.getProperty(color)))
				{
				logger.info("css Property Check Passed");
				extentTest.log(LogStatus.INFO, "Color of Element matched." + element + "-" + color);
				return true;
				}
			else
			{
				logger.info("css Property Check Failed");
				extentTest.log(LogStatus.INFO, "Color of Element not matched." + element + "-" + color);
				return false;
				
			}
				
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		

	}
	//Function to Mouseover and verify the CSS Properties
	public boolean moveToWebElementCSSColor(String element, String cssStyleProperty , String color)
	{
		PropertyReader properties = null;
		
		moveElement(getWebElement(element));
		
		
		
		try
		{
			Thread.sleep(5000);
			System.out.println("====>" + (getWebElement(element).getCssValue(cssStyleProperty)));
			properties = PropertyReader.getInstance("cssProperty.properties");
			if((getWebElement(element).getCssValue(cssStyleProperty)).equalsIgnoreCase(properties.getProperty(color)))
				{
				logger.info("css Property Check Passed");
				extentTest.log(LogStatus.INFO, "Color of Element matched." + element + "-" + color);
				return true;
				}
			else
			{
				logger.info("css Property Check Failed");
				extentTest.log(LogStatus.INFO, "Color of Element not matched." + element + "-" + color);
				return false;
				
			}
				
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		

	}


	public boolean verifyCountdownPresent() {
		if(waitOnElement("AD_COUNTDOWN", 10000) ) {
			logger.info("AdCountDown is found ");
			extentTest.log(LogStatus.FAIL, "AdCountDown is found ");
			return false;
		}
		else
		return true;
	}

	
	
	
}