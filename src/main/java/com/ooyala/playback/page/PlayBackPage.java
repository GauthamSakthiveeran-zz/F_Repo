package com.ooyala.playback.page;

import static java.lang.Thread.sleep;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ooyala.playback.url.UrlObject;
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
import com.ooyala.playback.factory.PlayBackFactory;
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
				extentTest.log(LogStatus.PASS, "Wait on element : " + elementKey + "");
				return true;
			} else {
				extentTest.log(LogStatus.INFO, "Wait on element : " + elementKey + ", failed after " + timeout + " ms");
				return false;
			}

		} catch (Exception ex) {
			extentTest.log(LogStatus.INFO,
					"wait on element " + elementKey + "  failed with exception " + ex.getMessage());
			logger.error(ex.getMessage());
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
			logger.error(ex.getMessage());
			logger.error("Exception occured while clicking on element " + elementKey);
			extentTest.log(LogStatus.INFO, "Exception occured while clicking on element " + elementKey);
			logger.info("Calling clickOnHiddenElement function on the element " + elementKey);
			extentTest.log(LogStatus.INFO, "Calling clickOnHiddenElement function on the element " + elementKey);
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
				extentTest.log(LogStatus.INFO, "Element not found : " + elementKey);
				return false;
			}
			extentTest.log(LogStatus.PASS, "Clicked on hidden element : " + elementKey);
			return true;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			logger.error("Exception while clicking on hidden element " + ex.getLocalizedMessage());
			extentTest.log(LogStatus.INFO, "Exception while clicking on hidden element " + ex.getLocalizedMessage());
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

	public boolean touchPress(WebElement element){
	    logger.info("Pressing on "+element);
	    try {
            Actions a = new Actions(driver);
            a.clickAndHold(element).build().perform();
            logger.info("Pressed on "+element);
            extentTest.log(LogStatus.INFO,"Pressed on "+element);
            return true;
        } catch (Exception ex){
	        logger.error("Can not click and hold on "+element);
	        extentTest.log(LogStatus.FAIL,"Can not click and hold on "+element);
	        return false;
        }
	}

	public boolean onmouseOver(WebElement element) {
		boolean result = false;
		boolean flag = false;
		try {
			Actions action = new Actions(driver);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			action.moveToElement(element).build().perform();
			result = true;
		} catch (Exception e) {
			logger.info(e.getMessage());
			result = false;
			flag = true;
		}

		if (flag){
			try {
			    logger.info("trying to hover on "+element+" using javascript");
				String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
				driver.executeScript(mouseOverScript, element);
				result = true;
                logger.info("hovered on "+element+" using javascript");
			}catch (Exception ex){
				logger.info(ex.getMessage());
				logger.error("Not able to hover on "+element+" using " +
                        "javascript after trying to move on element using selenium moveOnElement method");
				result = false;
			}
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
			logger.error(e.getMessage());
			result = false;
		}
		return result;
	}

	public String getBrowser() {
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		String browserName = cap.getBrowserName().toLowerCase();
		return browserName;
	}

	public void setExtentTest(ExtentTest extentTest) {
		this.extentTest = extentTest;
	}

	/**
	 * checking to see if the protocol is hds or hls or any protocol
	 *
	 * @param protocol
	 * @return
	 * @throws Exception
	 */
	public boolean isStreamingProtocolPrioritized(String protocol) throws Exception {
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
				query_pairs.put(URLDecoder.decode(pair.substring(0, index), "UTF-8"),
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
					flag = driver.findElement(By.className("oo-spinner")).isDisplayed();
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

	protected int getWindowHandleCount() {
		Set<String> windowHandles = driver.getWindowHandles();
		return windowHandles.size();
	}

	protected void closeOtherWindows(String baseWindowHdl) throws Exception {
		sleep(2000);

		int count = getWindowHandleCount();

		logger.info("Window handles : " + count);

		for (String winHandle : driver.getWindowHandles()) {
			if (!winHandle.equals(baseWindowHdl)) {
				driver.switchTo().window(winHandle);
				driver.close();
				driver.switchTo().window(baseWindowHdl);
			}
		}
	}

	public boolean clearCache() throws Exception {
		for (int i = 0; i < 20; i++) {
			((JavascriptExecutor) driver).executeScript(String.format("window.localStorage.clear();"));
		}
		return true;
	}

	public boolean adPlaying(boolean checkOnce) throws Exception {
		int time = 0;
		boolean flag;

		IsAdPlayingValidator adPlaying = new PlayBackFactory(driver, extentTest).isAdPlaying();

		if (checkOnce) {
			Thread.sleep(1000);
			return adPlaying.validate("", 1000);
		}

		while (true) {
			if (time <= 150) {
				try {
					flag = adPlaying.validate("", 1000);
					if (!flag) {
						flag = true;
						break;
					}
					Thread.sleep(1000);
					time++;
				} catch (Exception e) {
					return true;
				}
			} else {
				flag = false;
				break;
			}
		}
		if (!flag) {
			extentTest.log(LogStatus.FAIL, "Ad is playing for a really long time. Some issue.");
			assert false;
		}
		return flag;
	}

	public boolean waitTillAdPlays() {
		IsAdPlayingValidator adPlaying = new PlayBackFactory(driver, extentTest).isAdPlaying();
		boolean flag = false;
		int time = 0;
		while (true) {
			if (time <= 120) {
				try {
					flag = adPlaying.validate("", 1000);
					Thread.sleep(1000);
					time++;
				} catch (Exception e) {
				}
			} else {
				break;
			}

			if (flag) {
				return true;
			}
		}
		extentTest.log(LogStatus.FAIL, "Ad is not playing after waiting for a long time.");
		return false;
	}

	public boolean isPageLoaded() {
		/*WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(new Predicate<WebDriver>() {
			public boolean apply(WebDriver webDriver) {
				return driver.executeScript("return typeof pp").toString().equals("object");
			}
		});
		if (!driver.executeScript("return typeof pp").toString().equals("object")) {
			logger.error("pp object is not loaded");
			extentTest.log(LogStatus.FAIL, "pp object is not loaded");
			return false;
		}
		return true;*/
		int count = 120;
		while (count >= 0) {
			if (driver.executeScript("return typeof pp").toString().equals("object")) {
				return true;
			}
			count--;
		}
		return false;
	}

	public boolean isAdPluginPresent(String adPlugin) throws Exception {
		Map<String, String> map = parseURL();
		if (map != null && map.get("ad_plugin") != null && map.get("ad_plugin").contains(adPlugin.toLowerCase())) {
			return true;
		}
		return false;
	}

	// Use this method after page is loaded completely.
	public boolean startRecordingConsoleLogsForAd() {
		try {
			logger.info("Started recording VC related events in console log for ads");
			driver.executeScript("OO.DEBUG.startRecordingConsoleOutput(\"VC: For video 'ads'\");");
			return true;
		} catch (Exception e) {
			logger.error("Not able to record logs....");
			return false;
		}
	}

	// Use this method after ad played completely or video starts playing
	public void stopRecordingConsoleLog() {
		logger.info("Stopping recording console logs");
		driver.executeScript("OO.DEBUG.stopRecordingConsoleOutput();");
		logger.info("Stopped recording console logs");
	}

	public ArrayList<String> getLogsFromConsole() {
		@SuppressWarnings("unchecked")
		ArrayList<String> consoleOutput = (ArrayList<String>) driver.executeScript("return OO.DEBUG.consoleOutput");
		if (consoleOutput.size() == 0) {
			logger.info("*** there no logs recorded ***");
		}
		return consoleOutput;
	}

	public boolean isVideoPluginPresent(String videoPlugin) throws Exception {
		Map<String, String> map = parseURL();
		if (map != null && map.get("video_plugins") != null
				&& map.get("video_plugins").contains(videoPlugin.toLowerCase())) {
			return true;
		}
		return false;
	}

	public double getPlayAheadTime() {
		return Double
				.parseDouble(((JavascriptExecutor) driver).executeScript("return pp.getPlayheadTime();").toString());
	}
	
	public double getDuration() {
		return Double
				.parseDouble(((JavascriptExecutor) driver).executeScript("return pp.getDuration();").toString());
	}

    public boolean isAnalyticsElementPreset(String element){
        if (!waitOnElement(By.id(element),100000)){
            extentTest.log(LogStatus.FAIL,element+" element is not present");
            logger.error(element+" element is not present");
            return false;
        }
        extentTest.log(LogStatus.PASS,element+" element is present");
        logger.info(element+" element is present");
        return true;
    }
}
