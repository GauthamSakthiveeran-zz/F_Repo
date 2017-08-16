package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.url.UrlObject;
import com.relevantcodes.extentreports.LogStatus;

public class EmbedTabValidator extends PlayBackPage implements PlaybackValidator {

	private static Logger logger = Logger.getLogger(EmbedTabValidator.class);

	public EmbedTabValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("controlbar");
		addElementToPageElements("embedtab");
		addElementToPageElements("sharetab");
		addElementToPageElements("pause");
	}

	private UrlObject urlObject;

	public EmbedTabValidator setUrlObject(UrlObject urlObject) {
		this.urlObject = urlObject;
		return this;
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {

		try {
			if (isElementPresent("HIDDEN_CONTROL_BAR")) {
				logger.info("hovering mouse over the player");
				moveElement(getWebElement("HIDDEN_CONTROL_BAR"));
			}
			if (!(waitOnElement("SHARE_BTN", 10000) && clickOnIndependentElement("SHARE_BTN"))) {
				extentTest.log(LogStatus.FAIL, "SHARE_BTN is not found/ not clickable.");
				return false;
			}
		} catch (Exception e) {
			if (!(clickOnIndependentElement("MORE_OPTION_ITEM") && waitOnElement("SHARE_BTN", 10000)
					&& clickOnIndependentElement("SHARE_BTN"))) {
				extentTest.log(LogStatus.FAIL, "SHARE_BTN is not found/ not clickable.");
				return false;
			}

		}

		if (!isElementPresent("EMBED_TAB")) {
			extentTest.log(LogStatus.FAIL, "EMBED_TAB is not found");
			return false;
		}

		if (!clickOnIndependentElement("EMBED_TAB")) {
			extentTest.log(LogStatus.FAIL, "EMBED_TAB is not clickable");
			return false;
		}

		if (!isElementPresent("EMBED_FORM")) {
			extentTest.log(LogStatus.FAIL, "EMBED_FORM is not found");
			return false;
		}

		String iframeDetails = getWebElement("EMBED_FORM").getText();
		if (!iframeDetails.startsWith("<iframe") || !iframeDetails.endsWith("</iframe>")) {
			extentTest.log(LogStatus.FAIL, "iframe not found in embed tab");
			return false;
		}

		String iframesrc = "src='//player.ooyala.com/static/v4/candidate/latest/skin-plugin/iframe.html?ec="
				+ urlObject.getEmbedCode() + "&pbid=" + urlObject.getPlayerId() + "&pcode=" + urlObject.getPCode()
				+ "'";
		if (!iframeDetails.contains(iframesrc)) {
			extentTest.log(LogStatus.FAIL,
					"iframe src is incorrect, expected " + iframesrc + " actual : " + iframeDetails);
			return false;
		}
		
		if(isElementPresent("EMBED_CLOSE") && clickOnIndependentElement("EMBED_CLOSE")) {
			if(!isElementPresent("STATE_SCREEN_SELECTABLE")) {
				extentTest.log(LogStatus.FAIL, "Player's pause screen is not present after closing the embed tab");
				return false;
			}
		} else {
			extentTest.log(LogStatus.FAIL, "Unable to close the embed tab");
			return false;
		}

		return true;
	}

}
