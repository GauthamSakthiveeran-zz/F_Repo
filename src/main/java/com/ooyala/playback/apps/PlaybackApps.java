package com.ooyala.playback.apps;

import com.ooyala.facile.page.FacileWebElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import com.ooyala.facile.page.WebPage;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;


public abstract class PlaybackApps extends WebPage {


    protected AppiumDriver driver;


    final static Logger logger = Logger.getLogger(PlaybackApps.class);

    public PlaybackApps(AppiumDriver driver) {
        super(driver);
        this.driver = driver;
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

    public boolean seekVideoBack(String Slider, String Seekbar) throws InterruptedException {
        int startx = getSliderPosition(Slider);
        Element seekbar = getSeekBarPosition(Seekbar);
        logger.info("Seeking Back------------");
        tapScreenIfRequired();
        int seekBackLength = ((startx + 1) - seekbar.getStartXPosition()) / 2;
        driver.swipe((startx + 1), seekbar.getYposition(), ((startx + 1) - seekBackLength), seekbar.getYposition() + seekbar.getYposition(), 3);
        return true;
    }


    public boolean seekVideoForward(String Slider, String Seekbar) throws InterruptedException {
        int startx = getSliderPosition(Slider);
        Element seekbar =  getSeekBarPosition(Seekbar);
        logger.info("Seeking forward -------------------------  ");
        tapScreenIfRequired();
        int seekForwardLength = (seekbar.getEndXPosition() - (startx + 1)) - 30; //This will seek just before end of the the video
        driver.swipe((startx + 1), seekbar.getYposition(), ((startx + 1) + (seekForwardLength)), seekbar.getYposition() + seekbar.getYposition(), 3);
        return true;
    }

    public int getSliderPosition(String slider) throws InterruptedException {
        FacileWebElement anElement = new FacileWebElement((FacileWebElement)this.pageElements.get(slider));
        WebElement slide = this.getWebElementFromFacileWebElement(anElement);
        int sliderXPosition = slide.getLocation().getX();
        logger.info("Slider X Position >> : " + sliderXPosition);
        return sliderXPosition;
    }

    public Element getSeekBarPosition(String Seekbar) throws InterruptedException {
        waitAndTap();
        FacileWebElement anElement = new FacileWebElement((FacileWebElement)this.pageElements.get(Seekbar));
        WebElement SEEK = this.getWebElementFromFacileWebElement(anElement);
        Point seekbarElementPos = SEEK.getLocation();
        Element seekbar = new Element();
        seekbar.setStartXPosition(seekbarElementPos.getX());
        seekbar.setYposition(seekbarElementPos.getY());
        seekbar.setWidth(SEEK.getSize().getWidth());
        seekbar.setEndXPosition(seekbar.getWidth() + seekbar.getStartXPosition());
        logger.info("SeekBarPosition : StartXPosition > " + seekbar.getStartXPosition() + ", "
                + " EndXPosition > " + seekbar.getEndXPosition() + ", Width > " + seekbar.getWidth() + " Yposition > " + seekbar.getYposition());
        return seekbar;

    }


    public boolean seekVideo(String element) {
        WebElement seekBarField = driver.findElement(By.xpath(element));
        int seekBarFieldWidth = seekBarField.getLocation().getX();
        int seekBarFieldHeigth = seekBarField.getLocation().getY();
        driver.swipe(seekBarFieldWidth + 20, seekBarFieldHeigth, seekBarFieldWidth + 100, seekBarFieldHeigth, 3);
        return true;
    }



    public boolean handleLoadingSpinner() {
        int i = 0;
        int timeOut = 20;
        boolean flag = false;
        try {
            while (true) {

                if (i <= timeOut) {
                    try {
                        flag = driver.findElement(By.xpath("//XCUIElementTypeActivityIndicator[1]")).isDisplayed();
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

}
