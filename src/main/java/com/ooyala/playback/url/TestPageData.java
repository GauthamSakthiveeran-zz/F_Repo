package com.ooyala.playback.url;

import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Created by jitendra
 */
public class TestPageData {

	public static Logger logger = Logger.getLogger(TestPageData.class);
	
	public String baseURL = null;
	public String envURL = null;
	public String pluginURL = null;
	public String corePlayer = null;
	public String html5Skin = null;
	public String skinAsset = null;
	public String skinConf = null;
	public String skinDiscovery = null;
	public String playerBrandingId = null;
	public String providerCode = null;
	public String player_Config_Param = null;
	public String mainPlugin = null;
	public String bitmovinPlugin = null;
	public String osmfPlugin = null;
	public String akamaiPlugin = null;
	public String additionalPlugin = null;
	public String adPluginIMA = null;
	public String adPluginVast = null;
	public String adPluginFreewheel = null;
	public String adPluginPulse = null;
	public String defaultSkinConfig = null;
	public String skinConfigForOSMF_IMA = null;
	public String skinConfigForDiscovery = null;
	public String otherPlugin = null;
	public String discoveryApiPlugin = null;
	public Map<String, String> map = null;

	/**
	 * Initialize all the variable with respective value. Read the data from
	 * requiredDataFields and put it into Map
	 */

	public TestPageData() {
		try {
			map = PropertyReader
					.getProperty("src/test/resources/urlData");
			baseURL = map.get("baseUrl");
			pluginURL = map.get("video_Plugin");
			mainPlugin = map.get("main_Plugin");
			bitmovinPlugin = map.get("bitmovin_Plugin");
			osmfPlugin = map.get("osmf_Plugin");
			akamaiPlugin = map.get("akamai_Plugin");
			corePlayer = map.get("corePlayer");
			html5Skin = map.get("HTML5Skin");
			skinAsset = map.get("skinAsset");
			skinConf = map.get("skinConf");
			additionalPlugin = map.get("additional_plugin");
			skinDiscovery = map.get("skinDiscovery");
			playerBrandingId = map.get("playerBrandingId");
			providerCode = map.get("providerCode");
			adPluginFreewheel = map.get("freewheel_ad_plugin");
			adPluginIMA = map.get("ima_ad_plugin");
			adPluginVast = map.get("vast_ad_plugin");
			adPluginPulse = map.get("pulse_ad_plugin");
			defaultSkinConfig = map.get("defaultSkinConfig");
			skinConfigForOSMF_IMA = map.get("skinConfigForOSMF_IMA");
			player_Config_Param = map.get("player_Config_Param");
			skinConfigForDiscovery = map.get("skinConfigForDiscovery");
			discoveryApiPlugin = map.get("discovery_api_plugin");
			otherPlugin = map.get("other_plugin");

		} catch (Exception e) {
			System.out
					.println("Error while reading data from properties file :"
							+ e.getMessage());
		}
	}

	/**
	 * Initialzes the data as per the Environment in which test needs to be run.
	 * e.g Staging OR Production
	 * 
	 * @param envType
	 */
	public void initializeData(PlayerPropertyValue envType) {
		switch (envType) {

		case ENVIRONMENT_STAGING:
			envURL = map.get("staging_env_url");
			pluginURL = envURL + pluginURL;
			corePlayer = envURL + corePlayer;
			html5Skin = envURL + html5Skin;
			skinAsset = envURL + skinAsset;
			skinConf = "" + skinConf;
			skinDiscovery = envURL + otherPlugin + discoveryApiPlugin;
			break;
		case ENVIRONMENT_PRODUCTION:
			envURL = map.get("production_env_url");
			pluginURL = envURL + "/video-plugin/";
			corePlayer = envURL + corePlayer;
			html5Skin = envURL + html5Skin;
			skinAsset = envURL + skinAsset;
			skinConf = "" + skinConf;
			skinDiscovery = "" + envURL + otherPlugin + discoveryApiPlugin;
			;
			break;
		}
	}

	/**
	 * Create video plugin url for respective pluginType
	 * 
	 * @param pluginType
	 * @return return url of video plugin as per the Environment
	 */
	public String getPluginForStream(String pluginType) {
		switch (pluginType) {
		case "MAIN":
			return pluginURL + mainPlugin;
		case "BITMOVIN":
			return pluginURL + bitmovinPlugin;
		case "OSMF":
			return pluginURL + osmfPlugin;
		case "AKAMAI":
			return pluginURL + akamaiPlugin;
		}
		return "";
	}

	/**
	 * Create ad plugin url for respective adPluginType
	 * 
	 * @param adPluginType
	 * @return return url of ad plugin as per the Environment
	 */
	public String getPluginForAd(String adPluginType) {
		switch (adPluginType) {
		case "IMA":
			return envURL + adPluginIMA;
		case "VAST":
			return envURL + adPluginVast;
		case "FREEWHEEL":
			return envURL + adPluginFreewheel;
		case "PULSE":
			return envURL + adPluginPulse;
		}
		return "";
	}

	/**
	 * Create additional plugin url for respective additionalPluginType
	 * 
	 * @param additionalPluginType
	 * @return return url of additional plugin as per the Environment
	 */
	public String getAdditionalPlugin(String additionalPluginType) {
		switch (additionalPluginType) {
		case "DISCOVERY":
			return envURL + otherPlugin + discoveryApiPlugin;
		}

		return "";
	}

	/**
	 * Create skinConfig plugin url as per the video plugin type,adPlugin type
	 * and additionalPlugin type
	 * 
	 * @param plugins
	 *            ,adPlugin,additionalPlugin
	 * @return returns skinConfig plugin url as per the Environment
	 */
	public String getSkinConfigPlugin(String plugins, String adPlugin,
			String additionalPlugin) {

		if (plugins.contains(",")) {
			String str[] = plugins.split(",");
			for (int i = 0; i < str.length; i++) {
				if (additionalPlugin != "") {
					return skinConf + skinConfigForDiscovery;

				} else {
					if (str[i].equals("OSMF") && adPlugin.equals("IMA")) {
						return skinConf + skinConfigForOSMF_IMA;
					}
				}
			}

		} else {
			if (additionalPlugin != "") {
				return skinConf + skinConfigForDiscovery;
			}
		}

		return skinConf + defaultSkinConfig;

	}
}
