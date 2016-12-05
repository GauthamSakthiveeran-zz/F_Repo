package com.ooyala.playback.page.action;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.page.PlayBackPage;

public class PlayAction extends PlayBackPage implements PlayerAction {

	public PlayAction(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("play");
		addElementToPageElements("startscreen");
	}

	@Override
	public boolean startAction() throws Exception{
		return clickOnIndependentElement("PLAY_BUTTON");

	}
	
	public boolean startActionOnScreen() throws Exception{
		try {
			if(!waitOnElement("STATE_SCREEN_SELECTABLE", 5000)) return false;
			if(!clickOnIndependentElement("STATE_SCREEN_SELECTABLE")) return false;
		} catch (Exception e) {
			e.printStackTrace();
            moveElement(getWebElement("PLAY_BUTTON"));
			Thread.sleep(5000);
			return startAction();
		}
		return true;
	}
	
}
