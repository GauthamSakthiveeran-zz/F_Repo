package com.ooyala.playback;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.DataProvider;
import org.testng.asserts.SoftAssert;

import com.ooyala.facile.test.FacileTest;
import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.Testdata;
import com.ooyala.playback.apps.Testdata.Test;
import com.ooyala.playback.apps.utils.CommandLine;
import com.ooyala.playback.apps.utils.CommandLineParameters;
import com.ooyala.playback.factory.PlayBackFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class PlaybackAppsTest extends FacileTest {

	protected PlayBackFactory pageFactory;
	protected Testdata testData;
	
    @BeforeClass(alwaysRun = true)
    @Parameters({ "testData", "xmlFile"})
    public void setUp(@Optional String xmlFile,@Optional String xmlFilePkg) throws Exception {
        logger.info("************Inside setup*************");
        parseXmlFileData(xmlFile,xmlFilePkg);
        initializeDriver();
        pageFactory = new PlayBackFactory((AppiumDriver) driver);
    }

	private RemoteWebDriver initializeDriver() throws MalformedURLException {
		
		String app = testData.getApp().getName();
		if (System.getProperty(CommandLineParameters.platform).equalsIgnoreCase("ios")) {
			String ip = System.getProperty("appiumServer") != null ? System.getProperty("appiumServer") : "127.0.0.1";
			String port = System.getProperty("appiumPort") != null ? System.getProperty("appiumPort") : "4723";
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("platformVersion", System.getProperty(CommandLineParameters.platformVersion));
			capabilities.setCapability("deviceName", System.getProperty(CommandLineParameters.deviceName));
			capabilities.setCapability("app", System.getProperty(CommandLineParameters.appPackage)+app+".app");			
			capabilities.setCapability("udid", System.getProperty(CommandLineParameters.udid));
			capabilities.setCapability("platform", System.getProperty(CommandLineParameters.platform));
			capabilities.setCapability("showIOSLog", System.getProperty(CommandLineParameters.showIOSLog));
			capabilities.setCapability("automationName", System.getProperty(CommandLineParameters.automationName));
			capabilities.setCapability("newCommandTimeout",
					System.getProperty(CommandLineParameters.newCommandTimeout));
	        capabilities.setCapability("deviceName", System.getProperty(CommandLineParameters.deviceName));
	        capabilities.setCapability("xcodeOrgId", System.getProperty(CommandLineParameters.xcodeOrgId));
	        capabilities.setCapability("xcodeSigningId", System.getProperty(CommandLineParameters.xcodeSigningId));

			driver = new IOSDriver(new URL("http://" + ip + ":" + port + "/wd/hub"), capabilities);

		} else {
			String ip = System.getProperty("appiumServer") != null ? System.getProperty("appiumServer") : "127.0.0.1";
			String port = System.getProperty("appiumPort") != null ? System.getProperty("appiumPort") : "4723";

			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
			capabilities.setCapability("platform", System.getProperty(CommandLineParameters.platform));
			capabilities.setCapability(CapabilityType.VERSION,
					System.getProperty(CommandLineParameters.platformVersion));
			capabilities.setCapability("deviceName", System.getProperty(CommandLineParameters.deviceName));
			capabilities.setCapability("app", System.getProperty(CommandLineParameters.app));
			capabilities.setCapability("appPackage", System.getProperty(CommandLineParameters.appPackage));
			capabilities.setCapability("appActivity", System.getProperty(CommandLineParameters.appActivity));
			capabilities.setCapability("newCommandTimeout",
					System.getProperty(CommandLineParameters.newCommandTimeout));
			driver = new AndroidDriver(new URL("http://" + ip + ":" + port + "/wd/hub"), capabilities);
			// driver.manage().timeouts().implicitlyWait(3000,TimeUnit.SECONDS);
		}
		return driver;

	}
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() throws Exception {
		try {
			pageFactory.getLaunchAction().startAction();
		} catch (Exception e) {
			pageFactory = new PlayBackFactory((AppiumDriver) driver);
			pageFactory.getLaunchAction().startAction();
		}
		if (System.getProperty(CommandLineParameters.platform).equalsIgnoreCase("ios")) {
			Assert.assertTrue(
					new PlayBackFactory((AppiumDriver) driver).getQAModeSwitchAction().startAction("QA_MODE_SWITCH"),
					"QA Mode is not enabled. Hence failing test");
		} else {
			// For Android- Events will be written in the log file.
			String command = "adb push log.file /sdcard/";
			String[] final_command = CommandLine.command(command);
			Runtime run = Runtime.getRuntime();
			run.exec(final_command);
		}
	}
	
	@BeforeMethod(alwaysRun = true)
	public void handleTestMethodName(Method method, Object[] testData) {
		try {
			Field[] fs = this.getClass().getDeclaredFields();
			fs[0].setAccessible(true);
			for (Field property : fs) {
				if (property.getType().getSuperclass().isAssignableFrom(PlaybackApps.class)) {
					property.setAccessible(true);
					property.set(this, pageFactory.getObject(property.getType()));
					Method[] allMethods = property.get(this).getClass().getMethods();
					for (Method function : allMethods) {
						if (function.getName().equalsIgnoreCase("setExtentTest"))
							function.invoke(property.get(this));
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	  
	    private void parseXmlFileData(String xmlFile, String xmlFilePkg) {
	        try {
	            if (xmlFile == null || xmlFile.isEmpty()) {
	                xmlFile = getClass().getSimpleName();
	                String packagename = getClass().getPackage().getName();

	                if (packagename.contains(xmlFilePkg)) { 
	                    xmlFile = xmlFilePkg+"/" + xmlFile + ".xml";
	                }
	                else
	                    xmlFile = xmlFile;

	                logger.info("XML test data file:" + xmlFile);
	            }
	            File file = new File("src/main/resources/testdata/" + xmlFile);
	            JAXBContext jaxbContext = JAXBContext.newInstance(Testdata.class);
	            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	            testData = (Testdata) jaxbUnmarshaller.unmarshal(file);
	        } catch (Exception e) {
	            logger.error(e.getMessage());
	            logger.info(e.getMessage());
//	            if (e instanceof NullPointerException) {
//	                extentTest
//	                        .log(LogStatus.FAIL,
//	                                "The test data file should be mentioned as part of the testng parameter "
//	                                        + "or should be renamed to the name of the class using the test data!");
//	            }
	        }
	    }
	    
	    
	    @DataProvider(name = "testData")
	    public Object[][] getTestData() {

	        Map<String, TestParameters> tests =parseXmlDataProvider(testData.getApp().getName(), testData);
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
				testsGenerated.put(simpleName+"-"+data.getName(), testParam);			
			}
			return testsGenerated;
		}

}
