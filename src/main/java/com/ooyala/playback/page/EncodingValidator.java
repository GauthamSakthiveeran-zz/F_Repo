package com.ooyala.playback.page;

import static java.net.URLDecoder.decode;
import static org.testng.Assert.assertEquals;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * 
 * @author dmanohar
 *
 */
public class EncodingValidator extends PlayBackPage implements
		PlaybackValidator {

	public EncodingValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
	}

	String testUrl = new String();

	public void setTestUrl(String testUrl) {
		this.testUrl = testUrl;
	}

	public boolean validate(String element, int timeout) throws Exception {
		String result = decode(driver.getCurrentUrl(), "UTF-8");
		if(result==null) 
			return false;
		String[] options = result.split("options=");
		if(options==null || options.length<2)
			return false;
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(options[1]);
		Object expectedEncodings = "";
		if (obj.containsKey("encodingPriority")) {
			Object actualEncodings = obj.get("encodingPriority");
			expectedEncodings = ((JavascriptExecutor) driver)
					.executeScript("return pp.parameters.encodingPriority");
			if(actualEncodings==expectedEncodings)
				return true;
		}
		return false;
	}

}
