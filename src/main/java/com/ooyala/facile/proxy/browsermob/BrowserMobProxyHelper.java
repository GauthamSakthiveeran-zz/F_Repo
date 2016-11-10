package com.ooyala.facile.proxy.browsermob;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;
import net.lightbody.bmp.proxy.ProxyServer;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class BrowserMobProxyHelper.
 */
public class BrowserMobProxyHelper {

	/** The browser mob proxy server. */
	protected static ProxyServer browserMobProxyServer;

	/** The logger. */
	public static Logger logger = Logger.getLogger(BrowserMobProxyHelper.class);

	/**
	 * Gets the browser mob proxy server.
	 * 
	 * @return the browser mob proxy server
	 */
	public static ProxyServer getBrowserMobProxyServer() {

		if (browserMobProxyServer == null) {
			browserMobProxyServer = new ProxyServer(9090);
			logger.info("Starting BrowserMob Proxy on port "
					+ browserMobProxyServer.getPort());
		}

		return browserMobProxyServer;
	}

	/**
	 * Start browser mob proxy server.
	 */
	public static void startBrowserMobProxyServer() {
		try {
			if (browserMobProxyServer != null) {
				browserMobProxyServer.start();
			} else {
				getBrowserMobProxyServer().start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Stop browser mob proxy server.
	 */
	public static void stopBrowserMobProxyServer() {
		if (browserMobProxyServer != null) {
			try {
				logger.info("Stopping BrowserMob Proxy server ");
				browserMobProxyServer.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * New har.
	 * 
	 * @param harFile
	 *            the har file
	 */
	public static void startHar(String harFile) {
		browserMobProxyServer.newHar(harFile);
	}

	/**
	 * End har.
	 */
	public static void endHar() {
		browserMobProxyServer.endPage();
	}

	/**
	 * Gets the har entries.
	 * 
	 * @return the har entries
	 */
	public static List<HarEntry> getHarEntries() {

		HarLog harLog = browserMobProxyServer.getHar().getLog();
		List<HarEntry> harEntries = harLog.getEntries();
		return harEntries;

	}

	/**
	 * Gets the query map.
	 * 
	 * @param entry
	 *            the entry
	 * @return the query map
	 */
	public static Map<String, String> getQueryMap(HarEntry entry) {

		String query = getUrlFromHarEntry(entry);
		String[] params = query.split("&");
		Map<String, String> map = new HashMap<String, String>();
		for (String param : params) {
			String name = param.split("=")[0];
			String value = param.split("=")[1];

			String decodedValue = null, decodedName = null;
			try {
				decodedName = URLDecoder.decode(name, "UTF-8");
				decodedValue = URLDecoder.decode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			map.put(decodedName, decodedValue);
		}
		return map;
	}

	/**
	 * Gets the url from har entry.
	 * 
	 * @param entry
	 *            the entry
	 * @return the url from har entry
	 */
	public static String getUrlFromHarEntry(HarEntry entry) {

		URL siteCatelystUrl = null;
		try {
			siteCatelystUrl = new URL(entry.getRequest().getUrl());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return siteCatelystUrl.getQuery();
	}
}
