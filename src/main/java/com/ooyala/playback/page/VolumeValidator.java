package com.ooyala.playback.page;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by soundarya on 11/8/16.
 */
public class VolumeValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(VolumeValidator.class);

	public VolumeValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("volume");
		addElementToPageElements("controlbar");
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		double expectedmutevol = 0.0;
		double expectedmaxvol = 1.0;

		((JavascriptExecutor) driver).executeScript("pp.pause()");

		Long currentVolume = (Long) (((JavascriptExecutor) driver).executeScript("return pp.getVolume()"));
		logger.info("Current volume: " + currentVolume);
		try {

			if (!(isElementPresent("CONTROL_BAR"))) {
				moveElement(getWebElement("CONTROL_BAR"));
			}
			if (clickOnIndependentElement("VOLUME_MAX")) {
				double getmutevol = getVolume();
				asserts.assertEquals(getmutevol, expectedmutevol, "Issue with Mute Volume.");

			} else {
				asserts.assertTrue(false, "Click on VOLUME_MAX failed.");
			}
			if (clickOnIndependentElement("VOLUME_MUTE")) {
				double getMaxVol = getVolume();
				asserts.assertEquals(getMaxVol, expectedmaxvol, "Issue with Max Volume.");
				
			} else {
				asserts.assertTrue(false, "Click on VOLUME_MAX failed.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			asserts.assertTrue(false, "Volume control is not working properly" + e.getMessage());
			return false;
		}

		((JavascriptExecutor) driver).executeScript("pp.play()");
		return true;
	}

	protected double getVolume() throws Exception {
		Thread.sleep(3500);
		double volume = Double
				.parseDouble(((JavascriptExecutor) driver).executeScript("return pp.getVolume()").toString());
		extentTest.log(LogStatus.INFO, "volume set to " + volume);
		return volume;

	}
}
