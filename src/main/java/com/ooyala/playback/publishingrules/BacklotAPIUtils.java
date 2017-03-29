package com.ooyala.playback.publishingrules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ooyala.qe.common.util.PropertyReader;

public class BacklotAPIUtils {

	private static Logger logger = Logger.getLogger(BacklotAPIUtils.class);
	private PropertyReader properties;
	private String endPoint;
	private String version;
	private String assets;
	private String epochTime;
	private String publishing_rules;
	private String publishing_rule;

	public BacklotAPIUtils() {
		try {
			properties = PropertyReader.getInstance("backlot_api.properties");
			endPoint = properties.getProperty("endPoint");
			version = properties.getProperty("version");
			assets = properties.getProperty("assets");
			epochTime = properties.getProperty("epochTime");
			publishing_rules = properties.getProperty("publishing_rules");
			publishing_rule = properties.getProperty("publishing_rule");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getSignature(String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] digest = md.digest(string.getBytes());
		String b64url = Base64.encodeBase64String(digest);
		b64url = b64url.substring(0, 43);
		b64url = URLEncoder.encode(b64url, "UTF-8");
		return b64url;
	}

	@SuppressWarnings({ "deprecation", "resource" })
	private String getJSONResponse(String urlString, String requestMethod, String requestBody) throws IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();

		HttpRequestBase request;

		if (requestMethod.equals("PUT"))
			request = new HttpPut(urlString);
		else if (requestMethod.equals("GET"))
			request = new HttpGet(urlString);
		else
			request = new HttpGet(urlString);

		request.addHeader("accept", "application/json");

		HttpResponse response = httpClient.execute(request);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		logger.info(response.getStatusLine().getStatusCode());

		BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

		String output = br.readLine();

		httpClient.getConnectionManager().shutdown();
		return output;

	}

	public void updatePublishingRule(String embedCode, String publishingRuleId, String api_key, String secret)
			throws Exception {

		String toBeSigned = secret + "PUT/" + version + assets + embedCode + publishing_rule + publishingRuleId
				+ "api_key=" + api_key + "expires=" + epochTime;

		String url = endPoint + version + assets + embedCode + publishing_rule + publishingRuleId + "?api_key="
				+ api_key + "&expires=" + epochTime + "&signature=" + getSignature(toBeSigned);
		logger.info(toBeSigned);
		logger.info(url);
		getJSONResponse(url, "PUT", "");
	}

	public HashMap<String, String> getPublishingRuleIds(String api_key, String secret) throws Exception {
		String toBeSigned = secret + "GET/" + version + publishing_rules + "api_key=" + api_key + "expires="
				+ epochTime;
		String url = endPoint + version + publishing_rules + "?api_key=" + api_key + "&expires=" + epochTime
				+ "&signature=" + getSignature(toBeSigned);
		logger.info(toBeSigned);
		logger.info(url);
		JSONObject json = new JSONObject(getJSONResponse(url, "GET", ""));
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

	public static void main(String... strings) throws Exception {
		new BacklotAPIUtils().getPublishingRuleIds("x0b2cyOupu0FFK5hCr4zXg8KKcrm.-s6jH",
				"ZCMQt2CCVqlHWce6dG5w2WA6fkAM_JaWgoI_yzQp");
	}

}
