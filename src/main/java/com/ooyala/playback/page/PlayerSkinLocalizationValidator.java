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

public class PlayerSkinLocalizationValidator extends PlayBackPage{

	private static Logger logger = Logger.getLogger(PlayValidator.class);

	public PlayerSkinLocalizationValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);

		addElementToPageElements("sharetab");
		addElementToPageElements("controlbar");
		addElementToPageElements("scrubber");
		

	}

	//Function to Check the Localization 
	public boolean 	skinLocalizationValidate()
	
	{
		
		try {
			if (isElementPresent("HIDDEN_CONTROL_BAR")) {
				logger.info("hovering mouse over the player");
				moveElement(getWebElement("HIDDEN_CONTROL_BAR"));
			}
			if (!(waitOnElement("SHARE_BTN", 10000) && clickOnIndependentElement("SHARE_BTN"))) {
				extentTest.log(LogStatus.FAIL, "SHARE_BTN is not found/ not clickable.");
				return false;
			}
		} catch (Exception e) {
			if (!(clickOnIndependentElement("MORE_OPTION_ITEM") && waitOnElement("SHARE_BTN", 10000)
					&& clickOnIndependentElement("SHARE_BTN"))) {
				extentTest.log(LogStatus.FAIL, "SHARE_BTN is not found/ not clickable.");
				return false;
			}

		}
	
	String shareTab = readTextFromElement("SHARE_TAB");
	logger.info("Share Tab value " + shareTab);

	String embedTab = readTextFromElement("EMBED_TAB");
	logger.info("Text in Embed Tab  " + embedTab);
	
	if((shareTab.equals("シェア")) && (embedTab.equals("コードの埋め込み")))
		{
		extentTest.log(LogStatus.PASS, "Localization Test Passed");
		logger.info("Localization Test Passed");
		return true;
		}
	else
		{
		extentTest.log(LogStatus.PASS, "Localization Test Passed");
		logger.info("Localization Test Passed");
		return false;
		}

	
	}
	
	//Function to Check the Ad Screen Localization 
	public boolean 	skinAdScreenLocalizationValidate()
	
	{
		try {
				if(getWebElement("AD_COUNTDOWN").getText().contains("広告"))
				{
				logger.info("AdScreen is Localized ");
				return true;
				}
				else
				{
				logger.info("AdScreen is not Localized ");
				return false;
				}
			
			
			}
			catch(Exception e)
			{
				return false;
				
			}

	}
	
}