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
	}

	@Override
	public void startAction() {
        clickOnIndependentElement("playButton");
	}

}
