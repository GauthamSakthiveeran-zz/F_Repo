package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.ooyala.facile.page.WebPage;
import com.ooyala.playback.url.PropertyReader;

public abstract class PlayBackPage extends WebPage {

    public static Logger logger = Logger.getLogger(PlayBackPage.class);

    protected PropertyReader propertyReader;

    public PlayBackPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected String getIndexFileName() {
        return "resources/appElementsIndex.xml";
    }

    @Override
    protected String getLocalizedPageElementString(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean waitForPage() {
        // TODO Auto-generated method stub
        return false;
    }

    public String getPlatform() {
        Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        String platformName = cap.getPlatform().toString();
        return platformName;
    }

    @Override
    protected boolean clickOnIndependentElement(String elementKey) {
        try{
            return super.clickOnIndependentElement(elementKey);
        }catch(Exception e){
            return clickOnHiddenElement(elementKey);
        }

    }

    public boolean clickOnHiddenElement(String elementKey) {
        try{
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = getWebElementsList(elementKey).get(0);
            js.executeScript("arguments[0].click()", element);
            return true;
        } catch (Exception e){
            logger.info("Exception " +e);
            return false;
        }
    }

    public String getBrowser() {
        Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        String browserName = cap.getBrowserName().toLowerCase();
        return browserName;
    }

    public void loadingSpinner() {
        int time = 0;
        while (true) {
            if (time <= 120) {
                try {
                    boolean result = isElementVisible("SPINNER");
                    Thread.sleep(1000);
                    time++;
                } catch (Exception e) {
                    break;
                }
            } else {
                logger.info("Loading spinner is not vanishing i.e it occured more that 2 minutes");
                break;
            }
        }
    }
}
