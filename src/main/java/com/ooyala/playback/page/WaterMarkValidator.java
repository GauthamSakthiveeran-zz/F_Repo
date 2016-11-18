package com.ooyala.playback.page;

import com.ooyala.playback.page.BaseValidator;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.Set;

/**
 * Created by soundarya on 11/8/16.
 */
public class WaterMarkValidator  extends BaseValidator {

    public static Logger Log = Logger.getLogger(WaterMarkValidator.class);

    public WaterMarkValidator(WebDriver webDriver){
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        /**
         * Here we will tell Facile to add the page elements of our Login Page
         */
        addElementToPageElements("watermark");
    }

    @Override
    public void validate(String element,int timeout)throws Exception {
        String watermark_url = "https://dl.dropboxusercontent.com/u/344342926/Alice_Automation/fcb_wallpaper.jpg";
        Actions action = new Actions(driver);

        WebElement control_bar = getWebElement("CONTROL_BAR_ITEM");

        if(!(getBrowser().equalsIgnoreCase("safari")||getPlatform().equalsIgnoreCase("Android"))) {
            action.moveToElement(control_bar).build().perform();
        }

        waitOnElement("WATERMARK_LOGO", 60);
        Log.info("Watermark Image is displayed");
        Thread.sleep(10000);
        String ima_url = getWebElement("WATERMARK_LOGO").getAttribute("src");
        Assert.assertEquals(watermark_url, ima_url);
        Log.info("Watermark url is matched");
        Thread.sleep(10000);
        getlogoDimension();

        //Checking height and width in fullscreen
        if(!getBrowser().equalsIgnoreCase("safari")) {
            clickOnIndependentElement("FULLSCREEN_BTN");
            Assert.assertEquals(watermark_url, ima_url);
            getlogoDimension();
            waitOnElement("NORMAL_SCREEN", 10);
            clickOnIndependentElement("NORMAL_SCREEN");
        }

        getWindowHanldes();

    }

    protected void getlogoDimension() throws Exception{
        String width = getWebElement("WATERMARK_LOGO").getAttribute("width");
        String height = getWebElement("WATERMARK_LOGO").getAttribute("height");
        Log.info("Image width & height " + width+ " "+ height);
        Assert.assertEquals(width, "16", "Width matches ");
        Assert.assertEquals(height, "10", "Height matches ");
        Log.info("Height and width are matched ");
    }

    protected void getWindowHanldes() throws Exception
    {
        String oldTab = driver.getWindowHandle();
        clickOnIndependentElement("OOYALA_LOGO");
        Set<String> allWindows= driver.getWindowHandles();
        for(String aWindow:allWindows)
        {
            driver.switchTo().window(aWindow);
            boolean isTitleContains = driver.getTitle().contains("Ooyala | Deliver Content that Connects");
            Log.info("TitleContains :"+isTitleContains);

            if(driver.getTitle().contains("Ooyala | Deliver Content that Connects"))
            {
                Log.info("We are on click through page");
                break;
            }
        }

        Set<String> windowHandles = driver.getWindowHandles();
        int count = windowHandles.size();
        Log.info("Window opened :" + count);

        if(count > 1) {
            for (String winHandle : driver.getWindowHandles()) {
                driver.switchTo().window(winHandle);
            }
            driver.close();
            driver.switchTo().window(oldTab);
        }
    }
}
