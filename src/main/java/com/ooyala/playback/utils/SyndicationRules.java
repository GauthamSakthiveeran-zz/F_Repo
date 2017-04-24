package com.ooyala.playback.utils;

import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class SyndicationRules {

	private APIUtils api;
	private ExtentTest extentTest;
	private final int MAX_WAIT = 20;

	public SyndicationRules(ExtentTest extentTest) {
		this.extentTest = extentTest;
		api = new APIUtils();
	}

	public static Logger logger = Logger.getLogger(SyndicationRules.class);

	public boolean updatePublishingRule(String embedCode, String api_key, boolean defaultGroup) throws Exception {

		HashMap<String, String> rules = api.getPublishingRuleIds(api_key);

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

		if (api.updatePublishingRule(embedCode, publishingRuleId, api_key)) {
			int count = 0;
			while (count <= MAX_WAIT) {
				if (api.getPublishingRule(embedCode, publishingRuleId, api_key)) {
					return true;
				}
				count++;
			}
			extentTest.log(LogStatus.FAIL, "Issue with updating publishing rule");
			return false;
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
			int count = 0;
			while(count<=MAX_WAIT) {
				if(api.isEntitlementAvailable(pcode, embedCode)) {
					return true;
				}
				count++;
			}
			extentTest.log(LogStatus.FAIL, "Failed to add entitlement");
			return false;
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
		int count = 0;
		while(count<=MAX_WAIT) {
			if(api.getDevices(pcode)==null) {
				return true;
			}
			count++;
		}
		extentTest.log(LogStatus.FAIL, "Issue with deleting the devices.");
		return false;
	}

	public boolean updateUserDeviceLimit(String pcode, int limit) throws IOException {
		if (!api.updateDeviceLimit(pcode, limit)) {
			extentTest.log(LogStatus.FAIL, "Failed to update the device limit");
			return false;
		}
		return true;
	}

	public boolean isDeviceRegistered(String pcode, String userAgent) throws IOException {

		int count = 0;
		HashMap<String, String> devices = null;
		while (count <= MAX_WAIT) {
			devices = api.getDevices(pcode);
			if (devices != null) {
				break;
			}
			count++;
		}

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

}
