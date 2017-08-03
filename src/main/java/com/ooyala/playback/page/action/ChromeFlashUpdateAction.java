package com.ooyala.playback.page.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.page.PlayBackPage;
import com.ooyala.playback.page.PlaybackValidator;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by Gautham
 */
public class ChromeFlashUpdateAction extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(ChromeFlashUpdateAction.class);

	public ChromeFlashUpdateAction(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("chromeComponents");

	}

	public Boolean isFlashPluginUpdated()

	{
		
		boolean isadobeFlashVersion = isElementPresent("FLASH_VERSION");

		boolean isadobeFlashButton = isElementPresent("FLASH_BUTTON");

		WebElement versionDetails = null, adobeUpdateButton = null;

		int time = 0;

		if (isadobeFlashVersion && isadobeFlashButton)

		{

			versionDetails = getWebElement("FLASH_VERSION");

			adobeUpdateButton = getWebElement("FLASH_BUTTON");

			if (versionDetails.getText().contains("0.0.0.0")) {

				try {

					adobeUpdateButton.click();

					while (true) {
						if (versionDetails.getText().contains("0.0.0.0") && time <= 120) {
							Thread.sleep(1000);
							time++;
						}

						else if (!versionDetails.getText().contains("0.0.0.0")) {
							logger.info("AdobeFlash component is loaded in chrome://components - "
									+ versionDetails.getText());
							return true;
						} else {

							logger.info(
									"Error occured while loading AdobeFlash component is loaded in chrome://components");
							return false;
						}

					}

				} catch (Exception e) {
					logger.info(
							"Error occured while checking if the AdobeFlash component is loaded in chrome://components");
					
					return false;

				}

			}

			else
			{

				logger.info("The AdobeFlash Component is correctly updated in chrome://components");
				
				return true;
				
			}

		}

		else {
			logger.info("Failed to check if AdobeFlashPlayer is updated in chrome://components");
			
			return false;
		}
		

	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
