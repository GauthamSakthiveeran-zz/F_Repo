package com.ooyala.playback;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.w3c.dom.NodeList;

import com.ooyala.facile.listners.IMethodListener;
import com.ooyala.facile.test.FacileTest;
import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.page.PlayBackPage;
import com.ooyala.playback.report.ExtentManager;
import com.ooyala.playback.url.Testdata;
import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.qe.common.exception.OoyalaException;
import com.ooyala.qe.common.util.PropertyReader;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@Listeners(IMethodListener.class)
public abstract class PlaybackWebTest extends FacileTest {

	public static Logger logger = Logger.getLogger(PlaybackWebTest.class);
	protected String browser;
	protected ChromeDriverService service;
	protected PropertyReader propertyReader;
	protected PlayBackFactory pageFactory;
	// protected static NodeList nodeList;
	protected ExtentReports extentReport;
	protected ExtentTest extentTest;
	protected Testdata testData;

	private static String MAC_CHROME_DRIVER_PATH = "src/test/resources/lib-thirdparty/chromedriverformac/chromedriver";
	private static String WIN_CHROME_DRIVER_PATH = "src/test/resources/lib-thirdparty/chromedriverforwin/chromedriver.exe";
	private static String LINUX_CHROME_DRIVER_PATH = "src/test/resources/lib-thirdparty/chromedriverforlinux/chromedriver";

	public PlaybackWebTest() throws OoyalaException {

		try {
			propertyReader = PropertyReader.getInstance("config.properties");
		} catch (Exception e) {
			throw new OoyalaException("could not read properties file");
		}

		extentReport = ExtentManager.getReporter();
	}

	@BeforeMethod(alwaysRun = true)
	public void handleTestMethodName(Method method, Object[] testData) {
		String testCaseName = getTestCaseName(method, testData);
		extentTest = extentReport.startTest(testCaseName);
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
		} catch (Exception e) {
			e.printStackTrace();
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

	@BeforeClass(alwaysRun = true)
	@Parameters("testData")
	public void setUp(String xmlFile) throws Exception {
		logger.info("************Inside setup*************");
		logger.info("browser is " + browser);

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

	@AfterMethod(alwaysRun = true)
	protected void afterMethod(ITestResult result) {

		takeScreenshot(result.getTestName());
		if (result.getStatus() == ITestResult.FAILURE) {
			extentTest.log(
					LogStatus.INFO,
					"Snapshot is "
							+ extentTest.addScreenCapture("images/"
									+ result.getTestName()));
			extentTest.log(LogStatus.FAIL, result.getThrowable());
		} else if (result.getStatus() == ITestResult.SKIP) {
			extentTest.log(LogStatus.SKIP, result.getTestName()
					+ " Test skipped " + result.getThrowable());
		} else {
			extentTest.log(LogStatus.PASS, result.getTestName()
					+ " Test passed");
		}
		extentReport.endTest(extentTest);
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

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		extentReport.flush();
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

	public void parseXmlFileData(String xmlFile) {

		try {
			File file = new File("src/test/resources/" + xmlFile);
			JAXBContext jaxbContext = JAXBContext.newInstance(Testdata.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			testData = (Testdata) jaxbUnmarshaller.unmarshal(file);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
	}

	// public NodeList parseXmlFileData(String fileName) {
	//
	// try {
	// File xmlFile = new File("src/test/resources/" + fileName + ".xml");
	// DocumentBuilderFactory dbuilderFactory = DocumentBuilderFactory
	// .newInstance();
	// DocumentBuilder dbuilder = dbuilderFactory.newDocumentBuilder();
	// org.w3c.dom.Document doc = dbuilder.parse(xmlFile);
	// nodeList = doc.getElementsByTagName("test");
	//
	// } catch (Exception e) {
	// logger.info(e.getMessage());
	// }
	// return nodeList;
	// }

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
		extentTest.log(LogStatus.PASS, "Javascript injection is successful");
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

	public String takeScreenshot(String fileName) {
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

		List<String> urls = UrlGenerator.parseXmlDataProvider(getClass()
				.getSimpleName(), testData);
		String testName = getClass().getSimpleName();
		Object[][] output = new Object[urls.size()][2];
		for (int i = 0; i < urls.size(); i++) {
			output[i][0] = testName;
			output[i][1] = urls.get(i);
		}

		return output;

	}
}
