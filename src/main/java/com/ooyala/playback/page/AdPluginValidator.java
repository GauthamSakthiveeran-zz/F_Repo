package com.ooyala.playback.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.LogStatus;

public class AdPluginValidator extends PlayBackPage implements PlaybackValidator {

	public AdPluginValidator(WebDriver webDriver) {
		super(webDriver);
	}
	
	private String adPlugin;
	
	public AdPluginValidator setAdPlugin(String adPlugin) {
		this.adPlugin = adPlugin;
		return this;
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		if(waitOnElement(By.id(element), timeout)) {
			if(driver.findElement(By.id(element)).getText().contains(adPlugin.toLowerCase())) {
				extentTest.log(LogStatus.PASS, "Loaded the right ad plugin " + adPlugin);
				return true;
			}
		}
		extentTest.log(LogStatus.FAIL, "Unable to verify ad plugin");
		return false;
	}

}
