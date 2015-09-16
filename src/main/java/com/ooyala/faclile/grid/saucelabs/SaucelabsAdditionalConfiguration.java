/*
 * 
 */
package com.ooyala.faclile.grid.saucelabs;

import org.apache.log4j.Logger;

import com.ooyala.faclie.util.ReadPropertyFile;

// TODO: Auto-generated Javadoc
/**
 * This class handles the advance configuration when using Sauce Grid.
 * 
 * @author pkumar
 * 
 */
public class SaucelabsAdditionalConfiguration {

	/** The logger. */
	public static Logger logger = Logger
			.getLogger(SaucelabsAdditionalConfiguration.class);

	/** The Constant SAUCE_CONFIG_PATH. */
	private static final String SAUCE_CONFIG_PATH = "/config/sauce.properties";

	/**
	 * This method read the "SAUCE_VIDEO_RECORDING key from sauce.properties and
	 * decides if the video should be turned ON/OFF
	 * 
	 * @return the string
	 */
	protected static String isSauceVideoEnabled() {
		String isVideoEnabled;

		logger.info("Entering Method :"
				+ Thread.currentThread().getStackTrace()[1].getMethodName());
		// Checking if the Sauce Video recording is set to true/false. Reading
		// the value from sauce.properties. If the key is not found in the
		// config defaulting the value to True.
		if (!(ReadPropertyFile.checkConfigParameter(SAUCE_CONFIG_PATH,
				"SAUCE_VIDEO_RECORDING"))) {
			isVideoEnabled = "true";
			logger.debug("..Since the SAUCE_VIDEO_RECORDING key is not found in the properties file, defaulting the value to TRUE");
		} else {
			isVideoEnabled = ReadPropertyFile.getConfigurationParameter(
					SAUCE_CONFIG_PATH, "SAUCE_VIDEO_RECORDING");
			logger.debug("Reading SAUCE_VIDEO_RECORDING key from the properties file, value currently set to : "
					+ isVideoEnabled);
			// If the user has the key available but no value set, the default
			// is set to true.
			if ((isVideoEnabled.equalsIgnoreCase(""))
					|| (isVideoEnabled.equalsIgnoreCase(null))) {
				isVideoEnabled = "true";
				logger.debug("...Since the key SAUCE_VIDEO_RECORDING is present in the properties file but no value set, defaulting the value to true ...");
			}
		}
		logger.info("Exiting Method :"
				+ Thread.currentThread().getStackTrace()[1].getMethodName());
		return isVideoEnabled;
	}

	/**
	 * This method read the "SAUCE_CAPTURE_SCREENSHOTS key from sauce.properties
	 * and decides if the Capture Screenshots should be turned ON/OFF
	 * 
	 * @return the string
	 */
	protected static String isCaptureScreenshotsEnabled() {
		logger.info("Entering Method :"
				+ Thread.currentThread().getStackTrace()[1].getMethodName());
		String isScreenshotEnabled;

		// Checking if the Sauce Capture Screenshot is set to true/false.
		// Reading
		// the value from sauce.properties. If the key is not found in the
		// config defaulting the value to True.
		if (!(ReadPropertyFile.checkConfigParameter(SAUCE_CONFIG_PATH,
				"SAUCE_CAPTURE_SCREENSHOTS"))) {
			isScreenshotEnabled = "true";
		} else {
			isScreenshotEnabled = ReadPropertyFile.getConfigurationParameter(
					SAUCE_CONFIG_PATH, "SAUCE_CAPTURE_SCREENSHOTS");
			// If the user has the key available but no value set, the default
			// is set to true.
			if ((isScreenshotEnabled.equalsIgnoreCase(""))
					|| (isScreenshotEnabled.equalsIgnoreCase(null))) {
				isScreenshotEnabled = "true";
			}
		}
		logger.info("Entering Method :"
				+ Thread.currentThread().getStackTrace()[1].getMethodName()
				+ ", with return type : " + isScreenshotEnabled);
		return isScreenshotEnabled;
	}

	/**
	 * This method read the "SAUCE_CAPTURE_HTML key from sauce.properties and
	 * decides if the Capture HTML should be turned ON/OFF
	 * 
	 * @return the string
	 */
	protected static String isCaptureHTMLEnabled() {

		String isHTMLEnabled;
		if (!(ReadPropertyFile.checkConfigParameter(SAUCE_CONFIG_PATH,
				"SAUCE_CAPTURE_HTML"))) {
			isHTMLEnabled = "true";
		} else {
			isHTMLEnabled = ReadPropertyFile.getConfigurationParameter(
					SAUCE_CONFIG_PATH, "SAUCE_CAPTURE_HTML");
			// If the user has the key available but no value set, the default
			// is set to true.
			if ((isHTMLEnabled.equalsIgnoreCase(""))
					|| (isHTMLEnabled.equalsIgnoreCase(null))) {
				isHTMLEnabled = "true";
			}
		}
		return isHTMLEnabled;
	}

	/**
	 * This method read the "SAUCE_MAX_TEST_DURATION" key from sauce.properties
	 * 
	 * @return the max test duration timeout
	 */
	protected static int getMaxTestDurationTimeout() {

		String maxTimeout;
		if (!(ReadPropertyFile.checkConfigParameter(SAUCE_CONFIG_PATH,
				"SAUCE_MAX_TEST_DURATION"))) {
			maxTimeout = "300";
		} else {
			maxTimeout = ReadPropertyFile.getConfigurationParameter(
					SAUCE_CONFIG_PATH, "SAUCE_MAX_TEST_DURATION");
			if ((maxTimeout.equalsIgnoreCase(""))
					|| (maxTimeout.equalsIgnoreCase(null))) {
				maxTimeout = "300";
			}
		}
		return Integer.parseInt(maxTimeout);
	}

	/**
	 * This method read the "SAUCE_IDLE_TEST_TIMEOUT" key from sauce.properties
	 * 
	 * @return the idle test timeout
	 */
	protected static int getIdleTestTimeout() {

		String idleTestTimeout;
		if (System.getProperty("SAUCE_IDLE_TEST_TIMEOUT") == null) {
			if (!(ReadPropertyFile.checkConfigParameter(SAUCE_CONFIG_PATH,
					"SAUCE_IDLE_TEST_TIMEOUT"))) {
				idleTestTimeout = "60";
			} else {
				idleTestTimeout = ReadPropertyFile.getConfigurationParameter(
						SAUCE_CONFIG_PATH, "SAUCE_IDLE_TEST_TIMEOUT");
				// If the user has the key available but no value set, the
				// default
				// is set to true.
				if ((idleTestTimeout.equalsIgnoreCase(""))
						|| (idleTestTimeout.equalsIgnoreCase(null))) {
					idleTestTimeout = "60";
				}
			}

		} else {
			idleTestTimeout = System.getProperty("SAUCE_IDLE_TEST_TIMEOUT");
		}
		return Integer.parseInt(idleTestTimeout);
	}

	/**
	 * This method read the "SAUCE_SCREEN_RESOLUTION" key from sauce.properties
	 * 
	 * @return the string
	 */
	protected static String setScreenResoulution() {

		String screenResolution;
		if (!(ReadPropertyFile.checkConfigParameter(SAUCE_CONFIG_PATH,
				"SAUCE_SCREEN_RESOLUTION"))) {
			screenResolution = "1280x1024";
		} else {
			screenResolution = ReadPropertyFile.getConfigurationParameter(
					SAUCE_CONFIG_PATH, "SAUCE_SCREEN_RESOLUTION");
			if ((screenResolution.equalsIgnoreCase(""))
					|| (screenResolution.equalsIgnoreCase(null))) {
				screenResolution = "1280x1024";
			}
		}
		return (screenResolution);
	}
}
