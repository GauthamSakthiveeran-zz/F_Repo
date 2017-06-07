package com.ooyala.playback.utils;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class JSScriptInjection {

	private Logger logger = Logger.getLogger(JSScriptInjection.class);

	String[] jsUrl;
	ExtentTest extentTest;
	WebDriver driver;

	public JSScriptInjection(String[] jsUrl, ExtentTest extentTest, WebDriver driver) {
		this.jsUrl = jsUrl;
		this.extentTest = extentTest;
		this.driver = driver;
	}

	public void injectScript() throws Exception {
		if (jsUrl != null && jsUrl.length > 0) {
			for (String url : jsUrl) {
				try {
					logger.info("JS - " + url);
					injectScript(url);
				} catch (Exception e) {
					// e.printStackTrace();
					logger.error(e.getMessage());
					logger.info("Retrying...");
					injectScript(url);
				}
			}
			extentTest.log(LogStatus.PASS, "Javascript injection is successful");
		}
	}

	@SuppressWarnings("unused")
	private void injectScript(String scriptURL) throws Exception {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Object object = js.executeScript("function injectScript(url) {\n"
				+ "   var script = document.createElement ('script');\n" + "   script.src = url;\n"
				+ "   var head = document.getElementsByTagName( 'head')[0];\n" + "   head.appendChild(script);\n"
				+ "}\n" + "\n" + "var scriptURL = arguments[0];\n" + "injectScript(scriptURL);", scriptURL);
		Thread.sleep(1000); // to avoid js failures

		if (scriptURL.contains("common"))
			object = js.executeScript("subscribeToCommonEvents();");
		else
			object = js.executeScript("subscribeToEvents();");
	}
}
