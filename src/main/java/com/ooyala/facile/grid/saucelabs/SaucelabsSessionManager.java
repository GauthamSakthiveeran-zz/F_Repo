package com.ooyala.facile.grid.saucelabs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverLogLevel;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.ooyala.facile.proxy.browsermob.BrowserMobProxyHelper;
import com.ooyala.facile.test.FacileTest;
import com.ooyala.facile.util.CommonUtils;
import com.ooyala.facile.util.ReadPropertyFile;

// TODO: Auto-generated Javadoc
/**
 * The Class SaucelabsSessionManager.
 */
public class SaucelabsSessionManager {

	/** The logger. */
	public static Logger logger = Logger
			.getLogger(SaucelabsSessionManager.class);

	/** The Constant SAUCE_CONFIG_PATH. */
	public static final String SAUCE_CONFIG_PATH = "/config/sauce.properties";

	/** The driver. */
	protected RemoteWebDriver driver = null;

	/** The sauce conn url. */
	public static String sauceConnURL = getSauceConnectionURL();

	/**
	 * Method to return the Sauce Connection URL. It reads the username, api key
	 * and proxy information from the sauce.config properties.
	 * 
	 * @return the sauce connection url
	 */
	private static String getSauceConnectionURL() {
		// Reading the Sauce User Name, API and Proxy Details from the Config
		// File
		String userName;
		String apiKey;
		String url;
		String proxy;

		// // Checking if the Sauce User Name is provided as a system property
		// from mvn test. If
		// not, read from config file.
		if (CommonUtils.readPropertyOrEnv("SAUCE_USERNAME") == null) {
			// Checking if the Sauce User Name is provided in the config file.
			// If
			// the config file does not have the key, defaulting to qboqa
			if (!(ReadPropertyFile.checkConfigParameter(SAUCE_CONFIG_PATH,
					"SAUCE_USERNAME"))) {
				userName = "sbg_qbo";
			} else {
				userName = ReadPropertyFile.getConfigurationParameter(
						SAUCE_CONFIG_PATH, "SAUCE_USERNAME");
			}
		} else {
			userName = CommonUtils.readPropertyOrEnv("SAUCE_USERNAME");
		}

		// Checking if the Sauce API Key is provided as a system property from
		// mvn test.
		// If not, read from config.
		if (CommonUtils.readPropertyOrEnv("SAUCE_API_KEY") == null) {

			// Checking if the Sauce API Key is provided in the config file. If
			// the config file does not have the key defaulting to a hard coded
			// value.
			if (!(ReadPropertyFile.checkConfigParameter(SAUCE_CONFIG_PATH,
					"SAUCE_API_KEY"))) {
				// if a new API is generated this shud be updated
				apiKey = "37dc3fe0-3c96-4146-beda-7cf64805d407";
			} else {
				apiKey = ReadPropertyFile.getConfigurationParameter(
						SAUCE_CONFIG_PATH, "SAUCE_API_KEY");
			}
		} else {
			apiKey = CommonUtils.readPropertyOrEnv("SAUCE_API_KEY");
		}
		// Checking if the Sauce Proxy is provided as a system property from mvn
		// test.
		// If not, read from config.
		if (CommonUtils.readPropertyOrEnv("SAUCE_PROXY") == null) {
			// Checking if the Sauce Proxy is provided in the config file. If
			// the config file does not have the key defaulting to a hard coded
			// value.
			if (!(ReadPropertyFile.checkConfigParameter(SAUCE_CONFIG_PATH,
					"SAUCE_PROXY"))) {
				proxy = "ondemand.saucelabs.com";
			} else {
				proxy = ReadPropertyFile.getConfigurationParameter(
						SAUCE_CONFIG_PATH, "SAUCE_PROXY");
			}
		} else {
			proxy = CommonUtils.readPropertyOrEnv("SAUCE_PROXY");
		}

		url = "http://" + userName + ":" + apiKey + "@" + proxy + ":80/wd/hub";
		return url;
	}

	/**
	 * Method to check if Sauce is enabled, methods looks into sauce.properties
	 * file to decide if Sauce is set to True/False.
	 * 
	 * @return true, if is sauce enabled
	 */
	public static boolean isSauceEnabled() {
		String isSauceEnabled = ReadPropertyFile.getConfigurationParameter(
				SAUCE_CONFIG_PATH, "USE_SAUCELAB_GRID");
		return Boolean.parseBoolean(isSauceEnabled);
	}

	/**
	 * Method to create an Chrome Instance on Sauce labs onDemand Grid.
	 * 
	 * @return the remote web driver
	 */
	public RemoteWebDriver createChromeInstanceOnSauce() {
		DesiredCapabilities chCapabilities = DesiredCapabilities.chrome();
		String sauceBrowserVersion;
		String sauceOSPlatform;
		// Sauce Browser Platform

		if (CommonUtils.readPropertyOrEnv("SELENIUM_PLATFORM") == null) {
			sauceOSPlatform = ReadPropertyFile.getConfigurationParameter(
					SaucelabsSessionManager.SAUCE_CONFIG_PATH,
					"SELENIUM_PLATFORM");
			logger.debug("Selenium Platform is not set an environment variable so defaulting from Sauce.properties file:"
					+ sauceOSPlatform);
		} else {
			logger.debug("Selenium Platform :"
					+ CommonUtils.readPropertyOrEnv("SELENIUM_PLATFORM"));
			sauceOSPlatform = CommonUtils
					.readPropertyOrEnv("SELENIUM_PLATFORM");
		}

		if (CommonUtils.readPropertyOrEnv("SELENIUM_VERSION") == null) {
			sauceBrowserVersion = ReadPropertyFile.getConfigurationParameter(
					SaucelabsSessionManager.SAUCE_CONFIG_PATH,
					"SELENIUM_VERSION");
		} else {
			logger.debug("Selenium Browser Vesrion :"
					+ CommonUtils.readPropertyOrEnv("SELENIUM_VERSION"));
			sauceBrowserVersion = CommonUtils
					.readPropertyOrEnv("SELENIUM_VERSION");
		}

		chCapabilities.setCapability("platform", sauceOSPlatform);
		chCapabilities.setCapability("version", sauceBrowserVersion);

		chCapabilities.setCapability("record-video",
				SaucelabsAdditionalConfiguration.isSauceVideoEnabled());
		chCapabilities.setCapability("record-screenshots",
				SaucelabsAdditionalConfiguration.isCaptureScreenshotsEnabled());
		chCapabilities.setCapability("capture-html",
				SaucelabsAdditionalConfiguration.isCaptureHTMLEnabled());
		chCapabilities.setCapability("max-duration",
				SaucelabsAdditionalConfiguration.getMaxTestDurationTimeout());
		chCapabilities.setCapability("idle-timeout",
				SaucelabsAdditionalConfiguration.getIdleTestTimeout());

		ChromeOptions options = new ChromeOptions();
		List<String> list = new ArrayList<String>();
		list.add("disable-component-update");
		options.setExperimentalOption("excludeSwitches", list);
		
		chCapabilities.setCapability(ChromeOptions.CAPABILITY, options);

		try {

			logger.info("---- Browser ---- "
					+ chCapabilities.getCapability("browserName"));
			logger.info("---- Version ---- "
					+ chCapabilities.getCapability("version"));
			logger.info("---- OS ---- "
					+ chCapabilities.getCapability("platform"));
			logger.info(chCapabilities.toString());
			driver = new RemoteWebDriver(new URL(sauceConnURL), chCapabilities);
			driver.setFileDetector(new LocalFileDetector());
		} catch (MalformedURLException e) {
			logger.error(e);
		}
		return driver;
	}

	/**
	 * Method to create a Firefox instance on Sauce cloud.
	 * 
	 * @return the remote web driver
	 */
	public RemoteWebDriver createFirefoxInstanceonSauce() {
		DesiredCapabilities ffCapabilities = DesiredCapabilities.firefox();

		String sauceBrowserVersion;
		// Sauce Browser Version

		if (CommonUtils.readPropertyOrEnv("SELENIUM_VERSION") == null) {
			sauceBrowserVersion = ReadPropertyFile.getConfigurationParameter(
					SaucelabsSessionManager.SAUCE_CONFIG_PATH,
					"SELENIUM_VERSION");
		} else {
			logger.debug("Selenium Browser Vesrion :"
					+ CommonUtils.readPropertyOrEnv("SELENIUM_VERSION"));
			sauceBrowserVersion = CommonUtils
					.readPropertyOrEnv("SELENIUM_VERSION");
		}

		ffCapabilities.setCapability("version", sauceBrowserVersion);

		String sauceOSPlatform;
		// Sauce Browser Platform
		if (CommonUtils.readPropertyOrEnv("SELENIUM_PLATFORM") == null) {
			sauceOSPlatform = ReadPropertyFile.getConfigurationParameter(
					SaucelabsSessionManager.SAUCE_CONFIG_PATH,
					"SELENIUM_PLATFORM");
		} else {
			sauceOSPlatform = CommonUtils
					.readPropertyOrEnv("SELENIUM_PLATFORM");
		}

		ffCapabilities.setCapability("platform", sauceOSPlatform);
		ffCapabilities.setCapability("record-video",
				SaucelabsAdditionalConfiguration.isSauceVideoEnabled());
		ffCapabilities.setCapability("record-screenshots",
				SaucelabsAdditionalConfiguration.isCaptureScreenshotsEnabled());
		ffCapabilities.setCapability("capture-html",
				SaucelabsAdditionalConfiguration.isCaptureHTMLEnabled());
		ffCapabilities.setCapability("max-duration",
				SaucelabsAdditionalConfiguration.getMaxTestDurationTimeout());
		ffCapabilities.setCapability("idle-timeout",
				SaucelabsAdditionalConfiguration.getIdleTestTimeout());
		ffCapabilities.setCapability("screen-resolution", ReadPropertyFile
				.getConfigurationParameter(
						SaucelabsSessionManager.SAUCE_CONFIG_PATH,
						"SAUCE_SCREEN_RESOLUTION"));
		if (FacileTest.isBrowserMobProxyEnabled()) {
			logger.info("BrowserMob Proxy is enabled :");

			try {

				BrowserMobProxyHelper.startBrowserMobProxyServer();
				logger.info("starting browsermob proxy server on port "
						+ BrowserMobProxyHelper.getBrowserMobProxyServer()
								.getPort());

			} catch (Exception e) {
				e.printStackTrace();
			}

			// configure it as a desired capability
			ffCapabilities.setCapability("tunnel-identifier",
					"browsermobproxy_tunnel");

		}

		try {
			logger.info("---- Browser ---- "
					+ ffCapabilities.getCapability("browserName"));
			logger.info("---- Version ---- "
					+ ffCapabilities.getCapability("version"));
			logger.info("---- OS ---- "
					+ ffCapabilities.getCapability("platform"));

			driver = new RemoteWebDriver(new URL(sauceConnURL), ffCapabilities);
			driver.setFileDetector(new LocalFileDetector());
		} catch (MalformedURLException e) {
			logger.error(e);
		}
		return driver;
	}

	/**
	 * Method to create an iPhone instance on Sauce Cloud.
	 * 
	 * @return the remote web driver
	 */
	public RemoteWebDriver createiPhoneInstanceonSauce() {
		DesiredCapabilities capabillities = DesiredCapabilities.iphone();

		capabillities.setCapability("version", ReadPropertyFile
				.getConfigurationParameter(
						SaucelabsSessionManager.SAUCE_CONFIG_PATH,
						"SELENIUM_VERSION"));
		capabillities.setCapability("record-video",
				SaucelabsAdditionalConfiguration.isSauceVideoEnabled());
		capabillities.setCapability("record-screenshots",
				SaucelabsAdditionalConfiguration.isCaptureScreenshotsEnabled());
		capabillities.setCapability("capture-html",
				SaucelabsAdditionalConfiguration.isCaptureHTMLEnabled());
		capabillities.setCapability("max-duration",
				SaucelabsAdditionalConfiguration.getMaxTestDurationTimeout());
		capabillities.setCapability("idle-timeout",
				SaucelabsAdditionalConfiguration.getIdleTestTimeout());
		if (ReadPropertyFile.getConfigurationParameter(
				SaucelabsSessionManager.SAUCE_CONFIG_PATH, "SELENIUM_VERSION")
				.equalsIgnoreCase("5.1")
				|| ReadPropertyFile.getConfigurationParameter(
						SaucelabsSessionManager.SAUCE_CONFIG_PATH,
						"SELENIUM_VERSION").equalsIgnoreCase("6")) {
			capabillities.setCapability("platform", "Mac 10.8");

		} else {
			capabillities.setCapability("platform", "Mac 10.6");
		}
		try {
			driver = new RemoteWebDriver(new URL(sauceConnURL), capabillities);
		} catch (MalformedURLException e) {
			logger.error(e);
		}
		return driver;
	}

	/**
	 * Method to create an iPad instance on Sauce Cloud.
	 * 
	 * @return the remote web driver
	 */
	public RemoteWebDriver createiPadInstanceonSauce() {
		DesiredCapabilities capabillities = DesiredCapabilities.ipad();
		capabillities.setCapability("version", ReadPropertyFile
				.getConfigurationParameter(
						SaucelabsSessionManager.SAUCE_CONFIG_PATH,
						"SELENIUM_VERSION"));
		capabillities.setCapability("record-video",
				SaucelabsAdditionalConfiguration.isSauceVideoEnabled());
		capabillities.setCapability("record-screenshots",
				SaucelabsAdditionalConfiguration.isCaptureScreenshotsEnabled());
		capabillities.setCapability("capture-html",
				SaucelabsAdditionalConfiguration.isCaptureHTMLEnabled());
		capabillities.setCapability("max-duration",
				SaucelabsAdditionalConfiguration.getMaxTestDurationTimeout());
		capabillities.setCapability("idle-timeout",
				SaucelabsAdditionalConfiguration.getIdleTestTimeout());
		if (ReadPropertyFile.getConfigurationParameter(
				SaucelabsSessionManager.SAUCE_CONFIG_PATH, "SELENIUM_VERSION")
				.equalsIgnoreCase("5.1")
				|| ReadPropertyFile.getConfigurationParameter(
						SaucelabsSessionManager.SAUCE_CONFIG_PATH,
						"SELENIUM_VERSION").equalsIgnoreCase("6")) {
			capabillities.setCapability("platform", "Mac 10.8");

		} else {
			capabillities.setCapability("platform", "Mac 10.6");
		}

		try {
			driver = new RemoteWebDriver(new URL(sauceConnURL), capabillities);
		} catch (MalformedURLException e) {
			logger.error(e);
		}
		return driver;
	}

	/**
	 * Method to create an IE Instance on Sauce labs onDemand Grid.
	 * 
	 * @return the remote web driver
	 */
	public RemoteWebDriver createIEInstanceOnSauce() {
		DesiredCapabilities ieCapabilities = DesiredCapabilities
				.internetExplorer();
		String sauceBrowserVersion, sauceOSPlatform;

		// Sauce Browser Version
		if (CommonUtils.readPropertyOrEnv("SELENIUM_VERSION") == null) {
			sauceBrowserVersion = ReadPropertyFile.getConfigurationParameter(
					SaucelabsSessionManager.SAUCE_CONFIG_PATH,
					"SELENIUM_VERSION");
		} else {
			logger.debug("Selenium Browser Vesrion :"
					+ CommonUtils.readPropertyOrEnv("SELENIUM_VERSION"));
			sauceBrowserVersion = CommonUtils
					.readPropertyOrEnv("SELENIUM_VERSION");
		}

		// Sauce Browser Platform
		if (CommonUtils.readPropertyOrEnv("SELENIUM_PLATFORM") == null) {
			sauceOSPlatform = ReadPropertyFile.getConfigurationParameter(
					SaucelabsSessionManager.SAUCE_CONFIG_PATH,
					"SELENIUM_PLATFORM");
		} else {
			sauceOSPlatform = CommonUtils
					.readPropertyOrEnv("SELENIUM_PLATFORM");
		}
		try {

		} catch (NullPointerException npe) {

		}

		ieCapabilities.setCapability("version", sauceBrowserVersion);
		ieCapabilities.setCapability("platform", sauceOSPlatform);

		ieCapabilities.setCapability("record-video",
				SaucelabsAdditionalConfiguration.isSauceVideoEnabled());
		ieCapabilities.setCapability("record-screenshots",
				SaucelabsAdditionalConfiguration.isCaptureScreenshotsEnabled());
		ieCapabilities.setCapability("capture-html",
				SaucelabsAdditionalConfiguration.isCaptureHTMLEnabled());
		ieCapabilities.setCapability("max-duration",
				SaucelabsAdditionalConfiguration.getMaxTestDurationTimeout());
		ieCapabilities.setCapability("idle-timeout",
				SaucelabsAdditionalConfiguration.getIdleTestTimeout());
		ieCapabilities.setCapability(InternetExplorerDriver.LOG_FILE, "ie.log");
		ieCapabilities.setCapability(InternetExplorerDriver.LOG_LEVEL, "TRACE");
		ieCapabilities.setCapability("logLevel",
				InternetExplorerDriverLogLevel.TRACE);
		ieCapabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
				UnexpectedAlertBehaviour.ACCEPT);
		ieCapabilities
				.setCapability(
						InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);

		try {
			logger.info("---- Browser ---- "
					+ ieCapabilities.getCapability("browserName"));
			logger.info("---- Version ---- "
					+ ieCapabilities.getCapability("version"));
			logger.info("---- OS ---- "
					+ ieCapabilities.getCapability("platform"));
			driver = new RemoteWebDriver(new URL(sauceConnURL), ieCapabilities);
		} catch (MalformedURLException e) {
			logger.error(e);
		}
		return driver;
	}

	/**
	 * Method to create an android instance on Sauce Cloud.
	 * 
	 * @return the remote web driver
	 */
	public RemoteWebDriver createandroidInstanceonSauce() {
		DesiredCapabilities capabillities = DesiredCapabilities.android();

		capabillities.setCapability("version", ReadPropertyFile
				.getConfigurationParameter(
						SaucelabsSessionManager.SAUCE_CONFIG_PATH,
						"SELENIUM_VERSION"));
		capabillities.setCapability("platform", Platform.LINUX);
		capabillities.setCapability("record-video",
				SaucelabsAdditionalConfiguration.isSauceVideoEnabled());
		capabillities.setCapability("record-screenshots",
				SaucelabsAdditionalConfiguration.isCaptureScreenshotsEnabled());
		capabillities.setCapability("capture-html",
				SaucelabsAdditionalConfiguration.isCaptureHTMLEnabled());
		capabillities.setCapability("max-duration",
				SaucelabsAdditionalConfiguration.getMaxTestDurationTimeout());
		capabillities.setCapability("idle-timeout",
				SaucelabsAdditionalConfiguration.getIdleTestTimeout());
		if (ReadPropertyFile.getConfigurationParameter(SAUCE_CONFIG_PATH,
				"SAUCE_DEVICE_TYPE").equalsIgnoreCase("table")) {
			capabillities.setCapability("deviceType", "tablet");
		}
		try {
			driver = new RemoteWebDriver(new URL(sauceConnURL), capabillities);
		} catch (MalformedURLException e) {
			logger.error(e);
		}
		return driver;
	}
}
