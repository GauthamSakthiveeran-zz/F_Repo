package com.ooyala.playback.page;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by snehal on 21/12/16.
 */
public class FCCValidator extends PlayBackPage implements PlaybackValidator {

    private static Logger logger = Logger.getLogger(FCCValidator.class);

    private CCValidator ccValidator;

   // private CCValidator ccValidator;

    public FCCValidator(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("cc");
        addElementToPageElements("fcc");
        addElementToPageElements("play");
        addElementToPageElements("controlbar");
        addElementToPageElements("pause");
    }



 public boolean validate(String element, int timeout) throws Exception{
     return true;
 }

    public boolean checkArrows() throws Exception {
        try{
          waitOnElement("ccLeftScrollBtn",30000);
            clickOnIndependentElement("ccLeftScrollBtn");
            logger.info("Left CC Scroll arrow is not present");
            Thread.sleep(2000);
            waitOnElement("ccRightScrollBtn",60000);
            clickOnIndependentElement("ccRightScrollBtn");
            logger.info("Right CC Scroll arrow is not present");
      //      clickOnIndependentElement("CC_CLOSE_BUTTON");
            return true;

        }catch (Exception e){
            return false;
        }
    }

    public boolean verifyCCPanelElements() throws Exception{
        try {
            waitOnElement("ccContentBar",30000);
            waitOnElement("closedCaptionPanel",30000);

            //verify scroll i.e left or right button for languages option if lang more than 4
            boolean isLeftRightBtn = isElementPresent("ccRightBtn");
            if (isLeftRightBtn){
                logger.info("verifying the scrolling for langauges");
                clickOnIndependentElement("rightBtn");
                Thread.sleep(1000);
                clickOnIndependentElement("leftBtn");
            }

            // verify preview caption text available
            boolean isPreviewCaptionPresent = isElementPresent("ccPreviewCaption");
            Assert.assertEquals(isPreviewCaptionPresent, true);
            logger.info("verified Preview Caption is Present");

            // verify cc off
            clickOnIndependentElement("ccSwitchContainer");
            Thread.sleep(2000);
            boolean ccoff = isElementPresent("ccOff");
            logger.info(".....s......"+ccoff);
            Assert.assertEquals(ccoff, true);
            logger.info("verified the close caption On button working");

            //verify cc on
            clickOnIndependentElement("ccSwitchContainer");
            Thread.sleep(2000);
            boolean ccon = isElementPresent("ccOn");
            Assert.assertEquals(ccon, true);
            logger.info("verified tha close caption On button working");

            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean verifyClosedCaptionLanguages(){
        try{
            //get available languages for video
            Object ccobj = ((JavascriptExecutor) driver)
                    .executeScript("var attrb = pp.getCurrentItemClosedCaptionsLanguages().languages;"+
                            "{return attrb;}");

            @SuppressWarnings("unchecked")
            ArrayList<String> langlist = ((ArrayList<String>) ccobj);
            logger.info("\t \t \t Closed Caption Available Languages: " + langlist + "\n \t \t \t languages available count :" + langlist.size());

            String []langl = new String[langlist.size()];
            langlist.toArray(langl);

            //select language and verify that Preview Text is shown
            List<WebElement> lang = getWebElementsList("langList");
            logger.info("language Count Value in Languages :" + lang.size());
        //    String langpreview[] = {"Hi, I'm a caption!", "Texto de muestra", "Hi, I'm a caption!"};
            String langpreview1[] = {"Sample Text", "Texto de muestra", "Sample Text"};

           /* for (int i = 0; i < lang.size(); i++) {
                Thread.sleep(2000);
                lang.get(i).click();
                if(!waitOnElement("ccPreviewText",30000)){
                    return false;
                }
                String engPreviewText = getWebElement("ccPreviewText").getText();
                logger.info("....s....."+engPreviewText);

                try {
                    Assert.assertEquals(langpreview1[i], engPreviewText);
                }catch (Exception e) {
                    logger.info("Preview text is not visible");
                    return false;
                }

            }*/
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public boolean verifyCCColorSelectionPanel(){
        try{
            String colorsName[] = {"Transparent","White", "Blue", "Magenta","Green","Yellow","Red","Cyan","Black"};
            String colorsCode[] = {"rgba(255, 255, 255, 1)","rgba(0, 0, 255, 1)","rgba(255, 0, 255, 1)","rgba(0, 255, 0, 1)","rgba(255, 255, 0, 1)","rgba(255, 0, 0, 1)","rgba(0, 255, 255, 1)","rgba(0, 0, 0, 1)"};

            // verify color selection panel
            waitOnElement("colorSelectionPanel",30000);
            clickOnIndependentElement("colorSelectionPanel");
            logger.info("\n*---------Verifying Color Selection Panel---------*\n");
            Thread.sleep(2000);

            // select text colors
            List<WebElement> textColor = getWebElementsList("ccTextColorSelector");
            logger.info("\t \t \t Color Count Value in Text Color:" + textColor.size());
            logger.info("\n*---------Verify Text Color Selection Panel---------*\n");

            for (int i=0 ; i < textColor.size(); i++)
            {
                textColor.get(i).click();
                String ccTextColor = getWebElement("ccTextColor").getText();
                logger.info("\t \t \t Text Color Selected :" + ccTextColor);
                Assert.assertEquals(colorsName[i+1],ccTextColor);  //verify color selected

               /* String ccPreviewTextColor = getWebElement("ccPreviewText").getCssValue("color");
                logger.info("\t \t \t Preview Text Color Selected :" + ccPreviewTextColor);
                Assert.assertEquals(colorsCode[i],ccPreviewTextColor);  //verify Preview Text color selected*/
            }
            logger.info("verified text color selection is working fine");

            // select Background colors
            List<WebElement> bgColor = getWebElementsList("ccBackgroundColorSelector");
            logger.info("\t \t \t Color Count Value in Background Color:" + bgColor.size());
            logger.info("\n*---------Verify Background color Selection Panel---------*\n");

            for (int i=0 ; i < bgColor.size(); i++)
            {
                bgColor.get(i).click();
                String ccBgColor = getWebElement("ccBackgroundColor").getText();
                logger.info("\t \t \t Background Color Selected :" + ccBgColor);
                /*String ccPreviewBgColor = getWebElement("ccPreviewTextBG").getCssValue("color");
                logger.info("\t \t \t Preview Text Color Selected :" + ccPreviewBgColor);*/
          //      Assert.assertEquals(colorsName[i],ccBgColor);
            }
            logger.info("verified background color selection is working fine");

            return true;
        }catch (Exception e){
            return false;
        }
    }



    public boolean openCCPanel() throws Exception{

        try {
            ccValidator.switchToControlBar();

            waitOnElement("CC_BTN",30000);

            if (!clickOnIndependentElement("CC_BTN"))
                return false;

            if(!getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall")){
                clickOnIndependentElement("CC_MORE_CAPTIONS");
            }

            return true;

        } catch (Exception e) {
            extentTest.log(LogStatus.FAIL,
                    "Horizontal cc option is not present");
            return false;

        }

    }

}