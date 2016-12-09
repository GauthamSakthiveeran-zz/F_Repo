package com.ooyala.playback.live;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.qe.common.exception.OoyalaException;
import com.ooyala.qe.common.http.Response;

public class LiveChannel {

	private static Logger logger = Logger.getLogger(LiveChannel.class);

	private NeoRequest neoRequest;
	private Set<String> channelIds;
	private Map<String, String> channelProviders;
	private ElementalGroundEncoder groundEncoder;

	public LiveChannel() throws OoyalaException {
		neoRequest = NeoRequest.getInstance();
		channelIds = new HashSet<String>();
		groundEncoder = new ElementalGroundEncoder();
		channelProviders = new HashMap<String, String>();
	}

	public boolean startChannel(String testDescription) {
		boolean flag = false;
		String description = testDescription.substring(
				testDescription.indexOf(":") + 1).trim();
		// Starting the live channel if exists for this test case
		String channelId = UrlGenerator.getLiveChannelDetails()
				.get(description);
		String provider = UrlGenerator.getLiveChannelProviders().get(
				description);
		if (channelId != null && !channelIds.contains(channelId)
				&& provider != null) {
			channelIds.add(channelId);
			channelProviders.put(channelId, provider);

			// Elemental channel is fed from slate but for azure we ned to start
			// the ground encoder for sening rtmp stream
			if (provider.equalsIgnoreCase("azure"))
				groundEncoder.startEvent();
			Response response = neoRequest.makeRequest("POST", "{cid: \""
					+ channelId + "\"}", channelId, "start");
			logger.info("Channel start request response is "
					+ response.getResponse());
			logger.info("Channel start response code is "
					+ response.getResponseCode());
			if (response.getResponseCode() == 200)
				flag = true;
			flag = flag && checkStatus(channelId, "RUNNING");
			logger.info("Channel start status is " + flag);

		} else {
			logger.info("Either Channel id does not exist or it is already started, so not required to start it");
			flag = true;
		}
		return flag;

	}

	private boolean checkStatus(String channelId, String status) {
		for (long stop = System.nanoTime() + TimeUnit.MINUTES.toNanos(10); stop > System
				.nanoTime();) {
			Response response = neoRequest.makeRequest("GET", null, channelId);
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

			Response response = neoRequest.makeRequest("POST", null, channelId,
					"stop");
			logger.info("Channel stop request response is "
					+ response.getResponse());
			logger.info("Channel stop response code is "
					+ response.getResponseCode());
			if (response.getResponseCode() == 200)
				flag = flag && true;
			else {
				logger.error("Not able to stop the channel with id "
						+ channelId);
				flag = false;
			}
			flag = checkStatus(channelId, "STOPPED");
			logger.info("Channel stop status is " + flag);
			channelIds.remove(channelId);
		}

		return flag;
	}
}
