package com.ooyala.playback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;
import org.w3c.dom.NodeList;

import com.ooyala.facile.listners.IMethodListener;
import com.ooyala.facile.test.FacileTest;
import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.qe.common.exception.OoyalaException;
import com.ooyala.qe.common.util.PropertyReader;

@Listeners(IMethodListener.class)
public abstract class PlaybackWebTest extends FacileTest {

	public static Logger logger = Logger.getLogger(PlaybackWebTest.class);
	protected String browser;
	protected ChromeDriverService service;
	protected PropertyReader propertyReader;
	protected PlayBackFactory pageFactory;
	protected static NodeList nodeList;
	private static String MAC_CHROME_DRIVER_PATH = "src/test/resources/lib-thirdparty/chromedriverformac/chromedriver";
	private static String WIN_CHROME_DRIVER_PATH = "src/test/resources/lib-thirdparty/chromedriverforwin/chromedriver.exe";
	private static String LINUX_CHROME_DRIVER_PATH = "src/test/resources/lib-thirdparty/chromedriverforlinux/chromedriver";

	public PlaybackWebTest() throws OoyalaException {

		try {
			propertyReader = PropertyReader.getInstance("config.properties");
		} catch (Exception e) {
			throw new OoyalaException("could not read properties file");
		}

	}

	// @BeforeMethod(alwaysRun = true)



	@BeforeClass(alwaysRun = true)
	public void setUp() throws Exception {
		logger.info("************Inside setup*************");
		logger.info("browser is " + browser);

		String xmlFile = System.getProperty("xmlFile");
		System.out.println("xml file : "+xmlFile);
		if (xmlFile == null || xmlFile.equals(""))
			xmlFile = "Alice";
		browser = System.getProperty("browser");
		if (browser == null || browser.equals(""))
			browser = "firefox";
		String mode = System.getProperty("mode");
		if (mode != null && mode.equals(""))
			mode = "local";
		if (mode != null && mode.equals("remote")){
			driver = getRemoteDriver(browser);
		}

		else
			driver = getDriver(browser);

		logger.info("Driver initialized successfully");
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		// driver.manage().timeouts().implicitlyWait(240, TimeUnit.MINUTES);
		pageFactory = PlayBackFactory.getInstance(driver);
		parseXmlFileData(xmlFile);

	}

	public static DesiredCapabilities getDesiredCapabilities(String browser) {
		DesiredCapabilities dr = null;
		if (browser.equalsIgnoreCase("firefox")) {
			dr = DesiredCapabilities.firefox();
			dr.setBrowserName("firefox");
		} else if (browser.equalsIgnoreCase("chrome")) {
			dr = DesiredCapabilities.chrome();
			dr.setBrowserName("chrome");
		} else if (browser.equalsIgnoreCase("internet explorer")) {
			dr = DesiredCapabilities.internetExplorer();
			dr.setBrowserName("internet explorer");
		} else if (browser.equalsIgnoreCase("safari")) {
			dr = DesiredCapabilities.safari();
			dr.setBrowserName("safari");
		} else {
			dr = DesiredCapabilities.chrome();
			dr.setBrowserName("chrome");
		}
		return dr;
	}

	public RemoteWebDriver getRemoteDriver(String browserType) {

		DesiredCapabilities desiredCapabilities = getDesiredCapabilities(browserType);
		String version = System.getProperty("version");
		if (version != null)
			desiredCapabilities.setVersion(version);
		String platform = System.getProperty("platform");
		desiredCapabilities.setCapability(CapabilityType.PLATFORM, platform);
		String ipAddress = System.getProperty("ipaddress");
		if (ipAddress != null && ipAddress.equals(""))
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
				System.exit(1);
			}
		}
		return driver;

	}

	public void startChromeService() throws IOException {
		String chromeDriverPath = null;
		if (System.getProperty("os.name").toLowerCase().contains("mac"))
			chromeDriverPath = MAC_CHROME_DRIVER_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("win"))
			chromeDriverPath = WIN_CHROME_DRIVER_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("linux"))
			chromeDriverPath = LINUX_CHROME_DRIVER_PATH;
		service = new ChromeDriverService.Builder()
				.usingDriverExecutable(new File(chromeDriverPath))
				.usingAnyFreePort().build();
		service.start();
		logger.info("Started chrome service");
	}

	// @AfterMethod(alwaysRun = true)
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		logger.info("************Inside tearDown*************");
		if (driver != null) {
			driver.quit();
			driver = null;
		} else {
			logger.info("Driver is already null");
		}
		logger.info("Assigning the neopagefactory instance to null");
		PlayBackFactory.destroyInstance();
	}

	public void waitForSecond(int sec) {
		try {
			Thread.sleep(sec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public NodeList parseXmlFileData(String fileName) {

		try {
			File xmlFile = new File("src/test/resources/" + fileName + ".xml");
			DocumentBuilderFactory dbuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dbuilder = dbuilderFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dbuilder.parse(xmlFile);
			nodeList = doc.getElementsByTagName("test");

		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return nodeList;
	}

	public void injectScript(String scriptURL) throws Exception {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Object object = js.executeScript("function injectScript(url) {\n"
				+ "   var script = document.createElement ('script');\n"
				+ "   script.src = url;\n"
				+ "   var head = document.getElementsByTagName( 'head')[0];\n"
				+ "   head.appendChild(script);\n" + "}\n" + "\n"
				+ "var scriptURL = arguments[0];\n"
				+ "injectScript(scriptURL);", scriptURL);

		object = js.executeScript("subscribeToEvents();");
	}

	public long loadingSpinner() {
		long startTime;
		long endTime = 0L;
		int time = 0;
		long flag = 0L;

		while (true) {

			startTime = System.currentTimeMillis();
			// Giving hardcoded end time as 2 minutes i.e it will check loading
			// spinner upto 2 min otherwise will break
			if (time <= 120) {
				try {
					driver.findElement(By.className("oo-spinner"))
							.isDisplayed();
					Thread.sleep(1000);
					time++;
				} catch (Exception e) {
					endTime = System.currentTimeMillis();
					break;
				}
			} else {
				logger.info("Loading spinner is not vanishing i.e it occured more that 2 minutes");
				flag = 1;
				break;
			}

		}
		return flag;

	}

}
