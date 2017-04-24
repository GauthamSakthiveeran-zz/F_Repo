package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.ooyala.playback.url.UrlObject;
import com.relevantcodes.extentreports.LogStatus;

public class PackagingValidator extends PlayBackPage implements PlaybackValidator {

	private static Logger logger = Logger.getLogger(PackagingValidator.class);

	public PackagingValidator(WebDriver webDriver) {
		super(webDriver);
	}

	private UrlObject urlObject;
	private String testName = "";

	public PackagingValidator setUrlObject(UrlObject urlObject) {
		this.urlObject = urlObject;
		return this;
	}

	public PackagingValidator setTestName(String testName) {
		this.testName = testName;
		return this;
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		String videoPlayingurl = driver.findElement(By.id("videoPlayingurl")).getText();
		String streamType = urlObject.getStreamType();

		if (testName.contains("Elemental")) {

			if (!videoPlayingurl.contains("cf-c.ooyala.com")) {
				extentTest.log(LogStatus.FAIL,
						"Elemental packaging does not served from expected : cf-c.ooyala.com cloud server");
				return false;
			} else {
				extentTest.log(LogStatus.PASS,
						"Elemental packaging is served from expected :cf-c.ooyala.com cloud server");
			}

			if (!videoPlayingurl.contains("1." + streamType)) {
				logger.error("Elemental packaging manifest is not in proper format i.e 1." + streamType);
				extentTest.log(LogStatus.FAIL,
						"Elemental packaging manifest is not in proper format i.e 1." + streamType);
				return false;
			} else {
				logger.info("Elemental packaging manifest is in proper format i.e 1." + streamType);
				extentTest.log(LogStatus.PASS, "Elemental packaging manifest is in proper format i.e 1." + streamType);
			}

		} else if (testName.contains("Azure")) {

			if (!videoPlayingurl.contains("http://ooyalaeast.streaming.mediaservices.windows.net")) {
				logger.error(
						"Azure packaging does not served from expected : http://ooyalaeast.streaming.mediaservices.windows.net cloud server");
				extentTest.log(LogStatus.FAIL,
						"Elemental packaging does not served from expected : http://ooyalaeast.streaming.mediaservices.windows.net cloud server");
				return false;
			} else {
				logger.info(
						"Azure packaging gets served from expected : http://ooyalaeast.streaming.mediaservices.windows.net cloud server");
				extentTest.log(LogStatus.PASS,
						"Elemental packaging gets served from expected : http://ooyalaeast.streaming.mediaservices.windows.net cloud server");
			}

			if (!videoPlayingurl.contains(urlObject.getEmbedCode() + ".ism")) {
				logger.error(
						"Azure packaging does not served in expected format as : " + urlObject.getEmbedCode() + ".ism");
				extentTest.log(LogStatus.FAIL,
						"Azure packaging does not served in expcted format as : " + urlObject.getEmbedCode() + ".ism");
				return false;
			} else {
				logger.info("Azure packaging does gets served in expected format as : " + urlObject.getEmbedCode()
						+ ".ism");
				extentTest.log(LogStatus.PASS,
						"Azure packaging does gets served in expcted format as : " + urlObject.getEmbedCode() + ".ism");
			}
		} else if (testName.contains("FFMPEG")) {

			if (!videoPlayingurl.contains("http://player.ooyala.com/player")) {
				logger.error(
						"FFMPEG packaging does not served from expected : http://player.ooyala.com/player cloud server");
				extentTest.log(LogStatus.FAIL,
						"FFMPEG packaging does not served from expected : http://player.ooyala.com/player cloud server");
				return false;
			} else {
				logger.info(
						"FFMPEG packaging gets served from expected : http://player.ooyala.com/player cloud server");
				extentTest.log(LogStatus.PASS,
						"FFMPEG packaging gets served from expected : http://player.ooyala.com/player cloud server");
			}

			if (!videoPlayingurl.contains(urlObject.getEmbedCode() + "." + streamType)) {
				logger.error("FFMPEG packaging does not served in expected format as : " + urlObject.getEmbedCode()
						+ streamType);
				extentTest.log(LogStatus.FAIL, "Azure packaging does not served in expcted format as : "
						+ urlObject.getEmbedCode() + streamType);
				return false;
			} else {
				logger.info("FFMPEG packaging does gets served in expected format as : " + urlObject.getEmbedCode()
						+ streamType);
				extentTest.log(LogStatus.PASS, "Azure packaging does gets served in expcted format as : "
						+ urlObject.getEmbedCode() + streamType);
			}
		} else if (testName.contains("Akamai")) {

			if (testName.contains("Akamai") && testName.contains("HD2")) {
				if (!videoPlayingurl.contains("http://100403.hdn.c.ooyala.com")) {
					logger.error(
							"Akamai packaging does not served from expected : http://100403.hdn.c.ooyala.com cloud server");
					extentTest.log(LogStatus.FAIL,
							"Akamai packaging does not served from expected : http://100403.hdn.c.ooyala.com cloud server");
					return false;
				} else {
					logger.error(
							"Akamai packaging does gets served from expected : http://100403.hdn.c.ooyala.com cloud server");
					extentTest.log(LogStatus.FAIL,
							"Akamai packaging does gets served from expected : http://100403.hdn.c.ooyala.com cloud server");
				}
			} else {
				if (!videoPlayingurl.contains("https://acaooyalahd2-lh.akamaihd.net")) {
					logger.error(
							"Akamai packaging does not served from expected : https://acaooyalahd2-lh.akamaihd.net cloud server");
					extentTest.log(LogStatus.FAIL,
							"Akamai packaging does not served from expected : https://acaooyalahd2-lh.akamaihd.net cloud server");
					return false;
				} else {
					logger.error(
							"Akamai packaging does gets served from expected : https://acaooyalahd2-lh.akamaihd.net cloud server");
					extentTest.log(LogStatus.FAIL,
							"Akamai packaging does gets served from expected : https://acaooyalahd2-lh.akamaihd.net cloud server");
				}
			}
		} 

		return true;
	}

}
