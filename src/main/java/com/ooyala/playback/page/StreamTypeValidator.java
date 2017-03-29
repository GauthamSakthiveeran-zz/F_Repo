package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by suraj on 3/23/17.
 */
public class StreamTypeValidator extends PlayBackPage implements PlaybackValidator {
	private static Logger logger = Logger.getLogger(StreamTypeValidator.class);

	public StreamTypeValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
	}
	
	private String streamType;
	
	public StreamTypeValidator setStreamType(String streamType){
		this.streamType = streamType;
		return this;
	}

	public boolean validate(String element, int timeout) throws Exception {
		
		if(streamType.contains("mp4")) return true; // TODO for mp4
		
		String streamContains = driver.findElement(By.id(element)).getText();
		
		if (!streamContains.contains(streamType)) {
			logger.info("Stream is not matching as per expected result " + streamContains);
			extentTest.log(LogStatus.PASS, "Stream is not matching as per expected result " + streamContains);
			return false;
		}
		
		return true;
	}

}
