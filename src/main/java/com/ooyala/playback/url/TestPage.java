package com.ooyala.playback.url;

import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by jitendra
 */
public class TestPage {

	TestPageData testpagedata = null;
	String pluginType = null;
	PlayerPropertyValue environmentType = null;
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
	public String getURL(String embedCode, String pCode, String plugins,
			String adPlugin, String additionalPlugin, String playerParameter) {
		boolean result = false;
		String url = "";
		try {
			testpagedata.initializeData(environmentType);

			if (plugins.contains(",")) {
				String str[] = plugins.split(",");
				for (int i = 0; i < str.length; i++) {
					vplugin = vplugin + testpagedata.getPluginForStream(str[i])
							+ "\n";

				}

				url = testpagedata.baseURL
						+ "?ec="
						+ embedCode
						+ "&pbid="
						+ URLEncoder.encode(testpagedata.playerBrandingId,
								"UTF8")
						+ "&pcode="
						+ pCode
						+ "&core_player="
						+ URLEncoder.encode(testpagedata.corePlayer, "UTF8")
						+ "&video_plugins="
						+ URLEncoder.encode(vplugin, "UTF8")
						+ "&html5_skin="
						+ URLEncoder.encode(testpagedata.html5Skin, "UTF8")
						+ "&skin_asset="
						+ URLEncoder.encode(testpagedata.skinAsset, "UTF8")
						+ "&skin_config="
						+ URLEncoder.encode(testpagedata.getSkinConfigPlugin(
								plugins, adPlugin, additionalPlugin), "UTF8")
						+ "&ad_plugin="
						+ URLEncoder.encode(
								testpagedata.getPluginForAd(adPlugin), "UTF8")
						+ "&additional_plugins="
						+ URLEncoder.encode(testpagedata
								.getAdditionalPlugin(additionalPlugin), "UTF8")
						+ "&options="
						+ URLEncoder.encode(playerParameter, "UTF8");
			} else {
				url = testpagedata.baseURL
						+ "?ec="
						+ embedCode
						+ "&pbid="
						+ URLEncoder.encode(testpagedata.playerBrandingId,
								"UTF8")
						+ "&pcode="
						+ pCode
						+ "&core_player="
						+ URLEncoder.encode(testpagedata.corePlayer, "UTF8")
						+ "&video_plugins="
						+ URLEncoder.encode(
								testpagedata.getPluginForStream(plugins),
								"UTF8")
						+ "&html5_skin="
						+ URLEncoder.encode(testpagedata.html5Skin, "UTF8")
						+ "&skin_asset="
						+ URLEncoder.encode(testpagedata.skinAsset, "UTF8")
						+ "&skin_config="
						+ URLEncoder.encode(testpagedata.getSkinConfigPlugin(
								plugins, adPlugin, additionalPlugin), "UTF8")
						+ "&ad_plugin="
						+ URLEncoder.encode(
								testpagedata.getPluginForAd(adPlugin), "UTF8")
						+ "&additional_plugins="
						+ URLEncoder.encode(testpagedata
								.getAdditionalPlugin(additionalPlugin), "UTF8")
						+ "&options="
						+ URLEncoder.encode(playerParameter, "UTF8");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

}
