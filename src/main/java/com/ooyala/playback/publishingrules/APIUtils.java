package com.ooyala.playback.publishingrules;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.ooyala.qe.common.util.PropertyReader;

public abstract class APIUtils {

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
	protected String account_id;
	protected int httpStatus;
	protected String api_key;
	protected String expires;
	protected String signature;

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
			account_id = properties.getProperty("account_id");
			api_key = properties.getProperty("api_key");
			expires = properties.getProperty("expires");
			signature = properties.getProperty("signature");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected String getSignature(String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] digest = md.digest(string.getBytes());
		String b64url = Base64.encodeBase64String(digest);
		b64url = b64url.substring(0, 43);
		b64url = URLEncoder.encode(b64url, "UTF-8");
		return b64url;
	}

	@SuppressWarnings("deprecation")
	protected String makeAPIcall(String urlString, String requestMethod, String requestBody) throws IOException {
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
			return EntityUtils.toString(response.getEntity());

		} else if (requestMethod.equals("DELETE")) {
			request = new HttpDelete(urlString);
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

}
