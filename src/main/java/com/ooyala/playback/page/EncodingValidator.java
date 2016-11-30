package com.ooyala.playback.page;

import static java.lang.Character.CONTROL;
import static java.net.URLDecoder.decode;
import static org.openqa.selenium.Keys.DELETE;
import static org.testng.Assert.assertEquals;

import com.ooyala.playback.page.action.PlayAction;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
		addElementToPageElements("page");
	}

	String testUrl = new String();

	public void setTestUrl(String testUrl) {
		this.testUrl = testUrl;
	}

	public boolean validate(String element, int timeout) throws Exception {

		return verifyEncodingPriority(testUrl);

		}

	public String getNewUrl(String parameter, String browser){
		clickOnIndependentElement("OPTIONAL");
		waitOnElement("PLAYER_PARAMETER_INPUT",20000);

		if (browser.equalsIgnoreCase("internet explorer")){
			WebElement playerParameter = getWebElement("PLAYER_PARAMETER_INPUT");
			playerParameter.sendKeys(CONTROL + "a");
			playerParameter.sendKeys(DELETE);
		} else
			clearTextFromElement("PLAYER_PARAMETER_INPUT");

		writeTextIntoTextBox("PLAYER_PARAMETER_INPUT",parameter);
		clickOnIndependentElement("TEST_VIDEO");
		waitForPage();

		return driver.getCurrentUrl();
	}

	public boolean verifyEncodingPriority(String url) throws Exception{
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
			logger.info("\nActual encodingPriority :\n" + actualEncodings);
			expectedEncodings = ((JavascriptExecutor) driver)
					.executeScript("return pp.parameters.encodingPriority");
			logger.info("\nExpected encodingPriority :\n" + expectedEncodings);
			assertEquals(actualEncodings, expectedEncodings,
					"Encoding Priorities are as expected");
			if(actualEncodings.equals(expectedEncodings))
				return true;
		}

		return false;
	}


}
