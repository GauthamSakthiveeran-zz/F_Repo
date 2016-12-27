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
import java.util.HashMap;
import java.util.List;

/**
 * Created by snehal on 21/12/16.
 */
public class FCCValidator extends PlayBackPage implements PlaybackValidator {

    private static Logger logger = Logger.getLogger(FCCValidator.class);

   // private CCValidator ccValidator;
    List<WebElement> lang, textColor, bgColor,ccWinColor, ccFontSize, ccTextEnhancement;
    String previewTextSelected, textSelected, ccFontSizeBefore,ccFontSizeAfter, ccFontTypeBefore,ccFontTypeAfter, ccTextEnhancementSelectedBefore, ccTextEnhancementSelectedAfter;
    HashMap<String,String> ccOpacityMapBefore, ccOpacityMapAfter, ccColorSelectionBefore, ccColorSelectionAfter;

    public FCCValidator(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("cc");
        addElementToPageElements("fcc");
        addElementToPageElements("fullscreen");
        addElementToPageElements("pause");
        addElementToPageElements("controlbar");
    }

    public boolean validate(String element, int timeout) throws Exception {
        return switchToControlBar() && closedCaptionMicroPanel() && checkArrows() && verifyCCPanelElements()
                && verifyClosedCaptionLanguages() && verifyCCColorSelectionPanel()
                && verifyCCOpacityPanel("") && verifyCCFonttypePanel()
                && verifyCCFontSizePanel() && verifyCCTextEnhancementPanel() && closeCCPanel() && clearCache();
    }

    public boolean checkArrows() {
        try {
            if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall")) {
                waitOnElement("ccLeftScrollBtn", 30000);
                clickOnIndependentElement("ccLeftScrollBtn");
                logger.info("Left CC Scroll arrow is not present");
                Thread.sleep(2000);
                waitOnElement("ccRightScrollBtn", 60000);
                clickOnIndependentElement("ccRightScrollBtn");
                logger.info("Right CC Scroll arrow is not present");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyCCPanelElements() {
        try {
            waitOnElement("ccContentBar", 30000);
            waitOnElement("closedCaptionPanel", 30000);

            //verify scroll i.e left or right button for languages option if lang more than 4
            boolean isLeftRightBtn = isElementPresent("ccRightBtn");
            if (isLeftRightBtn) {
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
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyClosedCaptionLanguages() {
        try {
            //get available languages for video
            Object ccobj = ((JavascriptExecutor) driver)
                    .executeScript("var attrb = pp.getCurrentItemClosedCaptionsLanguages().languages;" +
                            "{return attrb;}");

            @SuppressWarnings("unchecked")
            ArrayList<String> langlist = ((ArrayList<String>) ccobj);
            logger.info("\t \t \t Closed Caption Available Languages: " + langlist + "\n \t \t \t languages available count :" + langlist.size());

            String[] langl = new String[langlist.size()];
            langlist.toArray(langl);

            //select language and verify that Preview Text is shown
            lang = getWebElementsList("langList");
            logger.info("language Count Value in Languages :" + lang.size());
            String langpreview1[] = {"Sample Text", "Texto de muestra", "Sample Text"};

            // issue id
            if (!getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall")) {
                if (!getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-small")){
                    for (int i = 0; i < lang.size(); i++) {
                        Thread.sleep(2000);
                        lang.get(i).click();
                        if (!waitOnElement("ccPreviewText", 30000)) {
                            return false;
                        }
                        String engPreviewText = getWebElement("ccPreviewText").getText();

                        try {
                            Assert.assertEquals(langpreview1[i], engPreviewText);
                        } catch (Exception e) {
                            logger.info("Preview text is not visible");
                            return false;
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyCCColorSelectionPanel() {
        try {
            String colorsName[] = {"Transparent", "White", "Blue", "Magenta", "Green", "Yellow", "Red", "Cyan", "Black"};
            String colorsCode[] = {"rgba(255, 255, 255, 1)", "rgba(0, 0, 255, 1)", "rgba(255, 0, 255, 1)", "rgba(0, 255, 0, 1)", "rgba(255, 255, 0, 1)", "rgba(255, 0, 0, 1)", "rgba(0, 255, 255, 1)", "rgba(0, 0, 0, 1)"};

            // verify color selection panel
            waitOnElement("colorSelectionPanel", 30000);
            clickOnIndependentElement("colorSelectionPanel");
            logger.info("\n*---------Verifying Color Selection Panel---------*\n");
            Thread.sleep(2000);

            // select text colors
            textColor = getWebElementsList("ccTextColorSelector");
            logger.info("\t \t \t Color Count Value in Text Color:" + textColor.size());
            logger.info("\n*---------Verify Text Color Selection Panel---------*\n");

            for (int i = 0; i < textColor.size(); i++) {
                textColor.get(i).click();
                String ccTextColor = getWebElement("ccTextColor").getText();
                logger.info("\t Text Color Selected :" + ccTextColor);
                Assert.assertEquals(colorsName[i + 1], ccTextColor);

                //verify color selected
                // issue id
                if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall")) {
                    if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-small")) {
                        String ccPreviewTextColor = getWebElement("ccPreviewText").getCssValue("color");
                        logger.info("\t Preview Text Color Selected :" + ccPreviewTextColor);
                        Assert.assertEquals(colorsCode[i], ccPreviewTextColor);  //verify Preview Text color selected
                    }
                }
            }
            logger.info("verified text color selection is working fine");

            // select Background colors
            bgColor = getWebElementsList("ccBackgroundColorSelector");
            logger.info("\t Color Count Value in Background Color:" + bgColor.size());
            logger.info("\n*---------Verify Background color Selection Panel---------*\n");

            for (int i = 0; i < bgColor.size(); i++) {
                bgColor.get(i).click();
                String ccBgColor = getWebElement("ccBackgroundColor").getText();
                logger.info("\t Background Color Selected :" + ccBgColor);
                // issue id
                if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall")) {
                    String ccPreviewBgColor = getWebElement("ccPreviewTextBG").getCssValue("color");
                    logger.info("\t Preview Text Color Selected :" + ccPreviewBgColor);
                    Assert.assertEquals(colorsName[i], ccBgColor);
                }
            }
            logger.info("verified background color selection is working fine");

            // select Windows  colors
            ccWinColor = getWebElementsList("ccWindowColorSelector");
            logger.info("\n Color Count Value in Windows Color:" + ccWinColor.size() + "\n");
            logger.info("\n*---------Verify Window Color Selection Panel---------*\n");
            for (int i = 0; i < ccWinColor.size(); i++) {
                ccWinColor.get(i).click();
                String ccWindowColor = getWebElement("ccWindowColor").getText();
                logger.info("\t Window Color Selected :" + ccWindowColor);
                String ccPreviewWinColor = getWebElement("ccPreviewWinColor").getCssValue("color");
                logger.info("\t Window color of Preview Text Selected :" + ccPreviewWinColor);
                Assert.assertEquals(colorsName[i], ccWindowColor);
            }
            logger.info("verified CC Windows color selection is working fine");

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyCCOpacityPanel(String testName) {
        try {
            // verify CC Opacity Panel
            clickOnIndependentElement("captionOpacityPanel");
            logger.info("\n*----------------------Verify Caption Opacity Panel--------------------*\n");
            Thread.sleep(2000);

            // select text Opacity
            WebElement slider = getWebElement("ccTextOpacitySelector");
            Actions move = new Actions(driver);
            int width = slider.getSize().getWidth();
            move.dragAndDropBy(slider, (width * 25) / 100, 0).build().perform();

            String ccTextOpacity = getWebElement("ccTextOpacity").getText();
            logger.info("\t Text Opacity Selected :" + ccTextOpacity);
            String ccPreviewTextOpacity = getWebElement("ccPreviewText").getCssValue("color");
            logger.info("\t Preview Text Opacity Selected :" + ccPreviewTextOpacity);
            if(!testName.contains("PlaybackFCCDefaultSettingTests")){
                Assert.assertEquals("rgba(0, 0, 0, 0.8)", ccPreviewTextOpacity);
            }
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

            if(!testName.contains("PlaybackFCCDefaultSettingTests")){
                Assert.assertEquals("rgba(0, 0, 0, 0.8)",ccPreviewBgOpacity);
            }

            logger.info("verified Background opacity selection is working fine");

            // select Windows Opacity
            WebElement slider3 = getWebElement("ccWindowOpacitySelector");
            int width3 = slider3.getSize().getWidth();
            move.dragAndDropBy(slider3, (width3 * 25) / 100, 0).build().perform();
            String ccWinOpacity = getWebElement("ccWindowOpacity").getText();
            logger.info("\t Window Color Selected :" + ccWinOpacity);
            String ccPreviewWinOpacity = getWebElement("ccPreviewWinColor").getCssValue("background-color");
            logger.info("\t Window Opacity of Preview Text Selected :" + ccPreviewWinOpacity);

            if(!testName.contains("PlaybackFCCDefaultSettingTests")){
                Assert.assertEquals("rgba(0, 0, 0, 0.8)", ccPreviewWinOpacity);
            }
            logger.info("verified Windows opacity selection is working fine");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyCCFonttypePanel() {
        try {
            // verify CC Font Type Panel
            clickOnIndependentElement("ccFontTypePanel");
            logger.info("\n*--------------Verify Font Type Panel-------------------------*\n");
            Thread.sleep(2000);

            List<WebElement> ccFontType = getWebElementsList("ccFontType");
            logger.info("\t \t \t Font Type Count Value :" + ccFontType.size());
            boolean ismoreFontType = false;
            int fontTypeCount = 0;
            ismoreFontType = isElementPresent("rigthArrow");
            if (ismoreFontType) {
                fontTypeCount = 1;
                clickOnIndependentElement("rigthArrow");
                boolean rightArrowPresent = isElementPresent("rightArrowHidden");
                ismoreFontType = isElementPresent("rightBtn");
                if (ismoreFontType)
                    fontTypeCount = +1;
                logger.info("\t Font Type Panel Count :" + fontTypeCount);
                Thread.sleep(3000);
                clickOnIndependentElement("leftBtn");
            }

            List<WebElement> ccFontType1 = getWebElementsList("ccFontType");

            for (int j = 0; j < fontTypeCount; j++) {
                for (int i = 0; i < ccFontType1.size(); i++) {
                    ccFontType1.get(i).click();
                    String ccFontTypeSelected = ccFontType1.get(i).getText();
                    logger.info(ccFontTypeSelected);
                    Thread.sleep(1000);
                    String ccPreviewTextFont = getWebElement("ccPreviewText").getCssValue("font-family");
                    logger.info("\t Font type selected for CC Preview Text :" + ccPreviewTextFont);
                }
                if (ismoreFontType)
                    clickOnIndependentElement("rigthArrow");
            }

            if(isElementPresent("leftBtn")){
                clickOnIndependentElement("leftBtn");
            }

            logger.info("verified Font Type selection is working fine");

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyCCFontSizePanel() {
        try {
            // Verify Font Size Panel
            String ccFonts[] = {"Small", "Medium", "Large", "Extra Large"};
            //String ccFontSizes[] = {"19.6px","25.2px","30.8px","36.4px"};
            String font_size_xsmall[] = {"0.8em", "1.2em", "1.6em", "2em"};
            String font_size_small[] = {"1em", "1.4em", "1.8em", "2.2em"};
            String font_size_medium[] = {"1.2em", "1.6em", "2em", "2.4em"};
            String font_size_large[] = {"1.4em", "1.8em", "2.2em", "2.6em"};

            clickOnIndependentElement("ccFontSizePanel");
            logger.info("\n*--------------Verify CC Font Size Panel---------------------*\n");
            Thread.sleep(2000);
            ccFontSize = getWebElementsList("ccFontSizeSelector");
            logger.info("\t \t \t Font Size Count Value :" + ccFontSize.size());

            for (int i = 0; i < ccFontSize.size(); i++) {
                ccFontSize.get(i).click();
                String ccTextFontSize = getWebElement("ccFontSizeSelected").getText();
                logger.info("\t Text Font Size Selected :" + ccTextFontSize);
                String ccPreviewTextFontSize[] = getWebElement("ccPreviewText").getAttribute("style").split(";");
                String fontSizeInEm = null;
                int flag = 0;
                for (String cc : ccPreviewTextFontSize) {
                    if (cc.contains("font-variant-caps:")) {
                        String fontSize[] = ccPreviewTextFontSize[4].split(":");
                        fontSizeInEm = fontSize[1].trim();
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) {
                    String fontSize[];
                    if (getBrowser().equalsIgnoreCase("internet explorer")){
                        fontSize = ccPreviewTextFontSize[2].split(":");
                        fontSizeInEm = fontSize[1].trim();
                    }else {
                        fontSize = ccPreviewTextFontSize[3].split(":");
                        fontSizeInEm = fontSize[1].trim();
                    }
                }
                logger.info("\t font size in em : " + fontSizeInEm);
                Assert.assertEquals(ccFonts[i], ccTextFontSize); //verify font size selected
                if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-large"))
                    Assert.assertEquals(font_size_large[i], fontSizeInEm);
                if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-medium"))
                    Assert.assertEquals(font_size_medium[i], fontSizeInEm);
                if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-small"))
                    Assert.assertEquals(font_size_small[i], fontSizeInEm);
                if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall"))
                    Assert.assertEquals(font_size_xsmall[i], fontSizeInEm);
            }

            logger.info("verified Font Size selection is working fine");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyCCTextEnhancementPanel() {
        try {
            String textEnName[] = {"Uniform", "Depressed", "Raised", "Shadow"};
            String textEnCode[] = {"none", "rgb(255, 255, 255) 1px 1px 0px", "rgb(255, 255, 255) -1px -1px 0px, rgb(0, 0, 0) -3px 0px 5px", "rgb(26, 26, 26) 2px 2px 2px"};
            String textEnCodeForIE[]={"none","1px 1px white","-1px -1px white, -3px 0px 5px black","2px 2px 2px #1a1a1a"};
            clickOnIndependentElement("ccTextEnhancement");
            logger.info("\n*---------------Verify CC Text Enhancement Panel--------------*\n");
            Thread.sleep(2000);

            ccTextEnhancement = getWebElementsList("ccTextEnhancementSelector");
            logger.info("\t Text Enhancement Type Count Value :" + ccTextEnhancement.size());
            for (int i = 0; i < ccTextEnhancement.size(); i++) {

                ccTextEnhancement.get(i).click();

                String ccTextEnh = getWebElement("ccFontSizeSelected").getText();
                logger.info("\t Text Enhancement Selected :" + ccTextEnh);
                String ccPreviewTextEnh = getWebElement("ccPreviewText").getCssValue("text-shadow");
                logger.info("\t Text Enhancement for CC Preview Text :" + ccPreviewTextEnh);
                if (!(textEnName[i].equals(ccTextEnh))){
                    logger.error("Preview Text enhancement is not matching");
                    return false;
                }
                // verify text enhancement selected
                if (getBrowser().equalsIgnoreCase("internet explorer")){
                    if (!(textEnCodeForIE[i].equals(ccPreviewTextEnh))){
                        logger.error("Preview Text enhancement is not matching for internet explorer browser");
                        return false;
                    }
                }else
                    if (!(textEnCode[i].equals(ccPreviewTextEnh))){
                        logger.error("Preview Text enhancement is not matching for "+getBrowser() + "browser");
                        return false;
                    }
            }
            logger.info("verified CC Text Enhancement selection is working fine");
            return true;
        } catch (Exception e) {
            logger.error("CC Text Enhancement selection is not working");
            return false;
        }
    }

    public boolean closeCCPanel() {
        try {
            Thread.sleep(2000);
            clickOnIndependentElement("CC_PANEL_CLOSE");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyFccInFullscreen() throws Exception {
        try {
            // PBW-5165 we are not verifying fullscreen change event for safari and
            // firefox browser as fullscreen is not working in safari in automation
            getFullscreen();

            validate("", 30000);

            getNormalscreen();

            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public boolean getFullscreen() {
        try {
            // PBW-5165 we are not verifying fullscreen change event for safari and
            // firefox browser as fullscreen is not working in safari in automation
            if (!(getBrowser().equalsIgnoreCase("safari") || getBrowser().equalsIgnoreCase("internet explorer")
                    || (getBrowser().equalsIgnoreCase("firefox") && getPlatform().equalsIgnoreCase("mac")))) {

                if (!clickOnIndependentElement("FULLSCREEN_BTN_1")) {
                    return false;
                }
                Thread.sleep(2000);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean getNormalscreen() {
        try{
            // PBW-5165 we are not verifying fullscreen change event for safari and
            // firefox browser as fullscreen is not working in safari in automation
            if (!(getBrowser().equalsIgnoreCase("safari") || getBrowser().equalsIgnoreCase("internet explorer")
                    || (getBrowser().equalsIgnoreCase("firefox") && getPlatform().equalsIgnoreCase("mac")))) {
                if (!clickOnIndependentElement("NORMAL_SCREEN"))
                    return false;
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }


    public boolean clearCache() throws Exception {
        for (int i = 0; i < 13; i++) {
            ((JavascriptExecutor) driver).executeScript(String.format("window.localStorage.clear();"));
        }
        return true;
    }

    public boolean closedCaptionMicroPanel() {
        try {

            waitOnElement("CC_BTN", 30000);

            if (!clickOnIndependentElement("CC_BTN"))
                return false;

            if (!getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall")) {
                if (!waitOnElement("CC_POPHOVER_HORIZONTAL", 6000))
                    return false;
                boolean horizontal_CC_Option = isElementPresent("CC_POPHOVER_HORIZONTAL");

                if (horizontal_CC_Option) {
                    return waitOnElement("CC_SWITCH_CONTAINER_HORIZONTAL", 20000)
                            && waitOnElement("CC_MORE_CAPTIONS", 10000)
                            && waitOnElement("CC_CLOSE_BUTTON", 10000)
                            && clickOnIndependentElement("CC_MORE_CAPTIONS");
                }
                return false;

            } else {
                return true;
            }

        } catch (Exception e) {
            extentTest.log(LogStatus.FAIL,
                    "Horizontal cc option is not present");
        }
        return false;
    }

    public boolean switchToControlBar() {
        try {
            if (isElementPresent("HIDDEN_CONTROL_BAR")) {
                logger.info("hovering mouse over the player");
                Thread.sleep(2000);
                moveElement(getWebElement("HIDDEN_CONTROL_BAR"));
            } else if (isElementPresent("CONTROL_BAR")) {
                moveElement(getWebElement("CONTROL_BAR"));
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean beforeRefreshCCSetting(){
        boolean result = true;
        try{
            Thread.sleep(2000);
            checkArrows();

            result = result && verifyCCPanelElements();

            // CC Languages
            result = result && verifyClosedCaptionLanguages();
            result = result && setClosedCaptionLanguage(2);
            Thread.sleep(2000);
            previewTextSelected = getCCLanguagePreviewText();
            logger.info("Preview Text Selected : " + previewTextSelected);

            // CC Color Selection
            result = result && verifyCCColorSelectionPanel();
            result = result && setCCColorSelectionOptions();
            ccColorSelectionBefore = getCCColorSelection();

            // CC Opacity Selection
            result = result && verifyCCOpacityPanel("PlaybackFCCDefaultSettingTests");
            Thread.sleep(2000);
            result = result && setCCOpacity();
            ccOpacityMapBefore = getCCOpacityValues();

            // CC Font Type Selection
            result = result && verifyCCFonttypePanel();
            Thread.sleep(2000);
            result =result && setFontType();
            ccFontTypeBefore =getFontType();

            // CC Font Size Selection
            result = result && verifyCCFontSizePanel();
            Thread.sleep(2000);
            result =result && setFontSize();
            ccFontSizeBefore=getCCFontSizePreviewText();

            // CC Text Enhancement Selection
            result = result && verifyCCTextEnhancementPanel();
            Thread.sleep(2000);
            result =result && setTextEnhancement();
            ccTextEnhancementSelectedBefore=getTextEnhancement();

            return result;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean setClosedCaptionLanguage(int index) {
        index = index-1;
        if(index < lang.size()){
            lang.get(index).click();
            return true;
        }else{
            logger.info("Invalid index passed.");
        }
        return false;
    }

    public boolean setCCColorSelectionOptions(){
        try{
            textColor.get(0).click();
            bgColor.get(6).click();
            ccWinColor.get(2).click();
            return  true;
        }catch(Exception e){
            logger.info("Error while setting cc color selection");
            return false;
        }
    }

    public HashMap<String,String> getCCColorSelection(){
        waitOnElement("colorSelectionPanel", 30000);
        clickOnIndependentElement("colorSelectionPanel");

        String ccTextColor, ccBgColor, ccWinColor;
        HashMap<String,String> ccColorMap = new HashMap<String,String>();

        ccTextColor = getWebElement("ccTextColor").getText();
        ccBgColor = getWebElement("ccBackgroundColor").getText();
        ccWinColor = getWebElement("ccWindowColor").getText();

        ccColorMap.put("ccTextColor", ccTextColor);
        ccColorMap.put("ccBgColor", ccBgColor);
        ccColorMap.put("ccWinColor", ccWinColor);

        return ccColorMap;
    }

    public boolean setCCOpacity(){
        try{
            WebElement slider;
            int width;
            Actions move = new Actions(driver);
            slider = getWebElement("ccTextOpacitySelector");
            width = slider.getSize().getWidth();
            move.dragAndDropBy(slider,(width*20)/100,0).build().perform();

            slider = getWebElement("ccBackgroundOpacitySelector");
            width = slider.getSize().getWidth();
            move.dragAndDropBy(slider,(width*20)/100,0).build().perform();

            slider = getWebElement("ccWindowOpacitySelector");
            width = slider.getSize().getWidth();
            move.dragAndDropBy(slider,(width*20)/100,0).build().perform();
            return true;
        }catch (Exception e){
            logger.info("Error while setting CC opacity");
            e.printStackTrace();
            return false;
        }
    }

    public boolean setFontSize(){
        try{
            ccFontSize.get(2).click();
            return true;
        }catch (Exception e){
            logger.info("Error while setting CC font size");
            e.printStackTrace();
            return false;
        }
    }

    public boolean setTextEnhancement(){
        try{
            Thread.sleep(2000);
            ccTextEnhancement.get(1).click();
            return true;
        }catch (Exception e){
            logger.info("Error while setting CC Text Enhancement");
            e.printStackTrace();
            return false;
        }
    }

    public String getTextEnhancement() throws Exception{
        clickOnIndependentElement("ccTextEnhancement");
        Thread.sleep(2000);
        String ccPreviewTextEnh = getWebElement("ccPreviewText").getCssValue("text-shadow");
        logger.info("\t Text Enhancement Selected :" + ccPreviewTextEnh);
        return ccPreviewTextEnh;
    }

    public boolean setFontType(){
        try{
            List<WebElement> ccFontType = getWebElementsList("ccFontType");
            Thread.sleep(1000);
            ccFontType.get(1).click();
            return true;
        }catch (Exception e){
            logger.info("Error while setting CC font type");
            e.printStackTrace();
            return false;
        }
    }

    public String getFontType() throws Exception {
        clickOnIndependentElement("ccFontTypePanel");
        Thread.sleep(2000);
        String ccPreviewTextFont = getWebElement("ccPreviewText").getCssValue("font-family");
        return ccPreviewTextFont;
    }

    public String getCCLanguagePreviewText() {
        String previewTextDefault = getWebElement("ccPreviewText").getText();
        return previewTextDefault;
    }

    public String getCCFontSizePreviewText() throws Exception{
        clickOnIndependentElement("ccFontSizePanel");
        Thread.sleep(2000);
        String ccTextFontSize = getWebElement("ccFontSizeSelected").getText();
        logger.info("\t Text Font Size Selected.... :" + ccTextFontSize);
        return ccTextFontSize;
    }

    public HashMap<String,String> getCCOpacityValues(){
        clickOnIndependentElement("captionOpacityPanel");

        String ccTextOpacity, ccBgOpacity, ccWinOpacity;
        HashMap<String,String> ccOpacityMap = new HashMap<String,String>();

        clickOnIndependentElement("captionOpacityPanel");
        ccTextOpacity = getWebElement("ccPreviewText").getCssValue("color");
        ccBgOpacity = getWebElement("ccPreviewTextBG").getCssValue("background-color");
        ccWinOpacity = getWebElement("ccPreviewWinColor").getCssValue("background-color");

        ccOpacityMap.put("ccTextOpacity", ccTextOpacity);
        ccOpacityMap.put("ccBgOpacity",ccBgOpacity);
        ccOpacityMap.put("ccWinOpacity",ccWinOpacity);

        return ccOpacityMap;
    }

    public boolean afterRefreshCCSettings() {
        boolean result = true;
        try {
            Object ccobj = ((JavascriptExecutor) driver)
                    .executeScript("var attrb = pp.getCurrentItemClosedCaptionsLanguages().languages;" +
                            "{return attrb;}");

            @SuppressWarnings("unchecked")
            ArrayList<String> langlist = ((ArrayList<String>) ccobj);
            logger.info("\t Closed Caption Available Languages: " + langlist + "\n \t languages available count :" + langlist.size());

            String[] langl = new String[langlist.size()];
            langlist.toArray(langl);

            // Select language and verify that Preview Text is shown
            lang = getWebElementsList("langList");
            textSelected = getCCLanguagePreviewText();
            result = result && textSelected.contains(previewTextSelected);

            // Comparing Color selected values before refresh and after refresh
            ccColorSelectionAfter = getCCColorSelection();
            result = result && compareValues(ccColorSelectionBefore, ccColorSelectionAfter);

            // Comparing Opacity values before refresh and after refresh
            ccOpacityMapAfter = getCCOpacityValues();
            result = result && compareValues(ccOpacityMapBefore, ccOpacityMapAfter);

            // Comparing Font Type values before refresh and after refresh
            ccFontTypeAfter = getFontType();
            result = result && ccFontTypeBefore.contains(ccFontTypeAfter);

            // Comparing Font size before refresh and after refresh
            ccFontSizeAfter=getCCFontSizePreviewText();
            result = result && ccFontSizeBefore.contains(ccFontSizeAfter);

            // Comparing Text Enhancement value before refresh and after refresh
            ccTextEnhancementSelectedAfter=getTextEnhancement();
            result =result && ccTextEnhancementSelectedBefore.contains(ccTextEnhancementSelectedAfter);

            return result;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean compareValues(HashMap<String, String> beforeRefreshPageMap, HashMap<String, String> afterRefreshValuesMap){
        for (final String key : afterRefreshValuesMap.keySet()) {
            if (afterRefreshValuesMap.containsKey(key) && beforeRefreshPageMap.containsKey(key)) {
                Assert.assertEquals(afterRefreshValuesMap.get(key), beforeRefreshPageMap.get(key));
                logger.info("ccOpacity Values for :" + key + " : Before Refresh" + beforeRefreshPageMap.get(key)+ " After Refresh : "+afterRefreshValuesMap.get(key));
            }
        }
        return true;
    }

}