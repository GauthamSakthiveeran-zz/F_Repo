package com.ooyala.playback.page;

import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.publishingrules.APIUtils;
import com.relevantcodes.extentreports.LogStatus;

public class SyndicationRuleValidator extends PlayBackPage implements PlaybackValidator {

	APIUtils api = new APIUtils();

	public SyndicationRuleValidator(WebDriver webDriver) {
		super(webDriver);
		addElementToPageElements("play");
	}

	public static Logger logger = Logger.getLogger(SyndicationRuleValidator.class);

	@Override
	public boolean validate(String element, int timeout) throws Exception {

		return false;
	}

	public boolean updatePublishingRule(String embedCode, String api_key, String secret, boolean defaultGroup)
			throws Exception {

		HashMap<String, String> rules = api.getPublishingRuleIds(api_key, secret);

		if (rules == null) {
			extentTest.log(LogStatus.FAIL, "Issue with getting the publishing rules");
			return false;
		}

		String publishingRuleId = "";

		if (defaultGroup) {
			publishingRuleId = rules.get("default");
		} else {
			publishingRuleId = rules.get("specific");
		}

		if (publishingRuleId.isEmpty()) {
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

	public boolean createEntitlement(String embedCode, String pcode, int deviceCount) throws Exception {

		if (api.isEntitlementAvailable(pcode, embedCode)) {
			return true;
		} else {

			if (!api.addEntitlement(pcode, embedCode, deviceCount)) {
				extentTest.log(LogStatus.FAIL, "Failed to add entitlement");
				return false;
			}
			return true;
		}
	}

	public boolean deleteEntitlement(String embedCode, String pcode) throws Exception {

		if (!api.isEntitlementAvailable(pcode, embedCode)) {
			return true;
		} else {
			if (!api.deleteEntitlement(pcode, embedCode)) {
				extentTest.log(LogStatus.FAIL, "Failed to delete entitlement");
				return false;
			}
			return true;
		}
	}

	public boolean deleteDevices(String pcode) throws IOException {
		if (!api.deleteAllDevices(pcode)) {
			extentTest.log(LogStatus.FAIL, "Failed to delete devices");
			return false;
		}
		return true;
	}

	public boolean isDeviceRegistered(String pcode) throws IOException {
		String userAgent = getUserAgent();
		HashMap<String, String> devices = api.getDevices(pcode);

		if (devices == null) {
			extentTest.log(LogStatus.INFO, "No devices registered.");
			return false;
		}

		if (devices.containsKey(userAgent)) {
			extentTest.log(LogStatus.INFO, "Device " + userAgent + " has been registered.");
			return true;
		}

		extentTest.log(LogStatus.INFO, "Device " + userAgent + " is not registered.");
		return false;
	}

	public boolean initializeNewDriverAndValidateError(String url, WebDriver newDriver, String errorCode,
			String errorDescription, String pcode) throws Exception {
		if (newDriver != null) {
			newDriver.get(url);

			PlayValidator playValidator = new PlayBackFactory(newDriver, extentTest).getPlayValidator();
			PlayAction playAction = new PlayBackFactory(newDriver, extentTest).getPlayAction();
			ErrorDescriptionValidator error = new PlayBackFactory(newDriver, extentTest).getErrorDescriptionValidator();

			if (playValidator.waitForPage()) {
				if (playAction.startAction()) {
					newDriver.navigate().refresh();
					return playValidator.waitForPage() && playAction.startAction() && error.expectedErrorCode(errorCode)
							.expectedErrorDesc(errorDescription).validate("", 60000);
				} else {
					extentTest.log(LogStatus.FAIL, "Page is not loaded");
				}

			} else {
				extentTest.log(LogStatus.FAIL, "Page is not loaded");
			}

			return false;
		}
		extentTest.log(LogStatus.FAIL, "new driver initialized is null");
		return false;
	}

	public boolean validatePlayback(WebDriver newDriver) throws Exception {
		PlayValidator playValidator = new PlayBackFactory(newDriver, extentTest).getPlayValidator();
		SeekValidator seek = new PlayBackFactory(newDriver, extentTest).getSeekValidator();
		EventValidator event = new PlayBackFactory(newDriver, extentTest).getEventValidator();

		if (playValidator.waitForPage()) {
			return playValidator.validate("playing_1", 10000) && seek.validate("seeked_1", 60000)
					&& event.validate("played_1", 60000);
		}
		extentTest.log(LogStatus.FAIL, "video not playable");
		return false;
	}

}
