package com.ooyala.playback.page;

import static java.lang.Character.CONTROL;
import static java.net.URLDecoder.decode;
import static org.openqa.selenium.Keys.DELETE;
import static org.testng.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;

/**
 *
 * @author dmanohar
 *
 */
public class EncodingValidator extends PlayBackPage implements PlaybackValidator {

	private static Logger logger = Logger.getLogger(EncodingValidator.class);

	public EncodingValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("page");
	}

	public boolean validate(String element, int timeout) throws Exception {

		String result = decode(driver.getCurrentUrl(), "UTF-8");
		if (result == null)
			return false;

		String[] options = result.split("options=");
		if (options == null || options.length < 2)
			return false;

		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(options[1]);
		Object expectedEncodings = "";
		if (obj.containsKey("encodingPriority")) {
			Object actualEncodings = obj.get("encodingPriority");
			logger.info("\nActual encodingPriority :\n" + actualEncodings);
			expectedEncodings = ((JavascriptExecutor) driver).executeScript("return pp.parameters.encodingPriority");
			logger.info("\nExpected encodingPriority :\n" + expectedEncodings);
			assertEquals(actualEncodings, expectedEncodings, "Encoding Priorities are as expected");
			if (!actualEncodings.equals(expectedEncodings))
				return false;

			StreamTypeValidator streams = new PlayBackFactory(driver, extentTest).getStreamTypeValidator();

			org.json.simple.JSONArray json = (org.json.simple.JSONArray) obj.get("encodingPriority");

			for (int i = 0; i < json.size(); i++) {
				String encoding = json.get(i).toString();
				if (encoding.contains("hls")) {
					return streams.setStreamType("m3u8").validate("videoPlayingurl", 6000);
				}
				if (encoding.contains("dash")) {
					return streams.setStreamType("mpd").validate("videoPlayingurl", 6000);
				}
				if (encoding.contains("mp4")) {
					return streams.setStreamType("mp4").validate("videoPlayingurl", 6000);
				}
				if (encoding.contains("hds")) {
					return streams.setStreamType("f4m").validate("videoPlayingurl", 6000);
				}
			}

		}

		return false;

	}

	public String getNewUrl(String parameter, String browser) {
		clickOnIndependentElement("OPTIONAL");
		waitOnElement("PLAYER_PARAMETER_INPUT", 20000);

		if (browser.equalsIgnoreCase("internet explorer")) {
			WebElement playerParameter = getWebElement("PLAYER_PARAMETER_INPUT");
			playerParameter.sendKeys(CONTROL + "a");
			playerParameter.sendKeys(DELETE);
		} else
			clearTextFromElement("PLAYER_PARAMETER_INPUT");

		writeTextIntoTextBox("PLAYER_PARAMETER_INPUT", parameter);
		clickOnIndependentElement("TEST_VIDEO");
		waitForPage();

		return driver.getCurrentUrl();
	}

	public boolean validateVCEventsFromConsole(){
		if (!getLogsFromConsole().get(0).isEmpty() && getLogsFromConsole().get(0) != null
				&& !getLogsFromConsole().get(1).isEmpty() && getLogsFromConsole().get(1) != null){
            logger.info("console logs are : \n"+getLogsFromConsole());
            return true;
		}

        return false;
	}

}
