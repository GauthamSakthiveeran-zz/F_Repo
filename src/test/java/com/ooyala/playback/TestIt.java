package com.ooyala.playback;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by suraj on 5/23/17.
 */
public class TestIt {

    @Test
    public void init() throws Exception{
        RemoteWebDriver driver;
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, System.getProperty("deviceName"));
        //capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,"9.3");
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
        capabilities.setCapability(MobileCapabilityType.UDID,System.getProperty("udid"));
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
        String ip = System.getProperty("ipaddress");
        driver = new RemoteWebDriver(new URL("http://"+ip+"/wd/hub"),
                capabilities);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get("https://goo.gl/2XjnYT");
        Thread.sleep(15000);
        driver.findElement(By.className("oo-icon-play-slick")).click();
    }

}
