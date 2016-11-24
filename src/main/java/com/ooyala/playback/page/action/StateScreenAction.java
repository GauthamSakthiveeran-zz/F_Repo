package com.ooyala.playback.page.action;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.page.PlayBackPage;

public class StateScreenAction extends PlayBackPage implements PlayerAction {

	public StateScreenAction(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("startscreen");
	}

	@Override
	public void startAction() throws Exception {
		WebElement element = getWebElement("STATE_SCREEN_SELECTABLE");
        Actions builder = new Actions(driver);
        builder.moveToElement(element).perform();
	}
	
}
