package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/16/16.
 */
public class EndScreenValidator extends PlayBackPage implements
		PlaybackValidator {

	public static Logger logger = Logger.getLogger(EndScreenValidator.class);

	public EndScreenValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("controlbar");
		addElementToPageElements("replay");
	}

	public boolean validate(String element, int timeout) throws Exception {
		if (!waitOnElement("END_SCREEN", 60000))
			return false;
		String replaytxt = getWebElement("PLAY_PAUSE").findElement(
				By.tagName("span")).getAttribute("class");

		if (!replaytxt.trim().equals("oo-icon oo-icon-system-replay")) {
			extentTest.log(LogStatus.FAIL,
					"Replay button is not present on end screen");
			return false;
		}

		double currenttime = Double.parseDouble(((JavascriptExecutor) driver)
				.executeScript("return pp.getPlayheadTime();").toString());
		double totaltime = Double.parseDouble(((JavascriptExecutor) driver)
				.executeScript("return pp.getDuration();").toString());
		
		if(!(getBrowser().equalsIgnoreCase("internet explorer") || (getBrowser().equalsIgnoreCase("firefox") && getPlatform().equalsIgnoreCase("windows")))){
			if (currenttime != totaltime) {
				extentTest
						.log(LogStatus.FAIL,
								"Current Time and TotalTime duration is not showing correctly");
				return false;
			}
		}
		return true;
	}

}