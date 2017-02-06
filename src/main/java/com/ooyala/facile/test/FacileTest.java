package com.ooyala.facile.test;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverLogLevel;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.reporters.EmailableReporter;

import com.ooyala.facile.grid.saucelabs.SauceConnectManager;
import com.ooyala.facile.grid.saucelabs.SauceREST;
import com.ooyala.facile.grid.saucelabs.SaucelabsSessionManager;
import com.ooyala.facile.listners.FacileTestListener;
import com.ooyala.facile.page.ContextAwareFirefoxDriver;
import com.ooyala.facile.page.WebPage;
import com.ooyala.facile.proxy.browsermob.BrowserMobProxyHelper;
import com.ooyala.facile.test.reporters.RetryMainHtmlReporter;
import com.ooyala.facile.test.reporters.RetrySuiteHTMLReporter;
import com.ooyala.facile.test.reporters.RetryTestHtmlReporter;
import com.ooyala.facile.test.reporters.RetryXmlReporter;
import com.ooyala.facile.util.CommonUtils;
import com.ooyala.facile.util.NoWatchdog;
import com.ooyala.facile.util.ReadPropertyFile;
import com.ooyala.facile.util.ReadTriggerFile;
import com.ooyala.facile.util.TestWatchdog;

// TODO: Auto-generated Javadoc
/**
 * A test utility class for WebDriver TestNG tests. The initial goal for this
 * class was as a TestListernerAdapter for TestNG to provide the following
 * functionality: <br>
 * 1) Screenshots when a test fails <br>
 * 2) Miscellaneous customizations for the TestNG platform (like rerouting std
 * error to the TestNG Test Report) <br>
 * 
 * @author pkumar
 * 
 */
@Listeners({ FacileTestListener.class, RetrySuiteHTMLReporter.class,
		RetryXmlReporter.class, EmailableReporter.class,
		RetryTestHtmlReporter.class, RetryMainHtmlReporter.class })
public class FacileTest implements IHookable {

	/** The logger. */
	public static Logger logger = Logger.getLogger(FacileTest.class);

	/** The driver. */
	protected RemoteWebDriver driver;

	/** The chrome server. */
	private static ChromeDriverService chromeServer;

	/** The ie server. */
	private static InternetExplorerDriverService ieServer;

	/** The Constant DRIVER_PHANTOMJS. */
	private static final String DRIVER_PHANTOMJS = "phantomjs";

	/** The web driver facile. */
	public static InheritableThreadLocal<RemoteWebDriver> webDriverFacile = new InheritableThreadLocal<RemoteWebDriver>();

	/** The s caps. */
	protected static DesiredCapabilities sCaps;

	// Screenshots will now be placed in the "Results" folder for autolab's
	// purposes.
	// They automatically store the results folder post-execution.
	/** The Constant DEFAULT_WIN_SCREENSHOT_PATH. */
	public static final String DEFAULT_WIN_SCREENSHOT_PATH = "C:/Results/screenshots/";
	// TODO: Where will this be on the Mac? I put in a dummy value for now
	/** The Constant DEFAULT_MAC_SCREENSHOT_PATH. */
	public static final String DEFAULT_MAC_SCREENSHOT_PATH = "/Users/Shared/screenshots/";

	/** The screenshot path. */
	public String screenshotPath = ((System.getProperty("os.name")
			.startsWith("Windows")) ? DEFAULT_WIN_SCREENSHOT_PATH
			: DEFAULT_MAC_SCREENSHOT_PATH);

	/** The Constant DEFAULT_WIN_CHROMEDRIVER_PATH. */
	public static final String DEFAULT_WIN_CHROMEDRIVER_PATH = "c:/temp/chromedriver.exe";

	/** The Constant DEFAULT_MAC_CHROMEDRIVER_PATH. */
	public static final String DEFAULT_MAC_CHROMEDRIVER_PATH = "/Users/Shared/chromedriver";

	/** The Constant DEFAULT_MAC_CHROMEDRIVER_PATH. */
	public static final String DEFAULT_LINUX_CHROMEDRIVER_PATH = "/Users/Shared/chromedriver";

	/** The Constant DEFAULT_WIN_IEDRIVER_PATH. */
	public static final String DEFAULT_WIN_IEDRIVER_PATH = "c:/temp/IEDriverServer.exe";

	/** The retrier. */
	protected static IRetryAnalyzer RETRIER = new FacileTestListener();

	/** The watchdog. */
	public static TestWatchdog watchdog = null;

	/** The watch dog thd. */
	private static Thread watchDogThd = null;

	/** The webdriver context. */
	protected Object webdriverContext = null;

	/** The Constant SAUCE_CONFIG_PATH. */
	public static final String SAUCE_CONFIG_PATH = "/config/sauce.properties";

	/**
	 * Method to check if Sauce is enabled/disabled.
	 * 
	 * @return the boolean
	 */
	public static Boolean isSauceEnabled() {

		if (System.getProperty("USE_SAUCELAB_GRID") == null) {
			logger.debug("USE_SAUCELAB_GRID is not set in Maven Cmd Agrs...");
			InputStream in = FacileTest.class
					.getResourceAsStream(SAUCE_CONFIG_PATH);
			if (!(in == null)) {
				logger.debug("Sauce.properties Config File Exists...");
				String isSauceEnabled = ReadPropertyFile
						.getConfigurationParameter(SAUCE_CONFIG_PATH,
								"USE_SAUCELAB_GRID");
				if (isSauceEnabled.equalsIgnoreCase("true")) {
					logger.debug("Saucelabs Option Set to True...");
					return true;
				} else {
					logger.debug("Saucelabs Option Set to False...");
					return false;
				}
			} else {
				logger.debug("Config File does not Exists...");
				return false;
			}
		} else {
			if (System.getProperty("USE_SAUCELAB_GRID")
					.equalsIgnoreCase("true")) {
				logger.debug("USE_SAUCELAB_GRID from Cmd Args : "
						+ System.getProperty("USE_SAUCELAB_GRID"));
				return true;
			} else {
				logger.debug("USE_SAUCELAB_GRID from Cmd Args : "
						+ System.getProperty("USE_SAUCELAB_GRID"));
				return false;
			}
		}
	}

	/**
	 * Initializes a webdriver test.
	 */
	public FacileTest() {
	}

	/**
	 * Prints the message.
	 * 
	 * @param str
	 *            the str
	 */
	protected static void PrintMessage(String str) {
		Reporter.log(str, 5, true); // Making a log entry.
	}

	/**
	 * Method to Create an IE Instance Locally.
	 * 
	 * @return the remote web driver
	 */
	public RemoteWebDriver createIEInstanceLocally() {
		logger.debug("Creating an Internet Explorer Instance Locally...");

		DesiredCapabilities ieCapabilities = DesiredCapabilities
				.internetExplorer();

		// Checking if the OS is MAC. Since MAC + IE is not a valid combination.
		if (CommonUtils.getOSName().contains("Mac")) {
			logger.error("Could not create an IE driver for the os: "
					+ CommonUtils.getOSName());
			throw new UnsupportedOperationException(
					"Could not create an IE driver for the os: "
							+ CommonUtils.getOSName());
		} else {
			logger.debug("Creating an IE Instance...");
			try {
				String ieDriverPath = null;

				ieDriverPath = getDriverPath("IEDriverServer.exe",
						CommonUtils.getOSName());
				if (ieDriverPath == "") {
					logger.info("There is some problem with copying ie driver to the temp directory so using default path "
							+ DEFAULT_WIN_IEDRIVER_PATH);
					ieDriverPath = DEFAULT_WIN_IEDRIVER_PATH;
				}
				ieServer = new InternetExplorerDriverService.Builder()
						.usingDriverExecutable(new File(ieDriverPath))
						.withLogLevel(InternetExplorerDriverLogLevel.TRACE)
						.usingAnyFreePort().build();
				if (ieServer != null) {
					logger.debug("Starting up the IE service...");
					ieServer.start();
					logger.debug("Started ID Service...");
				}
			} catch (IOException ex) {
				logger.error("Unable to startup the IE Server");
			}
			ieCapabilities
					.setCapability(
							InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
							true);
			ieCapabilities.setCapability(
					CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION,
					true);
			return new RemoteWebDriver(ieServer.getUrl(), ieCapabilities);
		}
	}

	/**
	 * Creates the firefox instance locally.
	 * 
	 * @return the remote web driver
	 */
	public RemoteWebDriver createFirefoxInstanceLocally() {
		logger.info("Creating Firefox Instance Locally...");
		FirefoxProfile profile = new FirefoxProfile();

		// Allows for accessing iFrames that originate in the different
		// domain. Per request from QBO4A
		profile.setPreference(
				"capability.policy.default.HTMLIFrameElement.name.get",
				"allAccess");
		profile.setPreference(
				"capability.policy.default.HTMLDocument.compatMode",
				"allAccess");
		profile.setPreference("capability.policy.default.Window.pageXOffset",
				"allAccess");
		profile.setPreference("capability.policy.default.Window.pageYOffset",
				"allAccess");
		profile.setPreference(
				"capability.policy.default.Window.mozInnerScreenY", "allAccess");
		profile.setPreference(
				"capability.policy.default.Window.mozInnerScreenX", "allAccess");
		profile.setPreference("capability.policy.default.Window.frameElement",
				"allAccess");
		profile.setPreference("dom.max_script_run_time", 0);
		profile.setPreference("dom.max_chrome_script_run_time", 0);

		// Commenting the below changes as this was breaking mint automation.
		// Will look into this and understand why was this added. Until then
		// commenting//
		// profile.setPreference("browser.link.open_newwindow", 1);

		// Setup a proxy for the Firefox driver if necessary
		String proxy = System.getProperty("http.proxyHost");
		String proxy_port = System.getProperty("http.proxyPort");
		String nonproxy_hosts = System.getProperty("http.nonProxyHosts");

		if (proxy != null) {
			profile.setPreference("network.proxy.type", 1);
			profile.setPreference("network.proxy.http", proxy);
			profile.setPreference("network.proxy.ssl", proxy);

			if (nonproxy_hosts != null) {
				String[] nonproxy = nonproxy_hosts.split("\\|");

				profile.setPreference("network.proxy.no_proxies_on",
						StringUtils.join(nonproxy, ","));
			}

			if (proxy_port != null) {
				try {
					int port = Integer.parseInt(proxy_port);
					profile.setPreference("network.proxy.http_port", port);
					profile.setPreference("network.proxy.ssl_port", port);
					logger.debug("Setting network.proxy.http_port and network.proxy.ssl_port to : "
							+ port);
				} catch (Exception ex) {
					logger.error("Error while setting preferences..", ex);
					// Do nothing but log the exception
					Reporter.log(ex.toString(), true);
				}
			}

			logger.debug("USING PROXY SETTINGS");
		}

		if (isBrowserMobProxyEnabled()) {
			logger.info("BrowserMob Proxy is enabled :");

			Proxy browserMobProxy = null;
			try {
				BrowserMobProxyHelper.startBrowserMobProxyServer();
				browserMobProxy = BrowserMobProxyHelper
						.getBrowserMobProxyServer().seleniumProxy();

			} catch (Exception e) {
				e.printStackTrace();
			}

			// configure it as a desired capability
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(CapabilityType.PROXY, browserMobProxy);

			FirefoxDriver driver = new FirefoxDriver(capabilities);
			return driver;

		}

		return (new ContextAwareFirefoxDriver(profile));

	}

	/**
	 * Checks if is browser mob proxy enabled.
	 * 
	 * @return true, if is browser mob proxy enabled
	 */
	public static boolean isBrowserMobProxyEnabled() {
		logger.info("... checking for browser mob proxy");
		boolean isProxyEnabled = false;
		File confFile = new File("src/test/resources/facile.properties");
		if (confFile.exists()) {
			ReadTriggerFile propertiesFile = new ReadTriggerFile(
					"src/test/resources/facile.properties");
			String isSiteCatelystEnabled = propertiesFile.getParameter(
					"browser_mob_proxy", "");
			if (isSiteCatelystEnabled != null) {
				if (isSiteCatelystEnabled.equalsIgnoreCase("true")) {
					isProxyEnabled = true;
					return true;
				}
			}

		}
		logger.info("Returning isProxyEnabled" + isProxyEnabled);
		return isProxyEnabled;
	}

	/**
	 * Method to Create a Chrome Instance Locally.
	 * 
	 * @return the remote web driver
	 */
	public RemoteWebDriver createChromeInstanceLocally() {
		String osName = System.getProperty("os.name").toLowerCase();
		logger.debug("OS Name : " + osName);

		String chromeDriverPath;

		DesiredCapabilities dc = DesiredCapabilities.chrome();
		String[] switches = { "--ignore-certificate-errors",
				"--disable-popup-blocking" };
		dc.setCapability("chrome.switches", Arrays.asList(switches));

		if (osName.contains("mac")) {
			logger.debug("OS type : MAC*");
			chromeDriverPath = getDriverPath("chromedriver", "mac");
			if (chromeDriverPath == "") {
				logger.info("There is some problem with copying chromedriver to the temp directory so using default path "
						+ DEFAULT_MAC_CHROMEDRIVER_PATH);
				chromeDriverPath = DEFAULT_MAC_CHROMEDRIVER_PATH;
			}

		} else if (osName.contains("win")) {
			logger.debug("OS type : Windows");
			chromeDriverPath = getDriverPath("chromedriver.exe", "windows");
			if (chromeDriverPath == "") {
				logger.info("There is some problem with copying chromedriver to the temp directory so using default path "
						+ DEFAULT_MAC_CHROMEDRIVER_PATH);
				chromeDriverPath = DEFAULT_WIN_CHROMEDRIVER_PATH;
			}

		} else if (osName.contains("linux")) {
			logger.debug("OS type : Linux*");
			chromeDriverPath = getDriverPath("chromedriver", "linux");
			if (chromeDriverPath == "") {
				logger.info("There is some problem with copying chromedriver to the temp directory so using default path "
						+ DEFAULT_LINUX_CHROMEDRIVER_PATH);
				chromeDriverPath = DEFAULT_LINUX_CHROMEDRIVER_PATH;
			}
		} else {
			logger.error("Could not create a driver for the OS : " + osName);
			throw new UnsupportedOperationException(
					"Could not create a driver for the os: " + osName);
		}

		try {
			chromeServer = new ChromeDriverService.Builder()
					.usingDriverExecutable(new File(chromeDriverPath))
					.usingAnyFreePort().build();
			if (chromeServer != null) {
				logger.debug("Starting up the chrome service.");
				chromeServer.start();
				logger.debug("Successfully started chrome service...");
			}
		} catch (IOException ex) {
			logger.error("Unable to startup the Chrome Server", ex);
		}

		ChromeOptions options = getChromeOptions();
		dc.setCapability(ChromeOptions.CAPABILITY, options);

		return (new RemoteWebDriver(chromeServer.getUrl(), dc));
	}

	public ChromeOptions getChromeOptions() {
		ChromeOptions options = new ChromeOptions();
		List<String> list = new ArrayList<String>();
		list.add("disable-component-update");
		options.setExperimentalOption("excludeSwitches", list);
		return options;
	}

	public static DesiredCapabilities getDesiredCapabilities(String browser) {
		DesiredCapabilities dr = null;
		if (browser.equalsIgnoreCase("firefox")) {
			dr = DesiredCapabilities.firefox();
			dr.setBrowserName("firefox");
		} else if (browser.equalsIgnoreCase("chrome")) {
			dr = DesiredCapabilities.chrome();
			dr.setBrowserName("chrome");
		} else if (browser.equalsIgnoreCase("internet explorer") || browser.contains("ie")) {
			dr = DesiredCapabilities.internetExplorer();
			dr.setBrowserName("internet explorer");
		} else if (browser.equalsIgnoreCase("safari")) {
			dr = DesiredCapabilities.safari();
			dr.setBrowserName("safari");
		} else if (browser.equalsIgnoreCase("MicrosoftEdge")){
			dr=DesiredCapabilities.edge();;
            dr.setBrowserName("MicrosoftEdge");
		} else {
			dr = DesiredCapabilities.chrome();
			dr.setBrowserName("chrome");
		}
		return dr;
	}

	public InheritableThreadLocal<RemoteWebDriver> getRemoteDriver(String browser) {

		DesiredCapabilities desiredCapabilities = getDesiredCapabilities(browser);
		String version = System.getProperty("version");
		if (version != null)
			desiredCapabilities.setVersion(version);
		String platform = System.getProperty("platform");
		desiredCapabilities.setCapability(CapabilityType.PLATFORM, platform);

		if (browser.equalsIgnoreCase("chrome")) {

			ChromeOptions options = getChromeOptions();
			desiredCapabilities
					.setCapability(ChromeOptions.CAPABILITY, options);
		}

		String ipAddress = System.getProperty("ipaddress");
		if (ipAddress == null || ipAddress.equals(""))
			ipAddress = "10.11.69.126:5555";

		String serverUrl = "http://" + ipAddress + "/wd/hub";

		try {
			driver = new RemoteWebDriver(new URL(serverUrl),
					desiredCapabilities);// Start
		} catch (Exception e) {
			logger.info("relauch browser");
			try {
				driver = new RemoteWebDriver(new URL(serverUrl),
						desiredCapabilities); // retry
			} catch (Exception e1) {
				logger.info("\n\nGot Exception : " + e.getMessage() + "\n\n");
				logger.info("\nNo Configuration Found platform:" + platform
						+ " browser:" + browser + " version:" + version);
				logger.info("\n\n");

			}
		}
		webDriverFacile.set(driver);
		return webDriverFacile;

	}

	/**
	 * Gets the driver.
	 * 
	 * @param browserName
	 *            the browser name
	 * @return the driver
	 */
	public InheritableThreadLocal<RemoteWebDriver> getDriver(String browserName) {

		// Reading the Browser Type From sauce.properties file if running
		// locally
		if (browserName == null) {
			browserName = ReadPropertyFile.getConfigurationParameter(
					SAUCE_CONFIG_PATH, "SELENIUM_BROWSER");
		}

		if (isSauceEnabled()) {
			browserName = System.getenv("SELENIUM_BROWSER");

		}

		logger.debug("Browser Name : " + browserName);
		browserName = browserName.toLowerCase().trim();

		// Selenium Grid
		String mode = System.getProperty("mode");
		if (mode != null && mode.equalsIgnoreCase("remote")) {
			return getRemoteDriver(browserName);
		}

		else {

			// Internet Explorer
			if (browserName.contains("internet") || browserName.equals("ie")
					|| browserName.equalsIgnoreCase("8A")
					|| browserName.equalsIgnoreCase("9A")
					|| browserName.equalsIgnoreCase("10A")) {
				logger.debug("Browser Type : " + browserName);

				if (isSauceEnabled()) {
					// Spin and return an IE instance on Sauce cloud
					logger.debug("Enable Sauce Grid is Set to : "
							+ isSauceEnabled());
					logger.debug("Creating " + browserName
							+ "Instance on Saucelabs Grid");
					SaucelabsSessionManager saucelabsSessionManager = new SaucelabsSessionManager();
					logger.debug("Successfully Created " + browserName
							+ " Instance on Saucelabs Grid");
					driver = saucelabsSessionManager.createIEInstanceOnSauce();

				} else {
					// Spin and return an IE instance locally
					logger.debug("Enable Sauce Grid is Set to : "
							+ isSauceEnabled());
					driver = createIEInstanceLocally();
					logger.debug("Launched Chrome Instance Successfully in local environment Sauce Grid is Set to : "
							+ isSauceEnabled());
				}
				webDriverFacile.set(maximizeMe(driver));
				return webDriverFacile;
			}

			// iPad
			if (browserName.contains("ipad")) {
				logger.debug("Browser Type : " + browserName);
				if (isSauceEnabled()) {
					logger.debug("Enable Sauce Grid is Set to : "
							+ isSauceEnabled());
					// Spin and return an IE instance on Sauce cloud
					logger.debug("Creating " + browserName
							+ "Instance on Saucelabs Grid");
					SaucelabsSessionManager saucelabsSessionManager = new SaucelabsSessionManager();
					logger.debug("Successfully Created " + browserName
							+ " Instance on Saucelabs Grid");
					driver = saucelabsSessionManager
							.createiPadInstanceonSauce();
				} else {
					logger.error("Unable to startup " + browserName + "Server");
					throw new UnsupportedOperationException(
							"Could not create an instance driver for the device: "
									+ browserName);
				}
				webDriverFacile.set(driver);
				return webDriverFacile;
			}

			// PhantomJS
			if (browserName.contains("phantomjs")) {
				// Prepare capabilities
				sCaps = new DesiredCapabilities();
				sCaps.setJavascriptEnabled(true);
				sCaps.setCapability("takesScreenshot", true);

				String phantomjsPath = getDriverPath("phantomjs",
						CommonUtils.getOSName());
				sCaps.setCapability(
						PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
						phantomjsPath);

				/*
				 * ArrayList<String> cliArgsCap = new ArrayList<String>();
				 * cliArgsCap.add("--web-security=false");
				 * cliArgsCap.add("--ssl-protocol=any");
				 * cliArgsCap.add("--ignore-ssl-errors=true");
				 * sCaps.setCapability
				 * (PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
				 */

				driver = new PhantomJSDriver(sCaps);
				webDriverFacile.set(driver);
				return webDriverFacile;
			}

			// iPhone
			if (browserName.contains("iphone")) {
				logger.debug("Browser Type : " + browserName);
				if (isSauceEnabled()) {
					logger.debug("Enable Sauce Grid is Set to : "
							+ isSauceEnabled());

					logger.debug("Creating " + browserName
							+ "Instance on Saucelabs Grid");
					SaucelabsSessionManager saucelabsSessionManager = new SaucelabsSessionManager();
					logger.debug("Successfully Created " + browserName
							+ " Instance on Saucelabs Grid");
					driver = saucelabsSessionManager
							.createiPhoneInstanceonSauce();

				} else {
					logger.error("Unable to startup " + browserName + "Server");
					throw new UnsupportedOperationException(
							"Could not create an instance driver for the device: "
									+ browserName);
				}
				webDriverFacile.set(driver);
				return webDriverFacile;
			}

			// Android
			if (browserName.contains("android")) {
				logger.debug("Browser Type : " + browserName);
				if (isSauceEnabled()) {
					logger.debug("Enable Sauce Grid is Set to : "
							+ isSauceEnabled());

					logger.debug("Creating " + browserName
							+ "Instance on Saucelabs Grid");
					SaucelabsSessionManager saucelabsSessionManager = new SaucelabsSessionManager();
					logger.debug("Successfully Created " + browserName
							+ " Instance on Saucelabs Grid");
					driver = saucelabsSessionManager
							.createandroidInstanceonSauce();
				} else {
					logger.error("Unable to startup " + browserName + "Server");
					throw new UnsupportedOperationException(
							"Could not create an instance driver for the device: "
									+ browserName);
				}
				webDriverFacile.set(driver);
				return webDriverFacile;
			}

			// Google Chrome
			if (browserName.contains("chrome")) {
				logger.debug("Browser Type : " + browserName);
				if (isSauceEnabled()) {
					logger.debug("Enable Sauce Grid is Set to : "
							+ isSauceEnabled());

					logger.debug("Creating " + browserName
							+ "Instance on Saucelabs Grid");
					SaucelabsSessionManager saucelabsSessionManager = new SaucelabsSessionManager();
					logger.debug("Successfully Created " + browserName
							+ " Instance on Saucelabs Grid");
					driver = saucelabsSessionManager
							.createChromeInstanceOnSauce();
				} else {
					logger.debug("Creating " + browserName
							+ "Instance on local instance");
					driver = createChromeInstanceLocally();
					logger.debug("Successfully Created " + browserName
							+ " Instance on local instance.");
				}
				webDriverFacile.set(maximizeMe(driver));
				return webDriverFacile;
			}

			// Firefox
			if (browserName.contains("firefox") || browserName.equals("ff")) {
				logger.debug("...Browser Type : " + browserName);

				FirefoxProfile profile = new FirefoxProfile();

				if (isSauceEnabled()) {

					logger.debug("...Enable Sauce Grid is Set to : "
							+ isSauceEnabled());

					logger.debug("...Creating " + browserName
							+ "Instance on Saucelabs Grid");
					SaucelabsSessionManager sauceLabsSessionManager = new SaucelabsSessionManager();

					logger.debug("...Successfully Created " + browserName
							+ " Instance on Saucelabs Grid");
					driver = sauceLabsSessionManager
							.createFirefoxInstanceonSauce();

				} else {
					logger.debug("Creating " + browserName
							+ "Instance on local instance");
					driver = createFirefoxInstanceLocally();

					logger.debug("Successfully Created " + browserName
							+ " Instance on local instance.");

				}

				webDriverFacile.set(maximizeMe(driver));
				return webDriverFacile;
			}

			// Safari
			if (browserName.contains("safari")) {
				logger.debug("Browser Type : " + browserName);

				if (isSauceEnabled()) {
					logger.debug("Enable Sauce Grid is Set to : "
							+ isSauceEnabled());

					logger.debug("Creating " + browserName
							+ "Instance on Saucelabs Grid");
					SaucelabsSessionManager sauceLabsSessionManager = new SaucelabsSessionManager();
					driver = sauceLabsSessionManager
							.createFirefoxInstanceonSauce();
					logger.debug("Successfully Created " + browserName
							+ " Instance on Saucelabs Grid");
				} else {
					logger.debug("Creating " + browserName
							+ "Instance on local instance");
					driver = new SafariDriver();
					logger.debug("Successfully Created " + browserName
							+ " Instance on local instance.");
				}
				webDriverFacile.set(maximizeMe(driver));
				return webDriverFacile;
			}
		}

		logger.error("Could not create a driver for the browser: "
				+ browserName);
		throw new UnsupportedOperationException(
				"Could not create a driver for the browser: " + browserName);
	}

	/*
	 * This method maximizes the browser window by taking driver instance
	 */
	/**
	 * Maximize me.
	 * 
	 * @param driver
	 *            the driver
	 * @return the remote web driver
	 */
	public RemoteWebDriver maximizeMe(RemoteWebDriver driver) {
		logger.debug("Maximize the browser window...");

		if (driver.getCapabilities().getBrowserName()
				.equalsIgnoreCase("chrome")) {
			logger.info("Maximizing chrome driver..");
			driver.manage().window().setSize(getWindowDimention());
			driver.manage().window().setPosition(new Point(0, 0));
		} else {
			driver.manage().window().maximize();
			logger.debug("Successfully maximized the browser window...");
		}
		return driver;
	}

	/**
	 * Gets the driver.
	 * 
	 * @param browserName
	 *            the browser name
	 * @param profileName
	 *            the profile name
	 * @return the driver
	 */
	public RemoteWebDriver getDriver(String browserName, String profileName) {
		logger.info("Browser Type : " + browserName);
		browserName = browserName.toLowerCase().trim();
		if (browserName.equals("firefox") || browserName.equals("ff")) {
			FirefoxDriver ffdriver = null;
			try { // attempt to get the profile specified, else return a regular
					// FF driver
				logger.debug("Attempting to load the specified Profile for Firefox...");
				FirefoxProfile profile = new FirefoxProfile(new File(
						profileName));
				ffdriver = new ContextAwareFirefoxDriver(profile);
				logger.debug("Successfully created the firefox instance with specified Profile.");
			} catch (NullPointerException ex) {
				logger.error("No Firefox profile with name '" + profileName
						+ "' exists.  Using default WebDriver-made profile", ex);
				System.err.println("No Firefox profile with name '"
						+ profileName
						+ "' exists.  Using default WebDriver-made profile");
				return new ContextAwareFirefoxDriver();
			}
			return ffdriver;
		}
		return getDriver(browserName).get();
	}

	/**
	 * Gets the non native driver.
	 * 
	 * @param browserName
	 *            the browser name
	 * @return the non native driver
	 */
	public RemoteWebDriver getNonNativeDriver(String browserName) {
		if (browserName.contains("firefox") || browserName.equals("ff")) {
			/*
			 * int index = 0; File logFile = new File("C:\\temp\\firefoxlogfile"
			 * + index + ".txt"); while (logFile.exists()) { index++; logFile =
			 * new File("C:\\temp\\firefoxlogfile" + index + ".txt"); }
			 * 
			 * System.setProperty("webdriver.firefox.logfile",
			 * logFile.getAbsolutePath());
			 */

			FirefoxProfile profile = new FirefoxProfile();
			// profile.setPreference("webdriver_log_to_console", true);

			/**
			 * This method serves as a temp fix for some issues we were seeing
			 * where mini interviews were appearing that couldn't be typed into
			 * when using webdriver. The problem resolves itself when native
			 * events are disabled within Firefox.
			 */
			profile.setEnableNativeEvents(false);
			return new FirefoxDriver(profile);
		} else {
			// Other browser dont support non-native events. Return the normal
			// driver in those instances.
			return getDriver(browserName).get();
		}

	}

	// store the old std err so we can still output to it in addition to the
	// ReporterPrintStream
	/** The old std err. */
	private static PrintStream oldStdErr = null;

	/**
	 * Before a test is run, we reroute Standard Error to the Testng Reporter
	 * log so we can review the error output in the TestNG Result files.
	 */
	@BeforeSuite
	public void beforeSuite() {
		ReporterPrintStream reporterPrintStream = null;
		try {
			reporterPrintStream = new ReporterPrintStream(File.createTempFile(
					"wdv", null));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		oldStdErr = System.err;
		System.setErr(reporterPrintStream);

		// Setup the Chrome Driver if it hasn't already.
		if (chromeServer == null) {
			/*
			 * chromeServer = new ChromeDriverService.Builder()
			 * .usingChromeDriverExecutable(new
			 * File("C:\\QAP\\chromedriver.exe")) .usingAnyFreePort() .build();
			 */
		}

		// If Sauce is enabled and @SauceConnect annotation is present in any of
		// your tests, it will start sauce connect
		if (isSauceEnabled() && isSauceConnectAnnotatationPresent()) {
			SauceConnectManager.startSauceConnect();
		}
	}

	/**
	 * After suite.
	 */
	@AfterSuite
	public void afterSuite() {
		if (chromeServer != null && chromeServer.isRunning()) {
			chromeServer.stop();
		}
		// If Sauce is enabled and @SauceConnect annotation is present in any of
		// your tests, it will stop sauce connect
		if (isSauceEnabled() && isSauceConnectAnnotatationPresent()) {
			SauceConnectManager.stopSauceConnect();
		}

	}

	/**
	 * A print stream that routes the stream to the TestNg Reporter Log.
	 * 
	 * @author pkumar
	 */
	public class ReporterPrintStream extends PrintStream {

		/**
		 * Instantiates a new reporter print stream.
		 * 
		 * @param file
		 *            the file
		 * @throws FileNotFoundException
		 *             the file not found exception
		 */
		public ReporterPrintStream(File file) throws FileNotFoundException {
			super(file);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.PrintStream#print(java.lang.String)
		 */
		@Override
		public void print(String s) {
			Reporter.log(s);
			oldStdErr.println(s);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.IHookable#run(org.testng.IHookCallBack,
	 * org.testng.ITestResult)
	 */
	public void run(final IHookCallBack icb, ITestResult result) {
		// System.out.println("FacileTest.run()");
		logger.info("Using 2.1.4-SNAPSHOT");

		// IRetryAnalyzer RETRIER = new FacileTestListener();
		// Each of our tests will be retried by the same IRetryAnalyzer object.
		// result.getMethod().setRetryAnalyzer(RETRIER);

		String fullTestName = getSuiteTestName(result.getName().toString(),
				result.getParameters());

		boolean noWatchdog = result.getMethod().getMethod()
				.isAnnotationPresent(NoWatchdog.class);

		// Initialize and start the watchdog timer.
		if (!noWatchdog) {
			initWatchdog(fullTestName);
		}

		logger.info("Webdriver Session :" + webDriverFacile.toString());
		try {
			if (webDriverFacile.get().getSessionId() != null) {
				String jobID = (webDriverFacile.get()).getSessionId()
						.toString();
				logger.info("Setting jobId " + jobID);
				logger.info("SauceOnDemandSessionID=" + jobID + " "
						+ "job-name=" + result.getMethod().getMethodName());
				result.setAttribute("jobId", jobID);
				if (isSauceEnabled()) {
					String sauceUserName = ReadPropertyFile
							.getConfigurationParameter(SAUCE_CONFIG_PATH,
									"SAUCE_USERNAME");
					String sauceAPiKey = ReadPropertyFile
							.getConfigurationParameter(SAUCE_CONFIG_PATH,
									"SAUCE_API_KEY");

					SauceREST client = new SauceREST(sauceUserName, sauceAPiKey);
					Map<String, Object> updates = new HashMap<String, Object>();
					updates.put("name", result.getMethod().getMethodName());
					client.updateJobInfo(jobID, updates);
				}

			}
		} catch (Exception e) {
			// e.printStackTrace();
			logger.error("Error:webDriverFacile is null:TBD need to fix it");
		}

		icb.runTestMethod(result);

		// If the watchdog is still running, we know that a screenshot hasn't
		// been
		// taken yet.
		if (noWatchdog || (!noWatchdog && watchdog.isRunning())) {
			if (!isSauceEnabled()) {
				takeScreenshot(fullTestName, screenshotPath, true);
			}
		}

		// If the watchdog is valid and had killed the browser, we wrap the
		// UnreachableBrowserException
		// in a new exception. NOT WORKING AS OF 03/20/12 [ASCHRAGE]
		/*
		 * if (!noWatchdog && watchdog.wasTerminated()) { if (result != null &&
		 * result.getThrowable() instanceof InvocationTargetException) {
		 * InvocationTargetException targetException =
		 * (InvocationTargetException)(result.getThrowable());
		 * 
		 * if (targetException.getCause() != null && targetException.getCause()
		 * instanceof UnreachableBrowserException) {
		 * Reporter.log("Watchdog timer killed the browser."); Exception
		 * watchdogException = new
		 * Exception("Watchdog timer killed the browser after " +
		 * TestWatchdog.DEFAULT_TIMEOUT + " milliseconds.");
		 * 
		 * watchdogException.setStackTrace(targetException.getCause().getStackTrace
		 * ());
		 * 
		 * result.setThrowable(watchdogException); } } }
		 */

		// If the test finishes (normally or due to the timer), we want to
		// ensure
		// that the watchdog stops executing so we call the terminate method.
		if (!noWatchdog) {
			watchdog.terminate();
			WebPage.detachListener(watchdog);

			try {
				// After the call to terminate, we should wait for the watchdog
				// to stop execution so that we can gc the thread.
				watchDogThd.join();
			} catch (InterruptedException ex) {
				logger.debug(ex.toString());
			}
		}
	}

	/**
	 * Initialize the TestWatchdog object with the current WebDriver instance
	 * that is being use to run the test. Then use that object to create and
	 * start a new thread representing the watchdog timer.
	 * 
	 * @param fulltestname
	 *            the fulltestname
	 */
	private void initWatchdog(final String fulltestname) {
		logger.info("***********Inside init********");
		watchdog = new TestWatchdog(driver);
		WebPage.attachListener(watchdog);

		// Init the listener for the work that should be done before and after
		// the
		// watchdog kills a test.
		watchdog.setListener(new TestWatchdog.WatchdogListener() {

			public void beforeTestKilled() {
				takeScreenshot(fulltestname, screenshotPath, false);
			}

			public void afterTestKilled() {
				// Do nothing special after WebDriver has been killed.
			}
		});

		watchDogThd = new Thread(watchdog, "WebDriver Watchdog");
		watchDogThd.start();
	}

	/**
	 * Take screenshot.
	 * 
	 * @return the string
	 */
	public String takeScreenshot() {
		return takeScreenshot(getCallingMethodName(), screenshotPath, true);
	}

	/**
	 * Take screenshot.
	 * 
	 * @param name
	 *            the name
	 * @return the string
	 */
	public String takeScreenshot(String name) {
		return takeScreenshot(name, screenshotPath, true);
	}

	/**
	 * Sets the screenshot path.
	 * 
	 * @param filePath
	 *            the new screenshot path
	 */
	public void setScreenshotPath(String filePath) {
		if (!(new File(filePath).exists()))
			// throw new RuntimeException("File path does not exist: " +
			// filePath);
			new File(filePath).mkdirs();

		screenshotPath = filePath;
	}

	/**
	 * Take screenshot.
	 * 
	 * @param testName
	 *            the test name
	 * @param destinationDir
	 *            the destination dir
	 * @param pass
	 *            the pass
	 * @return the string
	 */
	public String takeScreenshot(String testName, String destinationDir,
			boolean pass) {

		// Adding a quick fix to resolve a failure on saving SS when running
		// from jenkins
		if (System.getProperty("os.name").startsWith("Windows")
				|| System.getProperty("os.name").startsWith("Mac")) {
			if (!(new File(destinationDir).exists())) {
				logger.error("The destination directory: "
						+ destinationDir
						+ " does not exist, attempting to write to another directory (C:/ on windows /tmp on other)");
				if (new File(destinationDir).mkdirs())
					logger.debug("Directory created at Path : "
							+ destinationDir);
				else {
					if (System.getProperty("os.name").startsWith("Windows"))
						destinationDir = "C:\\";
					else {
						destinationDir = "/Users/Shared/";
					}
				}
			}
		}

		// If destinationDir still does not exist, then don't take screen shot.
		if (!(new File(destinationDir).exists())) {
			logger.debug("screen shot won't be captured as " + destinationDir
					+ " does not exist..");
			return "";
		}

		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		java.awt.Dimension screenSize = toolkit.getScreenSize();
		Rectangle screenRect = new Rectangle(screenSize);
		// create screen shot
		BufferedImage image = robot.createScreenCapture(screenRect);
		// save captured image to PNG file
		try {
			String imageFileName = null;
			if (!pass)
				imageFileName = destinationDir + "FAILED_" + testName;
			else
				imageFileName = destinationDir + testName;

			int count = 0;
			String currentImageFilePath = imageFileName;

			DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			Date date = new Date();
			currentImageFilePath = currentImageFilePath + "_"
					+ dateFormat.format(date) + ".png";
			ImageIO.write(image, "png", new File(currentImageFilePath));
			logger.debug("Saved screenshot titled " + currentImageFilePath
					+ " to: " + imageFileName);

			// Log the location of the screenshot in order to x-reference it
			// with the
			// actual test results. Must take into account the two locations
			// where
			// these results would be viewed: locally and through autolab
			Reporter.log("Final Screenshot:<br> <a href='file:///"
					+ currentImageFilePath
					+ "' target='new'> <img src='file:///"
					+ currentImageFilePath
					+ "' width='300px' height='200px' /></a> <br> "
					+ "<a href='../../../screenshots/"
					+ currentImageFilePath.substring(currentImageFilePath
							.lastIndexOf("/") + 1)
					+ "' target='new'>"
					+ "<img src='../../../screenshots/"
					+ currentImageFilePath.substring(currentImageFilePath
							.lastIndexOf("/") + 1)
					+ "' width='300px' height='200px'/>"
					+ " </a>"
					+ "<a href='../screenshots/"
					+ currentImageFilePath.substring(currentImageFilePath
							.lastIndexOf("/") + 1)
					+ "' target='new'>"
					+ "<img src='../screenshots/"
					+ currentImageFilePath.substring(currentImageFilePath
							.lastIndexOf("/") + 1)
					+ "' width='300px' height='200px'/>" + " </a>");

			return currentImageFilePath;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Gets the current method name.
	 * 
	 * @return the current method name
	 */
	protected String getCurrentMethodName() {
		return getCurrentMethodNameFromThread(0);
	}

	/**
	 * Gets the calling method name.
	 * 
	 * @return the calling method name
	 */
	private String getCallingMethodName() {
		return getCurrentMethodNameFromThread(2);
	}

	/**
	 * Gets the current method name from thread.
	 * 
	 * @param stackLevel
	 *            the stack level
	 * @return the current method name from thread
	 */
	private String getCurrentMethodNameFromThread(int stackLevel) {
		/*
		 * 0 - dumpThreads 1 - getStackTrace 2 - thisMethod =>
		 * getCurrentMethodNameFromThread 3 - callingMethod => method calling
		 * thisMethod 4 - method calling callingMethod
		 */
		StackTraceElement stackTraceElement = Thread.currentThread()
				.getStackTrace()[3 + stackLevel];

		String className = stackTraceElement.getClassName();
		String methodName = stackTraceElement.getMethodName();

		return className + "." + methodName;
	}

	/**
	 * Gets the suite test name.
	 * 
	 * @param fullTestName
	 *            the full test name
	 * @return the suite test name
	 */
	protected String getSuiteTestName(String fullTestName) {
		return fullTestName;
	}

	/**
	 * Gets the suite test name.
	 * 
	 * @param fullTestName
	 *            the full test name
	 * @param params
	 *            the params
	 * @return the suite test name
	 */
	protected String getSuiteTestName(String fullTestName, Object[] params) {
		if (params.length > 0) {
			fullTestName = fullTestName + "_dp_";
		}
		return fullTestName;
	}

	/**
	 * Gets the thread safe web driver.
	 * 
	 * @return the thread safe web driver
	 */
	public static WebDriver getThreadSafeWebDriver() {
		return webDriverFacile.get();
	}

	/**
	 * It checks whether you have @SauceConnect annotation present in your test
	 * methods.
	 * 
	 * @return true, if is sauce connect annotatation present
	 */
	private boolean isSauceConnectAnnotatationPresent() {

		boolean isSauceConnectPresent = false;
		Method[] methods = this.getClass().getMethods();
		for (Method method : methods) {
			Annotation annotations[] = method.getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation.annotationType().getSimpleName()
						.equals("SauceConnect")) {
					isSauceConnectPresent = true;
				}
			}
		}
		logger.info("Sauce connect annotation present " + isSauceConnectPresent);
		return isSauceConnectPresent;

	}

	/**
	 * Gets the window dimention.
	 * 
	 * @return the window dimention
	 */
	private Dimension getWindowDimention() {
		JavascriptExecutor jsExecute = (JavascriptExecutor) driver;
		Long screenWidth = (Long) jsExecute
				.executeScript("return screen.width");
		Long screenHeight = (Long) jsExecute
				.executeScript("return screen.width");
		return new Dimension(screenWidth.intValue(), screenHeight.intValue());
	}

	/**
	 * Gets the driver path.
	 * 
	 * @param driverExe
	 *            the driver exe
	 * @return the driver path
	 */
	private String getDriverPath(String driverExe, String osName) {
		// File tempDir = new File(System.getProperty("java.io.tmpdir"));
		// File temporaryFile = new File(tempDir, driverExe);
		//
		// String temporaryFilePath = "";
		//
		// if (!temporaryFile.exists()) {
		//
		// InputStream driverStream = getClass().getResourceAsStream(
		// "/drivers/" + osName + "/" + driverExe);
		// try {
		// logger.info(driverExe
		// + " driver does not exist, copying it to temp dir "
		// + temporaryFile.getAbsolutePath());
		// IOUtils.copy(driverStream, new FileOutputStream(temporaryFile));
		// /* Commenting the below line to fix the chrome issue. */
		// // temporaryFile.setExecutable(true);
		// temporaryFilePath = temporaryFile.getAbsolutePath();
		//
		// } catch (FileNotFoundException e1) {
		// temporaryFilePath = "";
		// e1.printStackTrace();
		// } catch (IOException e1) {
		// temporaryFilePath = "";
		// e1.printStackTrace();
		// }
		// } else {
		// logger.info("Driver path is " + temporaryFile.getAbsolutePath());
		// temporaryFilePath = temporaryFile.getAbsolutePath();
		// }
		// return temporaryFilePath;
		File driverFile = new File("src/test/resources/drivers/" + osName + "/" + driverExe);

		String temporaryFilePath = "";

		if (driverFile.exists()) {

			temporaryFilePath = driverFile.getAbsolutePath();
		} else {
			logger.error("Driver does not exist in the path  "
					+ driverFile.getAbsolutePath());
		}
		return temporaryFilePath;

	}

	@SuppressWarnings("deprecation")
	public void takeBrowserScreenshot(String fileName, String destinationDir) {
		if (!(new File(destinationDir).isDirectory())) {
			new File(destinationDir).mkdir();
		}
		// save captured image to JPEG file
		String imageFileName = destinationDir + "/" + fileName;
		String currentImageFilePath = null;
		currentImageFilePath = imageFileName + ".jpeg";
		File f = new File(currentImageFilePath);
		if (driver instanceof FirefoxDriver) {
			// ((FirefoxDriver) driver).saveScreenshot(new
			// File(currentImageFilePath));
			File ss = ((FirefoxDriver) driver).getScreenshotAs(OutputType.FILE);
			ss.renameTo(new File(currentImageFilePath));
		}
		logger.info("Saved screenshot titled " + fileName + " to: "
				+ imageFileName);

	}

}
