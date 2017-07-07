package com.ooyala.playback.page;

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

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by Gautham
 */
public class ChromeComponentValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(ChromeComponentValidator.class);

	public ChromeComponentValidator(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("chromeComponents");

	}

	public void isFlashPluginUpdated()

	{
		System.out.println("INSIDE FUNCTION");
		boolean isadobeFlashVersion = isElementPresent("FlashVersion");

		boolean isadobeFlashButton = isElementPresent("FlashButton");

		WebElement versionDetails = null, adobeUpdateButton = null;

		int time = 0;

		if (isadobeFlashVersion && isadobeFlashButton)

		{

			versionDetails = getWebElement("FlashVersion");

			adobeUpdateButton = getWebElement("FlashButton");

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
							break;
						} else {

							logger.info(
									"Error occured while loading AdobeFlash component is loaded in chrome://components");
						}

					}

				} catch (Exception e) {
					logger.info(
							"Error occured while checking if the AdobeFlash component is loaded in chrome://compoents");

				}

			}

			else

				logger.info("The AdobeFlash Component is correctly updated in chrome://components");

		}

		else {
			logger.info("Failed to check if AdobeFlashPlayer is updated in chrome://components");
		}

	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
