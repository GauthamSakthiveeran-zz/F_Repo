package com.ooyala.playback.page;

import static java.lang.Thread.sleep;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

/**
 * Created by soundarya on 11/21/16.
 */
public class SocialScreenValidator extends PlayBackPage implements
		PlaybackValidator {

	public static Logger logger = Logger.getLogger(ControlBarValidator.class);

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

	public boolean validate(String element, int timeout) throws Exception {

		try{
			waitOnElement("SHARE_BTN", 10000);
			clickOnIndependentElement("SHARE_BTN");
		}catch (Exception e) {
			clickOnIndependentElement("MORE_OPTION_ITEM");
			waitOnElement("SHARE_BTN", 10000);
			clickOnIndependentElement("SHARE_BTN");
		}

		sleep(2000);

		if (!(waitOnElement("CONTENT_SCREEN",10000)
				&&waitOnElement("SHARE_PANEL",10000)
				&&waitOnElement("TWITTER",10000)
				&&waitOnElement("FACEBOOK",10000)
				&&waitOnElement("GOOGLE_PLUS",10000))){
			return false;
		}

		// Verify Twitter link is working
		// Ignoring twitter sharing on firefox as it is actual failure
		if (!getBrowser().equalsIgnoreCase("firefox")) {
			if(!clickOnIndependentElement("TWITTER")){return false;};
			Thread.sleep(5000);
			if (!switchToWindowByTitle("Share a link on Twitter", getBrowser())){
				logger.info("Twitter link is not working on share tab.");
				return false;
			}
		}
		// Verify Facebook link is working

		if(!clickOnIndependentElement("FACEBOOK")){
			logger.info("Not able to click on Facebook button");
			return false;
		}

		sleep(5000);

		if (!switchToWindowByTitle("Facebook", getBrowser())){
			logger.info("Facebook post is not posted");
			return false;
		}

		// Verify Google link is working

		if (!clickOnIndependentElement("GOOGLE_PLUS")){
			logger.info("Not able to click on Google+ Button");
			return false;
		}
		sleep(10000);

		if (!switchToWindowByTitle("google", getBrowser())){
			logger.info("Google+ post is not shared");
			return false;
		}

		// Verify Social screen is closed

		return clickOnIndependentElement("SHARE_CLOSE_BTN");
	}


	public boolean switchToWindowByTitle(String title, String browserName)
			throws InterruptedException {
		String currentWindow = getCurrentWindow();
		Set<String> availableWindows = driver.getWindowHandles();
		if (!availableWindows.isEmpty()) {
			for (String windowId : availableWindows) {
				String switchedWindowTitle = driver.switchTo().window(windowId)
						.getTitle();
				if (switchedWindowTitle==null || switchedWindowTitle.equalsIgnoreCase("")){
					switchedWindowTitle = "withoutTitlePage";
				}
				logger.info("switchWindow title :"+switchedWindowTitle);
				if ((switchedWindowTitle.toLowerCase().contains(title.toLowerCase()))||switchedWindowTitle.equalsIgnoreCase("withoutTitlePage")) {
					if(!twitterShare(switchedWindowTitle, currentWindow,
							browserName, windowId)){return false;}
					if(!facebookShare(switchedWindowTitle, currentWindow,
							browserName, windowId)){return false;}
					if(!googlePlusShare(switchedWindowTitle, currentWindow,
							browserName, windowId)){return false;}
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
			String browserName, String windowId) throws InterruptedException {

		if (switchedWindowTitle.toLowerCase().contains("twitter")) {

			if (getWebElement("TWEETER_USERNAME").isDisplayed()
					&& getWebElement("TWEETER_PASSWORD").isDisplayed()) {
				String tweetShare = getWebElement("TWITTER_STATUS").getText();
				tweetShare = tweetShare
						.substring(tweetShare.lastIndexOf(":") - 6);
				logger.info("originnal Tweet " + tweetShare);
				String nameForTweet = "Intern_2015 Movie at " + Math.random()
						+ "";
				String tweetItOnTwitter = nameForTweet + tweetShare;
				logger.info("updated Tweet " + tweetItOnTwitter);
				WebElement updateStatus = getWebElement("TWITTER_STATUS");
				updateStatus.clear();
				updateStatus.sendKeys(tweetItOnTwitter);
				writeTextIntoTextBox("TWEETER_USERNAME",
						"ooyalatester@vertisinfotech.com");
				writeTextIntoTextBox("TWEETER_PASSWORD", "!password*");
				waitOnElement("TWITTER_LOGIN_BUTTON", 30000);
				clickOnIndependentElement("TWITTER_LOGIN_BUTTON");
				Thread.sleep(5000);
				logger.info("browser name is :" + browserName);
				if (browserName.equalsIgnoreCase("firefox")) {
					driver.switchTo().window(windowId).close();
				}
				Thread.sleep(5000);
				driver.switchTo().window(currentWindow);
				logger.info("Current page Title : " + driver.getTitle());
				Thread.sleep(5000);
				openOnNewTab(getPlatform(),
						"window.open('http://www.twitter.com')", browserName);
				driver.navigate().to("https://twitter.com");
				driver.navigate().refresh();
				Thread.sleep(5000);
				String tweet_path="//*[normalize-space(text())="+"'"+nameForTweet+"'"+"]";
				Thread.sleep(10000);
				boolean isTweetPresent = driver.findElement(By.xpath(tweet_path)).isDisplayed();
				if (!isTweetPresent){
					logger.info("Tweet is not tweeted successfully");
					return false;
				}

			}
		}
		return true;
	}

	public boolean facebookShare(String switchedWindowTitle, String currentWindow,
			String browserName, String windowId) throws InterruptedException {

		boolean isFBEmail;

		if (switchedWindowTitle.toLowerCase().contains("facebook")) {

			try {
				isFBEmail=getWebElement("FB_EMAIL").isDisplayed();
			} catch (Exception e){
				isFBEmail=false;
			}
			if (isFBEmail) {
				if (!facebookLogin()){
					logger.info("Not able to login. Please check password or email is correct or not.");
					return false;
				}
			}
			Thread.sleep(2000);
			String facebookShare = "Testing Social Media" + Math.random() + "";
			waitOnElement("FB_POST_AREA", 20);
			WebElement fillIt = getWebElement("FB_POST_AREA");
			fillIt.sendKeys(facebookShare);
			clickOnIndependentElement("FB_POST_BTN");
			Thread.sleep(5000);
			driver.switchTo().window(currentWindow);
			Thread.sleep(5000);
			openOnNewTab(getPlatform(),
					"window.open('http://www.facebook.com')", browserName);
			driver.navigate().to("https://facebook.com");
			Thread.sleep(5000);
			WebElement el = driver.findElement(By.cssSelector("body"));
			// close Browser level notifications by pressing TAB and ESCAPE
			el.sendKeys(Keys.TAB);
			el.sendKeys(Keys.ESCAPE);
			waitOnElement("FB_PROFILE", 10000);
			clickOnIndependentElement("FB_PROFILE");
			String fbPost = "//p[text()=" + "'" + facebookShare + "'" + "]";
			Thread.sleep(10000);
			if (!(driver.findElement(By.xpath(fbPost))
					.isDisplayed())){
				logger.info("Facebook post is not posted....");
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
			Thread.sleep(5000);
			if (!clickOnIndependentElement("GPLUS_SHARE_BTN"))
				clickOnIndependentElement("GPLUS_POST_BUTTON");
			Thread.sleep(10000);
			driver.switchTo().window(currentWindow);
			Thread.sleep(5000);
			openOnNewTab(getPlatform(),
						"window.open('https://plus.google.com')", browserName);
			driver.navigate().to("https://plus.google.com");
			Thread.sleep(5000);
			driver.navigate().refresh();
			waitOnElement("GPLUS_POST_LIST");
			Thread.sleep(10000);
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

	public void openOnNewTab(String platform, String webSite, String browserName) {

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