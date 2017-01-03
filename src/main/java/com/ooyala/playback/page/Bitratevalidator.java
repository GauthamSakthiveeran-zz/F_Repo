package com.ooyala.playback.page;

import static java.lang.Thread.sleep;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by soundarya on 11/17/16.
 */
public class Bitratevalidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(Bitratevalidator.class);

	public Bitratevalidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("play");
		addElementToPageElements("pause");
		addElementToPageElements("bitrate");
		addElementToPageElements("replay");
	}

	public boolean validate(String element, int timeout) throws Exception {

		try {
			if (!isElementPresent("BITRATE"))
				return false;
		} catch (Exception e) {
			if (!clickOnIndependentElement("MORE_OPTION_ITEM"))
				return false;
			sleep(1000);
			if (!isElementPresent("BITRATE"))
				return false;
		}

		if (!clickOnIndependentElement("BITRATE"))
			return false;

		if (isElementPresent("SET_BITRATE")){
			if (!clickOnIndependentElement("SET_BITRATE")){return false;}
		}

		try{
			if(isElementPresent("BITRATE_CLOSE_BTN")) {
				clickOnIndependentElement("BITRATE_CLOSE_BTN");
				((JavascriptExecutor) driver)
						.executeScript(" return pp.play()");
			}
		} catch (Exception e){
			logger.info("Bitrate close button is not present");
		}

		int length = Integer.parseInt(((JavascriptExecutor) driver)
				.executeScript("return pp.getBitratesAvailable().length")
				.toString());
		if (length > 1) {
			boolean flag = true;
			for (int i = 0; i < length - 1; i++) {

				String bitrate = ((JavascriptExecutor) driver).executeScript(
						"return pp.getBitratesAvailable()[" + i
								+ "]['bitrate']").toString();
				logger.info("Current bitrate : "+bitrate);
				((JavascriptExecutor) driver)
						.executeScript("return pp.setTargetBitrate('" + i
								+ "')");

				((JavascriptExecutor) driver)
						.executeScript("pp.seek(pp.getDuration()-10)");

				waitOnElement("END_SCREEN",30000);

				((JavascriptExecutor) driver)
						.executeScript(" return pp.play()");

				loadingSpinner();

				Thread.sleep(10000);

				flag = flag && waitOnElement(By.id("bitrateChanged_" + (bitrate)),20000);

				logger.info("\n Is "+"bitrateChanged_"+bitrate+" element present : "+flag);
			}
			return flag;
		} else {
			String currentBitrate = ((JavascriptExecutor) driver)
					.executeScript("return pp.getCurrentBitrate()[\"bitrate\"]")
					.toString();
			((JavascriptExecutor) driver).executeScript("return pp.play()");
			// Assert.assertNotNull(currentBitrate);
			if (currentBitrate == null) {
				return false;
			}
			return true;
		}

	}
}
