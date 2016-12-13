package com.ooyala.playback.url;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by jitendra
 */
public class UrlGenerator {

	private static TestPage test = null;
	private static String url;
	private static Map<PlayerPropertyKey, PlayerPropertyValue> playerProperties = new HashMap<PlayerPropertyKey, PlayerPropertyValue>();
	private static Logger logger = Logger.getLogger(UrlGenerator.class);
	private static String adPluginFilter = new String();
	private static Map<String, String> liveChannelDetails = new HashMap<String, String>();
	private static Map<String, String> liveChannelProviders = new HashMap<String, String>();

	/**
	 * @param embedcode
	 *            ,pcode,videoPlugin,adPlugin,additionalPlugin,
	 *            playerConfigParameter
	 * @return returns dynamically created link from above parameters
	 */
	public static String getURL(String sslEnabled, String embedcode,
			String pcode, String pbid, String videoPlugin, String adPlugin,
			String additionalPlugin, String playerConfigParameter) {

		String environment = System.getProperty("environment");
		if ((environment == null || environment.equals(""))) {
			playerProperties.put(PlayerPropertyKey.ENVIRONMENT,
					PlayerPropertyValue.STAGING);
		} else if (environment.equalsIgnoreCase("PRODUCTION")) {
			String v4Version = System.getProperty("v4Version");
			if (v4Version == null || v4Version.equals("")
					|| v4Version.equals("candidate/latest")) {
				logger.error("Please Provide V4 Version of Production Instance");
				logger.info("Running test on STAGING Environment as v4Version pointing to staging");
				playerProperties.put(PlayerPropertyKey.ENVIRONMENT,
						PlayerPropertyValue.STAGING);
			} else {
				logger.info("V4 Version is :: " + v4Version);
				playerProperties.put(PlayerPropertyKey.ENVIRONMENT,
						PlayerPropertyValue.PRODUCTION);
			}
		}

		test = new TestPage(playerProperties);
		url = test.getURL(sslEnabled, embedcode, pcode, pbid, videoPlugin,
				adPlugin, additionalPlugin, playerConfigParameter);
		return url;
	}

	/**
	 *
	 * @param testName
	 * @param testData
	 *            Passing node list so that we can have access to the nodes data
	 *            from xml file
	 * @return output : output contains two dimentional Object in which test
	 *         name and url is returned
	 */
	public static Map<String, String> parseXmlDataProvider(String testName,
			Testdata testData, String browserName, String browserVersion) {
		logger.info("Getting test url and test name from property file");

		liveChannelDetails = new HashMap<String, String>();
		Map<String, String> urlsGenerated = new HashMap<String, String>();
		String sslEnabled = null;
		for (Test data : testData.getTest()) {
			if (data.getName().equals(testName)) {
				List<Url> urls = data.getUrl();
				for (Url url : urls) {
					// Adding browser support here.The data provider will not
					// even give that data if we do not support for that
					// browser.
					if (url.getBrowsersSupported() != null
							&& url.getBrowsersSupported().getName() != null
							&& !browserName.contains(url.getBrowsersSupported()
									.getName()))
						continue;

					// Not returnign the data if the testdata contians the
					// driver browser version that is not matching
					// to the supported browser version

					if (url.getBrowserSupportedVersion() != null
							&& url.getBrowsersSupported().getName() != null) {
						String[] tokens = url.getBrowserSupportedVersion()
								.getName().split(",");
						if (tokens.length != 2)
							continue;
						if (!browserName.contains(tokens[0]))
							continue;
						else if (!tokens[1].contains(browserVersion))
							continue;
					}

					// to run tests for specific ad plugins
					if (applyFilter()
							&& url.getAdPlugins().getName() != null
							&& !url.getAdPlugins().getName().isEmpty()
							&& !url.getAdPlugins().getName()
									.equalsIgnoreCase(adPluginFilter)) {
						continue;
					}

					if (url.getLive() != null
							&& url.getLive().getChannelId() != null) {
						liveChannelDetails.put(url.getDescription().getName(),
								url.getLive().getChannelId());
						liveChannelProviders.put(
								url.getDescription().getName(), url.getLive()
										.getProvider());
					}

					String embedCode = url.getEmbedCode().getName();
					// String embedCode = test.;
					String pCode = url.getPcode().getName();
					String videoPlugin = url.getPlugins().getName();
					String adPlugin = url.getAdPlugins().getName();
					String additionalPlugin = url.getAdditionalPlugins()
							.getName();
					String playerParameter = new String(url
							.getPlayerParameter().getBytes());
					String pbid = url.getPbid().getName();

					try {
						sslEnabled = url.getSslEnabled().getName();
					} catch (Exception e) {
						sslEnabled = "";
					}

					String urlGenerated = UrlGenerator.getURL(sslEnabled,
							embedCode, pCode, pbid, videoPlugin, adPlugin,
							additionalPlugin, playerParameter);
					String desc = url.getDescription().getName();
					urlsGenerated.put(desc, urlGenerated);

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

	public static Map<String, String> getLiveChannelProviders() {
		return liveChannelProviders;
	}

}