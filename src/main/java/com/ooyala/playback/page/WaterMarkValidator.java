package com.ooyala.playback.page;

import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/8/16.
 */
public class WaterMarkValidator extends PlayBackPage implements
		PlaybackValidator {

	public static Logger Log = Logger.getLogger(WaterMarkValidator.class);

	public WaterMarkValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("watermark");
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
        String watermark_url = "https://dl.dropbox.com/s/kdo5hlgg2i3ptv0/ooyala-logo.png";

		boolean flag = true;

		Actions action = new Actions(driver);

		WebElement control_bar = getWebElement("CONTROL_BAR_ITEM");
		
		if(control_bar==null) return false;

		if (!(getBrowser().equalsIgnoreCase("safari") || getPlatform()
				.equalsIgnoreCase("Android"))) {
			action.moveToElement(control_bar).build().perform();
		}

		if(!waitOnElement("WATERMARK_LOGO", 60000)) return false;
		Log.info("Watermark Image is displayed");
		Thread.sleep(10000);

		String img_url = getWebElement("WATERMARK_LOGO").getAttribute("src");
		
		if(!watermark_url.equals(img_url)){
			extentTest.log(LogStatus.FAIL, "Watermark url is matched");
			flag = false;
		}

		Thread.sleep(10000);
		flag  = validatelogoDimension();

		// Checking height and width in fullscreen
		if (!getBrowser().equalsIgnoreCase("safari")) {

			if(!clickOnIndependentElement("FULLSCREEN_BTN")) return false;
			
			if(!watermark_url.equals(img_url)){
				extentTest.log(LogStatus.FAIL, "Watermark url is matched in full screen");
				flag = false;
			}
			
			flag = validatelogoDimension();
			if(!waitOnElement("NORMAL_SCREEN", 10000)) return false;
			if(!clickOnIndependentElement("NORMAL_SCREEN")) return false;

		}

		return flag || getWindowHanldes(); // as flag = true or false does not affect the excution of getWindowHandles.
	}

	protected boolean validatelogoDimension() throws Exception {
		boolean flag = true;
		String width = getWebElement("WATERMARK_LOGO").getAttribute("width");
		String height = getWebElement("WATERMARK_LOGO").getAttribute("height");

		Log.info("Image width & height " + width + " " + height);
		
		if(!width.equals("16")){
			flag = false;
			extentTest.log(LogStatus.FAIL, "Logo dimension - width mismatched.");
		}
		
		if(!height.equals("10")){
			flag = false;
			extentTest.log(LogStatus.FAIL, "Logo dimension - height mismatched.");
		}
		
		return flag;
	}

	protected boolean getWindowHanldes() throws Exception {
		String oldTab = driver.getWindowHandle();

		if(!clickOnIndependentElement("OOYALA_LOGO")) return false;

		Set<String> allWindows = driver.getWindowHandles();
		for (String aWindow : allWindows) {
			driver.switchTo().window(aWindow);
			boolean isTitleContains = driver.getTitle().contains(
					"Ooyala | Deliver Content that Connects");
			Log.info("TitleContains :" + isTitleContains);

			if (driver.getTitle().contains(
					"Ooyala | Deliver Content that Connects")) {
				Log.info("We are on click through page");
				break;
			}
		}

		Set<String> windowHandles = driver.getWindowHandles();
		int count = windowHandles.size();
		Log.info("Window opened :" + count);

		if (count > 1) {
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
			}
			driver.close();
			driver.switchTo().window(oldTab);
		}
		return true;
	}
}
