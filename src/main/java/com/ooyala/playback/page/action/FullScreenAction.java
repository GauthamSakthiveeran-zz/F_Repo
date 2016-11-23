package com.ooyala.playback.page.action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.page.PlayBackPage;

public class FullScreenAction extends PlayBackPage implements PlayerAction {

	public FullScreenAction(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("fullscreen");
		addElementToPageElements("pause");
	}

	@Override
	public void startAction() throws Exception {
		WebElement player = getWebElement("ooplayer");
		Actions action = new Actions(driver);
		action.moveToElement(player).perform();
		waitOnElement("FULLSCREEN_BTN", 60);

		if (getPlatform().equalsIgnoreCase("Android")) {
			clickOnIndependentElement("FULLSCREEN_BTN");
		} else {
			clickOnIndependentElement("PAUSE_BUTTON");
			clickOnIndependentElement("FULLSCREEN_BTN");
		}

		if (!(getBrowser().equalsIgnoreCase("safari")
				|| getBrowser().equalsIgnoreCase("firefox")
				|| getBrowser().equalsIgnoreCase("internet explorer") || getPlatform()
				.equalsIgnoreCase("Android"))) {
			waitOnElement(By.id("fullscreenChangedtrue"), 60);
			logger.info("Changed into Fullscreen");
		}

	}

}
