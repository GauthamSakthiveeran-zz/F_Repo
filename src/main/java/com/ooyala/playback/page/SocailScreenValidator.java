package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.System.out;
import static java.lang.Thread.sleep;
import static org.testng.Assert.assertEquals;

/**
 * Created by soundarya on 11/21/16.
 */
public class SocailScreenValidator  extends PlayBackPage implements
        PlaybackValidator {

    public static Logger logger = Logger.getLogger(ControlBarValidator.class);

    public SocailScreenValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        /**
         * Here we will tell Facile to add the page elements of our Login Page
         */
        addElementToPageElements("controlbar");

    }

    public void validate(String element, int timeout) throws Exception {

        {
            //Verify Social screen is display
            try {
                waitOnElement("shareBtn", 60);
                clickOnIndependentElement("shareBtn");
            } catch (Exception e) {
                clickOnIndependentElement("moreOptionIcon");
                waitOnElement("shareBtn", 60);
                clickOnIndependentElement("shareBtn");
            }
            sleep(2000);
            boolean issharetab = isElementPresent("shareTab"); //Added code for firefox
            if (!issharetab)
                clickOnIndependentElement("shareBtn");
            sleep(2000);
            boolean isSocialScreenpresent = isElementPresent("contentScreen");
            assertEquals(isSocialScreenpresent, true, "Social screen is not showing.");

            // Verify by default Share tab is display on Social screen
            boolean isShareTabpresent = isElementPresent("sharePanel");
            assertEquals(isShareTabpresent, true, "Share tab is not display as default on Social screen.");

            // Verify Twitter, Facebook and Google plus on Share screen
            boolean istwitterpresent = isElementPresent("twitter");
            assertEquals(istwitterpresent, true, "Social screen is not showing.");
            boolean isfacebookpresent = isElementPresent("facebook");
            assertEquals(isfacebookpresent, true, "Social screen is not showing.");
            boolean isSgooglepresent = isElementPresent("googlePlus");
            assertEquals(isSgooglepresent, true, "Social screen is not showing.");


            // Verify Twitter link is working
            clickOnIndependentElement("oo-twitter");
            // webDriver.findElement(By.className("oo-twitter")).click();
            Thread.sleep(5000);
            Assert.assertEquals(switchToWindowByTitle("Share a link on Twitter"), true, "Twitter link is not working on share tab.");

            // Verify Facebook link is working
            clickOnIndependentElement("facebook");

            sleep(5000);
            assertEquals(switchToWindowByTitle("Facebook"), true, "Facebook link is not working on share tab.");

            clickOnIndependentElement("googlePlus");
            // Verify Google link is working
            sleep(10000);
            assertEquals(switchToWindowByTitle("Google+"), true, "GooglePlus link is not working on share tab.");


            // Verify Social screen is closed
            clickOnIndependentElement("shareClose");
            sleep(2000);
            boolean isSocialScreenpresent1 = isElementPresent("contentScreen");
            assertEquals(isSocialScreenpresent1, false, "Social screen is not showing.");

        }
    }

    public boolean switchToWindowByTitle(String title) throws Exception {
        String currentWindow = driver.getWindowHandle();
        System.out.println("currentWindow is :" + currentWindow);
        Set<String> availableWindows = driver.getWindowHandles();
        if (!availableWindows.isEmpty()) {
            for (String windowId : availableWindows) {
                String switchedWindowTitle = driver.switchTo().window(windowId).getTitle();
                if ((switchedWindowTitle.equals(title))) {
                    if (switchedWindowTitle.equals("Share a link on Twitter")) {
                    validateTwitterShare(currentWindow,windowId);
                    }
                    if (switchedWindowTitle.equals("Facebook")) {
                       validateFacebookShare(currentWindow);
                    }
                    if (switchedWindowTitle.equals("Google+")) {
                        validateGooglePlusShare(currentWindow);
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


    public void validateFacebookShare(String currentWindow ) throws Exception {
            if (driver.findElement(By.id("email")).isDisplayed() && driver.findElement(By.id("pass")).isDisplayed()) {
                driver.findElement(By.id("email")).sendKeys("ooyalatester@vertisinfotech.com");
                driver.findElement(By.id("pass")).sendKeys("!password*");
                driver.findElement(By.name("login")).submit();

                Thread.sleep(2000);
                String facebookShare = "Testing Social Media" + Math.random() + "";

                waitOnElement("mentionsTextarea",20);//classname

                WebElement fillIt = getWebElement("mentionsTextarea");//classname
                fillIt.sendKeys(facebookShare);
                Assert.assertTrue((driver.findElement(By.id("u_0_9")).isDisplayed()), "Facebook post is not updated");

                String share = driver.findElement(By.id("u_0_9")).getText();
                System.out.println("share :" + share);
                driver.findElement(By.id("u_0_9")).submit();

                Thread.sleep(5000);
                driver.switchTo().window(currentWindow);
                Thread.sleep(5000);
                if (getPlatform().equalsIgnoreCase("Mac")) {
                    clickOnIndependentElement("shareClose");
                    ((JavascriptExecutor) driver).executeScript("window.open('http://www.facebook.com')");
                    ArrayList<String> multipleTabs = new ArrayList<String>(driver.getWindowHandles());
                    System.out.println("total tabs opened : " + multipleTabs.size());
                    clickOnIndependentElement("shareBtn");

                } else {
                    ((JavascriptExecutor) driver).executeScript("window.open('http://www.facebook.com')");
                    ArrayList<String> multipleTabs = new ArrayList<String>(driver.getWindowHandles());
                    System.out.println("total tabs opened : " + multipleTabs.size());
                }
                ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                if (getBrowser().equalsIgnoreCase("firefox")) {
                    driver.switchTo().window(tabs.get(0));
                } else
                    driver.switchTo().window(tabs.get(1));

                driver.navigate().to("https://facebook.com");
                Thread.sleep(5000);
                WebElement el = driver.findElement(By.cssSelector("body"));
                el.sendKeys(Keys.TAB);
                el.sendKeys(Keys.ESCAPE);
                Thread.sleep(10000);
            }
    }

    public void validateGooglePlusShare(String currentWindow ) throws Exception {
        if (driver.findElement(By.id("Email")).isDisplayed()) {
            driver.findElement(By.id("Email")).sendKeys("ooyalatester@forgeahead.io");
            driver.findElement(By.id("next")).submit();
            (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id("Passwd")));
            Thread.sleep(2000);
            driver.findElement(By.name("Passwd")).sendKeys("!password*");
            driver.findElement(By.id("signIn")).submit();
            (new WebDriverWait(driver, 60))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id(":0.f")));

            double random_number = Math.random();

            String title_for_sharing_asset = "Sharing this post on Google+" + random_number + "";

            driver.findElement(By.id(":0.f")).sendKeys(title_for_sharing_asset);
            Thread.sleep(5000);
            driver.findElement(By.xpath("//div[@guidedhelpid='sharebutton']")).click();
            Thread.sleep(5000);
            //driver.findElement(By.className("d-k-l")).click();
            driver.switchTo().window(currentWindow);
            Thread.sleep(5000);
            if (getPlatform().equalsIgnoreCase("Mac")) {
                clickOnIndependentElement("shareClose");
                ((JavascriptExecutor) driver).executeScript("window.open('https://plus.google.com')");
                ArrayList<String> multipleTabs = new ArrayList<String>(driver.getWindowHandles());
                System.out.println("total tabs opened : " + multipleTabs.size());
                clickOnIndependentElement("shareBtn");


            } else {
                //driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
                ((JavascriptExecutor) driver).executeScript("window.open('https://plus.google.com')");
                ArrayList<String> multipleTabs = new ArrayList<String>(driver.getWindowHandles());
                System.out.println("total tabs opened : " + multipleTabs.size());
            }
            ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
            if (getBrowser().equalsIgnoreCase("firefox")) {
                driver.switchTo().window(tabs.get(0));
            } else
                driver.switchTo().window(tabs.get(1));
            driver.navigate().to("https://plus.google.com");
            Thread.sleep(5000);
            driver.navigate().refresh();
            (new WebDriverWait(driver, 20))
                    .until(ExpectedConditions.presenceOfElementLocated(By.className("wftCae")));
            Thread.sleep(10000);
            System.out.println("title_for_sharing_asset :" + title_for_sharing_asset);
            List<WebElement> AllElements = driver.findElements(By.className("wftCae"));
            String assetName = "";
            int flag = 1;
            for (int i = 0; i < AllElements.size(); i++) {
                System.out.println("data :" + AllElements.get(i).getText());
                assetName = AllElements.get(i).getText();
                if (assetName.equalsIgnoreCase(title_for_sharing_asset)) {
                    System.out.println("Asset shated on G+ is : " + assetName);
                    flag = 2;
                    break;
                }
            }
            Assert.assertEquals(flag, 2, "Asset is not shared on Google+");
        }

    }

    public void validateTwitterShare(String currentWindow,String windowId) throws Exception{
        if (driver.findElement(By.id("username_or_email")).isDisplayed() && driver.findElement(By.id("password")).isDisplayed()) {
            String tweetShare = driver.findElement(By.id("status")).getText();
            tweetShare = tweetShare.substring(tweetShare.lastIndexOf(":") - 6);

            System.out.println("original Tweet " + tweetShare);

            String nameForTweet = "Intern_2015 Movie at " + Math.random() + "";

            String tweetItOnTwitter = nameForTweet + tweetShare;
            System.out.println("updated Tweet " + tweetItOnTwitter);
            WebElement updateStatus = driver.findElement(By.id("status"));
            updateStatus.clear();
            updateStatus.sendKeys(tweetItOnTwitter);
            driver.findElement(By.id("username_or_email")).sendKeys("ooyalatester@vertisinfotech.com");
            driver.findElement(By.id("password")).sendKeys("!password*");
            Thread.sleep(5000);
            driver.findElement(By.xpath("//input[@value='Log in and Tweet']")).click();
            Thread.sleep(5000);
            //webDriver.findElement(By.xpath(".//*[@value='Tweet']")).click();
            System.out.println("browser name is : " + getBrowser());
            if (getBrowser().equalsIgnoreCase("firefox")) {
                driver.switchTo().window(windowId).close();
            }
            Thread.sleep(5000);
            driver.switchTo().window(currentWindow);
            System.out.println("Current page Title : " + driver.getTitle());
            Thread.sleep(5000);
            if (getPlatform().equalsIgnoreCase("MAC")) {
                clickOnIndependentElement("shareClose");
                ((JavascriptExecutor) driver).executeScript("window.open('http://www.twitter.com')");
                ArrayList<String> multipleTabs = new ArrayList<String>(driver.getWindowHandles());
                System.out.println("total tabs opened : " + multipleTabs.size());
                clickOnIndependentElement("shareBtn");

            } else {
                //driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"t");
                ((JavascriptExecutor) driver).executeScript("window.open('http://www.twitter.com')");
                ArrayList<String> multipleTabs = new ArrayList<String>(driver.getWindowHandles());
                System.out.println("total tabs opened : " + multipleTabs.size());
            }

            ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
            if (getBrowser().equalsIgnoreCase("firefox")) {
                driver.switchTo().window(tabs.get(0));
            } else
                driver.switchTo().window(tabs.get(1));
            driver.navigate().to("https://twitter.com");
            driver.navigate().refresh();


            //   String s=driver.findElement(By.xpath(".//*[@id='stream-items-id']/li[1]/div[1]/div[2]/p")).getText();
            WebDriverWait wait = new WebDriverWait(driver, 200);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='stream-items-id']/li[1]/div/div[2]/div[2]/p")));
            Thread.sleep(5000);
            String s = driver.findElement(By.xpath(".//*[@id='stream-items-id']/li[1]/div/div[2]/div[2]/p")).getText();
            System.out.println("String is  :" + s);

            for (int i = 1; i <= 10; i++) {
                if (s.matches(".*\\b" + nameForTweet + "\\b.*")) {
                    System.out.println("Comparing strings");
                    break;
                }
                //  s=driver.findElement(By.xpath(".//*[@id='stream-items-id']/li["+i+"]/div[1]/div[2]/p")).getText();
                s = driver.findElement(By.xpath(".//*[@id='stream-items-id']/li[" + i + "]/div/div[2]/div[2]/p")).getText();
            }

            System.out.println("tweet :" + s);
            System.out.println("nameForTweet : " + nameForTweet);

            // Twitter is not getting twit PBW-5581
            Assert.assertTrue(s.matches(".*\\b" + nameForTweet + "\\b.*"), "Tweet is not posted on twitter");
            driver.switchTo().window(tabs.get(1));

        } else {
            driver.findElement(By.xpath("//input[@value='Tweet']")).submit();
            Assert.assertTrue((driver.findElement(By.id("post-error")).isDisplayed()), "Twitter post is not updated");
        }

    }
}