package com.ooyala.playback.page.action;

import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

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
	}

	@Override
	public void startAction() {
		boolean isElement;
		Actions action = new Actions(driver);
		// loadingSpinner();
		try {
			isElement = isElementPresent("HIDDEN_CONTROL_BAR");
			if (!isElement) {
				System.out.println("hovering mouse over the player");
				action.moveToElement(getWebElement("oo-state-screen"))
						.perform();
			}
			clickOnIndependentElement("PAUSE_BUTTON");
		} catch (ElementNotVisibleException e) {
			clickOnIndependentElement("PAUSE_BUTTON");
		}
	}

}
