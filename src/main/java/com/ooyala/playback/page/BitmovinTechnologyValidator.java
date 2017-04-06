package com.ooyala.playback.page;

import static java.net.URLDecoder.decode;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.LogStatus;

public class BitmovinTechnologyValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(BitmovinTechnologyValidator.class);

	public BitmovinTechnologyValidator(WebDriver webDriver) {
		super(webDriver);
	}

	private String streamType;

	public void getConsoleLogs() {
		driver.executeScript(
				"var oldf = console.log;console.log = function() {oldf.apply(console, arguments);if(arguments[0].includes('Bitmovin player is using technology')) OO.$(\"#ooplayer\").append(\"<p id=bitmovin_technology>\" + arguments[0] + \"</p>\");}");
	}

	public BitmovinTechnologyValidator setStream(String streamType) {
		this.streamType = streamType;
		return this;
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {

		String result = decode(driver.getCurrentUrl(), "UTF-8");
		if (result == null)
			return false;

		String[] options = result.split("options=");
		String expectedValue = "html5";
		if (options != null && options.length >= 2) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(options[1]);
			if (json.containsKey("platform")) {
				expectedValue = (String) json.get("platform");
			}
		}

		expectedValue = expectedValue + "." + streamType;

		String techString = driver.findElementById("bitmovin_technology").getText();

		String actualValue = techString.trim().split("Bitmovin player is using technology:")[1].trim().split(",")[0]
				.trim();

		if (!actualValue.equals(expectedValue)) {
			logger.error("Expected to find " + expectedValue + " in " + techString);
			extentTest.log(LogStatus.FAIL, "Expected to find " + expectedValue + " in " + techString);
			return false;
		}

		logger.info("Expected to find " + expectedValue + " in " + techString);
		extentTest.log(LogStatus.PASS, "Expected to find " + expectedValue + " in " + techString);
		return true;
	}

}
