package com.ooyala.playback.url;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ooyala.playback.enums.PlayerPropertyKey;
import com.ooyala.playback.enums.PlayerPropertyValue;
import org.apache.log4j.Logger;

import com.ooyala.playback.utils.CommandLineParameters;
import com.ooyala.qe.common.util.PropertyReader;

/**
 * Created by jitendra
 */
public class UrlGenerator {

	private TestPage test = null;
	private String url;
	private Map<PlayerPropertyKey, PlayerPropertyValue> playerProperties = new HashMap<PlayerPropertyKey, PlayerPropertyValue>();
	private Logger logger = Logger.getLogger(UrlGenerator.class);
	private String adPluginFilter = new String();
	private String videoPluginFilter = new String();
	private String descriptionFilter = new String();
	private String priority = new String();
	private Map<String, String> liveChannelDetails = new HashMap<String, String>();
	private Map<String, String> liveChannelProviders = new HashMap<String, String>();
	private Map<String, String> streamTypeDetails = new HashMap<String, String>();
	private TestDataValidator testDataValidator = new TestDataValidator();

	/**
	 * @param embedcode
	 *            ,pcode,videoPlugin,adPlugin,additionalPlugin,
	 *            playerConfigParameter
	 * @return returns dynamically created link from above parameters
	 */
	public String getURL(String sslEnabled, String embedcode, String pcode, String pbid, String videoPlugin,
			String adPlugin, String additionalPlugin, String playerConfigParameter,String skinJson) {

		String environment = System.getProperty(CommandLineParameters.environment);
		String v4Version = "latest";
		if (environment == null || environment.isEmpty()) {
			playerProperties.put(PlayerPropertyKey.ENVIRONMENT, PlayerPropertyValue.STAGING);
		} else if (environment.equalsIgnoreCase("PRODUCTION")) {
			playerProperties.put(PlayerPropertyKey.ENVIRONMENT, PlayerPropertyValue.PRODUCTION);
		} else if (environment.equalsIgnoreCase("STAGING")) {
			playerProperties.put(PlayerPropertyKey.ENVIRONMENT, PlayerPropertyValue.STAGING);
		} else if (environment.equalsIgnoreCase("STABLE")) {
			playerProperties.put(PlayerPropertyKey.ENVIRONMENT, PlayerPropertyValue.STABLE);
		} else if (environment.equalsIgnoreCase("SANDBOX")) {
			playerProperties.put(PlayerPropertyKey.ENVIRONMENT, PlayerPropertyValue.SANDBOX);
		}

		v4Version = System.getProperty(CommandLineParameters.v4Version);
		if (v4Version == null || v4Version.equals("")) {
			v4Version = "latest";
		}

		test = new TestPage(playerProperties);
		url = test.getURL(sslEnabled, embedcode, pcode, pbid, videoPlugin, adPlugin, additionalPlugin,
                playerConfigParameter, v4Version,skinJson);
		logger.info(url);
		return url;
	}

	/**
	 * @param testName
	 * @param testData
	 *            Passing node list so that we can have access to the nodes data
	 *            from xml file
	 * @return output : output contains two dimensional Object in which test
	 *         name and url is returned
	 */
	public Map<String, UrlObject> parseXmlDataProvider(String testName, Testdata testData, String browserName,
			String browserVersion) {
		logger.info("Getting test url and test name from property file");

		liveChannelDetails = new HashMap<String, String>();
		Map<String, UrlObject> urlsGenerated = new HashMap<String, UrlObject>();
		try {
			String sslEnabled;
			String sslEnabledBrowser;
			boolean browserExisted;
			for (Test data : testData.getTest()) {
				if (data.getName().equals(testName)) {
					List<Url> urls = data.getUrl();
					for (Url url : urls) {
						// Adding browser support here.
						// The data provider will not even give that data if we
						// do not support for that browser.
						if (url.getBrowsersSupported() != null && url.getBrowsersSupported().getName() != null
								&& !url.getBrowsersSupported().getName().contains(browserName))
							continue;
						else
							browserExisted = true;
						
						if(browserName.equalsIgnoreCase("firefox") && url.getPlugins().getName().contains("osmf_flash")) {
							// TODO there is an issue with flash plugin in FF with the current version
							continue;
						}

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

						if (applyPriorityFilter()) {

							if (!(url.getPriority() != null && url.getPriority().getName() != null
									&& !url.getPriority().getName().isEmpty()
									&& url.getPriority().getName().toLowerCase().contains(priority.toLowerCase()))) {
								continue;
							}
						}

						if (System.getProperty(CommandLineParameters.adobeTVSDK) != null
								&& !System.getProperty(CommandLineParameters.adobeTVSDK).isEmpty()
								&& System.getProperty(CommandLineParameters.adobeTVSDK).toLowerCase()
										.contains("false")) {
							if (url.getPlugins().getName() != null && !url.getPlugins().getName().isEmpty()
									&& url.getPlugins().getName().toUpperCase().contains("ADOBETVSDK")) {
								continue;
							}
						}

						if (url.getPlatformsSupported() != null && url.getPlatformsSupported().getName() != null
								&& !url.getPlatformsSupported().getName().toLowerCase()
										.contains(System.getProperty(CommandLineParameters.platform).toLowerCase())) {
							continue;
						}

						// to run tests for specific ad plugins

						if (applyFilter() && url.getAdPlugins().getName() != null
								&& !url.getAdPlugins().getName().isEmpty()) {
							if (adPluginFilter.equalsIgnoreCase("VPAID")) {
								if (!url.getDescription().getName().startsWith("VPAID")) {
									continue;
								}
							} else {
								if (!url.getAdPlugins().getName().equalsIgnoreCase(adPluginFilter)) {
									continue;
								}
							}
						}

						// to run the tests for specific test based on
						// description
						if (applyDescriptionFilter() && url.getDescription().getName() != null
								&& !url.getDescription().getName().isEmpty()
								&& !url.getDescription().getName().equalsIgnoreCase(descriptionFilter)) {
							continue;
						}

						// to run tests for specific video plugins
						if (applyVideoFilter() && url.getPlugins().getName() != null
								&& !url.getPlugins().getName().isEmpty() && !url.getPlugins().getName().toUpperCase()
										.contains(videoPluginFilter.toUpperCase())) {
							if (!testDataValidator.validateVideoPlugin(videoPluginFilter))
								break;
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
						String skinJson = url.getSkinJson() == null ? ""
								: url.getSkinJson().getName() != null ? url.getSkinJson().getName() : "";

						try {
							sslEnabled = url.getSslEnabled().getName();
							sslEnabledBrowser = url.getSslEnabled().getBrowser();
							if (sslEnabledBrowser != null && !sslEnabledBrowser.contains(browserName)) {
								sslEnabled = "";
							}
						} catch (Exception e) {
							sslEnabled = "";
							sslEnabledBrowser = "";
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

						String urlGenerated = getURL(sslEnabled, embedCode, pCode, pbid, videoPlugin, adPlugin,
								additionalPlugin, playerParameter,skinJson);

						UrlObject urlObject = new UrlObject();
						urlObject.setUrl(urlGenerated);
						urlObject.setEmbedCode(embedCode);
						urlObject.setPCode(pCode);
						urlObject.setPlayerId(pbid);

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
						String adStartTime = url.getAdPlugins().getAdPlayTime();

						if (adFirstPlay != null && !adFirstPlay.isEmpty() && adFrequency != null
								&& !adFrequency.isEmpty()) {
							urlObject.setAdFirstPlay(adFirstPlay);
							urlObject.setAdFrequency(adFrequency);
						}

						if (adStartTime != null && !adStartTime.isEmpty()) {
							urlObject.setAdStartTime(url.getAdPlugins().getAdPlayTime());
						}

						if (url.getError() != null && url.getError().getCode() != null) {
							urlObject.setErrorCode(url.getError().getCode());
						}

						if (url.getError() != null && url.getError().getDescription() != null) {
							urlObject.setErrorDescription(url.getError().getDescription());
						}

						String desc = url.getDescription().getName();

						boolean flag = true;
						if (url.getStreamType() != null && url.getStreamType().getName() != null
								&& !url.getStreamType().getName().isEmpty()) {
							urlObject.setStreamType(url.getStreamType().getName());
							flag = false;
						}

						if (flag) {
							if (url.getStreamType() != null && !url.getStreamType().getSupportedMuxFormat().isEmpty()) {
								urlObject.setSupportedMuxFormat(url.getStreamType().getSupportedMuxFormat());
							}
						}

						if (url.getLive() != null && url.getLive().getChannelId() != null
								&& !url.getLive().getChannelId().isEmpty()) {
							urlObject.setChannelId(url.getLive().getChannelId());
						}

						if (url.getLive() != null && url.getLive().getProvider() != null
								&& !url.getLive().getProvider().isEmpty()) {
							urlObject.setProvider(url.getLive().getProvider());
						}

						if (url.getPlayerParameter() != null) {
							urlObject.setPlayerParameter(url.getPlayerParameter());
						}

						if (url.getPlugins() != null) {
							urlObject.setVideoPlugins(url.getPlugins().getName());
						}

						if (url.getAdPlugins().getOverlayPlayTime() != null
								&& !url.getAdPlugins().getOverlayPlayTime().isEmpty()) {
							urlObject.setOverlayPlayTime(url.getAdPlugins().getOverlayPlayTime());
						}

						if (url.getAdPlugins() != null) {
							urlObject.setAdPlugins(url.getAdPlugins().getName());
							if (url.getAdPlugins().getClickthrough() != null
									&& !url.getAdPlugins().getClickthrough().isEmpty()
									&& url.getAdPlugins().getClickthrough().equalsIgnoreCase("false")) {
								urlObject.setIgnoreClickThrough(true);
							} else {
								urlObject.setIgnoreClickThrough(false);
							}
						}

						if (url.getAdditionalPlugins() != null) {
							urlObject.setAdditionalPlugins(url.getAdditionalPlugins().getName());
						}
						urlsGenerated.put(desc, urlObject);
					}
				} else {
					/*logger.error("test name from xml file : " + data.getName() + " and Actal test name: " + testName
							+ "are not matching");*/
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return urlsGenerated;
	}

	public Map<String, String> getLiveChannelDetails() {
		return liveChannelDetails;
	}

	private boolean applyFilter() {
		adPluginFilter = System.getProperty(CommandLineParameters.adPlugin);
		return adPluginFilter != null && !adPluginFilter.isEmpty();
	}

	private boolean applyVideoFilter() {
		videoPluginFilter = System.getProperty(CommandLineParameters.videoPlugin);
		return videoPluginFilter != null && !videoPluginFilter.isEmpty();
	}

	private boolean applyPriorityFilter() {
		priority = System.getProperty(CommandLineParameters.priority);
		return priority != null && !priority.isEmpty();
	}

	private boolean applyDescriptionFilter() {
		descriptionFilter = System.getProperty("description");
		if (descriptionFilter != null && !descriptionFilter.isEmpty()) {
			return true;
		}
		return false;
	}

	private boolean enableSASstaging() {
		String isSASStaging;
		isSASStaging = System.getProperty(CommandLineParameters.runSASStaging);
		return isSASStaging != null && isSASStaging.equalsIgnoreCase("true");
	}

	public Map<String, String> getLiveChannelProviders() {
		return liveChannelProviders;
	}

	public Map<String, String> getStreamTypeDetails() {
		return streamTypeDetails;
	}
}