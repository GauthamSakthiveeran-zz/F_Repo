package com.ooyala.playback;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.ooyala.facile.test.FacileTest;
import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.Testdata;
import com.ooyala.playback.apps.Testdata.Test;
import com.ooyala.playback.apps.report.ExtentManager;
import com.ooyala.playback.apps.utils.CommandLine;
import com.ooyala.playback.apps.utils.CommandLineParameters;
import com.ooyala.playback.apps.utils.RemoveEventsLogFile;
import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

public class PlaybackAppsTest extends FacileTest {
	protected Logger logger = Logger.getLogger(PlaybackAppsTest.class);
	protected PlayBackFactory pageFactory;
	protected Testdata testData;
	protected ExtentTest extentTest;
	static boolean isAppClosed = false;
	
    @BeforeClass(alwaysRun = true)
    @Parameters({ "testData", "xmlFile"})
    public void setUp(@Optional String xmlFile,@Optional String xmlFilePkg) throws Exception {
        logger.info("************Inside setup*************");
        parseXmlFileData(xmlFile,xmlFilePkg);
        initializeDriver();
        isAppClosed = false;
    }

	private RemoteWebDriver initializeDriver() {
		
		
		String app = testData.getApp().getName();
		
		String ip = System.getProperty(CommandLineParameters.APPIUM_SERVER) != null ? System.getProperty(CommandLineParameters.APPIUM_SERVER) : "127.0.0.1";
		String port = System.getProperty(CommandLineParameters.APPIUM_PORT) != null ? System.getProperty(CommandLineParameters.APPIUM_PORT) : "4723";

		try {

		if (System.getProperty(CommandLineParameters.PLATFORM).equalsIgnoreCase("ios")) {
			
			boolean useNewWDA = System.getProperty(CommandLineParameters.USE_NEW_WDA) != null
					? new Boolean(System.getProperty(CommandLineParameters.USE_NEW_WDA)) : false;

			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("platformVersion", System.getProperty(CommandLineParameters.PLATFORM_VERSION));
			capabilities.setCapability("deviceName", System.getProperty(CommandLineParameters.DEVICE_NAME));
			capabilities.setCapability("app", System.getProperty(CommandLineParameters.APP_PACKAGE)+app+".app");			
			capabilities.setCapability("udid", System.getProperty(CommandLineParameters.UDID));
			capabilities.setCapability("platform", System.getProperty(CommandLineParameters.PLATFORM));
			capabilities.setCapability("platformName", MobilePlatform.IOS);
			capabilities.setCapability("showIOSLog", System.getProperty(CommandLineParameters.SHOW_IOS_LOG));
			capabilities.setCapability("automationName", System.getProperty(CommandLineParameters.AUTOMATION_NAME));
			capabilities.setCapability("newCommandTimeout",
					System.getProperty(CommandLineParameters.NEW_COMMAND_TIMEOUT));
	        capabilities.setCapability("xcodeOrgId", System.getProperty(CommandLineParameters.XCODE_ORG_ID));
	        capabilities.setCapability("xcodeSigningId", System.getProperty(CommandLineParameters.XCODE_SIGNING_ID));
	        capabilities.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS, Boolean.TRUE);
	        capabilities.setCapability("useNewWDA", useNewWDA);
	        capabilities.setCapability("usePrebuiltWDA", true);
	        capabilities.setCapability("launchTimeout", "30000");
	        capabilities.setCapability("wdaLaunchTimeout", "30000");
	        capabilities.setCapability("wdaConnectionTimeout", "10000");
	        capabilities.setCapability("wdaStartupRetries", "4");
	        capabilities.setCapability("wdaStartupRetryInterval", "10000");


			driver = new IOSDriver(new URL("http://" + ip + ":" + port + "/wd/hub"), capabilities);

		} else {

			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
			capabilities.setCapability("platformName", System.getProperty(CommandLineParameters.PLATFORM));
			capabilities.setCapability(CapabilityType.VERSION,
					System.getProperty(CommandLineParameters.PLATFORM_VERSION));
			capabilities.setCapability("deviceName", System.getProperty(CommandLineParameters.DEVICE_NAME));
			capabilities.setCapability("app", System.getProperty(CommandLineParameters.APP));
			capabilities.setCapability("appPackage", System.getProperty(CommandLineParameters.APP_PACKAGE));
			capabilities.setCapability("appActivity", System.getProperty(CommandLineParameters.APP_ACTIVITY));
			capabilities.setCapability("newCommandTimeout",
					System.getProperty(CommandLineParameters.NEW_COMMAND_TIMEOUT));
			driver = new AndroidDriver(new URL("http://" + ip + ":" + port + "/wd/hub"), capabilities);
			// driver.manage().timeouts().implicitlyWait(3000,TimeUnit.SECONDS);
		}}
		catch(Exception e) {
			logger.info(e.getMessage());
		}
		
		return driver;

	}
	
	@BeforeMethod(alwaysRun = true)
	public void handleTestMethodName(Method method, Object[] testData) {
		try {

//			Thread.sleep(15000);
//			
//			if (driver == null || driver.getSessionId() == null) {
//				logger.info("driver value is "+driver);
//				
//				if(driver != null)
//				logger.info(driver.getSessionId());
//				initializeDriver();
//				isAppClosed = false;
//			}
			int attempts = 0;
			while(driver == null || driver.getSessionId() == null ) {
				if(attempts<10) {
					Thread.sleep(15000);
				initializeDriver();
				}
				else {
					break;
				}
				attempts++;
			}

			
			extentTest = ExtentManager.startTest(testData[0].toString());
			pageFactory = new PlayBackFactory((AppiumDriver) driver, extentTest);
			
			if (isAppClosed) {
				try {
					pageFactory.getLaunchAction().launchApp();

				} catch (Exception e) {
					e.printStackTrace();
					pageFactory = new PlayBackFactory((AppiumDriver) driver, extentTest);
					pageFactory.getLaunchAction().launchApp();
				}
			}
			
			if (System.getProperty(CommandLineParameters.PLATFORM).equalsIgnoreCase("ios")) {
				if (!this.testData.getApp().getName().equals("OoyalaSkinSampleApp")) {
					Assert.assertTrue(new PlayBackFactory((AppiumDriver) driver, extentTest).getQAModeSwitchAction()
					        .startAction("QA_MODE_SWITCH"), "QA Mode is not enabled. Hence failing test");
				}
			} else {
				// For Android- Events will be written in the log file.
				String command = "adb -s " + System.getProperty(CommandLineParameters.UDID) + " push log.file /sdcard/";
				String[] final_command = CommandLine.command(command);
				Runtime run = Runtime.getRuntime();
				run.exec(final_command);
				logger.info("We have executed the command log file has been pushed");
				Thread.sleep(5000);
			}
			
			Field[] fs = this.getClass().getDeclaredFields();
			fs[0].setAccessible(true);
			for (Field property : fs) {
				if (property.getType().getSuperclass().isAssignableFrom(PlaybackApps.class)) {
					property.setAccessible(true);
					property.set(this, pageFactory.getObject(property.getType()));
					Method[] allMethods = property.get(this).getClass().getMethods();
					for (Method function : allMethods) {
						if (function.getName().equalsIgnoreCase("setExtentTest"))
							function.invoke(property.get(this), extentTest);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@AfterMethod(alwaysRun = true)
	protected void afterMethod(ITestResult result) throws Exception {

		try {
			// delete log file for android
			if (System.getProperty(CommandLineParameters.PLATFORM).equalsIgnoreCase("android"))
				RemoveEventsLogFile.removeEventsFileLog();

			if (result.getStatus() == ITestResult.FAILURE) {

				extentTest.log(LogStatus.FAIL, result.getThrowable().getMessage());
				logger.error("**** Test " + extentTest.getTest().getName() + " failed ******");
				String fileName = takeScreenshot(extentTest.getTest().getName());
				extentTest.log(LogStatus.INFO, "Snapshot is " + extentTest.addScreenCapture(fileName));

			} else if (result.getStatus() == ITestResult.SKIP) {
				extentTest.log(LogStatus.SKIP,
						extentTest.getTest().getName() + " Test skipped " + result.getThrowable());
				logger.info("**** Test" + extentTest.getTest().getName() + " Skipped ******");
			} else if (result.getStatus() == ITestResult.SUCCESS) {

				logger.info("**** Test" + extentTest.getTest().getName() + " passed ******");
			} else {
				extentTest.log(LogStatus.FAIL, result.getThrowable());
				logger.error("**** Test " + extentTest.getTest().getName() + " failed ******");
			}
			isAppClosed = pageFactory.getLaunchAction().closeApp();
		} catch (Exception ex) {
			extentTest.log(LogStatus.INFO, ex.getMessage());
			logger.warn(ex.getMessage());
		}
		ExtentManager.endTest(extentTest);
		ExtentManager.flush();
	}
	  
	private void parseXmlFileData(String xmlFile, String xmlFilePkg) {
		try {
			if (xmlFile == null || xmlFile.isEmpty()) {
				xmlFile = getClass().getSimpleName();
				String packagename = getClass().getPackage().getName();

				if (packagename.contains(xmlFilePkg)) {
					xmlFile = xmlFilePkg + "/" + xmlFile + ".xml";
				} 

				logger.info("XML test data file:" + xmlFile);
			}
			File file = new File("src/main/resources/testdata/" + xmlFile);
			JAXBContext jaxbContext = JAXBContext.newInstance(Testdata.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			testData = (Testdata) jaxbUnmarshaller.unmarshal(file);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.info(e.getMessage());
		}
	}

	@DataProvider(name = "testData")
	public Object[][] getTestData() {

		Map<String, TestParameters> tests = parseXmlDataProvider(testData.getApp().getName(), testData);
		Object[][] output = new Object[tests.size()][2];
		Iterator<Map.Entry<String, TestParameters>> entries = tests.entrySet().iterator();
		int i = 0;
		while (entries.hasNext()) {
			Map.Entry<String, TestParameters> entry = entries.next();
			output[i][0] = entry.getKey();
			output[i][1] = entry.getValue();
			i++;
		}

		return output;
	}

	private Map<String, TestParameters> parseXmlDataProvider(String simpleName, Testdata testData) {

		Map<String, TestParameters> testsGenerated = new HashMap<String, TestParameters>();

		for (Test data : testData.getTest()) {
			TestParameters testParam = new TestParameters();
			testParam.setDescription(data.getDescription().getName());
			testParam.setAsset(data.getName());
			testParam.setApp(simpleName);
			testsGenerated.put(simpleName + "-" + data.getName(), testParam);
		}
		return testsGenerated;
	}
	
	public String takeScreenshot(String fileName) {
        try {
            logger.info("Taking Screenshot");
            File destDir = new File("images/");
            if (!destDir.exists())
                destDir.mkdir();
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(scrFile, new File("images/" + fileName));
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Not able to take the screenshot");
            }
        } catch (Exception ex) {
            logger.info(ex.getMessage());
        }
        return "images/" + fileName;
    }
	
	@AfterClass(alwaysRun = true)
	public void afterClass() {
		try {
			if (System.getProperty(CommandLineParameters.PLATFORM).equalsIgnoreCase("android")) {
				
				((AndroidDriver) driver).closeApp();
				logger.info("Closing App");
			}else{
				driver.quit();

			}
		} catch (Exception e) {
			logger.info("Error While Closing App");
		}
	}
}
