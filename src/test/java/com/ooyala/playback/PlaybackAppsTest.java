package com.ooyala.playback;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import com.ooyala.facile.test.FacileTest;
import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.utils.CommandLineParameters;
import com.ooyala.playback.factory.PlayBackFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class PlaybackAppsTest extends FacileTest {

	AppiumDriver driver;
	protected PlayBackFactory pageFactory;

	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws Exception {
		initializeDriver();

	}

	private AppiumDriver initializeDriver() throws MalformedURLException {

		if (System.getProperty(CommandLineParameters.platform).equalsIgnoreCase("ios")) {
			String ip = System.getProperty("appiumServer") != null ? System.getProperty("appiumServer") : "127.0.0.1";
			String port = System.getProperty("appiumPort") != null ? System.getProperty("appiumPort") : "4723";
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("platformVersion", System.getProperty(CommandLineParameters.platformVersion));
			capabilities.setCapability("deviceName", System.getProperty(CommandLineParameters.deviceName));
			capabilities.setCapability("app", System.getProperty(CommandLineParameters.app));
			capabilities.setCapability("udid", System.getProperty(CommandLineParameters.udid));
			capabilities.setCapability("platform", System.getProperty(CommandLineParameters.platform));
			capabilities.setCapability("showIOSLog", System.getProperty(CommandLineParameters.showIOSLog));
			capabilities.setCapability("automationName", System.getProperty(CommandLineParameters.automationName));
			capabilities.setCapability("newCommandTimeout",
					System.getProperty(CommandLineParameters.newCommandTimeout));

			driver = new IOSDriver(new URL("http://" + ip + ":" + port + "/wd/hub"), capabilities);
			return driver;

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
			return driver;
		}

	}
	
	@BeforeMethod(alwaysRun=true)
    public void beforeMethod() throws Exception {
    	Assert.assertTrue(new PlayBackFactory(driver).getQAModeSwitchAction().startAction(""), "QA Mode is not enabled. Hence failing test");
    }
	
	  @BeforeMethod(alwaysRun = true)
	    public void handleTestMethodName(Method method, Object[] testData) {
	        try {
	            Field[] fs = this.getClass().getDeclaredFields();
	            fs[0].setAccessible(true);
	            for (Field property : fs) {
	                if (property.getType().getSuperclass()
	                        .isAssignableFrom(PlaybackApps.class)) {
	                    property.setAccessible(true);
	                    property.set(this,
	                            pageFactory.getObject(property.getType()));
	                    Method[] allMethods = property.get(this).getClass()
	                            .getMethods();
	                    for (Method function : allMethods) {
	                        if (function.getName()
	                                .equalsIgnoreCase("setExtentTest"))
	                            function.invoke(property.get(this));
	                    }
	                }
	            }
	        } catch (Exception e) {
	            logger.error(e.getMessage());
	        }
	    }

}
