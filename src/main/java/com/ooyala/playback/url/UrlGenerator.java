package com.ooyala.playback.url;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ooyala.qe.common.util.PropertyReader;

/**
 * Created by jitendra
 */
public class UrlGenerator {

	private static TestPage test = null;
	private static String url;
	private static Map<PlayerPropertyKey, PlayerPropertyValue> playerProperties = new HashMap<PlayerPropertyKey, PlayerPropertyValue>();
	private static Logger logger = Logger.getLogger(UrlGenerator.class);
	private static String adPluginFilter = new String();
	private static String videoPluginFilter = new String();
	private static Map<String, String> liveChannelDetails = new HashMap<String, String>();
	private static Map<String, String> liveChannelProviders = new HashMap<String, String>();
	private static Map<String, String> streamTypeDetails = new HashMap<String, String>();

	/**
	 * @param embedcode
	 *            ,pcode,videoPlugin,adPlugin,additionalPlugin,
	 *            playerConfigParameter
	 * @return returns dynamically created link from above parameters
	 */
	public static String getURL(String sslEnabled, String embedcode, String pcode, String pbid, String videoPlugin,
			String adPlugin, String additionalPlugin, String playerConfigParameter) {

		String environment = System.getProperty("environment");
		if ((environment == null || environment.equals(""))) {
			playerProperties.put(PlayerPropertyKey.ENVIRONMENT, PlayerPropertyValue.STAGING);
		} else if (environment.equalsIgnoreCase("PRODUCTION")) {
			String v4Version = System.getProperty("v4Version");
			if (v4Version == null || v4Version.equals("") || v4Version.equals("candidate/latest")) {
				logger.error("Please Provide V4 Version of Production Instance");
				logger.info("Running test on STAGING Environment as v4Version pointing to staging");
				playerProperties.put(PlayerPropertyKey.ENVIRONMENT, PlayerPropertyValue.STAGING);
			} else {
				logger.info("V4 Version is :: " + v4Version);
				playerProperties.put(PlayerPropertyKey.ENVIRONMENT, PlayerPropertyValue.PRODUCTION);
			}
		}

		test = new TestPage(playerProperties);
		url = test.getURL(sslEnabled, embedcode, pcode, pbid, videoPlugin, adPlugin, additionalPlugin,
				playerConfigParameter);
		return url;
	}

	/**
	 *
	 * @param testName
	 * @param testData
	 *            Passing node list so that we can have access to the nodes data
	 *            from xml file
	 * @return output : output contains two dimensional Object in which test
	 *         name and url is returned
	 */
	public static Map<String, UrlObject> parseXmlDataProvider(String testName, Testdata testData, String browserName,
			String browserVersion) {
		logger.info("Getting test url and test name from property file");

		liveChannelDetails = new HashMap<String, String>();
		Map<String, UrlObject> urlsGenerated = new HashMap<String, UrlObject>();
		String sslEnabled = null;
		boolean browserExisted = false;
		for (Test data : testData.getTest()) {
			if (data.getName().equals(testName)) {
				List<Url> urls = data.getUrl();
				for (Url url : urls) {
					// Adding browser support here.The data provider will not
					// even give that data if we do not support for that
					// browser.
					if (url.getBrowsersSupported() != null && url.getBrowsersSupported().getName() != null
							&& !url.getBrowsersSupported().getName().contains(browserName))
						continue;
					else
						browserExisted = true;

					// Not returning the data if the testdata contains the
					// driver browser version that is not matching
					// to the supported browser version

					if (url.getBrowserSupportedVersion() != null && url.getBrowsersSupported().getName() != null) {
						String[] tokens = url.getBrowserSupportedVersion().getName().split(",");
						if (tokens.length != 2)
							continue;
						if (browserExisted && browserName.contains(tokens[0])) {
							if (!browserVersion.contains(tokens[1]))
								continue;
						}

					}

					// to run tests for specific ad plugins
					if (applyFilter() && url.getAdPlugins().getName() != null && !url.getAdPlugins().getName().isEmpty()
							&& !url.getAdPlugins().getName().equalsIgnoreCase(adPluginFilter)) {
						continue;
					}

					// to run tests for specific video plugins
					if (applyVideoFilter() && url.getPlugins().getName() != null
							&& !url.getPlugins().getName().isEmpty()
							&& !url.getPlugins().getName().toUpperCase().contains(videoPluginFilter.toUpperCase())) {
						continue;
					}

					if (url.getLive() != null && url.getLive().getChannelId() != null) {
						liveChannelDetails.put(url.getDescription().getName(), url.getLive().getChannelId());
						liveChannelProviders.put(url.getDescription().getName(), url.getLive().getProvider());
					}

					String embedCode = url.getEmbedCode().getName();
					String pCode = url.getPcode().getName();
					String videoPlugin = url.getPlugins().getName();
					String adPlugin = url.getAdPlugins().getName();
					String additionalPlugin = url.getAdditionalPlugins().getName();
					String playerParameter = new String(url.getPlayerParameter().getBytes());
					String pbid = url.getPbid().getName();

					try {
						sslEnabled = url.getSslEnabled().getName();
					} catch (Exception e) {
						sslEnabled = "";
					}
					// enabling sas staging - to run test in sas staging
					// environment
					if (enableSASstaging()) {
						PropertyReader properties = null;
						try {
							properties = PropertyReader.getInstance("urlData.properties");
						} catch (Exception e) {
							e.getMessage();
						}
						int index = playerParameter.lastIndexOf("}");
						if (index == -1) {
							playerParameter = "{" + properties.getProperty("sas_staging_url") + "}";
						} else {
							playerParameter = playerParameter.substring(0, index) + ","
									+ properties.getProperty("sas_staging_url") + playerParameter.substring(index);
						}
					}

					String urlGenerated = UrlGenerator.getURL(sslEnabled, embedCode, pCode, pbid, videoPlugin, adPlugin,
							additionalPlugin, playerParameter);

					UrlObject urlObject = new UrlObject();
					urlObject.setUrl(urlGenerated);
					urlObject.setEmbedCode(embedCode);
					urlObject.setPCode(pCode);

					if (url.getSecret() != null && url.getSecret().getName() != null
							&& !url.getSecret().getName().isEmpty()) {
						urlObject.setSecret(url.getSecret().getName());
					}

					if (url.getApiKey() != null && url.getApiKey().getName() != null
							&& !url.getApiKey().getName().isEmpty()) {
						urlObject.setApiKey(url.getApiKey().getName());
					}

					String adFirstPlay = url.getAdPlugins().getAdFirstPlay();
					String adFrequency = url.getAdPlugins().getAdFrequency();

					if (adFirstPlay != null && !adFirstPlay.isEmpty() && adFrequency != null
							&& !adFrequency.isEmpty()) {
						urlObject.setAdFirstPlay(adFirstPlay);
						urlObject.setAdFrequency(adFrequency);
					}

					if (url.getError() != null && url.getError().getCode() != null) {
						urlObject.setErrorCode(url.getError().getCode());
					}

					if (url.getError() != null && url.getError().getDescription() != null) {
						urlObject.setErrorDescription(url.getError().getDescription());
					}

					String desc = url.getDescription().getName();

					if (url.getStreamType() != null && !url.getStreamType().getName().isEmpty()) {
						urlObject.setStreamType(url.getStreamType().getName());
					}

					if (url.getLive() != null && url.getLive().getChannelId() != null
							&& !url.getLive().getChannelId().isEmpty()) {
						urlObject.setChannelId(url.getLive().getChannelId());
					}

					if (url.getLive() != null && url.getLive().getProvider() != null
							&& !url.getLive().getProvider().isEmpty()) {
						urlObject.setProvider(url.getLive().getProvider());
					}

					urlsGenerated.put(desc, urlObject);

				}
			}
		}
		return urlsGenerated;
	}

	public static Map<String, String> getLiveChannelDetails() {
		return liveChannelDetails;
	}

	private static boolean applyFilter() {
		adPluginFilter = System.getProperty("adPlugin");
		if (adPluginFilter != null && !adPluginFilter.isEmpty()) {
			return true;
		}
		return false;
	}

	private static boolean applyVideoFilter() {
		videoPluginFilter = System.getProperty("videoPlugin");
		if (videoPluginFilter != null && !videoPluginFilter.isEmpty()) {
			return true;
		}
		return false;
	}

	private static boolean enableSASstaging() {
		String isSASStaging;
		isSASStaging = System.getProperty("runSASStaging");
		if (isSASStaging != null && isSASStaging.equalsIgnoreCase("true")) {
			return true;
		}
		return false;
	}

	public static Map<String, String> getLiveChannelProviders() {
		return liveChannelProviders;
	}

	public static Map<String, String> getStreamTypeDetails() {
		return streamTypeDetails;
	}

}