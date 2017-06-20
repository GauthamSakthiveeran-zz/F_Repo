package com.ooyala.playback.page.action;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.page.PlayBackPage;
import com.relevantcodes.extentreports.LogStatus;

public class FullScreenAction extends PlayBackPage implements PlayerAction {

	private static Logger logger = Logger.getLogger(FullScreenAction.class);
	
	public FullScreenAction(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("fullscreen");
		addElementToPageElements("pause");
		addElementToPageElements("play");
		addElementToPageElements("controlbar");
	}

	@Override
	public boolean startAction() throws Exception {
		WebElement player = getWebElement("OOPLAYER");

		if (player == null) {
			return false;
		}
		if(!switchToControlBar()) {
			extentTest.log(LogStatus.FAIL, "Unable to switch to control bar");
			return false;
		}

		/*try{
			clickOnIndependentElement("STATE_SCREEN_SELECTABLE");
		}catch (Exception e){
			logger.info("error in click on state screen");
		}*/
		// if(!clickOnIndependentElement("FULLSCREEN_BTN")) {

		if (!clickOnIndependentElement("FULLSCREEN_BTN_1")) {
			return false;
		}

		if (!(getBrowser().equalsIgnoreCase("safari")
				|| getBrowser().equalsIgnoreCase("firefox")
				|| getBrowser().equalsIgnoreCase("internet explorer") || getPlatform()
				.equalsIgnoreCase("Android"))) {

			Thread.sleep(5000);
			
			if (!isElementPresent(By.id("fullscreenChanged_true"))) {
				return false;
			}

			logger.info("Changed into Fullscreen");
			extentTest.log(LogStatus.INFO, "Changed into Fullscreen");

		}
		extentTest.log(LogStatus.PASS, "Clicked on Full Screen button");
		return true;

	}
}
