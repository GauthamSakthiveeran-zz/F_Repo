package com.ooyala.playback.url;

import java.net.InetAddress;

import org.apache.log4j.Logger;

import com.ooyala.playback.enums.PlayerPropertyValue;
import com.ooyala.playback.httpserver.SimpleHttpServer;
import com.ooyala.qe.common.util.PropertyReader;

/**
 * Created by jitendra
 */
public class TestPageData {

    private static Logger logger = Logger.getLogger(TestPageData.class);

    private String baseURL;
    private String envURL;
    private String pluginURL;
    private String corePlayer;
    private String html5Skin;
    private String skinAsset;
    private String skinConf;
    private String skinDiscovery;
    private String playerBrandingId;
    private String providerCode;
    private String player_Config_Param;
    private String mainPlugin;
    private String bitmovinPlugin;
    private String osmfPlugin;
    private String akamaiPlugin;
    private String additionalPlugin;
    private String adPluginIMA;
    private String adPluginVast;
    private String adPluginFreewheel;
    private String adPluginPulse;
    private String defaultSkinConfig;
    private String skinConfigForOSMF_IMA;
    private String skinConfigForDiscovery;
    private String otherPlugin;
    private String discoveryApiPlugin;
    private String playlistPlugin;
    private String valhalla;
    private String valhalla_build;
    private String valhalla_build_id;
    private String adobeTVSDK;
    private PropertyReader properties;
    private InetAddress inetAddress;
    private String captionsJson;
    private String localizationJson;
    private String screenScrubberJson;
    private String shareJson;
    private String adJson;
    private String watermarkJson;
    private String buttonsJson;
    private String moreOptionsJson;

    /**
     * Initialize all the variable with respective value. Read the data from
     * requiredDataFields and put it into properties
     */

    public TestPageData() {
        try {
            properties = PropertyReader.getInstance("urlData.properties");
            baseURL = properties.getProperty("baseUrl");
            pluginURL = properties.getProperty("video_Plugin");
            mainPlugin = properties.getProperty("main_Plugin");
            bitmovinPlugin = properties.getProperty("bitmovin_Plugin");
            osmfPlugin = properties.getProperty("osmf_Plugin");
            akamaiPlugin = properties.getProperty("akamai_Plugin");
            corePlayer = properties.getProperty("corePlayer");
            html5Skin = properties.getProperty("HTML5Skin");
            skinAsset = properties.getProperty("skinAsset");
            skinConf = properties.getProperty("skinConf");
            additionalPlugin = properties.getProperty("additional_plugin");
            skinDiscovery = properties.getProperty("skinDiscovery");
            playerBrandingId = properties.getProperty("playerBrandingId");
            providerCode = properties.getProperty("providerCode");
            adPluginFreewheel = properties.getProperty("freewheel_ad_plugin");
            adPluginIMA = properties.getProperty("ima_ad_plugin");
            adPluginVast = properties.getProperty("vast_ad_plugin");
            adPluginPulse = properties.getProperty("pulse_ad_plugin");
            defaultSkinConfig = properties.getProperty("defaultSkinConfig");
            skinConfigForOSMF_IMA = properties.getProperty("skinConfigForOSMF_IMA");
            player_Config_Param = properties.getProperty("player_Config_Param");
            skinConfigForDiscovery = properties.getProperty("skinConfigForDiscovery");
            discoveryApiPlugin = properties.getProperty("discovery_api_plugin");
            otherPlugin = properties.getProperty("other_plugin");
            playlistPlugin = properties.getProperty("playlist_plugin");
            valhalla = properties.getProperty("valhalla");
            valhalla_build = properties.getProperty("valhalla_build");
            valhalla_build_id = properties.getProperty("valhalla_build_id");
            adobeTVSDK = properties.getProperty("adobeTVSDK_plugin");
            inetAddress = InetAddress.getLocalHost();
            captionsJson = properties.getProperty("Captions_json");
            localizationJson = properties.getProperty("Localization_json");
            screenScrubberJson = properties.getProperty("Screen_json");
            shareJson = properties.getProperty("Share_json");
            adJson = properties.getProperty("ad_json");
            watermarkJson = properties.getProperty("watermark_json");
            buttonsJson = properties.getProperty("buttons_json");
            moreOptionsJson = properties.getProperty("moreOptions_json");
            
        } catch (Exception e) {
            logger.error("Error while reading data from properties file :" + e.getMessage());
        }
    }

    public String getValhallaBuildId() {
        return valhalla_build_id;
    }

    public void setValhallaBuildId(String valhalla_build_id) {
        this.valhalla_build_id = valhalla_build_id;
    }

    public String getValhalla() {
        return valhalla;
    }

    public void setValhalla(String valhalla) {
        this.valhalla = valhalla;
    }

    public String getValhallaBuild() {
        return valhalla_build;
    }

    public void setValhallaBuild(String valhalla_build) {
        this.valhalla_build = valhalla_build;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getEnvURL() {
        return envURL;
    }

    public void setEnvURL(String envURL) {
        this.envURL = envURL;
    }

    public String getPluginURL() {
        return pluginURL;
    }

    public void setPluginURL(String pluginURL) {
        this.pluginURL = pluginURL;
    }

    public String getCorePlayer() {
        return corePlayer;
    }

    public void setCorePlayer(String corePlayer) {
        this.corePlayer = corePlayer;
    }

    public String getHtml5Skin() {
        return html5Skin;
    }

    public void setHtml5Skin(String html5Skin) {
        this.html5Skin = html5Skin;
    }

    public String getSkinAsset() {
        return skinAsset;
    }

    public void setSkinAsset(String skinAsset) {
        this.skinAsset = skinAsset;
    }

    public String getSkinConf() {
        return skinConf;
    }

    public void setSkinConf(String skinConf) {
        this.skinConf = skinConf;
    }

    public String getSkinDiscovery() {
        return skinDiscovery;
    }

    public void setSkinDiscovery(String skinDiscovery) {
        this.skinDiscovery = skinDiscovery;
    }

    public String getPlayerBrandingId() {
        return playerBrandingId;
    }

    public void setPlayerBrandingId(String playerBrandingId) {
        this.playerBrandingId = playerBrandingId;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public String getPlayer_Config_Param() {
        return player_Config_Param;
    }

    public void setPlayer_Config_Param(String player_Config_Param) {
        this.player_Config_Param = player_Config_Param;
    }

    public String getMainPlugin() {
        return mainPlugin;
    }

    public void setMainPlugin(String mainPlugin) {
        this.mainPlugin = mainPlugin;
    }

    public String getBitmovinPlugin() {
        return bitmovinPlugin;
    }

    public void setBitmovinPlugin(String bitmovinPlugin) {
        this.bitmovinPlugin = bitmovinPlugin;
    }

    public String getOsmfPlugin() {
        return osmfPlugin;
    }

    public void setOsmfPlugin(String osmfPlugin) {
        this.osmfPlugin = osmfPlugin;
    }

    public String getAkamaiPlugin() {
        return akamaiPlugin;
    }

    public void setAkamaiPlugin(String akamaiPlugin) {
        this.akamaiPlugin = akamaiPlugin;
    }

    public String getAdditionalPlugin() {
        return additionalPlugin;
    }

    public void setAdditionalPlugin(String additionalPlugin) {
        this.additionalPlugin = additionalPlugin;
    }

    public String getAdPluginIMA() {
        return adPluginIMA;
    }

    public void setAdPluginIMA(String adPluginIMA) {
        this.adPluginIMA = adPluginIMA;
    }

    public String getAdPluginVast() {
        return adPluginVast;
    }

    public void setAdPluginVast(String adPluginVast) {
        this.adPluginVast = adPluginVast;
    }

    public String getAdPluginFreewheel() {
        return adPluginFreewheel;
    }

    public void setAdPluginFreewheel(String adPluginFreewheel) {
        this.adPluginFreewheel = adPluginFreewheel;
    }

    public String getAdPluginPulse() {
        return adPluginPulse;
    }

    public void setAdPluginPulse(String adPluginPulse) {
        this.adPluginPulse = adPluginPulse;
    }

    public String getDefaultSkinConfig() {
        return defaultSkinConfig;
    }

    public void setDefaultSkinConfig(String defaultSkinConfig) {
        this.defaultSkinConfig = defaultSkinConfig;
    }

    public String getSkinConfigForOSMF_IMA() {
        return skinConfigForOSMF_IMA;
    }

    public void setSkinConfigForOSMF_IMA(String skinConfigForOSMF_IMA) {
        this.skinConfigForOSMF_IMA = skinConfigForOSMF_IMA;
    }

    public String getSkinConfigForDiscovery() {
        return skinConfigForDiscovery;
    }

    public void setSkinConfigForDiscovery(String skinConfigForDiscovery) {
        this.skinConfigForDiscovery = skinConfigForDiscovery;
    }

    public String getOtherPlugin() {
        return otherPlugin;
    }

    public void setOtherPlugin(String otherPlugin) {
        this.otherPlugin = otherPlugin;
    }

    public String getDiscoveryApiPlugin() {
        return discoveryApiPlugin;
    }

    public void setDiscoveryApiPlugin(String discoveryApiPlugin) {
        this.discoveryApiPlugin = discoveryApiPlugin;
    }

    /**
     * Initialzes the data as per the Environment in which test needs to be run.
     * e.g Staging OR Production
     *
     * @param envType
     */
    public void initializeData(String sslEnabled, PlayerPropertyValue envType, String v4Version) {

        if (sslEnabled == "" || sslEnabled == null) {
            sslEnabled = "http";
        } else
            sslEnabled = "https";

        switch (envType) {

            case STAGING:
                envURL = sslEnabled + properties.getProperty("staging_env_url") + v4Version;
                break;
            case PRODUCTION:
                envURL = sslEnabled + properties.getProperty("production_env_url") + v4Version;
                break;
            case STABLE:
            	envURL = sslEnabled + properties.getProperty("stable_env_url") + v4Version;
                break;
                
            case SANDBOX:
            	envURL = sslEnabled + properties.getProperty("sandbox_env_url") + v4Version;
                break;    
                
            default:
                break;
        }
        
        pluginURL = envURL + "/video-plugin/";
        corePlayer = envURL + corePlayer;
        html5Skin = envURL + html5Skin;
        skinAsset = envURL + skinAsset;
        skinConf = "" + skinConf;
        skinDiscovery = "" + envURL + otherPlugin + discoveryApiPlugin;
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
            case "ADOBETVSDK":
                return pluginURL + adobeTVSDK;
            case "ANALYTICS":
                return "http://" + inetAddress.getHostAddress() + ":"
                        + SimpleHttpServer.portNumber + "/js?fileName=analytics/AnalyticsQEPlugin.js";
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
            case "PLAYLIST":
                return envURL + otherPlugin + playlistPlugin;
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
    public String getSkinConfigPlugin(String plugins, String adPlugin, String additionalPlugin, String skinJson) {

        if (plugins.isEmpty())
            return "";

        if (additionalPlugin.contains("DISCOVERY")) {
        	    if(skinJson.contains("MOREOPTIONS")) {
        	    	 return skinConf + moreOptionsJson;
        	    }
            return skinConf + skinConfigForDiscovery;
        } else if (adPlugin.equals("IMA")) {
            if (plugins.equals("MAIN")) {
                return skinConf + skinConfigForOSMF_IMA;
            }
            if (plugins.contains(",") && plugins.contains("OSMF")) {
                return skinConf + skinConfigForOSMF_IMA;
            }
        }
        
        if(skinJson.contains("CAPTIONS"))
        	return skinConf + captionsJson;
        else if(skinJson.contains("LOCALIZATION"))
        	return skinConf + localizationJson;
        else if(skinJson.contains("SCRUBBER"))
        	return skinConf + screenScrubberJson;
        else if(skinJson.contains("AD"))
        	return skinConf + adJson;
        else if(skinJson.contains("WATERMARK"))
        	return skinConf + watermarkJson;
        else if(skinJson.contains("SHARE"))
        	return skinConf + shareJson;
        else if(skinJson.contains("MOREOPTIONS"))
        	return skinConf + moreOptionsJson;
        else if(skinJson.contains("BUTTONS"))
        	return skinConf + buttonsJson;
        

		/*
		 * if (plugins.contains(",")) { String str[] = plugins.split(","); for
		 * (int i = 0; i < str.length; i++) { if (additionalPlugin != "") {
		 * return skinConf + skinConfigForDiscovery;
		 * 
		 * } else { if (str[i].equals("OSMF") && adPlugin.equals("IMA")) {
		 * return skinConf + skinConfigForOSMF_IMA; } } }
		 * 
		 * } else { if (additionalPlugin != "") { return skinConf +
		 * skinConfigForDiscovery; } }
		 */

        return skinConf + defaultSkinConfig;

    }

}
