package com.ooyala.playback.page;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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
            String langpreview1[] = {"Sample Text", "Texto de muestra", "Sample Text"};

            // issue id
            if(getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall")){
                for (int i = 0; i < lang.size(); i++) {
                    Thread.sleep(2000);
                    lang.get(i).click();
                    if(!waitOnElement("ccPreviewText",30000)){
                        return false;
                    }
                    String engPreviewText = getWebElement("ccPreviewText").getText();

                    try {
                        Assert.assertEquals(langpreview1[i], engPreviewText);
                    }catch (Exception e) {
                        logger.info("Preview text is not visible");
                        return false;
                    }
                }
            }
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
                Assert.assertEquals(colorsName[i+1],ccTextColor);

                //verify color selected
                // issue id
                if(getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall")){
                    String ccPreviewTextColor = getWebElement("ccPreviewText").getCssValue("color");
                    logger.info("\t \t \t Preview Text Color Selected :" + ccPreviewTextColor);
                    Assert.assertEquals(colorsCode[i],ccPreviewTextColor);  //verify Preview Text color selected
                }
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
                // issue id
                if(getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall")){
                    String ccPreviewBgColor = getWebElement("ccPreviewTextBG").getCssValue("color");
                    logger.info("\t \t \t Preview Text Color Selected :" + ccPreviewBgColor);
                    Assert.assertEquals(colorsName[i],ccBgColor);
                }
            }
            logger.info("verified background color selection is working fine");

            // select Windows  colors
            List<WebElement> ccWinColor = getWebElementsList("ccWindowColorSelector");
            logger.info("\n \t \t \t Color Count Value in Windows Color:" + ccWinColor.size() + "\n");
            logger.info("\n*---------Verify Window Color Selection Panel---------*\n");
            for (int i=0 ; i < ccWinColor.size(); i++)
            {
                ccWinColor.get(i).click();
                String ccWindowColor = getWebElement("ccWindowColor").getText();
                logger.info("\t \t \t Window Color Selected :" + ccWindowColor);
                String ccPreviewWinColor = getWebElement("ccPreviewWinColor").getCssValue("color");
                logger.info("\t \t \t Window color of Preview Text Selected :" + ccPreviewWinColor);
                Assert.assertEquals(colorsName[i],ccWindowColor);
            }
            logger.info("verified CC Windows color selection is working fine");

            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean verifyCCOpacityPanel()  {
        try {
            // verify CC Opacity Panel
            clickOnIndependentElement("captionOpacityPanel");
            logger.info("\n*----------------------Verify Caption Opacity Panel--------------------*\n");
            Thread.sleep(2000);

            // select text Opacity
            WebElement slider = getWebElement("ccTextOpacitySelector");
            Actions move = new Actions(driver);
            int width = slider.getSize().getWidth();
            move.dragAndDropBy(slider,(width*25)/100,0).build().perform();

            String ccTextOpacity = getWebElement("ccTextOpacity").getText();
            logger.info("\t \t \t Text Opacity Selected :" + ccTextOpacity);
            String ccPreviewTextOpacity = getWebElement("ccPreviewText").getCssValue("color");
            logger.info("\t \t \t Preview Text Opacity Selected :" + ccPreviewTextOpacity);
            Assert.assertEquals("rgba(0, 0, 0, 0.8)",ccPreviewTextOpacity);
            logger.info("verified text opacity selection is working fine");

            // select Background Opacity
            WebElement slider1 = getWebElement("ccBackgroundOpacitySelector");
            int width1 = slider.getSize().getWidth();
            move.dragAndDropBy(slider1,(width1*25)/100,0).build().perform();
            Thread.sleep(2000);
            String ccBgOpacity = getWebElement("ccBackgroundOpacity").getText();
            logger.info("\t \t \t Background Opacity Selected :" + ccBgOpacity);
            String ccPreviewBgOpacity = getWebElement("ccPreviewTextBG").getCssValue("background-color");
            logger.info("\t \t \t Preview Text Background Opacity Selected :" + ccPreviewBgOpacity);
            Assert.assertEquals("rgba(0, 0, 0, 0.8)",ccPreviewBgOpacity);
            logger.info("verified Background opacity selection is working fine");

            // select Windows Opacity
            WebElement slider3 = getWebElement("ccWindowOpacitySelector");
            int width3 = slider3.getSize().getWidth();
            move.dragAndDropBy(slider3,(width3*25)/100,0).build().perform();
            String ccWinOpacity = getWebElement("ccWindowOpacity").getText();
            logger.info("\t \t \t Window Color Selected :" + ccWinOpacity);
            String ccPreviewWinOpacity = getWebElement("ccPreviewWinColor").getCssValue("background-color");
            logger.info("\t \t \t Window Opacity of Preview Text Selected :" + ccPreviewWinOpacity);
            Assert.assertEquals("rgba(0, 0, 0, 0.8)",ccPreviewWinOpacity);
            logger.info("verified Windows opacity selection is working fine");
            return true;
        }catch (Exception e){
           return false;
        }
    }

    public boolean verifyCCFonttypePanel(){
        try{
            // verify CC Font Type Panel
            clickOnIndependentElement("ccFontTypePanel");
            logger.info("\n*--------------Verify Font Type Panel-------------------------*\n");
            Thread.sleep(2000);

            List<WebElement> ccFontType = getWebElementsList("ccFontType");
            logger.info("\t \t \t Font Type Count Value :" + ccFontType.size());
            boolean ismoreFontType = false;
            int fontTypeCount = 0;
            ismoreFontType = isElementPresent("rigthArrow");
            if(ismoreFontType)
            {
                fontTypeCount = 1;
                clickOnIndependentElement("rigthArrow");

                boolean rightArrowPresent = isElementPresent("rightArrowHidden");
                if(!rightArrowPresent){
                    logger.info("\t \t \t No more right button :");
                }else{
                    ismoreFontType = isElementPresent("rightBtn");
                    if(ismoreFontType)
                        fontTypeCount =+1 ;
                    logger.info("\t \t \t Font Type Panel Count :" + fontTypeCount);
                    Thread.sleep(5000);
                    clickOnIndependentElement("rigthArrow");
                }
            }

            List<WebElement> ccFontType1 = getWebElementsList("ccFontType");

            for(int j= 0 ; j < fontTypeCount ; j++) {
                for (int i = 0; i < ccFontType1.size(); i++) {
                    ccFontType1.get(i).click();
                    String ccFontTypeSelected = ccFontType1.get(i).getText();
                    logger.info(ccFontTypeSelected);
                    Thread.sleep(1000);
                    String ccPreviewTextFont = getWebElement("ccPreviewText").getCssValue("font-family");
                    logger.info("\t \t \t Font type selected for CC Preview Text :" + ccPreviewTextFont);
                }

                boolean rightArrowPresent = isElementPresent("rightArrowHidden");
                if(!rightArrowPresent){}else {
                if(ismoreFontType)
                    clickOnIndependentElement("rigthArrow");
                }
            }
            logger.info("verified Font Type selection is working fine");

            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean verifyCCFontSizePanel(){
        try{
            // Verify Font Size Panel
            String ccFonts[] = {"Small","Medium","Large","Extra Large"};
            //String ccFontSizes[] = {"19.6px","25.2px","30.8px","36.4px"};
            String font_size_xsmall [] ={"0.8em","1.2em","1.6em","2em"};
            String font_size_small [] ={"1em","1.4em","1.8em","2.2em"};
            String font_size_medium [] = {"1.2em","1.6em","2em","2.4em"};
            String font_size_large [] = {"1.4em","1.8em","2.2em","2.6em"};

            clickOnIndependentElement("ccFontSizePanel");
            logger.info("\n*--------------Verify CC Font Size Panel---------------------*\n");
            Thread.sleep(2000);
            List<WebElement> ccFontSize = getWebElementsList("ccFontSizeSelector");
            logger.info("\t \t \t Font Size Count Value :" + ccFontSize.size());

            for (int i = 0; i < ccFontSize.size(); i++) {
                ccFontSize.get(i).click();
                String ccTextFontSize =getWebElement("ccFontSizeSelected").getText();
                logger.info("\t \t \t Text Font Size Selected :" + ccTextFontSize);
                String ccPreviewTextFontSize[] = getWebElement("ccPreviewText").getAttribute("style").split(";");
                String fontSizeInEm = null;
                int flag =0;
                for(String cc : ccPreviewTextFontSize){
                    if(cc.contains("font-variant-caps:")){
                        String fontSize [] = ccPreviewTextFontSize[4].split(":");
                        fontSizeInEm = fontSize[1].trim();
                        flag = 1;
                        break;
                    }
                }
                if (flag==0) {
                    String fontSize [] = ccPreviewTextFontSize[3].split(":");
                    fontSizeInEm = fontSize[1].trim();
                }
                logger.info("\t \t \t font size in em : " + fontSizeInEm);
                Assert.assertEquals(ccFonts[i],ccTextFontSize); //verify font size selected
                if(getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-large"))
                    Assert.assertEquals(font_size_large[i],fontSizeInEm);
                if(getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-medium"))
                    Assert.assertEquals(font_size_medium[i],fontSizeInEm);
                if(getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-small"))
                    Assert.assertEquals(font_size_small[i],fontSizeInEm);
                if(getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall"))
                    Assert.assertEquals(font_size_xsmall[i],fontSizeInEm);
            }
            logger.info("verified Font Size selection is working fine");

            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean verifyCCTextEnhancementPanel(){
        try{
            String textEnName[] = {"Uniform","Depressed","Raised","Shadow"};
            String textEnCode[] = {"none","rgb(255, 255, 255) 1px 1px 0px","rgb(255, 255, 255) -1px -1px 0px, rgb(0, 0, 0) -3px 0px 5px","rgb(26, 26, 26) 2px 2px 2px"};
            clickOnIndependentElement("ccTextEnhancement");
            logger.info("\n*---------------Verify CC Text Enhancement Panel--------------*\n");
            Thread.sleep(2000);

            List<WebElement> ccTextEnhancement = getWebElementsList("ccTextEnhancementSelector");
            logger.info("\t \t \t Text Enhancement Type Count Value :" + ccTextEnhancement.size());
            for (int i = 0; i < ccTextEnhancement.size(); i++) {

                ccTextEnhancement.get(i).click();
                clickOnIndependentElement("ccPreviewCaption");
                String ccTextEnh = getWebElement("ccFontSizeSelected").getText();
                logger.info("\t \t \t Text Enhancement Selected :" + ccTextEnh);
                String ccPreviewTextEnh = getWebElement("ccPreviewText").getCssValue("text-shadow");
                logger.info("\t \t \t Text Enhancement for CC Preview Text :" + ccPreviewTextEnh);
                Assert.assertEquals(textEnName[i], ccTextEnh); // verify text enhancement selected
                Assert.assertEquals(textEnCode[i], ccPreviewTextEnh);
            }
            logger.info("verified CC Text Enhancement selection is working fine");
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean closeCCPanel(){
       try {
           Thread.sleep(2000);
           clickOnIndependentElement("CC_PANEL_CLOSE");
           return true;
       } catch (Exception e){
        return false;
       }
    }

    public boolean clearCache() throws Exception{
        for(int i=0;i<13;i++){
            ((JavascriptExecutor) driver).executeScript(String.format("window.localStorage.clear();"));
        }
        return true;
    }

    public boolean openCCPanel(){

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