package com.ooyala.playback.live;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.ooyala.playback.utils.NeoRequest;
import com.ooyala.qe.common.exception.OoyalaException;
import com.ooyala.qe.common.http.Response;
import com.ooyala.qe.common.util.PropertyReader;

public class LiveChannel {

	private static Logger logger = Logger.getLogger(LiveChannel.class);

	private NeoRequest neoRequest;
	private Set<String> channelIds;
	private Map<String, String> channelProviders;
	private ElementalGroundEncoder groundEncoder;
	private String HOST_ADDRESS;
	private PropertyReader properties;
	private String API_KEY;
	private String urlPath = "/v2/channels";

	public LiveChannel() throws OoyalaException {
		neoRequest = new NeoRequest();
		channelIds = new HashSet<String>();
		groundEncoder = new ElementalGroundEncoder();
		channelProviders = new HashMap<String, String>();
		try {
			properties = PropertyReader.getInstance("urlData.properties");
			API_KEY = properties.getProperty("api_key");
			logger.info("API Key set to : " + API_KEY);
			HOST_ADDRESS = "https://live.ooyala.com";
			logger.info("Live URL set to : " + HOST_ADDRESS);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Not able to create NeoRequest instance");
			throw new OoyalaException("Not able to create NeoRequest instance");
		}
	}

	public boolean startChannel(String channelId, String provider) {
		if(channelId==null || channelId.isEmpty()) {
			return true;
		}
		boolean flag = false;
		if (channelId != null && !channelIds.contains(channelId) && provider != null) {
			logger.info("Starting live channel " + channelId);
			channelIds.add(channelId);
			channelProviders.put(channelId, provider);

			// Elemental channel is fed from slate but for azure we ned to start
			// the ground encoder for sening rtmp stream
			if (provider.equalsIgnoreCase("azure"))
				groundEncoder.startEvent();
			Response response = neoRequest.makeRequest(HOST_ADDRESS, urlPath, API_KEY, "POST",
					"{cid: \"" + channelId + "\"}", null,channelId, "start");
			logger.info("Channel start request response is " + response.getResponse());
			logger.info("Channel start response code is " + response.getResponseCode());
			if (response.getResponseCode() == 200)
				flag = true;
			flag = flag && checkStatus(channelId, "RUNNING");
			logger.info("Channel start status is " + flag);

		} else {
			flag = true;
		}
		return flag;

	}

	private boolean checkStatus(String channelId, String status) {
		for (long stop = System.nanoTime() + TimeUnit.MINUTES.toNanos(10); stop > System.nanoTime();) {
			Response response = neoRequest.makeRequest(HOST_ADDRESS, urlPath, API_KEY, "GET", null, null,channelId);
			JSONObject jsonResponse = new JSONObject(response.getResponse());
			String statuResponse = jsonResponse.getString("status");
			// logger.info("Logger status of start/stop is : " + statuResponse);
			logger.info("start/stop status is : " + statuResponse);
			if (statuResponse.equalsIgnoreCase(status.toString()))
				switch (status) {
				case "RUNNING":
					return statuResponse.equals("Running");
				case "STOPPED":
					return statuResponse.equals("Stopped");
				case "ERROR":
					return false;
				case "DELETING":
					return statuResponse.equals("Deleting");
				}
		}
		return false;
	}

	// Stopping the live channel if exists
	public boolean stopChannels() {
		boolean flag = true;
		for (String channelId : channelIds) {

			// For elemental channels there is no ground encoder started but for
			// azure we need to stop the event which has been started
			if (channelProviders.get(channelId).equalsIgnoreCase("azure")) {
				groundEncoder.stopEvent();
				groundEncoder.resetEvent();
			}

			Response response = neoRequest.makeRequest(HOST_ADDRESS, urlPath, API_KEY, "POST", null, null, channelId, "stop");
			logger.info("Channel stop request response is " + response.getResponse());
			logger.info("Channel stop response code is " + response.getResponseCode());
			if (response.getResponseCode() == 200)
				flag = flag && true;
			else {
				logger.error("Not able to stop the channel with id " + channelId);
				flag = false;
			}
			flag = checkStatus(channelId, "STOPPED");
			logger.info("Channel stop status is " + flag);
			channelIds.remove(channelId);
		}

		return flag;
	}
}
