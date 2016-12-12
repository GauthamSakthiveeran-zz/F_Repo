package com.ooyala.playback;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.ooyala.facile.listners.IMethodListener;
import com.ooyala.facile.proxy.browsermob.BrowserMobProxyHelper;
import com.ooyala.facile.test.FacileTest;
import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.httpserver.SimpleHttpServer;
import com.ooyala.playback.live.LiveChannel;
import com.ooyala.playback.live.NeoRequest;
import com.ooyala.playback.page.PlayBackPage;
import com.ooyala.playback.report.ExtentManager;
import com.ooyala.playback.url.Testdata;
import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@Listeners(IMethodListener.class)
public abstract class PlaybackWebTest extends FacileTest {

	private Logger logger = Logger.getLogger(PlaybackWebTest.class);
	protected String browser;
	protected ChromeDriverService service;
	// protected PropertyReader propertyReader;
	protected PlayBackFactory pageFactory;
	protected ExtentReports extentReport;
	protected ExtentTest extentTest;
	protected Testdata testData;
	protected String[] jsUrl;
	protected NeoRequest neoRequest;
	protected LiveChannel liveChannel;

	public PlaybackWebTest() throws OoyalaException {

		try {
			// propertyReader = PropertyReader.getInstance("config.properties");
		} catch (Exception e) {
			throw new OoyalaException("could not read properties file");
		}

		extentReport = ExtentManager.getReporter();
		neoRequest = NeoRequest.getInstance();
		liveChannel = new LiveChannel();
	}

	@BeforeMethod(alwaysRun = true)
	public void handleTestMethodName(Method method, Object[] testData) {
		logger.info("*** Test " + testData[0].toString() + " started *********");
		extentTest = extentReport.startTest(testData[0].toString());

		try {
			Field[] fs = this.getClass().getDeclaredFields();
			fs[0].setAccessible(true);
			for (Field property : fs) {
				if (property.getType().getSuperclass()
						.isAssignableFrom(PlayBackPage.class)) {
					property.setAccessible(true);
					property.set(this,
							pageFactory.getObject(property.getType()));
					Method[] allMethods = property.get(this).getClass()
							.getMethods();
					for (Method function : allMethods) {
						if (function.getName()
								.equalsIgnoreCase("setExtentTest"))
							function.invoke(property.get(this), extentTest);
					}
				}
			}
			liveChannel.startChannel(testData[0].toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getJSFile(String jsFile) throws Exception {
		logger.info("************Getting the JS file*************");
		String[] jsFiles;
		if (jsFile.contains(",")) {
			jsFiles = jsFile.split(",");
		} else {
			jsFiles = new String[1];
			jsFiles[0] = jsFile;
		}
		// String jsHost = readPropertyOrEnv("jshostIpAddress","10.11.66.55");
		if (jsFiles != null && jsFiles.length > 0) {
			jsUrl = new String[jsFiles.length];
			for (int i = 0; i < jsFiles.length; i++) {
				InetAddress inetAdd = InetAddress.getLocalHost();
				jsUrl[i] = "http://" + inetAdd.getHostAddress() + ":"
						+ SimpleHttpServer.portNumber + "/js?fileName="
						+ jsFiles[i];
			}
		}

	}

	public String getTestCaseName(Method method, Object[] testData) {
		String testCase = "";
		if (testData != null && testData.length > 0) {
			for (Object testParameter : testData) {
				if (testParameter instanceof String) {
					String testCaseParams = (String) testParameter;
					testCase = testCaseParams;
					break;
				}
			}
			testCase = String.format("%s(%s)", method.getName(), testCase);
		} else
			testCase = method.getName();

		return testCase;
	}

	@BeforeSuite(alwaysRun = true)
	public void beforeSuiteInPlaybackWeb() throws OoyalaException {
		int portNumber = getRandomOpenPort();
		SimpleHttpServer.startServer(portNumber);
	}

	public int getRandomOpenPort() {
		int retry = 4;
		int index = 1;
		while (index < retry) {
			int min = 10000;
			int max = 50000;
			Random rand = new Random();
			int randomPort = min + rand.nextInt((max - min) + 1);
			boolean isPortOpen = checkPort(randomPort);
			if (isPortOpen)
				return randomPort;
			index++;
		}
		return -1;
	}

	public boolean checkPort(int portNumber) {
		try {
			logger.info("Checking if port open by trying to connect as a client");
			Socket sock = new Socket("localhost", portNumber);
			sock.close();
			logger.info("Port looks like is not open " + portNumber);
		} catch (Exception e) {
			if (e.getMessage().contains("refused")) {
				return true;
			}
		}
		return false;
	}

	@AfterSuite()
	public void afterSuiteInPlaybackWeb() throws OoyalaException {
		SimpleHttpServer.stopServer();

	}

	@BeforeClass(alwaysRun = true)
	@Parameters({ "testData", "jsFile" })
	public void setUp(@Optional String xmlFile, String jsFile) throws Exception {
		logger.info("************Inside setup*************");

		browser = System.getProperty("browser");
		if (browser == null || browser.equals(""))
			browser = "chrome";
		logger.info("browser is " + browser);

		driver = getDriver(browser);
		if (driver != null)
			logger.info("Driver initialized successfully");
		else {
			logger.error("Driver is not initialized successfully");
			throw new OoyalaException("Driver is not initialized successfully");
		}

		// driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// driver.manage().timeouts().implicitlyWait(240, TimeUnit.MINUTES);
		pageFactory = PlayBackFactory.getInstance(driver);
		if (!getPlatform().equalsIgnoreCase("android")) {
			maximizeMe(driver);
		}
		parseXmlFileData(xmlFile);
		getJSFile(jsFile);

	}

	@AfterMethod(alwaysRun = true)
	protected void afterMethod(ITestResult result) {

		logger.info("****** Inside @AfterMethod*****");
		logger.info(driver);

		if (driver != null
				&& (driver.getSessionId() == null || driver.getSessionId()
						.toString().isEmpty())) {
			logger.error("Browser closed during the test run. Renitializing the driver as the test failed during the test");

			driver = getDriver(browser);
			pageFactory.destroyInstance();
			pageFactory = PlayBackFactory.getInstance(driver);
		} else {
			takeScreenshot(extentTest.getTest().getName());
		}
		if (result.getStatus() == ITestResult.FAILURE) {
			extentTest.log(
					LogStatus.INFO,
					"Snapshot is "
							+ extentTest.addScreenCapture("images/"
									+ extentTest.getTest().getName()));
			extentTest.log(LogStatus.FAIL, result.getThrowable());
			logger.error("**** Test " + extentTest.getTest().getName()
					+ " failed ******");
		} else if (result.getStatus() == ITestResult.SKIP) {
			extentTest.log(LogStatus.SKIP, extentTest.getTest().getName()
					+ " Test skipped " + result.getThrowable());
			logger.error("**** Test" + extentTest.getTest().getName()
					+ " Skipped ******");
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			extentTest.log(LogStatus.PASS, extentTest.getTest().getName()
					+ " Test passed");
			logger.error("**** Test" + extentTest.getTest().getName()
					+ " passed ******");
		} else {
			extentTest.log(LogStatus.UNKNOWN, extentTest.getTest().getName()
					+ " Test result is unknown");
			logger.error("**** Test" + extentTest.getTest().getName()
					+ " passed ******");
		}
		extentReport.endTest(extentTest);

	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		if (isBrowserMobProxyEnabled())
			BrowserMobProxyHelper.stopBrowserMobProxyServer();
		extentReport.flush();
		logger.info("************Inside tearDown*************");
		if (driver != null) {
			driver.quit();
			driver = null;
		} else {
			logger.info("Driver is already null");
		}
		logger.info("Assigning the neopagefactory instance to null");
		pageFactory.destroyInstance();
		// Stopping the live channels if exists after this class
		liveChannel.stopChannels();
	}

	public void waitForSecond(int sec) {
		try {
			Thread.sleep(sec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void parseXmlFileData(String xmlFile) {

		try {

			if (xmlFile == null || xmlFile.isEmpty()) {
				xmlFile = getClass().getSimpleName();
				String packagename = getClass().getPackage().getName();
				if (packagename.contains("amf")) { // TODO
					xmlFile = "amf/" + xmlFile + ".xml";
				}
			}

			File file = new File("src/test/resources/testdata/" + xmlFile);
			JAXBContext jaxbContext = JAXBContext.newInstance(Testdata.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			testData = (Testdata) jaxbUnmarshaller.unmarshal(file);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			if (e instanceof NullPointerException) {
				extentTest
						.log(LogStatus.FAIL,
								"The test data file should be mentioned as part of the testng parameter "
										+ "or should be renamed to the name of the class using the test data!");
			}
		}
	}

	public void injectScript() throws Exception {
		if (jsUrl != null && jsUrl.length > 0) {
			for (String url : jsUrl) {
				try {
					logger.info("JS - " + url);
					injectScript(url);
				} catch (Exception e) {
					// e.printStackTrace();
					logger.error(e.getMessage());
					logger.info("Retrying...");
					injectScript(url);
				}
			}
			extentTest
					.log(LogStatus.PASS, "Javascript injection is successful");
		}
	}

	private void injectScript(String scriptURL) throws Exception {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Object object = js.executeScript("function injectScript(url) {\n"
				+ "   var script = document.createElement ('script');\n"
				+ "   script.src = url;\n"
				+ "   var head = document.getElementsByTagName( 'head')[0];\n"
				+ "   head.appendChild(script);\n" + "}\n" + "\n"
				+ "var scriptURL = arguments[0];\n"
				+ "injectScript(scriptURL);", scriptURL);
		Thread.sleep(1000); // to avoid js failures
		if (scriptURL.contains("common"))
			object = js.executeScript("subscribeToCommonEvents();");
		else
			object = js.executeScript("subscribeToEvents();");
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

	public String getBrowserVersion() {
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		String version = cap.getVersion().toString();
		return version;
	}

	public static String readPropertyOrEnv(String key, String defaultValue) {
		String v = System.getProperty(key);
		if (v == null)
			v = System.getenv(key);
		if (v == null)
			v = defaultValue;
		return v;
	}

	public String takeScreenshot(String fileName) {

		logger.info("Taking Screenshot");

		File destDir = new File("images/");
		if (!destDir.exists())
			destDir.mkdir();

		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File("images/" + fileName));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Not able to take the screenshot");
		}
		return "images/" + fileName;
	}

	@DataProvider(name = "testUrls")
	public Object[][] getTestData() {

		String version = getBrowserVersion();
		Map<String, String> urls = UrlGenerator.parseXmlDataProvider(getClass()
				.getSimpleName(), testData, browser, version);
		String testName = getClass().getSimpleName();
		Object[][] output = new Object[urls.size()][2];

		Iterator<Map.Entry<String, String>> entries = urls.entrySet()
				.iterator();
		int i = 0;
		while (entries.hasNext()) {
			Map.Entry<String, String> entry = entries.next();
			output[i][0] = testName + " : " + entry.getKey();
			output[i][1] = entry.getValue();
			i++;
		}

		return output;

	}

	protected String removeSkin(String url) { // TODO
		return url
				.replace(
						"http%3A%2F%2Fplayer.ooyala.com%2Fstatic%2Fv4%2Fcandidate%2Flatest%2Fskin-plugin%2Fhtml5-skin.min.js",
						"");
	}

	protected Object executeScript(String script) {
		return ((JavascriptExecutor) driver).executeScript(script);
	}

}
