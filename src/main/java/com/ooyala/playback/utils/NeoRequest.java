package com.ooyala.playback.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.ooyala.qe.common.backjaxauth.Utils;
import com.ooyala.qe.common.exception.OoyalaException;
import com.ooyala.qe.common.http.HttpTestClient;
import com.ooyala.qe.common.http.Response;

import net.lightbody.bmp.proxy.http.HttpDeleteWithBody;

/**
 * This class is responsible to make http request to any of neo api endpoint
 * Usage : Just use NeoRequest.makeRequest(params ....)
 *
 * Created by Pavan on 05/11/15.
 */
public class NeoRequest {
	private Logger logger = Logger.getLogger(NeoRequest.class);
	private long timeOut;

	public NeoRequest() throws OoyalaException {

		timeOut = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30);

	}

	/***
	 * Making http request,this method will automatically perform authentication
	 * to Neo API Server
	 * 
	 * @param //method
	 *            GET,POST,DELETE,PUT
	 * @param path
	 *            starting from v2, i.e for channel "v2/channels"
	 * @param body
	 *            payload need to sent in http request
	 * @return HttpResponse for processed request
	 */
	public Response makeRequest(String hostAddress, String path, String apiKey, String httpMethod, String body,
			Map<String, String> queryString, String... addReqPath) {
		HttpTestClient httpTestClient = new HttpTestClient();
		// Getting signature for current processing request

		String urlPath = path;
		logger.info("urlPath_1 is :" + urlPath);
		logger.info("--------------------------------------------------------------------");
		if (addReqPath != null && addReqPath.length > 0) {
			for (String addPath : addReqPath) {
				urlPath = urlPath + "/" + addPath;
				logger.info("urlPath_2 is :" + urlPath);
				logger.info("--------------------------------------------------------------------");
			}
		}

		String requestUrl = hostAddress + urlPath;

		if (apiKey != null && !apiKey.isEmpty()) {
			String encodedUrl = Utils.getSignatureKey(apiKey, httpMethod, urlPath, body, timeOut, queryString);
			logger.info("encoded_url is :" + encodedUrl);
			logger.info("--------------------------------------------------------------------");
			logger.info(hostAddress + "-------------HOST_Address");
			requestUrl = hostAddress + urlPath + encodedUrl;
			logger.info("--------------------------------------------------------------------");
			logger.info("request_url is :" + requestUrl);
		}

		if (requestUrl.contains("&include=input")) {
			requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf("&"));
			logger.info("updated request_url is :" + requestUrl);
		}

		logger.info("Request url is " + requestUrl);
		try {
			if(httpMethod.contains("DELETE") && body!=null && !body.isEmpty())
				return makeAPIcall(requestUrl, body);
			return httpTestClient.makeRequest(httpMethod, requestUrl, body, null);
		} catch (OoyalaException e1) {
			e1.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// TODO need to make changes in qecommon for httpdeletewithbody
	private Response makeAPIcall(String urlString, String requestBody) throws IOException {
		HttpClient httpClient = HttpClientBuilder.create().build();

		HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(urlString);
		StringEntity input = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
		httpDelete.setEntity(input);
		HttpResponse res = httpClient.execute(httpDelete);
		Response response = getResponse(res);
		logger.debug("Response code from http is " + response.getResponseCode());
		return response;

	}

	// TODO
	private Response getResponse(HttpResponse res) throws UnsupportedOperationException {
		String output = null;
		BufferedReader br = null;
		Response response = null;
		StringBuffer responseHolder = new StringBuffer("");
		int responoseCode = res.getStatusLine().getStatusCode();
		logger.debug("Http request response code is " + responoseCode);
		// Get-Capture Complete application/xml body response
		try {
			br = new BufferedReader(new InputStreamReader((res.getEntity().getContent())));

			while ((output = br.readLine()) != null)
				responseHolder = responseHolder.append(output);
			Map<String, String> outPutHeaders = new HashMap<String, String>();
			Header[] tempHeaders = res.getAllHeaders();
			for (Header header : tempHeaders)
				outPutHeaders.put(header.getName(), header.getValue());

			response = new Response(responoseCode, responseHolder.toString(), outPutHeaders);
		} catch (IOException ex) {
			logger.error("Error while reading response " + ex.getLocalizedMessage());
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					logger.error("Error while closing the stream " + e.getLocalizedMessage());
				}
		}
		return response;
	}

}
