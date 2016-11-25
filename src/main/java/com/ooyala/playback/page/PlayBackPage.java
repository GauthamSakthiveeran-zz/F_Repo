package com.ooyala.playback.page;

import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.ooyala.facile.page.WebPage;
import com.ooyala.playback.url.PropertyReader;
import com.ooyala.playback.url.Url;
import com.relevantcodes.extentreports.ExtentTest;

public abstract class PlayBackPage extends WebPage {

	public static Logger logger = Logger.getLogger(PlayBackPage.class);

	protected ExtentTest extentTest;

	protected PropertyReader propertyReader;

	public PlayBackPage(WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	protected String getIndexFileName() {
		return "resources/appElementsIndex.xml";
	}

	@Override
	protected String getLocalizedPageElementString(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean waitForPage() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getPlatform() {
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		String platformName = cap.getPlatform().toString();
		return platformName;
	}

	@Override
	protected boolean clickOnIndependentElement(String elementKey) {
		try {
			return super.clickOnIndependentElement(elementKey);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception occured while clicking on element with key "
					+ elementKey);
			logger.error("Calling clickOnHiddenElement function on the element "
					+ elementKey);
			return clickOnHiddenElement(elementKey);
		}

	}

	public boolean clickOnHiddenElement(String elementKey) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = getWebElement(elementKey);
			if (element != null)
				js.executeScript("arguments[0].click()", element);
			else
				return false;
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Exception while clicking on hidden element "
					+ ex.getLocalizedMessage());
			return false;
		}
	}

	public String getBrowser() {
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		String browserName = cap.getBrowserName().toLowerCase();
		return browserName;
	}

	public void loadingSpinner() {
		int time = 0;
		while (true) {
			if (time <= 120) {
				try {
					boolean result = isElementVisible("SPINNER");
					Thread.sleep(1000);
					time++;
				} catch (Exception e) {
					break;
				}
			} else {
				logger.info("Loading spinner is not vanishing i.e it occured more that 2 minutes");
				break;
			}
		}
	}

	public void setExtentTest(ExtentTest test) {
		this.extentTest = test;
	}

	/**
	 * checking to see if the protocol is hds or hls or any protocol
	 * 
	 * @param data
	 * @param protocol
	 * @return
	 */
	public boolean isStreamingProtocolPrioritized(Map<String, String> data,
			String protocol) {
		if (data == null) {
			logger.error("url object is null");
			return false;
		}

		String playerParameter = data.get("options");
		if (playerParameter != null) {
			JSONObject json = new JSONObject(playerParameter);
			if (json != null && json.has("encodingPriority")) {
				JSONArray array = json.getJSONArray("encodingPriority");
				return array.get(0).equals(protocol);
			}
		}

		return false;
	}
}
