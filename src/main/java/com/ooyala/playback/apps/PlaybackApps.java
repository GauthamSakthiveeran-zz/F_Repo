package com.ooyala.playback.apps;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ooyala.facile.page.WebPage;

import io.appium.java_client.AppiumDriver;

public abstract class PlaybackApps extends WebPage {

	final static Logger logger = Logger.getLogger(PlaybackApps.class);

	public PlaybackApps(AppiumDriver driver) {
		super(driver);
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

	@Override
	public boolean waitOnElement(String elementKey, int timeout) {

		try {
			if (super.waitOnElement(elementKey, timeout)) {
				logger.info("Wait on element : " + elementKey + "");
				return true;
			} else {
				logger.info("Wait on element : " + elementKey + ", failed after " + timeout + " ms");
				return false;
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return false;

	}

	@Override
	protected boolean clickOnIndependentElement(String elementKey) {
		try {
			return super.clickOnIndependentElement(elementKey);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			logger.error("Exception occured while clicking on element " + elementKey);
			return false;
		}

	}

	public boolean handleLoadingSpinner() {
		int i = 0;
		int timeOut = 20;
		boolean flag = false;
		try {
			while (true) {

				if (i <= timeOut) {
					try {
						flag = driver.findElement(By.xpath("//XCUIElementTypeActivityIndicator[1]")).isDisplayed();
						if (!flag) {
							flag = true;
							break;
						}
						Thread.sleep(1000);
						i++;
						logger.info("Video Buffering...");
					} catch (Exception e) {
						return true;
					}
				} else {
					logger.info("Video has been buffering for a really long time i.e it occured more that 20 secs");
					flag = false;
					break;
				}
			}

		} catch (Exception e) {
			logger.error("Loading spinner not present !!!");
		}
		return flag;
	}
	
	public boolean tapScreenIfRequired() {
    	if (!isElementPresent(By.xpath("//XCUIElementTypeToolbar[1]"))) {
    		return tapScreen();
    	}
    	return true;
    }
	
	public boolean tapScreen() {
		return clickOnIndependentElement(By.xpath("//XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther"));
    }
	
}
