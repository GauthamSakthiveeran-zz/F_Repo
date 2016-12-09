package com.ooyala.playback.live;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.ooyala.qe.common.backjaxauth.Utils;
import com.ooyala.qe.common.exception.OoyalaException;
import com.ooyala.qe.common.http.HttpTestClient;
import com.ooyala.qe.common.http.Response;
import com.ooyala.qe.common.util.PropertyReader;

/**
 * This class is responsible to make http request to any of neo api endpoint
 * Usage : Just use NeoRequest.makeRequest(params ....)
 *
 * Created by Pavan on 05/11/15.
 */
public class NeoRequest {
	private Logger logger = Logger.getLogger(NeoRequest.class);
	private String HOST_ADDRESS;
	private PropertyReader properties;
	private String API_KEY;
	private long timeOut;
	private static NeoRequest neoRequest;

	private NeoRequest() throws OoyalaException {

		try {
			properties = PropertyReader.getInstance("urlData.properties");
			API_KEY = properties.getProperty("api_key");
			logger.info("API Key set to : " + API_KEY);
			HOST_ADDRESS = "https://live.ooyala.com";
			logger.info("Live URL set to : " + HOST_ADDRESS);
			timeOut = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Not able to create NeoRequest instance");
			throw new OoyalaException("Not able to create NeoRequest instance");
		}

	}

	public static NeoRequest getInstance() throws OoyalaException {
		if (neoRequest == null)
			neoRequest = new NeoRequest();
		return neoRequest;

	}

	/***
	 * Making http request,this method will automatically perform authentication
	 * to Neo API Server
	 * 
	 * @param //method GET,POST,DELETE,PUT
	 * @param path
	 *            starting from v2, i.e for channel "v2/channels"
	 * @param body
	 *            payload need to sent in http request
	 * @return HttpResponse for processed request
	 */
	public Response makeRequest(String httpMethod, String body,
			String... addReqPath) {
		HttpTestClient httpTestClient = new HttpTestClient();
		// Getting signature for current processing request
		Map<String, String> queryString = new HashMap<>();

		String urlPath = "/v2/channels";
		logger.info("urlPath_1 is :" + urlPath);
		logger.info("--------------------------------------------------------------------");
		if (addReqPath != null && addReqPath.length > 0) {
			for (String addPath : addReqPath) {
				urlPath = urlPath + "/" + addPath;
				logger.info("urlPath_2 is :" + urlPath);
				logger.info("--------------------------------------------------------------------");
			}
		}

		String encodedUrl = Utils.getSignatureKey(API_KEY, httpMethod, urlPath,
				body, timeOut, queryString);
		logger.info("encoded_url is :" + encodedUrl);
		logger.info("--------------------------------------------------------------------");
		logger.info(HOST_ADDRESS + "-------------HOST_Address");
		String requestUrl = HOST_ADDRESS + urlPath + encodedUrl;
		logger.info("--------------------------------------------------------------------");
		logger.info("request_url is :" + requestUrl);

		if (requestUrl.contains("&include=input")) {
			requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf("&"));
			logger.info("updated request_url is :" + requestUrl);
		}

		logger.info("Request url is " + requestUrl);
		try {
			return httpTestClient.makeRequest(httpMethod, requestUrl, body,
					null);
		} catch (OoyalaException e1) {
			e1.printStackTrace();
			return null;
		}
	}

}
