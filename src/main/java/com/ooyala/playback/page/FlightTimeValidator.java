package com.ooyala.playback.page;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.publishingrules.BacklotAPIUtils;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by suraj on 3/23/17.
 */
public class FlightTimeValidator extends PlayBackPage implements PlaybackValidator {

	private static Logger logger = Logger.getLogger(FlightTimeValidator.class);

	public FlightTimeValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("play");
	}

	public boolean validate(String element, int timeout) throws Exception {
		// TODO
		return true;
	}

	private HashMap<String, String> rules = null;

	public boolean updatePublishingRule(String embedCode, String api_key, String secret, boolean defaultGroup)
			throws Exception {
		BacklotAPIUtils api = new BacklotAPIUtils();
		if (rules == null) {
			rules = api.getPublishingRuleIds(api_key, secret);
		}
		
		if(rules==null) {
			extentTest.log(LogStatus.FAIL, "Issue with getting the publishing rules");
			return false;
		}

		String publishingRuleId = "";

		if (defaultGroup) {
			publishingRuleId = rules.get("default");
		} else {
			publishingRuleId = rules.get("specific");
		}
		
		if(publishingRuleId.isEmpty()) {
			extentTest.log(LogStatus.FAIL, "Issue with getting the publishing rules");
			return false;
		}

		if (api.updatePublishingRule(embedCode, publishingRuleId, api_key, secret)) {
			return true;
		} else {
			extentTest.log(LogStatus.FAIL, "Issue with updating publishing rule");
			return false;
		}

	}

}
