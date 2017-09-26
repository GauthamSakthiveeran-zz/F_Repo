package com.ooyala.playback.apps;

import io.appium.java_client.TouchAction;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import com.ooyala.facile.page.FacileWebElement;
import com.ooyala.facile.page.WebPage;
import com.ooyala.playback.apps.utils.CommandLineParameters;
import com.relevantcodes.extentreports.ExtentTest;

import io.appium.java_client.AppiumDriver;


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


    class Element {
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

    public boolean seekVideoBack(String slider, String seekbar) throws InterruptedException {
        int startx = getSliderPosition(slider);
        Element seekbarElement = getSeekBarPosition(seekbar);
        logger.info("Seeking Back------------");
        tapScreenIfRequired();
        int seekBackLength = ((startx + 1) - seekbarElement.getStartXPosition()) / 2;
        TouchAction touch = new TouchAction(driver);
        touch.longPress((startx + 1), seekbarElement.getYposition()).moveTo(((startx + 1) - seekBackLength), seekbarElement.getYposition() + seekbarElement.getYposition()).release().perform();
        return true;
    }

    public boolean seekVideoForward(String slider, String seekbar) throws InterruptedException {
        int startx = getSliderPosition(slider);
        Element seekbarElement =  getSeekBarPosition(seekbar);
        logger.info("Seeking forward -------------------------  ");
        tapScreenIfRequired();
        int seekForwardLength = (seekbarElement.getEndXPosition() - (startx + 1)) - 30;
        TouchAction touch = new TouchAction(driver);
        touch.longPress((startx + 1), seekbarElement.getYposition()).moveTo(((startx + 1) + (seekForwardLength)), seekbarElement.getYposition() + seekbarElement.getYposition()).release().perform();
        return true;
    }

    public int getSliderPosition(String slider) throws InterruptedException {
        FacileWebElement anElement = new FacileWebElement((FacileWebElement)this.pageElements.get(slider));
        WebElement slide = this.getWebElementFromFacileWebElement(anElement);
        int sliderXPosition = slide.getLocation().getX();
        logger.info("Slider X Position >> : " + sliderXPosition);
        return sliderXPosition;
    }

    public Element getSeekBarPosition(String seekbar) throws InterruptedException {
        waitAndTap();
        FacileWebElement anElement = new FacileWebElement((FacileWebElement)this.pageElements.get(seekbar));
        WebElement SEEK = this.getWebElementFromFacileWebElement(anElement);
        Point seekbarElementPos = SEEK.getLocation();
        Element seekbarElement = new Element();
        seekbarElement.setStartXPosition(seekbarElementPos.getX());
        seekbarElement.setYposition(seekbarElementPos.getY());
        seekbarElement.setWidth(SEEK.getSize().getWidth());
        seekbarElement.setEndXPosition(seekbarElement.getWidth() + seekbarElement.getStartXPosition());
        logger.info("SeekBarPosition : StartXPosition > " + seekbarElement.getStartXPosition() + ", "
                + " EndXPosition > " + seekbarElement.getEndXPosition() + ", Width > " + seekbarElement.getWidth() + " Yposition > " + seekbarElement.getYposition());
        return seekbarElement;

    }

    public boolean getPlayPause(String playpause){
        int[] play = new int[2];
        FacileWebElement anElement = new FacileWebElement((FacileWebElement)this.pageElements.get(playpause));
        WebElement button = this.getWebElementFromFacileWebElement(anElement);
        play[0] = button.getLocation().getX();
        play[1] = button.getLocation().getY();
        playCoordinates[0] = play[0] + button.getSize().getWidth() / 2;
        playCoordinates[1] = play[1] + button.getSize().getHeight() / 2;
        //System.out.println("Play button x cordinate"+playCoordinates[0]);
        //System.out.println("Play button x cordinate"+playCoordinates[1]);
        TouchAction touch = new TouchAction(driver);
        touch.tap(playCoordinates[0],  playCoordinates[1]).release().perform();
        return true;
    }

    public boolean getPause() {
        TouchAction touch = new TouchAction(driver);
        touch.tap(playCoordinates[0],  playCoordinates[1]).release().perform();
        return true;
    }

    public void tapOnScreen() throws InterruptedException {
        TouchAction touch = new TouchAction(driver);
        touch.tap(playCoordinates[0],  playCoordinates[1]).release().perform();
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
