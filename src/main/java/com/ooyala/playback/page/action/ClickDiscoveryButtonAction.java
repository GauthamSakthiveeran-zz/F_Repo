package com.ooyala.playback.page.action;

import static java.lang.Thread.sleep;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.page.PlayBackPage;

/**
 * Created by soundarya on 11/17/16.
 */
public class ClickDiscoveryButtonAction extends PlayBackPage implements
		PlayerAction {

	private Logger logger = Logger.getLogger(ClickDiscoveryButtonAction.class);

	public ClickDiscoveryButtonAction(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("discovery");
		addElementToPageElements("controlbar");

	}

	@Override
	public boolean startAction() throws Exception {
		try {

			if (!clickDiscoveryButton())
				return false;

			sleep(2000);
			if (!isElementPresent("CONTENT_SCREEN")) {
				if (!clickDiscoveryButton())
					return false;
			}
		} catch (Exception e) {
			logger.error("exception " + e.getMessage());
			if (!clickOnIndependentElement("MORE_OPTION_ICON"))
				return false;
			Thread.sleep(2000);
			if (!clickDiscoveryButton())
				return false;
		}
		return true;
	}

	private boolean clickDiscoveryButton() {
		return clickOnIndependentElement("DISCOVERY_BTN");
	}
}
