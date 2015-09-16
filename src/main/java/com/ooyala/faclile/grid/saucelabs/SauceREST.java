package com.ooyala.faclile.grid.saucelabs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONValue;

import sun.misc.BASE64Encoder;

// TODO: Auto-generated Javadoc
/**
 * Simple Java API that invokes the Sauce REST API.
 */
public class SauceREST {

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(SauceREST.class
			.getName());

	/** The username. */
	protected String username;

	/** The access key. */
	protected String accessKey;

	/** The Constant RESTURL. */
	public static final String RESTURL = "https://saucelabs.com/rest/v1/%1$s";

	/** The Constant USER_RESULT_FORMAT. */
	private static final String USER_RESULT_FORMAT = RESTURL + "/%2$s";

	/** The Constant JOB_RESULT_FORMAT. */
	private static final String JOB_RESULT_FORMAT = RESTURL + "/jobs/%2$s";

	/** The Constant DOWNLOAD_VIDEO_FORMAT. */
	private static final String DOWNLOAD_VIDEO_FORMAT = JOB_RESULT_FORMAT
			+ "/results/video.flv";

	/** The Constant DOWNLOAD_LOG_FORMAT. */
	private static final String DOWNLOAD_LOG_FORMAT = JOB_RESULT_FORMAT
			+ "/results/video.flv";

	/** The Constant DATE_FORMAT. */
	private static final String DATE_FORMAT = "yyyyMMdd_HHmmSS";

	/**
	 * Instantiates a new sauce rest.
	 *
	 * @param username
	 *            the username
	 * @param accessKey
	 *            the access key
	 */
	public SauceREST(String username, String accessKey) {
		this.username = username;
		this.accessKey = accessKey;
	}

	/**
	 * Marks a Sauce Job as 'passed'.
	 *
	 * @param jobId
	 *            the Sauce Job Id, typically equal to the Selenium/WebDriver
	 *            sessionId
	 */
	public void jobPassed(String jobId) {
		Map<String, Object> updates = new HashMap<String, Object>();
		updates.put("passed", true);
		updateJobInfo(jobId, updates);
	}

	/**
	 * Marks a Sauce Job as 'failed'.
	 *
	 * @param jobId
	 *            the Sauce Job Id, typically equal to the Selenium/WebDriver
	 *            sessionId
	 */
	public void jobFailed(String jobId) {
		Map<String, Object> updates = new HashMap<String, Object>();
		updates.put("passed", false);
		updateJobInfo(jobId, updates);
	}

	/**
	 * Downloads the video for a Sauce Job to the filesystem. The file will be
	 * stored in a directory specified by the <code>location</code> field.
	 *
	 * @param jobId
	 *            the Sauce Job Id, typically equal to the Selenium/WebDriver
	 *            sessionId
	 * @param location
	 *            the location
	 */
	public void downloadVideo(String jobId, String location) {
		URL restEndpoint = null;
		try {
			restEndpoint = new URL(String.format(DOWNLOAD_VIDEO_FORMAT,
					username, jobId));
		} catch (MalformedURLException e) {
			logger.log(Level.WARNING, "Error constructing Sauce URL", e);
		}
		downloadFile(jobId, location, restEndpoint);
	}

	/**
	 * Downloads the log file for a Sauce Job to the filesystem. The file will
	 * be stored in a directory specified by the <code>location</code> field.
	 *
	 * @param jobId
	 *            the Sauce Job Id, typically equal to the Selenium/WebDriver
	 *            sessionId
	 * @param location
	 *            the location
	 */
	public void downloadLog(String jobId, String location) {
		URL restEndpoint = null;
		try {
			restEndpoint = new URL(String.format(DOWNLOAD_LOG_FORMAT, username,
					jobId));
		} catch (MalformedURLException e) {
			logger.log(Level.WARNING, "Error constructing Sauce URL", e);
		}
		downloadFile(jobId, location, restEndpoint);
	}

	/**
	 * Retrieve results.
	 *
	 * @param path
	 *            the path
	 * @return the string
	 */
	public String retrieveResults(String path) {
		URL restEndpoint = null;
		try {
			restEndpoint = new URL(String.format(USER_RESULT_FORMAT, username,
					path));
		} catch (MalformedURLException e) {
			logger.log(Level.WARNING, "Error constructing Sauce URL", e);
		}
		return retrieveResults(restEndpoint);
	}

	/**
	 * Gets the job info.
	 *
	 * @param jobId
	 *            the job id
	 * @return the job info
	 */
	public String getJobInfo(String jobId) {
		URL restEndpoint = null;
		try {
			restEndpoint = new URL(String.format(JOB_RESULT_FORMAT, username,
					jobId));
		} catch (MalformedURLException e) {
			logger.log(Level.WARNING, "Error constructing Sauce URL", e);
		}
		return retrieveResults(restEndpoint);
	}

	/**
	 * Retrieve results.
	 *
	 * @param restEndpoint
	 *            the rest endpoint
	 * @return the string
	 */
	public String retrieveResults(URL restEndpoint) {
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();
		try {
			HttpURLConnection connection = (HttpURLConnection) restEndpoint
					.openConnection();

			connection.setDoOutput(true);
			String auth = encodeAuthentication();
			connection.setRequestProperty("Authorization", auth);
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				builder.append(inputLine);
			}
		} catch (IOException e) {
			logger.log(Level.WARNING, "Error retrieving Sauce Results", e);
		}
		try {
			if (reader != null) {
				reader.close();
			}
		} catch (IOException e) {
			logger.log(Level.WARNING, "Error closing Sauce input stream", e);
		}
		return builder.toString();
	}

	/**
	 * Download file.
	 *
	 * @param jobId
	 *            the job id
	 * @param location
	 *            the location
	 * @param restEndpoint
	 *            the rest endpoint
	 */
	private void downloadFile(String jobId, String location, URL restEndpoint) {
		try {
			HttpURLConnection connection = (HttpURLConnection) restEndpoint
					.openConnection();

			connection.setDoOutput(true);
			connection.setRequestMethod("PUT");
			String auth = encodeAuthentication();
			connection.setRequestProperty("Authorization", auth);

			InputStream stream = connection.getInputStream();
			BufferedInputStream in = new BufferedInputStream(stream);
			SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
			String saveName = jobId + format.format(new Date());
			if (restEndpoint.getPath().endsWith(".flv")) {
				saveName = saveName + ".flv";
			} else {
				saveName = saveName + ".log";
			}
			FileOutputStream file = new FileOutputStream(new File(location,
					saveName));
			BufferedOutputStream out = new BufferedOutputStream(file);
			int i;
			while ((i = in.read()) != -1) {
				out.write(i);
			}
			out.flush();
		} catch (IOException e) {
			logger.log(Level.WARNING, "Error downloading Sauce Results");
		}
	}

	/**
	 * Update job info.
	 *
	 * @param jobId
	 *            the job id
	 * @param updates
	 *            the updates
	 */
	public void updateJobInfo(String jobId, Map<String, Object> updates) {
		HttpURLConnection postBack = null;
		try {
			URL restEndpoint = new URL(String.format(JOB_RESULT_FORMAT,
					username, jobId));
			postBack = (HttpURLConnection) restEndpoint.openConnection();
			postBack.setDoOutput(true);
			postBack.setRequestMethod("PUT");
			String auth = encodeAuthentication();
			postBack.setRequestProperty("Authorization", auth);
			String jsonText = JSONValue.toJSONString(updates);
			postBack.getOutputStream().write(jsonText.getBytes());
		} catch (IOException e) {
			logger.log(Level.WARNING, "Error updating Sauce Results", e);
		}

		try {
			if (postBack != null) {
				postBack.getInputStream().close();
			}
		} catch (IOException e) {
			logger.log(Level.WARNING, "Error closing result stream", e);
		}

	}

	/**
	 * Encode authentication.
	 *
	 * @return the string
	 */
	private String encodeAuthentication() {
		String auth = username + ":" + accessKey;
		// Handle long strings encoded using BASE64Encoder - see
		// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6947917
		BASE64Encoder encoder = new BASE64Encoder() {
			@Override
			protected int bytesPerLine() {
				return 9999;
			}
		};
		auth = "Basic " + new String(encoder.encode(auth.getBytes()));
		return auth;
	}
}