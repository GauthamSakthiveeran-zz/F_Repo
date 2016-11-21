package com.ooyala.playback.page;

import static java.lang.Integer.parseInt;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PoddedAdValidator extends BaseValidator{

	public PoddedAdValidator(WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public void validate(String element, int timeout) throws Exception {
		try{
			int result = parseInt((((JavascriptExecutor) driver).executeScript("return "+element+".textContent")).toString());
			for (int i = 1; i <= result; i++) {
	            (new WebDriverWait(driver, 100)).until(presenceOfElementLocated(id("willPlaySingleAd_" + i)));
//	            test.log(PASS, "Midroll Podded Ad started");
	           (new WebDriverWait(driver, 160)).until(presenceOfElementLocated(id("singleAdPlayed_" + i)));
//	            test.log(PASS, "Midroll Podded Ad Completed");
	        }
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

}
