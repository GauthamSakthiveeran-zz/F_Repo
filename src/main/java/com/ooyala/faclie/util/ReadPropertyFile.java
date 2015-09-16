/**
 * 
 */
package com.ooyala.faclie.util;

// TODO: Auto-generated Javadoc
/**
 * The Class ReadPropertyFile.
 * 
 * @author pkumar
 */
public class ReadPropertyFile {

	/*
	 * This method returns the value of the configuration parameter from the
	 * specified configuration file.
	 */
	/**
	 * Gets the configuration parameter.
	 * 
	 * @param propertyFileName
	 *            the property file name
	 * @param key
	 *            the key
	 * @return the configuration parameter
	 */
	public static String getConfigurationParameter(String propertyFileName,
			String key) {
		String parameterValue = null;

		try {
			ReadTriggerFile parameterFile = new ReadTriggerFile(
					propertyFileName);
			parameterValue = parameterFile.getParameter(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return parameterValue;
	}

	/**
	 * This method checks if the given key is found in the PropertyFile.
	 * 
	 * @param propertyFileName
	 *            the property file name
	 * @param key
	 *            the key
	 * @return True/False
	 */
	public static boolean checkConfigParameter(String propertyFileName,
			String key) {
		ReadTriggerFile parameterFile = new ReadTriggerFile(propertyFileName);
		return parameterFile.checkParameter(key);
	}
}
