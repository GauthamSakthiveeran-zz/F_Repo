package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/8/16.
 */
public class VolumeValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger Log = Logger.getLogger(VolumeValidator.class);

	public VolumeValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("volume");
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		double expectedmutevol = 0.0;
		double expectedmaxvol = 1.0;

		Long currentVolume = (Long) (((JavascriptExecutor) driver)
				.executeScript("return pp.getVolume()"));
		Log.info("Current volume: " + currentVolume);
		try {

			if (!(isElementPresent("CONTROL_BAR"))) {
				Actions action = new Actions(driver);
				action.moveToElement(getWebElement("CONTROL_BAR")).build()
						.perform();
			}
			double getmutevol = getVolume("VOLUME_MAX");
			if(getmutevol!=expectedmutevol){
				extentTest.log(LogStatus.FAIL, "Mute volume is't matching");
				return false;
			}
			Thread.sleep(2000);

			double getMaxVol = getVolume("VOLUME_MUTE");
			
			if(getMaxVol!=expectedmaxvol){
				extentTest.log(LogStatus.FAIL, "Max volume is not the same");
				return false;
			}
			
		} catch (Exception e) {
            Log.info("Volume control is not working properly"
					+ e.getMessage());
            return false;
		}
		return true;
	}

	protected double getVolume(String element) throws Exception {
		waitOnElement(element, 10);
		clickOnIndependentElement(element);
		Thread.sleep(3500);
		double volume = Double.parseDouble(((JavascriptExecutor) driver)
				.executeScript("return pp.getVolume()").toString());
		Log.info("volume set to " + volume);
		return volume;

	}
}
