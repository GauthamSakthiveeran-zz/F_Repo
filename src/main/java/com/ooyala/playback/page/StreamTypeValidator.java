package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

import java.util.ArrayList;

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

        if(streamType.contains("mp4")){
            String mp4Url = driver.findElementById(element).getText();
            driver.executeScript("window.open('"+mp4Url+"')");
            ArrayList<String> tabs = new ArrayList<String>(
                    driver.getWindowHandles());
            Thread.sleep(2000);
            driver.switchTo().window(tabs.get(1));
            Thread.sleep(2000);
            waitOnElement(By.xpath(".//*[@type='video/mp4']"),20000);
            String isMp4 = driver.findElement(By.xpath(".//*[@type='video/mp4']")).getAttribute("type");
            driver.close();
            Thread.sleep(2000);
            driver.switchTo().window(tabs.get(0));
            Thread.sleep(2000);
            if (isMp4.contains("mp4")){
                return true;
            } else {
                logger.info("Stream is not matching as per expected result " + streamType);
                extentTest.log(LogStatus.PASS, "Stream is not matching as per expected result " + streamType);
                return false;
            }
        }
		
		String streamContains = driver.findElement(By.id("videoPlayingurl")).getText();
		
		if (!streamContains.contains(streamType)) {
			logger.info("Stream is not matching as per expected result " + streamContains);
			extentTest.log(LogStatus.PASS, "Stream is not matching as per expected result " + streamContains);
			return false;
		}
		
		return true;
	}

}
