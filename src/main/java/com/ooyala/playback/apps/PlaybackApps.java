package com.ooyala.playback.apps;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ooyala.facile.page.FacileWebElement;
import com.ooyala.facile.page.WebPage;
import com.ooyala.playback.apps.utils.CommandLineParameters;
import com.relevantcodes.extentreports.ExtentTest;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;


public abstract class PlaybackApps extends WebPage {


    protected AppiumDriver driver;
    protected ExtentTest extentTest;


    final static  int[] playCoordinates = new int[2];

    final static Logger logger = Logger.getLogger(PlaybackApps.class);

    public PlaybackApps(AppiumDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public void setExtentTest(ExtentTest extentTest) {
        this.extentTest = extentTest;
    }


    public class Element {
        private int startXPosition;
        private int endXPosition;
        private int yPosition;
        private int width;
        int height;

        public int getYposition() {
            return yPosition;
        }

        public void setYposition(int yPosition) {
            this.yPosition = yPosition;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getStartXPosition() {
            return startXPosition;
        }

        public void setStartXPosition(int xPosition) {
            this.startXPosition = xPosition;
        }


        public int getEndXPosition() {
            return endXPosition;
        }

        public void setEndXPosition(int xPosition) {
            this.endXPosition = xPosition;
        }
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

    @Override
    public boolean waitOnElement(String elementKey, int timeout) {

        try {
            if (super.waitOnElement(elementKey, timeout)) {
                logger.info("Wait on element : " + elementKey + "");
                return true;
            } else {
                logger.info("Wait on element : " + elementKey + ", failed after " + timeout + " ms");
                return false;
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return false;

    }

    @Override
    protected boolean clickOnIndependentElement(String elementKey) {
        try {
            return super.clickOnIndependentElement(elementKey);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error("Exception occured while clicking on element " + elementKey);
            return false;
        }
    }

    public boolean getPlayPause(String playpause){
        TouchAction touch = new TouchAction(driver);
        List<WebElement> e = getWebElementsList(playpause);
        WebElement button = e.get(0);
        playCoordinates[0] = button.getLocation().getX();
        playCoordinates[1] = button.getLocation().getY();
       // playCoordinates[0] = play[0] + button.getSize().getWidth() / 2;
        //playCoordinates[1] = play[1] + button.getSize().getHeight() / 2;
        touch.tap(playCoordinates[0],playCoordinates[1]).perform();
        return true;
    }

    public boolean getPause(String playpause) throws Exception {
    	    int[] play = new int[2];
        TouchAction touch = new TouchAction(driver);
        //using the play button coordinates to pause the video
        touch.tap(playCoordinates[0],playCoordinates[1]).perform();
        return true;
    }

    public void tapOnScreen() throws InterruptedException {
        TouchAction touch = new TouchAction(driver);
        touch.tap(playCoordinates[0],  playCoordinates[1]).perform();
        logger.info("We have tapped successfully");
        // Thread.sleep(2000);
    }

    public boolean seekVideo(String element) {
        FacileWebElement anElement = new FacileWebElement((FacileWebElement)this.pageElements.get(element));
        WebElement seekbar = this.getWebElementFromFacileWebElement(anElement);
        int seekBarFieldWidth = seekbar.getLocation().getX();
        int seekBarFieldHeigth = seekbar.getLocation().getY();
        TouchAction touch = new TouchAction(driver);
        touch.longPress(seekBarFieldWidth + 20, seekBarFieldHeigth).moveTo(seekBarFieldWidth + 100, seekBarFieldHeigth).release().perform();
        return true;
    }

    public boolean handleLoadingSpinner() {
        int i = 0;
        int timeOut = 20;
        boolean flag = false;
        boolean ios = getPlatform().equalsIgnoreCase("ios");
        try {
            while (true) {

                if (i <= timeOut) {
                    try {
                        if (ios) {
                            flag = driver.findElement(By.xpath("//XCUIElementTypeActivityIndicator[1]")).isDisplayed();
                        } else {
                            // TODO
                        }

                        if (!flag) {
                            flag = true;
                            break;
                        }
                        Thread.sleep(1000);
                        i++;
                        logger.info("Video Buffering...");
                    } catch (Exception e) {
                        return true;
                    }
                } else {
                    logger.info("Video has been buffering for a really long time i.e it occured more that 20 secs");
                    flag = false;
                    break;
                }
            }

        } catch (Exception e) {
            logger.error("Loading spinner not present !!!");
        }
        return flag;
    }

    public boolean tapScreenIfRequired() {
        if (!isElementPresent(By.xpath("//XCUIElementTypeToolbar[1]"))) {
            return tapScreen();
        }
        return true;
    }

    public boolean tapScreen() {
        return clickOnIndependentElement(By.xpath("//XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther"));
    }

    public boolean waitAndTap() throws InterruptedException {
        Thread.sleep(5000);
        return clickOnIndependentElement(By.xpath("//XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther"));
    }

    public String getPlatform() {
        if(System.getProperty(CommandLineParameters.PLATFORM)!=null && !System.getProperty(CommandLineParameters.PLATFORM).isEmpty()) {
            return System.getProperty(CommandLineParameters.PLATFORM);
        }
        return "";
    }
    
	public boolean letVideoPlayForSec(int sec) throws InterruptedException {
		int count = 0;
		while (count < sec) {
			if (!waitForSec(1))
				return false;
			count++;
		}

		return true;
	}

	private boolean waitForSec(int sec) {
		try {
			Thread.sleep(sec * 1000);
			logger.info("Waiting for " + sec + " seconds");
		} catch (InterruptedException e) {
			logger.error(e);
			return false;
		}
		return true;
	}

}
