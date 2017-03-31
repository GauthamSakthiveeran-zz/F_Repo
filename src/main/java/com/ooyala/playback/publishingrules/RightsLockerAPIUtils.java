package com.ooyala.playback.publishingrules;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class RightsLockerAPIUtils extends APIUtils {

	private static Logger logger = Logger.getLogger(RightsLockerAPIUtils.class);

	public RightsLockerAPIUtils() {
		super();
	}

	public boolean isEntitlementAvailable(String pcode, String embedCode) throws Exception {
		String url = rl_api_endpoint + version + entitlements + providers + pcode + "/" + accounts + account_id + "/"
				+ content + assets + embedCode + "/" + external_products + "default";

		makeAPIcall(url, "GET", "");

		if (httpStatus == 200)
			return true;
		
		return false;
	}
	
	public boolean deleteEntitlement(String pcode, String embedCode) throws Exception {
		String url = rl_api_endpoint + version + entitlements + providers + pcode + "/" + accounts + account_id + "/"
				+ content + assets + embedCode + "/" + external_products + "default";

		makeAPIcall(url, "DELETE", "");

		if (httpStatus == 200)
			return true;
		
		return false;
	}
	
	public boolean addEntitlement(String pcode, String embedCode) throws Exception {
		String url = rl_api_endpoint + version + entitlements + providers + pcode + "/" + accounts + account_id + "/"
				+ content;
		
		JSONObject json = new JSONObject();
		json.put("content_id", embedCode);
		json.put("external_product_id", "default");
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(json);
		JSONObject requestBody = new JSONObject();
		requestBody.put("assets", jsonArray);

		makeAPIcall(url, "POST", requestBody.toString());

		if (httpStatus == 200)
			return true;
		
		return false;
	}

}
