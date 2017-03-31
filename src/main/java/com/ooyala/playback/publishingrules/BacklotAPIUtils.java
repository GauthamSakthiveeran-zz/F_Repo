package com.ooyala.playback.publishingrules;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class BacklotAPIUtils extends APIUtils {

	private static Logger logger = Logger.getLogger(BacklotAPIUtils.class);

	public BacklotAPIUtils() {
		super();
	}

	public boolean updatePublishingRule(String embedCode, String publishingRuleId, String api_key, String secret)
			throws Exception {

		String toBeSigned = secret + "PUT/" + version + assets + embedCode + "/" + publishing_rule + publishingRuleId
				+ this.api_key + "=" + api_key + expires + "=" + epochTime;

		String url = backlot_api_endPoint + version + assets + embedCode + "/" + publishing_rule + publishingRuleId + "?"
				+ this.api_key + "=" + api_key + "&" + expires + "=" + epochTime + "&" + signature + "="
				+ getSignature(toBeSigned);

		makeAPIcall(url, "PUT", "");

		if (httpStatus == 200) {
			return true;
		}
		return false;
	}

	public HashMap<String, String> getPublishingRuleIds(String api_key, String secret) throws Exception {
		String toBeSigned = secret + "GET/" + version + publishing_rules + this.api_key + "=" + api_key + expires + "="
				+ epochTime;
		String url = backlot_api_endPoint + version + publishing_rules + "?" + this.api_key + "=" + api_key + "&"
				+ expires + "=" + epochTime + "&" + signature + "=" + getSignature(toBeSigned);

		String output = makeAPIcall(url, "GET", "");
		
		if (output != null && !output.isEmpty()) {
			JSONObject json = new JSONObject(output);
			HashMap<String, String> rules = new HashMap<>();

			JSONArray items = json.getJSONArray("items");

			for (int i = 0; i < items.length(); i++) {
				JSONObject j = items.getJSONObject(i);
				if (j.getString("name").equals("Default Group")) {
					rules.put("default", j.getString("id"));
				} else if (j.getString("name").equals("Flight Time") || j.getString("name").equals("Geo Restriction")) {
					rules.put("specific", j.getString("id"));
				}
			}

			return rules;
		}

		return null;

	}
	
	public static void main(String... strings) throws Exception {
		HashMap<String, String> rules = new BacklotAPIUtils().getPublishingRuleIds("x0b2cyOupu0FFK5hCr4zXg8KKcrm.-s6jH",
				"ZCMQt2CCVqlHWce6dG5w2WA6fkAM_JaWgoI_yzQp");
		System.out.println(rules.get("default"));
		System.out.println(rules.get("specific"));
	}

}
