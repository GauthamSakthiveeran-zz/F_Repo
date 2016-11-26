package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;

/**
 * Created by soundarya on 10/27/16.
 */
public class SeekValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(SeekValidator.class);

	public SeekValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("play");
	}

	public void validate(String element, int timeout) throws Exception {
		/*
		 * while (true) { if (Double.parseDouble(((JavascriptExecutor)
		 * driver).executeScript( "return pp.getPlayheadTime();").toString()) >
		 * 5) { PlayBackFactory.getInstance(driver).getSeekAction().seek(7,
		 * true); // loadingSpinner(); ((JavascriptExecutor)
		 * driver).executeScript("pp.pause();"); Thread.sleep(2000);
		 * ((JavascriptExecutor) driver).executeScript("pp.play();"); break; } }
		 */

		PlayBackFactory.getInstance(driver).getSeekAction().seekTillEnd().startAction();

		waitOnElement(By.id(element), timeout);
		logger.info("Video seeked successfully");
	}

}
