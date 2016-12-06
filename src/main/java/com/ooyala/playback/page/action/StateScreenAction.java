package com.ooyala.playback.page.action;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.page.PlayBackPage;

public class StateScreenAction extends PlayBackPage implements PlayerAction {

	public StateScreenAction(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("startscreen");
	}

	@Override
	public boolean startAction() throws Exception {
		try {
			WebElement element = getWebElement("STATE_SCREEN_SELECTABLE");
			if (element == null)
				return false;
			moveElement(element);
			return true;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return false;
		}
	}

}
