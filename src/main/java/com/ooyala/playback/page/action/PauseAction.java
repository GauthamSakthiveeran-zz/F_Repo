package com.ooyala.playback.page.action;

import static java.lang.Thread.sleep;

import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.page.PlayBackPage;

public class PauseAction extends PlayBackPage implements PlayerAction {

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

	@Override
	public boolean startAction() {
		boolean isElement;
		Actions action = new Actions(driver);
		try {
			isElement = isElementPresent("HIDDEN_CONTROL_BAR");
			if (isElement) {
				logger.info("hovering mouse over the player");
				action.moveToElement(getWebElement("HIDDEN_CONTROL_BAR"))
						.perform();
			}
			return clickOnIndependentElement("PAUSE_BUTTON");
		} catch (ElementNotVisibleException e) {
			logger.error(e.getMessage());
			return clickOnIndependentElement("PAUSE_BUTTON");
		}
	}
	
	public void startActionOnScreen() throws Exception{
		try {
			waitOnElement("STATE_SCREENS", 50);
			clickOnIndependentElement("STATE_SCREENS");
			logger.info("Clicked on screen to pause the video");
		} catch (Exception e) {
			Actions action = new Actions(driver);
			action.moveToElement(getWebElement("PAUSE_BUTTON")).build()
					.perform();
			sleep(5000);
			try {
				PlayBackFactory.getInstance(driver).getPauseAction().startAction();
				logger.info("Clicked on Pause button to pause the video");
			} catch (Exception e1) {
				clickOnIndependentElement("STATE_SCREEN_SELECTABLE");
				logger.info("Clicked on screen which is selectable to pause the video");
			}
		}
	}

}
