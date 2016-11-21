package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

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
	public void validate(String element, int timeout) throws Exception {
		double expectedmutevol = 0.0;
		double expectedmaxvol = 1.0;

		Long currentVolume = (Long) (((JavascriptExecutor) driver)
				.executeScript("return pp.getVolume()"));
		Log.info("Current volume: " + currentVolume);
		try {

			if (!(isElementPresent("CONTROL_BAR"))) {

				Actions action = new Actions(driver);
				action.moveToElement(getWebElement("controlBar")).build()
						.perform();
			}
			double getmutevol = getVolume("VOLUME_MAX");
			Assert.assertEquals(getmutevol, expectedmutevol,
					"Mute volume is't matching");
			Thread.sleep(2000);

			double getMaxVol = getVolume("VOLUME_MUTE");
			Assert.assertEquals(getMaxVol, expectedmaxvol,
					"Max volume is not matched");
		} catch (Exception e) {
			logger.error("Volume control is not working properly"
					+ e.getMessage());
		}

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
