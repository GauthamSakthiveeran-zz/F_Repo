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

	@Override
	public boolean validate(String element, int timeout) throws Exception {

		String result = decode(driver.getCurrentUrl(), "UTF-8");
		if (result == null)
			return false;

		String[] options = result.split("options=");
		if (options == null || options.length < 2)
			return false;

		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(options[1]);

		String expectedValue = "html5.hls";

		if (json.containsKey("platform")) {
			expectedValue = (String) json.get("platform") + ".hls";
		}

		String techString = driver.findElementById("bitmovin_technology").getText();

		String actualValue = techString.trim().split("Bitmovin player is using technology:")[1].trim().split(",")[0]
				.trim();

		if (!actualValue.equals(expectedValue)) {
			extentTest.log(LogStatus.FAIL, "Expected to find " + expectedValue + " in " + techString);
			return false;
		}

		return true;
	}

}
