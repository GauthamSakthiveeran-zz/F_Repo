package com.ooyala.playback.page;

import static java.lang.Thread.sleep;

import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.ooyala.facile.page.WebPage;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public abstract class PlayBackPage extends WebPage {

	private static Logger logger = Logger.getLogger(PlayBackPage.class);

	protected ExtentTest extentTest;

	public PlayBackPage(WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	protected String getIndexFileName() {
		return "resources/appElementsIndex.xml";
	}

	@Override
	protected String getLocalizedPageElementString(String arg0) {
		// TODO Auto:generated method stub
		return null;
	}

	@Override
	public boolean waitForPage() {
		// TODO Auto:generated method stub
		return false;
	}

	public String getPlatform() {
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		String platformName = cap.getPlatform().toString();
		return platformName;
	}

	@Override
	public boolean waitOnElement(String elementKey, int timeout) {

		try {
			if (super.waitOnElement(elementKey, timeout)) {
				extentTest.log(LogStatus.PASS, "Wait on element : "
						+ elementKey + "");
				return true;
			} else {
				extentTest.log(LogStatus.INFO, "Wait on element : "
						+ elementKey + ", failed after " + timeout + " ms");
				return false;
			}

		} catch (Exception ex) {
			extentTest.log(LogStatus.INFO, "wait on element " + elementKey
					+ "  failed with exception " + ex.getMessage());
			ex.printStackTrace();
		}
		return false;

	}

	@Override
	protected boolean clickOnIndependentElement(String elementKey) {
		try {
			boolean flag = super.clickOnIndependentElement(elementKey);
			if (!flag) {
				flag = clickOnHiddenElement(elementKey);

			} else
				logger.info("Clicked on element :" + elementKey);

			return flag;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception occured while clicking on element "
					+ elementKey);
			extentTest
					.log(LogStatus.INFO,
							"Exception occured while clicking on element "
									+ elementKey);
			logger.error("Calling clickOnHiddenElement function on the element "
					+ elementKey);
			extentTest.log(LogStatus.INFO,
					"Calling clickOnHiddenElement function on the element "
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
			else {
				extentTest.log(LogStatus.INFO, "Element not found : "
						+ elementKey);
				return false;
			}
			extentTest.log(LogStatus.PASS, "Clicked on hidden element : "
					+ elementKey);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Exception while clicking on hidden element "
					+ ex.getLocalizedMessage());
			extentTest.log(
					LogStatus.INFO,
					"Exception while clicking on hidden element "
							+ ex.getLocalizedMessage());
			return false;
		}
	}

	// Added moveElement since Safari doesnt handle Action API interactions
	public boolean moveElement(WebElement element) {
		if (getBrowser().equalsIgnoreCase("safari")) {
			return onMouseOverSafari(element);
		} else {
			return onmouseOver(element);
		}
	}

	public boolean onmouseOver(WebElement element) {
		boolean result = false;
		try {
			Actions action = new Actions(driver);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			action.moveToElement(element).build().perform();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;

	}

	public boolean onMouseOverSafari(WebElement element) {
		boolean result = false;
		try {
			String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(mouseOverScript, element);
			Thread.sleep(1000);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public String getBrowser() {
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		String browserName = cap.getBrowserName().toLowerCase();
		return browserName;
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
	 * @throws Exception
	 */
	public boolean isStreamingProtocolPrioritized(String protocol)
			throws Exception {
		Map<String, String> data = parseURL();
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

	public Map<String, String> parseURL() throws Exception {
		String urlString = driver.getCurrentUrl();
		if (urlString != null && !urlString.isEmpty()) {
			URL url = new URL(urlString);
			Map<String, String> query_pairs = new HashMap<String, String>();
			String query = url.getQuery();
			String[] pairs = query.split("&");
			for (String pair : pairs) {
				int index = pair.indexOf("=");
				query_pairs.put(
						URLDecoder.decode(pair.substring(0, index), "UTF-8"),
						URLDecoder.decode(pair.substring(index + 1), "UTF-8"));

			}
			return query_pairs;
		}

		return null;
	}

	public boolean loadingSpinner() {
		int time = 0;
		boolean flag;

		while (true) {

			// Giving hardcoded end time as 2 minutes i.e it will check loading
			// spinner upto 2 min otherwise will break
			if (time <= 120) {
				try {
					flag = driver.findElement(By.className("oo-spinner"))
							.isDisplayed();
					if (!flag) {
						flag = true;
						break;
					}
					Thread.sleep(1000);
					time++;
					logger.info("In loading spinner");
				} catch (Exception e) {
					return true;
				}
			} else {
				logger.info("Loading spinner is not vanishing i.e it occured more that 2 minutes");
				flag = false;
				break;
			}

		}
		return flag;

	}
	
	protected void closeOtherWindows(String baseWindowHdl) throws Exception{
		sleep(2000);
		java.util.Set<java.lang.String> windowHandles = driver
				.getWindowHandles();
		int count = windowHandles.size();
		logger.info("Window handles : " + count);

		for (String winHandle : driver.getWindowHandles()) {
			if (!winHandle.equals(baseWindowHdl)) {
				driver.switchTo().window(winHandle);
				driver.close();
				driver.switchTo().window(baseWindowHdl);
			}
		}
	}
}
