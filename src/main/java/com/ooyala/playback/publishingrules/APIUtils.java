package com.ooyala.playback.publishingrules;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ooyala.qe.common.util.PropertyReader;

import net.lightbody.bmp.proxy.http.HttpDeleteWithBody;

public class APIUtils {

	private static Logger logger = Logger.getLogger(APIUtils.class);

	private PropertyReader properties;
	protected String backlot_api_endPoint;
	protected String version;
	protected String assets;
	protected String epochTime;
	protected String publishing_rules;
	protected String publishing_rule;
	protected String rl_api_endpoint;
	protected String entitlements;
	protected String providers;
	protected String accounts;
	protected String content;
	protected String external_products;
	protected String accountId;
	protected int httpStatus;
	protected String api_key;
	protected String expires;
	protected String signature;
	protected String internal;
	protected String device_management;
	protected String pcode;
	protected String account_id;
	protected String devices;
	
	public APIUtils() {
		try {
			properties = PropertyReader.getInstance("api.properties");
			backlot_api_endPoint = properties.getProperty("backlot_api_endPoint");
			version = properties.getProperty("version");
			assets = properties.getProperty("assets");
			epochTime = properties.getProperty("epochTime");
			publishing_rules = properties.getProperty("publishing_rules");
			publishing_rule = properties.getProperty("publishing_rule");
			rl_api_endpoint = properties.getProperty("rl_api_endpoint");
			entitlements = properties.getProperty("entitlements");
			providers = properties.getProperty("providers");
			accounts = properties.getProperty("accounts");
			content = properties.getProperty("content");
			external_products = properties.getProperty("external_products");
			accountId = properties.getProperty("accountId");
			api_key = properties.getProperty("api_key");
			expires = properties.getProperty("expires");
			signature = properties.getProperty("signature");
			internal = properties.getProperty("internal");
			device_management = properties.getProperty("device_management");
			pcode = properties.getProperty("pcode");
			account_id = properties.getProperty("account_id");
			devices = properties.getProperty("devices");

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

	@SuppressWarnings("deprecation")
	private String makeAPIcall(String urlString, String requestMethod, String requestBody) throws IOException {
		HttpClient httpClient = HttpClientBuilder.create().build();

		HttpRequestBase request = null;

		if (requestMethod.equals("PUT")) {
			request = new HttpPut(urlString);
		} else if (requestMethod.equals("GET")) {
			request = new HttpGet(urlString);
		} else if (requestMethod.equals("POST")) {

			HttpPost post = new HttpPost(urlString);
			HttpEntity entity = new ByteArrayEntity(requestBody.getBytes("UTF-8"));
			post.setEntity(entity);
			HttpResponse response = httpClient.execute(post);
			httpStatus = response.getStatusLine().getStatusCode();
			return EntityUtils.toString(response.getEntity());

		} else if (requestMethod.equals("DELETE")) {
			
			if(requestBody==null || requestBody.isEmpty())
				request = new HttpDelete(urlString);
			else{
				HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(urlString);
		        StringEntity input = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
		        httpDelete.setEntity(input); 
		        HttpResponse response = httpClient.execute(httpDelete);
				httpStatus = response.getStatusLine().getStatusCode();
				return EntityUtils.toString(response.getEntity());
			}
			
		} else {
			request = new HttpGet(urlString);
		}

		request.addHeader("accept", "application/json");

		HttpResponse response = httpClient.execute(request);

		httpStatus = response.getStatusLine().getStatusCode();

		if (httpStatus == 200) {
			String output = EntityUtils.toString(response.getEntity());
			logger.info(output);
			httpClient.getConnectionManager().shutdown();
			return output;
		}

		logger.info(response.getStatusLine().getStatusCode());

		return null;

	}
	
	public boolean isEntitlementAvailable(String pcode, String embedCode) throws Exception {
		String url = rl_api_endpoint + version + entitlements + providers + pcode + "/" + accounts + accountId + "/"
				+ content + "/" + assets + embedCode + "/" + external_products + "default";

		makeAPIcall(url, "GET", "");

		if (httpStatus == 200)
			return true;

		return false;
	}

	public boolean deleteEntitlement(String pcode, String embedCode) throws Exception {
		String url = rl_api_endpoint + version + entitlements + providers + pcode + "/" + accounts + accountId + "/"
				+ content + "/" + assets + embedCode + "/" + external_products + "default";

		makeAPIcall(url, "DELETE", "");

		if (httpStatus == 200)
			return true;

		return false;
	}

	public boolean addEntitlement(String pcode, String embedCode, int deviceCount) throws Exception {
		String url = rl_api_endpoint + version + entitlements + providers + pcode + "/" + accounts + accountId + "/"
				+ content;

		JSONObject json = new JSONObject();
		json.put("content_id", embedCode);
		json.put("external_product_id", "default");
		json.put("num_devices_to_bind", deviceCount);
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(json);
		JSONObject requestBody = new JSONObject();
		requestBody.put("assets", jsonArray);

		makeAPIcall(url, "POST", requestBody.toString());

		if (httpStatus == 200)
			return true;

		return false;
	}

	public HashMap<String, String> getDevices(String pcode) throws IOException {

		String url = rl_api_endpoint + device_management + this.pcode + pcode + "/" + account_id + accountId
				+ "/" + devices;
		String response = makeAPIcall(url, "GET", "");

		if (httpStatus == 200) {
			HashMap<String, String> devices = new HashMap<>();
			JSONObject json = new JSONObject(response);
			JSONArray jsonArray = json.getJSONArray("devices");
			for (int i = 0; i < jsonArray.length(); i++) {
				devices.put(jsonArray.getJSONObject(i).getString("user_agent"),
						jsonArray.getJSONObject(i).getString("public_device_id"));
			}
			return devices;
		}
		return null;

	}

	public boolean deleteAllDevices(String pcode) throws IOException {

		String url = rl_api_endpoint + device_management + this.pcode + pcode + "/" + account_id + accountId
				+ "/" + devices;
		JSONObject json = new JSONObject();
		json.put("actor", "sasport");
		json.put("actor_type", "admin");

		String response = makeAPIcall(url, "DELETE", json.toString());

		if (httpStatus == 200) {
			return true;
		}
		if (httpStatus == 404 && response.contains("device does not exist")) // device already deleted
			return true;

		return false;

	}
	
	public boolean deleteDevice(String pcode, String deviceId) throws IOException {

		String url = rl_api_endpoint + device_management + this.pcode + pcode + "/" + account_id + accountId
				+ "/" + devices + "/" + deviceId;
		JSONObject json = new JSONObject();
		json.put("actor", "sasport");
		json.put("actor_type", "admin");

		String response = makeAPIcall(url, "DELETE", json.toString());

		if (httpStatus == 200) {
			return true;
		}
		if (httpStatus == 404 && response.contains("device does not exist")) // device already deleted
			return true;

		return false;

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
		HashMap<String, String> rules = new APIUtils().getPublishingRuleIds("x0b2cyOupu0FFK5hCr4zXg8KKcrm.-s6jH",
				"ZCMQt2CCVqlHWce6dG5w2WA6fkAM_JaWgoI_yzQp");
		System.out.println(rules.get("default"));
		System.out.println(rules.get("specific"));
	}

}
