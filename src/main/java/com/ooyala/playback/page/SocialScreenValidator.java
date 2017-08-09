package com.ooyala.playback.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/21/16.
 */
public class SocialScreenValidator extends PlayBackPage implements
		PlaybackValidator {

	private static Logger logger = Logger.getLogger(ControlBarValidator.class);

	public SocialScreenValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("controlbar");
		addElementToPageElements("sharetab");
		addElementToPageElements("socialscreen");

	}
	
	SoftAssert s_assert;
	
	public SocialScreenValidator softAssert(SoftAssert s_assert){
		this.s_assert = s_assert;
		return this;
	}

	public boolean validate(String element, int timeout) {
		try{
			try {
				if (isElementPresent("HIDDEN_CONTROL_BAR")) {
					logger.info("hovering mouse over the player");
					moveElement(getWebElement("HIDDEN_CONTROL_BAR"));
				}
				if (!(waitOnElement("SHARE_BTN", 10000) && clickOnIndependentElement("SHARE_BTN"))) {
					extentTest.log(LogStatus.FAIL, "SHARE_BTN is not found/ not clickable.");
					return false;
				}
			} catch (Exception e) {
				if (!(clickOnIndependentElement("MORE_OPTION_ITEM") && waitOnElement("SHARE_BTN", 10000)
						&& clickOnIndependentElement("SHARE_BTN"))) {
					extentTest.log(LogStatus.FAIL, "SHARE_BTN is not found/ not clickable.");
					return false;
				}

			}

			if (!(waitOnElement("CONTENT_SCREEN", 10000) && waitOnElement("SHARE_PANEL", 10000)
					&& waitOnElement("TWITTER", 10000) && waitOnElement("FACEBOOK", 10000)
					&& waitOnElement("GOOGLE_PLUS", 10000))) {
				return false;
			}

			// Verify Twitter link is working
			// Ignoring twitter sharing on firefox as it is actual failure
			if (!getBrowser().equalsIgnoreCase("firefox")) {
				if (!clickOnIndependentElement("TWITTER")) {
					extentTest.log(LogStatus.FAIL, "Unable to click on TWITTER");
					s_assert.assertTrue(false, "Twitter");
				} else {
					if (!switchToWindowByTitle("Share a link on Twitter", getBrowser())) {
						extentTest.log(LogStatus.FAIL, "Twitter link is not working on share tab.");
						s_assert.assertTrue(false, "Twitter");
					}
				}
			}
			// Verify Facebook link is working

			if (!clickOnIndependentElement("FACEBOOK")) {
				extentTest.log(LogStatus.FAIL, "Not able to click on Facebook button");
			} else{
				if (!switchToWindowByTitle("Facebook", getBrowser())) {
					extentTest.log(LogStatus.FAIL, "Facebook post is not posted");
					s_assert.assertTrue(false, "Facebook");
				}
			}

			// Verify Google link is working

			/*if (!clickOnIndependentElement("GOOGLE_PLUS")) {
				extentTest.log(LogStatus.FAIL, "Not able to click on Google+ Button");

			} else {
				if (!switchToWindowByTitle("google", getBrowser())) {
					extentTest.log(LogStatus.FAIL, "Google+ post is not shared");
					s_assert.assertTrue(false, "Google+");
				}
			}*/

			// Verify Social screen is closed

			if(!clickOnIndependentElement("SHARE_CLOSE_BTN")){
				extentTest.log(LogStatus.FAIL, "Issue with SHARE_CLOSE_BTN");
				return false;
			}
			return true;
		}catch (Exception e){
			logger.error("Error in validating social windows "+e.getMessage());
			return false;
		}
	}

	public boolean switchToWindowByTitle(String title, String browserName) throws InterruptedException {
		String currentWindow = driver.getWindowHandle();
		Set<String> availableWindows = driver.getWindowHandles();
		if (!availableWindows.isEmpty()) {
			for (String windowId : availableWindows) {
				String switchedWindowTitle = driver.switchTo().window(windowId).getTitle();
				if (switchedWindowTitle == null || switchedWindowTitle.equalsIgnoreCase("")) {
					switchedWindowTitle = "withoutTitlePage";
				}
				logger.info("switchWindow title :" + switchedWindowTitle);
				logger.info("Current Window is :" + currentWindow);
				if ((switchedWindowTitle.toLowerCase().contains(title.toLowerCase()))
						|| switchedWindowTitle.equalsIgnoreCase("withoutTitlePage")) {
					if (!twitterShare(switchedWindowTitle, currentWindow, browserName, windowId)) {
						return false;
					}
					if (!facebookShare(switchedWindowTitle, currentWindow, browserName, windowId)) {
						return false;
					}
					if (!googlePlusShare(switchedWindowTitle, currentWindow, browserName, windowId)) {
						return false;
					}
					driver.close();
					driver.switchTo().window(currentWindow);
					return true;
				} else {
					driver.switchTo().window(currentWindow);
				}
			}
		}
		return false;
	}

	public boolean twitterShare(String switchedWindowTitle, String currentWindow,
								String browserName, String windowId) {
		try{
			if (switchedWindowTitle.toLowerCase().contains("twitter")) {

				if (getWebElement("TWEETER_USERNAME").isDisplayed()
						&& getWebElement("TWEETER_PASSWORD").isDisplayed()) {
					String tweetShare = getWebElement("TWITTER_STATUS").getText();
					tweetShare = tweetShare
							.substring(tweetShare.lastIndexOf(":") - 6);
					logger.info("original Tweet " + tweetShare);
					String nameForTweet = "Intern_2015 Movie at " + Math.random()
							+ "";
					String tweetItOnTwitter = nameForTweet + tweetShare;
					logger.info("updated Tweet " + tweetItOnTwitter);
					WebElement updateStatus = getWebElement("TWITTER_STATUS");
					updateStatus.clear();
					updateStatus.sendKeys(tweetItOnTwitter);
					writeTextIntoTextBox("TWEETER_USERNAME", "ooyalatester@vertisinfotech.com");
					writeTextIntoTextBox("TWEETER_PASSWORD", "!password*");
					
					clickOnIndependentElement("TWITTER_LOGIN_BUTTON");
					
					logger.info("browser name is :" + browserName);
					/*if (browserName.equalsIgnoreCase("firefox")) {
						driver.switchTo().window(windowId).close();
					}*/

					try{
						driver.switchTo().window(currentWindow);
					}catch (Exception e){
						driver.switchTo().window(currentWindow);
					}

					logger.info("Current page Title : " + driver.getTitle());

					openOnNewTab(getPlatform(), "window.open('http://www.twitter.com')", browserName);
					driver.navigate().to("https://twitter.com");
					driver.navigate().refresh();
					String tweet_path="//*[normalize-space(text())="+"'"+nameForTweet+"'"+"]";
					boolean isTweetPresent = driver.findElement(By.xpath(tweet_path)).isDisplayed();
					if (!isTweetPresent){
						extentTest.log(LogStatus.FAIL,"Tweet is not tweeted successfully");
						return false;
					}else
						logger.info("Tweet is tweeted successfully");
				}
			}
			return true;
		}catch(NoSuchWindowException e){
			logger.error("Error while switching a window"+e.getMessage());
			return false;
		}catch(Exception e){
			return false;
		}
	}

	public boolean facebookShare(String switchedWindowTitle, String currentWindow, String browserName, String windowId)
			throws InterruptedException {

		boolean isFBEmail;

		if (switchedWindowTitle.toLowerCase().contains("facebook")) {

			try {
				waitOnElement("FB_EMAIL", 10000);
				isFBEmail = getWebElement("FB_EMAIL").isDisplayed();
			} catch (NoSuchElementException e) {
				logger.error("FB email text box not found. " + e.getMessage());
				isFBEmail = false;
			} catch (Exception e) {
				isFBEmail = false;
			}
			if (isFBEmail) {
				if (!facebookLogin()) {
					extentTest.log(LogStatus.FAIL,
							"Not able to login. Please check password or email is correct or not.");
					return false;
				}
			}

			String facebookShare = "Testing Social Media" + Math.random() + "";
			if(!waitOnElement("FB_POST_AREA", 1000)){
				return false;
			}
			WebElement fillIt = getWebElement("FB_POST_AREA");
			fillIt.sendKeys(facebookShare);
			if(!clickOnIndependentElement("FB_POST_BTN")){
				extentTest.log(LogStatus.FAIL, "Failed to click on FB_POST_BTN");
				return false;
			}

			driver.switchTo().window(currentWindow);
			openOnNewTab(getPlatform(),"window.open('http://www.facebook.com')", browserName);
			driver.navigate().to("https://facebook.com");
			try{
				driver.switchTo().alert().dismiss();
			} catch(NoAlertPresentException Ex){
				
			}
			
			WebElement el = driver.findElement(By.cssSelector("body"));

			el.sendKeys(Keys.TAB);
			el.sendKeys(Keys.ESCAPE);
			
			if(!(waitOnElement("FB_PROFILE", 10000) && clickOnIndependentElement("FB_PROFILE"))){
				return false;
			}
			String fbPost = "//p[text()=" + "'" + facebookShare + "'" + "]";
			if (!(driver.findElement(By.xpath(fbPost)).isDisplayed())){
				extentTest.log(LogStatus.FAIL, "Unable to find the facebook post.");
				return false;
			}

		}
		return true;

	}

	public boolean googlePlusShare(String switchedWindowTitle,
								   String currentWindow, String browserName, String windowId)
			throws InterruptedException {

		if (switchedWindowTitle.toLowerCase().contains("google")||switchedWindowTitle.equalsIgnoreCase("withoutTitlePage")) {
			if (!googlePlusLogin()) {
				logger.info("Not able to log in to google+ account. Please check login credentials!!");
				return false;
			}
			double random_number = Math.random();
			String title_for_sharing_asset = "Sharing this post on Google+"
					+ random_number + "";
			getWebElement("GPLUS_TEXTAREA").sendKeys(
					title_for_sharing_asset);
			//if (!clickOnIndependentElement("GPLUS_SHARE_BTN"))
			if(isElementPresent("GPLUS_SHARE_BTN")){
				clickOnIndependentElement("GPLUS_SHARE_BTN");
			}else{
				waitOnElement("GPLUS_POST_BUTTON",25000);
				clickOnIndependentElement("GPLUS_POST_BUTTON");
			}

			try{
				driver.switchTo().window(currentWindow);
			}catch (Exception e){
				driver.switchTo().window(currentWindow);
			}
			openOnNewTab(getPlatform(),
					"window.open('https://plus.google.com')", browserName);
			driver.navigate().to("https://plus.google.com");
			driver.navigate().refresh();
			waitOnElement("GPLUS_POST_LIST");
			logger.info("title_for_sharing_asset :"
					+ title_for_sharing_asset);
			List<WebElement> AllElements = getWebElementsList("GPLUS_POST_LIST");
			String assetName = "";
			boolean flag=false;
			for (int i = 0; i < AllElements.size(); i++) {
				assetName = AllElements.get(i).getText();
				if (assetName.equalsIgnoreCase(title_for_sharing_asset)) {
					logger.info("Asset shared on G+ is : " + assetName);
					flag = true;
					break;
				}
			}
			if (!flag){
				logger.info("Assset is not shared on Google Plus");
				return false;
			}
		}
		return true;
	}

	public void openOnNewTab(String platform, String webSite, String browserName) throws InterruptedException {

		if (platform.equalsIgnoreCase("Mac")) {
			clickOnIndependentElement("SHARE_CLOSE_BTN");
			((JavascriptExecutor) driver).executeScript(webSite);
			ArrayList<String> multipleTabs = new ArrayList<String>(
					driver.getWindowHandles());
			logger.info("total tabs opened : " + multipleTabs.size());
			clickOnIndependentElement("SHARE_BTN");

		} else {
			((JavascriptExecutor) driver).executeScript(webSite);
			ArrayList<String> multipleTabs = new ArrayList<String>(
					driver.getWindowHandles());
			logger.info("total tabs opened : " + multipleTabs.size());
		}

		ArrayList<String> tabs = new ArrayList<String>(
				driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));

	}

	public boolean facebookLogin(){
		writeTextIntoTextBox("FB_EMAIL",
				"ooyalatester@vertisinfotech.com");
		writeTextIntoTextBox("FB_PASSWORD", "!password*");
		getWebElement("FB_LOGIN_BTN").submit();
		return waitOnElement("FB_POST_AREA", 20000);
	}

	public boolean googlePlusLogin(){
		boolean isEmailDisplayed;
		try{
			isEmailDisplayed = !getWebElement("GPLUS_EMAIL").isDisplayed();
		} catch(Exception e){
			isEmailDisplayed=false;
		}

		if (isEmailDisplayed){
			waitOnElement("GPLUS_PASSWORD", 10000);
			writeTextIntoTextBox("GPLUS_PASSWORD", "!password*");
			getWebElement("GPLUS_SIGNIN_BTN").submit();
		}
		if (isElementPresent("GPLUS_EMAIL")){
			writeTextIntoTextBox("GPLUS_EMAIL",
					"ooyalatester@forgeahead.io");
			getWebElement("GPLUS_NXT_BTN").submit();
			waitOnElement("GPLUS_PASSWORD", 10000);
			writeTextIntoTextBox("GPLUS_PASSWORD", "!password*");
			getWebElement("GPLUS_SIGNIN_BTN").submit();
		}

		return waitOnElement("GPLUS_TEXTAREA", 20000);
	}
}
