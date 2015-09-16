/*
 * 
 */
package com.ooyala.facile.util;

// TODO: Auto-generated Javadoc
/**
 * The Class CommonUtils.
 */
public class CommonUtils {

	/**
	 * Gets the oS name.
	 * 
	 * @return the oS name
	 */
	public static String getOSName() {
		String osName = System.getProperty("os.name");
		return osName;
	}

	/**
	 * Read property or env.
	 *
	 * @param key
	 *            the key
	 * @param defaultValue
	 *            the default value
	 * @return the string
	 */
	public static String readPropertyOrEnv(String key) {
		String v = System.getProperty(key);
		if (v == null) {
			v = System.getenv(key);
		}
		return v;
	}
}
