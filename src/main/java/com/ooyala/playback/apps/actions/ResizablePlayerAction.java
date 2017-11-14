package com.ooyala.playback.apps.actions;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;

public class ResizablePlayerAction extends PlaybackApps implements Actions  {

	public ResizablePlayerAction(AppiumDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("resizableplayer");
	}

	@Override
	public boolean startAction(String element) throws Exception {
		return clickOnIndependentElement(element);
	}
	
	public boolean validateTallPlayer() throws Exception {
		Boolean result = true;
		
		result = result && isElementFoundinResizablePlayerScreen("SKINLAYOUT_LEFT_TEXT");
		result = result && isElementFoundinResizablePlayerScreen("SKINLAYOUT_RIGHT_TEXT");
		result = result && !isElementFoundinResizablePlayerScreen("SKINLAYOUT_ABOVE_TEXT");
		result = result && !isElementFoundinResizablePlayerScreen("SKINLAYOUT_BELOW_TEXT");
		
		return result;
	}
	
	public boolean validateWidePlayer() throws Exception {
		Boolean result = true;
		
		result = result && !isElementFoundinResizablePlayerScreen("SKINLAYOUT_LEFT_TEXT");
		result = result && !isElementFoundinResizablePlayerScreen("SKINLAYOUT_RIGHT_TEXT");
		result = result && isElementFoundinResizablePlayerScreen("SKINLAYOUT_ABOVE_TEXT");
		result = result && isElementFoundinResizablePlayerScreen("SKINLAYOUT_BELOW_TEXT");
		
		return result;
	}
	
	public boolean validateFillScreenPlayer() throws Exception {
		Boolean result = true;
		
		result = result && !isElementFoundinResizablePlayerScreen("SKINLAYOUT_LEFT_TEXT");
		result = result && !isElementFoundinResizablePlayerScreen("SKINLAYOUT_RIGHT_TEXT");
		result = result && !isElementFoundinResizablePlayerScreen("SKINLAYOUT_ABOVE_TEXT");
		result = result && !isElementFoundinResizablePlayerScreen("SKINLAYOUT_BELOW_TEXT");
		
		return result;
	}
	
	// Function to check if an element is present in discovery screen

	public Boolean isElementFoundinResizablePlayerScreen(String element) {
		try {

			if (waitOnElement(element, 5000)) {
				WebElement elementToFind = getWebElement(element);
				if (elementToFind.isEnabled()) {
					extentTest.log(LogStatus.PASS, element + " is present in Resizable Screen");
					return true;
				} else {
					extentTest.log(LogStatus.INFO, element + " is not present in Resizable Screen");
					return false;

				}
			} else {
				extentTest.log(LogStatus.INFO, element + " is not present in Resizable Screen");
				return false;

			}

		} catch (Exception e) {

			e.printStackTrace();
			extentTest.log(LogStatus.FAIL, element + " Exception while location element");
			return false;

		}
	}


}
