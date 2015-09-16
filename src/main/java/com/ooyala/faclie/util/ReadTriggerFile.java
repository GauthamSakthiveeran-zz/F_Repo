package com.ooyala.faclie.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class ReadTriggerFile.
 */
public class ReadTriggerFile {

	/** The parameters. */
	private Properties parameters;

	/**
	 * Sets up the file to be used by class.
	 *
	 * @param file
	 *            path to the parameter file
	 */
	public ReadTriggerFile(String file) {
		parameters = new Properties();
		InputStream is;
		try {
			// is = new FileInputStream(file);
//			Process p = Runtime.getRuntime().exec("echo pwd");
//			is = p.getInputStream();
//			while(is.)
			
			is = ReadTriggerFile.class.getClassLoader().getResourceAsStream(file);
			if (is == null)
				is = new FileInputStream(file);

			if (file.endsWith(".xml")) {
				parameters.loadFromXML(is);
			} else
				parameters.load(is);
		} catch (IOException e) {
			System.err.println("Error reading file from: " + file);
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the value for the specified parameter and throws an exception
	 * if it can't be found.
	 *
	 * @param param
	 *            the param
	 * @return the parameter
	 */
	public String getParameter(String param) {
		return getParameter(param, true);
	}

	/**
	 * Retrieves the value for the specified parameter and errors out if the
	 * parameter is required but is not found in the file.
	 *
	 * @param param
	 *            the param
	 * @param paramRequired
	 *            the param required
	 * @return value of the parameter
	 */
	public String getParameter(String param, boolean paramRequired) {
		String parameter = parameters.getProperty(param);
		if (parameter == null && paramRequired)
			throw new NullPointerException("Parameter: " + param
					+ " not found in the file");
		return parameter;
	}

	/**
	 * Gets the parameter.
	 *
	 * @param param
	 *            the param
	 * @param defaultValue
	 *            the default value
	 * @return the parameter
	 */
	public String getParameter(String param, String defaultValue) {
		String parameter = parameters.getProperty(param, defaultValue);
		return parameter;
	}

	/**
	 * Method to check if a given property is present in the properties file.
	 *
	 * @param param
	 *            the param
	 * @return true, if successful
	 */
	public boolean checkParameter(String param) {
		String parameter = parameters.getProperty(param);
		if (parameter == null) {
			return false;
		} else {
			return true;
		}
	}

}