package com.ooyala.playback.page;

import static java.lang.Thread.sleep;
import static org.testng.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

		try {
			waitOnElement("SHARE_BTN", 10);
			clickOnIndependentElement("SHARE_BTN");
		} catch (Exception e) {
			clickOnIndependentElement("MORE_OPTION_ITEM");
			waitOnElement("SHARE_BTN", 10);
			clickOnIndependentElement("SHARE_BTN");
		}

		sleep(2000);

		if (!isElementPresent("SHARE_TAB"))
			clickOnIndependentElement("SHARE_BTN");
		sleep(2000);

		assertEquals(isElementPresent("CONTENT_SCREEN"), true,
				"Social screen is not showing.");

		assertEquals(isElementPresent("SHARE_PANEL"), true,
				"Share tab is not display as default on Social screen.");

		assertEquals(isElementPresent("TWITTER"), true,
				"Social screen is not showing.");

		assertEquals(isElementPresent("FACEBOOK"), true,
				"Social screen is not showing.");

		assertEquals(isElementPresent("GOOGLE_PLUS"), true,
				"Social screen is not showing.");

        // Verify Twitter link is working
        clickOnIndependentElement("TWITTER");
        Thread.sleep(5000);
        Assert.assertEquals(switchToWindowByTitle("Share a link on Twitter",getBrowser()), true, "Twitter link is not working on share tab.");
        // Verify Facebook link is working
        clickOnIndependentElement("FACEBOOK");
        sleep(5000);
        assertEquals(switchToWindowByTitle("Facebook", getBrowser()), true, "Facebook link is not working on share tab.");
        // Verify Google link is working
        clickOnIndependentElement("GOOGLE_PLUS");
        sleep(10000);
        assertEquals(switchToWindowByTitle("Google+", getBrowser()), true, "GooglePlus link is not working on share tab.");
        // Verify Social screen is closed
        return clickOnIndependentElement("SHARE_CLOSE_BTN");
    }

    public boolean switchToWindowByTitle(String title,String browserName) throws InterruptedException {
        String currentWindow = getCurrentWindow();
        Set<String> availableWindows = driver.getWindowHandles();
        if (!availableWindows.isEmpty()) {
            for (String windowId : availableWindows) {
                String switchedWindowTitle=driver.switchTo().window(windowId).getTitle();
                if ((switchedWindowTitle.equals(title))){
                    twitterShare(switchedWindowTitle,currentWindow,browserName,windowId);
                    facebookShare(switchedWindowTitle,currentWindow,browserName,windowId);
                    googlePlusShare(switchedWindowTitle,currentWindow,browserName,windowId);
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



    public void twitterShare(String switchedWindowTitle,String currentWindow,String browserName,String windowId) throws InterruptedException {

        if(switchedWindowTitle.equals("Share a link on Twitter"))
        {
            if(getWebElement("TWEETER_USERNAME").isDisplayed()&&getWebElement("TWEETER_PASSWORD").isDisplayed())
            {
                String tweetShare=getWebElement("TWITTER_STATUS").getText();
                tweetShare=tweetShare.substring(tweetShare.lastIndexOf(":")-6);
                logger.info("originnal Tweet "+tweetShare);
                String nameForTweet="Intern_2015 Movie at "+Math.random()+"";
                String tweetItOnTwitter=nameForTweet + tweetShare;
                logger.info("updated Tweet "+tweetItOnTwitter);
                WebElement updateStatus=getWebElement("TWITTER_STATUS");
                updateStatus.clear();
                updateStatus.sendKeys(tweetItOnTwitter);
                writeTextIntoTextBox("TWEETER_USERNAME","ooyalatester@vertisinfotech.com");
                writeTextIntoTextBox("TWEETER_PASSWORD","!password*");
                waitOnElement("TWITTER_LOGIN_BUTTON",30);
                clickOnHiddenElement("TWITTER_LOGIN_BUTTON");
                Thread.sleep(5000);
                logger.info("browser name is :"+browserName);
                if(browserName.equalsIgnoreCase("firefox"))
                {
                    driver.switchTo().window(windowId).close();
                }
                Thread.sleep(5000);
                driver.switchTo().window(currentWindow);
                logger.info("Current page Title : "+driver.getTitle());
                Thread.sleep(5000);

                openOnNewTab(getPlatform(),"window.open('http://www.twitter.com')",browserName);

                driver.navigate().to("https://twitter.com");
                driver.navigate().refresh();

                waitOnElement("TWEET_TEXT",60);
                Thread.sleep(5000);
                String tweet=getWebElement("TWEET_TEXT").getText();
                for(int i=1;i<=10;i++){
                    if(tweet.matches(".*\\b" + nameForTweet + "\\b.*"))
                    {
                        logger.info("Comparing strings");
                        break;
                    }
                    tweet=driver.findElement(By.xpath(".//*[@id='stream-items-id']/li["+i+"]/div/div[2]/div[2]/p")).getText();
                }

                logger.info("tweet :"+tweet);

                Assert.assertTrue(tweet.matches(".*\\b" + nameForTweet + "\\b.*"), "Tweet is not posted on twitter");
                //driver.switchTo().window(tabs.get(1));

            }

            else {
                getWebElement("TWEET_BUTTON").submit();
                Assert.assertTrue(getWebElement("TWEET_POST_ERROR").isDisplayed(),"Twitter post is not updated");
                logger.info("Tweet is not posted");
            }
        }

    }

    public void facebookShare(String switchedWindowTitle,String currentWindow,String browserName,String windowId) throws InterruptedException {

        if(switchedWindowTitle.equals("Facebook"))
        {
            if(getWebElement("FB_EMAIL").isDisplayed()&&getWebElement("FB_PASSWORD").isDisplayed())
            {
                writeTextIntoTextBox("FB_EMAIL","ooyalatester@vertisinfotech.com");
                writeTextIntoTextBox("FB_PASSWORD","!password*");
                getWebElement("FB_LOGIN_BTN").submit();
                Thread.sleep(2000);
                String facebookShare="Testing Social Media"+Math.random()+"";
                waitOnElement("FB_POST_AREA",20);
                WebElement fillIt=getWebElement("FB_POST_AREA");
                fillIt.sendKeys(facebookShare);
                clickOnIndependentElement("FB_POST_BTN");
                Thread.sleep(5000);
                driver.switchTo().window(currentWindow);
                Thread.sleep(5000);

                openOnNewTab(getPlatform(),"window.open('http://www.facebook.com')",browserName);

                driver.navigate().to("https://facebook.com");
                Thread.sleep(5000);
                WebElement el = driver.findElement(By.cssSelector("body"));
                // close Browser level notifications by pressing TAB and ESCAPE
                el.sendKeys(Keys.TAB);
                el.sendKeys(Keys.ESCAPE);
                waitOnElement("FB_PROFILE",10);
                clickOnIndependentElement("FB_PROFILE");
                String fbPost = "//p[text()="+"'"+facebookShare+"'"+"]";
                boolean fbPostPresnet = driver.findElement(By.xpath(fbPost)).isDisplayed();
                if (fbPostPresnet){
                    logger.info("Facebook Post : "+driver.findElement(By.xpath(fbPost)).getText());
                    logger.info("Facebook post is posted successfully");
                }else{
                logger.error("Facebook post is NOT posted successfully");
            }
                Assert.assertEquals(driver.findElement(By.xpath(fbPost)).isDisplayed(),true,"Facebook Post is not posted");

            }

        }

    }

    public void googlePlusShare(String switchedWindowTitle,String currentWindow,String browserName,String windowId) throws InterruptedException {

        if(switchedWindowTitle.equals("Google+"))
        {
            if(getWebElement("GPLUS_EMAIL").isDisplayed())
            {
                writeTextIntoTextBox("GPLUS_EMAIL","ooyalatester@forgeahead.io");
                getWebElement("GPLUS_NXT_BTN").submit();
                waitOnElement("GPLUS_PASSWORD",10);
                writeTextIntoTextBox("GPLUS_PASSWORD","!password*");

                getWebElement("GPLUS_SIGNIN_BTN").submit();
                waitOnElement("GPLUS_TEXTAREA",20);
                double random_number=Math.random();
                String title_for_sharing_asset="Sharing this post on Google+"+random_number+"";
                getWebElement("GPLUS_TEXTAREA").sendKeys(title_for_sharing_asset);
                Thread.sleep(5000);
                clickOnIndependentElement("GPLUS_SHARE_BTN");
                Thread.sleep(5000);
                driver.switchTo().window(currentWindow);
                Thread.sleep(5000);

                openOnNewTab(getPlatform(),"window.open('https://plus.google.com')",browserName);

                driver.navigate().to("https://plus.google.com");
                Thread.sleep(5000);
                driver.navigate().refresh();
                waitOnElement("GPLUS_POST_LIST");
                Thread.sleep(10000);
                logger.info("title_for_sharing_asset :"+title_for_sharing_asset);
                List<WebElement> AllElements = getWebElementsList("GPLUS_POST_LIST");
                String assetName = "";
                int flag = 1;
                for (int i=0; i<AllElements.size(); i++){
                    assetName = AllElements.get(i).getText();
                    if(assetName.equalsIgnoreCase(title_for_sharing_asset)){
                        logger.info("Asset shared on G+ is : "+assetName);
                        flag=2;
                        break;
                    }
                }
                Assert.assertEquals(flag,2, "Asset is not shared on Google+");
            }

        }
    }

    public void openOnNewTab(String platform,String webSite,String browserName){

        if(platform.equalsIgnoreCase("Mac")) {
            clickOnIndependentElement("SHARE_CLOSE_BTN");
            ((JavascriptExecutor) driver).executeScript(webSite);
            ArrayList<String> multipleTabs = new ArrayList<String>(driver.getWindowHandles());
            logger.info("total tabs opened : "+ multipleTabs.size());
            clickOnIndependentElement("SHARE_BTN");

        }
        else {
            ((JavascriptExecutor) driver).executeScript(webSite);
            ArrayList<String> multipleTabs = new ArrayList<String>(driver.getWindowHandles());
            logger.info("total tabs opened : "+ multipleTabs.size());
        }

        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        if(browserName.equalsIgnoreCase("firefox")) {
            driver.switchTo().window(tabs.get(0));
        }
        else
            driver.switchTo().window(tabs.get(1));

    }
}