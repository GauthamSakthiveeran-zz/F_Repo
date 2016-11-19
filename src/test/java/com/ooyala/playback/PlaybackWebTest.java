package com.ooyala.playback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
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

		driver = getDriver(browser);

		logger.info("Driver initialized successfully");
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		// driver.manage().timeouts().implicitlyWait(240, TimeUnit.MINUTES);
		pageFactory = PlayBackFactory.getInstance(driver);
		parseXmlFileData(xmlFile);

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

	public String getPlatform() {
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		String platformName = cap.getPlatform().toString();
		return platformName;
	}

	public String getBrowser() {
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		String browser = cap.getBrowserName().toString();
		return browser;
	}
    public static String readPropertyOrEnv(String key, String defaultValue) {
        String v = System.getProperty(key);
        if (v == null)
            v = System.getenv(key);
        if (v == null)
            v = defaultValue;
        return v;
    }

    public String jsURL(){
        String jsHost = readPropertyOrEnv("jshostIpAddress","10.11.66.55");
        String url = "http://"+jsHost+":8080/alice_full.js";
        return url;
    }

}
