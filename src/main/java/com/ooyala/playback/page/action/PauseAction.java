package com.ooyala.playback.page.action;

import org.openqa.selenium.By;
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
	}

	@Override
	public void startAction() {
	        boolean isElementPresent;
	        Actions action = new Actions(driver);
	        loadingSpinner();
	        try {
	            //isElementPresent = isElementVisible("HIDDEN_CONTROL_BAR");
	            //if(!isElementPresent) {
	                System.out.println("hovering mouse over the player");
	                action.moveToElement(driver.findElement(By.className("oo-state-screen"))).perform();
	            //}
	            clickOnIndependentElement("PAUSE_BUTTON");
	        } catch (ElementNotVisibleException e) {
	        	clickOnHiddenElement("PAUSE_BUTTON");
	        }
	}

}

