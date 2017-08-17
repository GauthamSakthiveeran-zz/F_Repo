package com.ooyala.playback.url;

import java.net.URLEncoder;
import java.util.Map;

import com.ooyala.playback.enums.PlayerPropertyKey;
import com.ooyala.playback.enums.PlayerPropertyValue;
import com.ooyala.playback.utils.CommandLineParameters;

/**
 * Created by jitendra
 */
public class TestPage {

	TestPageData testpagedata = null;
	String pluginType = null;
	PlayerPropertyValue environmentType = null;
	String additionalPlugins = "";
	String vplugin = "";

	/**
	 * Stream Type - MP4/HDS/HLS/DASH/WEBM Environment Type - Staging/Production
	 *
	 * @param playerProperties
	 */

	public TestPage(Map<PlayerPropertyKey, PlayerPropertyValue> playerProperties) {
		testpagedata = new TestPageData();
		environmentType = playerProperties.get(PlayerPropertyKey.ENVIRONMENT);
	}

	/**
	 * @param embedCode
	 *            ,pCode,plugins,adPlugin,additionalPlugin,playerParameter
	 * @return returns the url based on different input parameter mentioned
	 *         above in @Param
	 */
	public String getURL(String sslEnabled, String embedCode, String pCode, String pbid, String plugins,
			String adPlugin, String additionalPlugin, String playerParameter, String v4Version) {

		String url = "";

		try {
			testpagedata.initializeData(sslEnabled, environmentType, v4Version);

			if (plugins.contains(",")) {
				String str[] = plugins.split(",");
				for (int i = 0; i < str.length; i++) {
					vplugin = vplugin + testpagedata.getPluginForStream(str[i]) + "\n";
				}
			} else {
				vplugin = testpagedata.getPluginForStream(plugins);
			}

			if (additionalPlugin.contains(",")) {
				String str[] = additionalPlugin.split(",");
				for (int i = 0; i < str.length; i++) {
					if (!testpagedata.getAdditionalPlugin(str[i]).isEmpty())
						additionalPlugins = additionalPlugins + testpagedata.getAdditionalPlugin(str[i]) + "\n";
					else
						additionalPlugins = additionalPlugins + testpagedata.getPluginForAd(str[i]) + "\n";
				}
			} else if (!additionalPlugin.isEmpty()) {
				additionalPlugins = testpagedata.getAdditionalPlugin(additionalPlugin);
				if (additionalPlugins.isEmpty())
					additionalPlugins = testpagedata.getPluginForStream(additionalPlugin);
				if (additionalPlugins.isEmpty())
					additionalPlugins = testpagedata.getPluginForAd(additionalPlugin);
			}

			String corePlayer = testpagedata.getCorePlayer();
			String html5Skin = testpagedata.getHtml5Skin();
			String skinAsset = testpagedata.getSkinAsset();

			if (plugins.isEmpty()) {
				String buildId = System.getProperty(CommandLineParameters.valhalla_build_id);
				if (buildId == null || buildId.isEmpty()) {
					buildId = testpagedata.getValhallaBuildId();
				}
				corePlayer = testpagedata.getValhalla() + pbid;
				html5Skin = skinAsset = "";
			}

			if (sslEnabled == "" || sslEnabled == null) {
				sslEnabled = "http";
			} else
				sslEnabled = "https";

			url = testpagedata.getBaseURL().replaceFirst("http", sslEnabled) + "?ec=" + embedCode + "&pbid=" + pbid
					+ "&pcode=" + pCode + "&core_player=" + URLEncoder.encode(corePlayer, "UTF8") + "&video_plugins="
					+ URLEncoder.encode(vplugin, "UTF8") + "&html5_skin=" + URLEncoder.encode(html5Skin, "UTF8")
					+ "&skin_asset=" + URLEncoder.encode(skinAsset, "UTF8") + "&skin_config="
					+ URLEncoder.encode(testpagedata.getSkinConfigPlugin(plugins, adPlugin, additionalPlugin), "UTF8")
					+ "&ad_plugin=" + URLEncoder.encode(testpagedata.getPluginForAd(adPlugin), "UTF8")
					+ "&additional_plugins=" + URLEncoder.encode(additionalPlugins, "UTF8") + "&options="
					+ URLEncoder.encode(playerParameter, "UTF8").replace("+", "%20");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

}