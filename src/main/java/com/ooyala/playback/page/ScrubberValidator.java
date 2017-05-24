package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;

public class ScrubberValidator extends PlayBackPage implements PlaybackValidator {

	public ScrubberValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("scrubber");
		addElementToPageElements("discovery");
	}

	public static Logger logger = Logger.getLogger(ScrubberValidator.class);

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		
		DiscoveryValidator discovery = new PlayBackFactory(driver, extentTest).getDiscoveryValidator();
		if(discovery.isDiscoveryToasterPresent()) {
			Thread.sleep(1000);
			discovery.clickOnDiscoveryCloseButton();
			logger.info("Closed the discovery button");
			Thread.sleep(1000);
		}
		
		if (!adPlaying(false)) {
			return true;
		}
		
		if(!isElementPresent("SCRUBBER_BAR")) {
			extentTest.log(LogStatus.FAIL, "Scrubber bar is not present");
			return false;
		}
		
		if(!isElementPresent("POINTER")) {
			extentTest.log(LogStatus.FAIL, "Scrubber pointer is not present");
			return false;
		}
		
		
		double duration = getDuration();
		double played = getPlayAheadTime();

		double percentagePlayed = Math.round(played / duration * 100);
		logger.info("Percentage played " + percentagePlayed);

		double width = getWebElement("SCRUBBER_BAR").getSize().width;
		logger.info("width of scrubber bar" + width);

		double pointer = Math.round(width * percentagePlayed / 100);
		logger.info("Pointer location " + pointer);

		double percentagePlayedFromUI = getWebElement("PLAYED").getSize().getWidth();

		percentagePlayedFromUI = (percentagePlayedFromUI / width) * 100;
		logger.info("percentagePlayedFromUI " + percentagePlayedFromUI);

		double pointerLocation = Double.parseDouble(getWebElement("POINTER").getCssValue("left").replaceAll("px", ""));
		logger.info("pointerLocation " + pointerLocation);

		double scrubberDiff = Math.abs(Math.round(percentagePlayed - percentagePlayedFromUI));
		double pointerDiff = Math.abs(Math.round(pointer - pointerLocation));
		
		logger.info("pointerDiff " + pointerDiff);
		logger.info("scrubberDiff " + scrubberDiff);

		boolean flag = true;

		if (scrubberDiff > 1) {
			extentTest.log(LogStatus.FAIL, "Scrubber bar for completion is incorrect - Actual : "
					+ percentagePlayedFromUI + " Expected : " + percentagePlayed);
			flag = false;
		}

		if (percentagePlayed == 100) {
			pointerDiff = Math.abs(Math.round(pointer) - Math.round(width));
		}

		if (pointerDiff > 10.0) {
			extentTest.log(LogStatus.INFO, "Scrubber pointer for completion is incorrect - Actual : " + pointerLocation
					+ " Expected : " + pointer);
		}

		if (flag) {
			extentTest.log(LogStatus.PASS, "Validated scrubber");
		}

		return flag;
	}

}
