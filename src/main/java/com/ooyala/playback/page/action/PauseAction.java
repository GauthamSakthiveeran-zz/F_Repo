package com.ooyala.playback.page.action;

import static java.lang.Thread.sleep;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.page.PlayBackPage;

public class PauseAction extends PlayBackPage implements PlayerAction {

	private  Logger logger = Logger.getLogger(PauseAction.class);
	
	public PauseAction(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("pause");
		addElementToPageElements("play");
		addElementToPageElements("startscreen");
	}

	private boolean onScreen = false;
	
	public PauseAction onScreen(){
		onScreen = true;
		return this;
	}
	
	@Override
	public boolean startAction() throws Exception {
		
		if(onScreen){
			return startActionOnScreen();
		}
		
		boolean isElement;
		isElement = isElementPresent("HIDDEN_CONTROL_BAR");
		if (isElement) {
			logger.info("hovering mouse over the player");
			moveElement(getWebElement("HIDDEN_CONTROL_BAR"));
		}
		return clickOnIndependentElement("PAUSE_BUTTON");

	}

	private boolean startActionOnScreen() throws Exception {
		try {
			if (!waitOnElement("STATE_SCREENS", 50000))
				return false;
			if (!clickOnIndependentElement("STATE_SCREENS"))
				return false;
			logger.info("Clicked on screen to pause the video");
			sleep(5000);
		} catch (Exception e) {
			moveElement(getWebElement("PAUSE_BUTTON"));
			
			try {
				return (new PlayBackFactory(driver,extentTest)).getPauseAction().startAction();
			} catch (Exception e1) {
				return clickOnIndependentElement("STATE_SCREEN_SELECTABLE");
			}
		}
		return true;
	}

}
