package com.ooyala.playback.live;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ooyala.qe.common.http.HttpTestClient;
import com.ooyala.qe.common.http.Response;

public class ElementalGroundEncoder {

	private Logger logger = Logger.getLogger(ElementalGroundEncoder.class);
	private String url;
	private HttpTestClient httpTestClient;
	private Map<String, String> headers;

	public ElementalGroundEncoder() {
		url = "https://r5pnmvglhp3w1.cloud.elementaltechnologies.com/api/live_events";
		httpTestClient = new HttpTestClient();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/xml");
	}

	public boolean resetEvent() {
		boolean flag = false;
		try {
			Response response = httpTestClient.makeRequest("POST", url
					+ "/175/reset", "<reset></reset>", headers);

			logger.info("Response of put reset is" + response.getResponse());
			logger.info("Response code of put reset is"
					+ response.getResponseCode());
			if (response.getResponseCode() == 200)
				flag = true;
			else
				// waiting just to make sure event finished the reset action
				Thread.sleep(20000);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Could not reset the elemental event");
			flag = false;
		}
		return flag;
	}

	public boolean stopEvent() {
		boolean flag = false;
		try {
			logger.info(url + "/175/stop");
			Response response = httpTestClient.makeRequest("POST", url
					+ "/175/stop", "<stop></stop>", null);
			logger.info("Response Code of Stop is" + response.getResponseCode());
			if (response.getResponseCode() == 200)
				flag = true;
			// waiting just to make sure event finished the stop action
			Thread.sleep(20000);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Could not stop the elemental event");
			flag = false;
		}
		return flag;
	}

	public boolean startEvent() {
		try {

			Map<String, String> headers = new HashMap<String, String>();

			headers.put("Accept", "application/xml");
			headers.put("Content-Type", "application/xml");
			Response response = httpTestClient.makeRequest("POST", url
					+ "/175/start", "<start></start>", headers);
			logger.info("Response code is " + response.getResponseCode());
			logger.info("Response of put reset is" + response.getResponse());

			if (response.getResponseCode() == 201
					|| response.getResponseCode() == 200) {
				logger.info("Waiting for 40sec  elemental ground encoder to start the event ");
				Thread.sleep(1000 * 40); // Sleep 2 mins to start events
				return true;
			} else {
				logger.info("Failed to start elemental event");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Could not start the elemental event");
			return false;
		}
	}

}
