package com.ooyala.playback.page;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by suraj on 3/23/17.
 */
public class StreamValidator extends PlayBackPage implements PlaybackValidator {
	private static Logger logger = Logger.getLogger(StreamValidator.class);

	public StreamValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
	}

	private String streamType;

	public StreamValidator setStreamType(String streamType) {
		this.streamType = streamType;
		return this;
	}

	public boolean validate(String element, int timeout) throws Exception {

		if (streamType.contains("mp4")) {
			logger.info("checking mp4 stream type");
			String mp4Url = driver.findElementById(element).getText();
			logger.info("opening a new tab");
			driver.executeScript("window.open('" + mp4Url + "')");
			ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());

			driver.switchTo().window(tabs.get(1));
			logger.info("navigated to new tab");

			if (waitOnElement(By.xpath("//video"), 20000)) {
				String isMp4 = driver.findElement(By.xpath("//video//source")).getAttribute("type");
				driver.close();

				driver.switchTo().window(tabs.get(0));

				if (isMp4.contains("mp4")) {
					logger.info("Stream is matching as per expected result " + streamType);
					extentTest.log(LogStatus.PASS, "Stream is matching as per expected result " + streamType);
					return true;
				} else {
					logger.info("Stream is not matching as per expected result " + streamType);
					extentTest.log(LogStatus.FAIL, "Stream is not matching as per expected result " + streamType);
					return false;
				}
			} else {
				extentTest.log(LogStatus.FAIL, "Issue with checking stream mp4.");
				return false;
			}

		}

		String streamContains = driver.findElement(By.id("videoPlayingurl")).getText();

		if (!streamContains.contains(streamType)) {
			extentTest.log(LogStatus.FAIL, "Stream is not matching as per expected result " + streamContains);
			return false;
		}
		logger.info("verified Stream type :" + streamType);
		extentTest.log(LogStatus.PASS, "verified Stream type :" + streamType);
		return true;
	}

	public boolean verifyStreamType(String encoding) throws Exception {
		StreamValidator streams = new PlayBackFactory(driver, extentTest).getStreamTypeValidator();
		if (encoding.contains("hls") || encoding.contains("m3u8")) {
			return streams.setStreamType("m3u8").validate("videoPlayingurl", 6000);
		}
		if (encoding.contains("dash") || encoding.contains("mpd")) {
			return streams.setStreamType("mpd").validate("videoPlayingurl", 6000);
		}
		if (encoding.contains("mp4")) {
			return streams.setStreamType("mp4").validate("videoPlayingurl", 6000);
		}
		if (encoding.contains("hds") || encoding.contains("f4m")) {
			return streams.setStreamType("f4m").validate("videoPlayingurl", 6000);
		}

		return false;
	}

}
