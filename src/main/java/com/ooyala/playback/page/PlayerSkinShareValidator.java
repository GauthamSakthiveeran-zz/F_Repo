package com.ooyala.playback.page;
/**
 * Created by Gautham
 */

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import com.relevantcodes.extentreports.LogStatus;

public class PlayerSkinShareValidator extends PlayBackPage{

	private static Logger logger = Logger.getLogger(PlayValidator.class);

	public PlayerSkinShareValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */

		addElementToPageElements("controlbar");
		addElementToPageElements("scrubber");
		addElementToPageElements("sharetab");

		

	}

	//Function to verify if the  Social Media Buttons mentioned in the Json file are properly reflected.
	public boolean shareScreenValidate()
	{
		Boolean flag = true;
	
		try {
			if (isElementPresent("HIDDEN_CONTROL_BAR")) {
				logger.info("hovering mouse over the player");
				moveElement(getWebElement("HIDDEN_CONTROL_BAR"));
			}
			if (!(waitOnElement("SHARE_BTN", 10000) && clickOnIndependentElement("SHARE_BTN"))) {
				extentTest.log(LogStatus.FAIL, "SHARE_BTN is not found/ not clickable.");
				return false;
			}
			
			
			if(waitOnElement("TWITTER", 10000) ) {
				extentTest.log(LogStatus.FAIL, "Twitter Button found in Share Screen");
				flag =  false;
			}
			
			if(waitOnElement("FACEBOOK", 10000) ) {
				extentTest.log(LogStatus.FAIL, "Facebook Button found in Share Screen");
				flag = flag && false ;
			}
			
			
			
		} catch (Exception e) {
			if (!(clickOnIndependentElement("MORE_OPTION_ITEM") && waitOnElement("SHARE_BTN", 10000)
					&& clickOnIndependentElement("SHARE_BTN"))) {
				extentTest.log(LogStatus.FAIL, "SHARE_BTN is not found/ not clickable.");
				return false;
			}

		}


		return flag;
	
	}
	
	
	
	
}