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
	public boolean startAction() throws Exception {
		WebElement player = getWebElement("ooplayer");
		
		if(player==null) return false;
		
		Actions action = new Actions(driver);
		action.moveToElement(player).perform();
		if(!waitOnElement("FULLSCREEN_BTN", 60000)) return false;

		if (getPlatform().equalsIgnoreCase("Android")) {
			if(!clickOnIndependentElement("FULLSCREEN_BTN")) return false;
		} else {
			if(!clickOnIndependentElement("PAUSE_BUTTON")) return false;
			if(!clickOnIndependentElement("FULLSCREEN_BTN")) return false;
		}

		if (!(getBrowser().equalsIgnoreCase("safari")
				|| getBrowser().equalsIgnoreCase("firefox")
				|| getBrowser().equalsIgnoreCase("internet explorer") || getPlatform()
				.equalsIgnoreCase("Android"))) {
			
			if(!waitOnElement(By.id("fullscreenChangedtrue"),60000)) return false;
			logger.info("Changed into Fullscreen");
			
		}
		
		return true;

	}

}
