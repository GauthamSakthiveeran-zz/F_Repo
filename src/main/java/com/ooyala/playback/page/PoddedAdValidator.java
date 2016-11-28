package com.ooyala.playback.page;

import static java.lang.Integer.parseInt;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

public class PoddedAdValidator extends PlayBackPage implements
		PlaybackValidator {

	public static Logger logger = Logger.getLogger(PoddedAdValidator.class);

	public PoddedAdValidator(WebDriver webDriver) {
		super(webDriver);
	}

	public boolean validate(String element, int timeout) throws Exception {
		try {
			int result = parseInt((((JavascriptExecutor) driver)
					.executeScript("return " + element + ".textContent"))
					.toString());
			for (int i = 1; i <= result; i++) {
				WebElement willPlaySingleAd = (new WebDriverWait(driver, 100))
						.until(presenceOfElementLocated(id("willPlaySingleAd_"
								+ i)));
				extentTest.log(LogStatus.PASS, "Podded Ad started");
				WebElement singleAdPlayed = (new WebDriverWait(driver, 160))
						.until(presenceOfElementLocated(id("singleAdPlayed_"
								+ i)));
				
				if(willPlaySingleAd==null || singleAdPlayed==null){
					extentTest.log(LogStatus.FAIL, "Ad started elements from injected scripts are not found");
					return false;
				}
					
				extentTest.log(LogStatus.PASS, "Podded Ad Completed");
			}
			return true;
		} catch (Exception ex) {
			extentTest.log(LogStatus.FAIL, ex.getLocalizedMessage());
			ex.printStackTrace();
		}
		return false;
	}

}
