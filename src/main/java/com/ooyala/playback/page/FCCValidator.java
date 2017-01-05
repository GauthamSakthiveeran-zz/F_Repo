package com.ooyala.playback.page;

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
        addElementToPageElements("discovery");
    }

    public boolean validate(String element, int timeout) throws Exception {
        return switchToControlBar() && closedCaptionMicroPanel() && checkArrows() && verifyCCPanelElements()
                && verifyClosedCaptionLanguages()  && verifyCCColorSelectionPanel()
               && verifyCCOpacityPanel("") && verifyCCFonttypePanel()
                && verifyCCFontSizePanel() && verifyCCTextEnhancementPanel() &&  closeCCPanel() && clearCache();
    }

    public boolean checkArrows() {
        try {
            if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall")) {
                waitOnElement("CC_LEFT_SCROLL_BTN", 30000);
                clickOnIndependentElement("CC_LEFT_SCROLL_BTN");
                logger.info("Left CC Scroll arrow is not present");
                Thread.sleep(2000);
                waitOnElement("CC_RIGHT_SCROLL_BTN", 60000);
                clickOnIndependentElement("CC_RIGHT_SCROLL_BTN");
                logger.info("Right CC Scroll arrow is not present");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyCCPanelElements() {
        try {
            boolean flag =true;
            waitOnElement("CC_CONTROL_BAR", 30000);
            waitOnElement("CLOSED_CAPTION_PANEL", 30000);

            //verify scroll i.e left or right button for languages option if lang more than 4
            boolean isLeftRightBtn = isElementPresent("CC_RIGHT_BTN");
            if (isLeftRightBtn) {
                logger.info("verifying the scrolling for langauges");
                clickOnIndependentElement("RIGHT_BTN");
                Thread.sleep(1000);
                clickOnIndependentElement("LEFT_BTN");
            }

            // verify preview caption text available
            boolean isPreviewCaptionPresent = isElementPresent("CC_PREVIEW_CAPTION");
            flag = flag && isPreviewCaptionPresent;
            logger.info("verified Preview Caption is Present");

            // verify cc off
            clickOnIndependentElement("CC_SWITCH_CONTAINER");
            Thread.sleep(2000);
            boolean ccoff = isElementPresent("CC_OFF");
            flag = flag && ccoff;
            logger.info("verified the close caption On button working");

            //verify cc on
            clickOnIndependentElement("CC_SWITCH_CONTAINER");
            Thread.sleep(2000);
            boolean ccon = isElementPresent("CC_ON");
            flag = flag && ccon;
            logger.info("verified tha close caption On button working");

            return flag;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyClosedCaptionLanguages() {
        try {
            boolean flag= true;
            //get available languages for video
            Object ccobj = ((JavascriptExecutor) driver)
                    .executeScript("var attrb = pp.getCurrentItemClosedCaptionsLanguages().languages;" +
                            "{return attrb;}");

            @SuppressWarnings("unchecked")
            ArrayList<String> langlist = ((ArrayList<String>) ccobj);
            logger.info("\t Closed Caption Available Languages: " + langlist + "\n \t  languages available count :" + langlist.size());

            String[] langl = new String[langlist.size()];
            langlist.toArray(langl);

            //select language and verify that Preview Text is shown
            lang = getWebElementsList("LANG_LIST");
            logger.info("language Count Value in Languages :" + lang.size());
            String langpreview1[] = {"Sample Text", "Texto de muestra", "Sample Text","Sample Text"};

            // issue id
            if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall")) {
                for (int j = 0; j < langlist.size(); j++) {
                    for (int i = 0; i < lang.size(); i++) {
                        lang.get(i).click();
                        if (isElementPresent("RIGHT_ARROW")) {
                            clickOnIndependentElement("RIGHT_ARROW");
                        }
                    }
                }
                if (isElementPresent("LEFT_ARROW")) {
                    clickOnIndependentElement("LEFT_ARROW");
                }
            } else {
                for (int i = 0; i < lang.size(); i++) {
                    Thread.sleep(1000);
                    lang.get(i).click();

                    if (!getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-small")){
                        if (!waitOnElement("CC_PREVIEW_TEXT", 30000)) {
                            return false;
                        }
                        String engPreviewText = getWebElement("CC_PREVIEW_TEXT").getText();
                        flag = flag && langpreview1[i].equalsIgnoreCase(engPreviewText);
                    }
                }
            }
            return flag;
        } catch (Exception e) {
            logger.error("Preview text is not visible");
            return false;
        }
    }

    public boolean verifyCCColorSelectionPanel() {
        try {
            String colorsName[] = {"Transparent", "White", "Blue", "Magenta", "Green", "Yellow", "Red", "Cyan", "Black"};
            String colorsCode[] = {"rgba(255, 255, 255, 1)", "rgba(0, 0, 255, 1)", "rgba(255, 0, 255, 1)", "rgba(0, 255, 0, 1)", "rgba(255, 255, 0, 1)", "rgba(255, 0, 0, 1)", "rgba(0, 255, 255, 1)", "rgba(0, 0, 0, 1)"};
            String colorsCode1[] = {"rgba(255, 255, 255, 0.8)", "rgba(0, 0, 255, 0.8)", "rgba(255, 0, 255, 0.8)", "rgba(0, 255, 0, 0.8)", "rgba(255, 255, 0, 0.8)", "rgba(255, 0, 0, 0.8)", "rgba(0, 255, 255, 0.8)", "rgba(0, 0, 0, 0.8)"};
            String bgColorsCode[] ={"transparent","rgba(255, 255, 255, 0.8)","rgba(0, 0, 255, 0.8)","rgba(255, 0, 255, 0.8)","rgba(0, 255, 0, 0.8)","rgba(255, 255, 0, 0.8)","rgba(255, 0, 0, 0.8)","rgba(0, 255, 255, 0.8)","rgba(0, 0, 0, 0.8)"};
            boolean flag = true;

            // verify color selection panel
            waitOnElement("COLOR_SELECTION_PANEL", 30000);
            clickOnIndependentElement("COLOR_SELECTION_PANEL");
            logger.info("\n*---------Verifying Color Selection Panel---------*\n");
            Thread.sleep(2000);

            // select text colors
            textColor = getWebElementsList("CC_TEXT_COLOR_SELECTOR");
            logger.info("\t \t \t Color Count Value in Text Color:" + textColor.size());
            logger.info("\n*---------Verify Text Color Selection Panel---------*\n");

            for (int i = 0; i < textColor.size(); i++) {
                textColor.get(i).click();
                String ccTextColor = getWebElement("CC_TEXT_COLOR").getText();  // e.g. Text color: White
                logger.info("\t Text Color Selected :" + ccTextColor);
                flag = flag && colorsName[i + 1].equalsIgnoreCase(ccTextColor);

                //verify color selected
                // issue id
                if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-large")){
                    String ccPreviewTextColor = getWebElement("CC_PREVIEW_TEXT").getCssValue("color");
                    logger.info("\t Preview Text Color Selected :" + ccPreviewTextColor);
                    flag = flag && colorsCode[i].equalsIgnoreCase(ccPreviewTextColor);  //verify Preview Text color selected}
                    if(!flag){
                        flag = true && colorsCode1[i].equalsIgnoreCase(ccPreviewTextColor);
                        logger.info("cache is not cleared in fullscreen");
                    }
                }
            }
            if(!flag){
                logger.error("Error in Text color selection");
                return false;
            }
            logger.info("verified text color selection is working fine");

            // select Background colors
            bgColor = getWebElementsList("CC_BACKGROUND_COLOR_SELECTOR");
            logger.info("\t Color Count Value in Background Color:" + bgColor.size());
            logger.info("\n*---------Verify Background color Selection Panel---------*\n");

            for (int i = 0; i < bgColor.size(); i++) {
                if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall")){
                    WebElement element = bgColor.get(i);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                    bgColor.get(i).click();
                }else {
                    bgColor.get(i).click();
                }

                String ccBgColor = getWebElement("CC_BACKGROUND_COLOR").getText(); // e.g Background color: Black
                logger.info("\t Background Color Selected :" + ccBgColor);
                flag = flag && colorsName[i].equalsIgnoreCase(ccBgColor);
                // issue id
                if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-large")) {
                    String ccPreviewBgColor = getWebElement("CC_PREVIEW_TEXT_BG").getCssValue("background-color");
                    logger.info("\t Background color of Preview Text Selected :" + ccPreviewBgColor);
                    flag = flag && bgColorsCode[i].equalsIgnoreCase(ccPreviewBgColor);
                }
            }
            if(!flag){
                logger.error("Error in Background color selection");
                return false;
            }
            logger.info("verified background color selection is working fine");

            // select Windows  colors
            ccWinColor = getWebElementsList("CC_WINDOW_COLOR_SELECTOR");
            logger.info("\n Color Count Value in Windows Color:" + ccWinColor.size() + "\n");
            logger.info("\n*---------Verify Window Color Selection Panel---------*\n");
            for (int i = 0; i < ccWinColor.size(); i++) {
                if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall")){
                    WebElement element = ccWinColor.get(i);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                    ccWinColor.get(i).click();
                }else {
                    ccWinColor.get(i).click();
                }
                String ccWindowColor = getWebElement("CC_WINDOW_COLOR").getText();
                logger.info("\t Window Color Selected :" + ccWindowColor);
                flag = flag && colorsName[i].equalsIgnoreCase(ccWindowColor);
                // issue id
                if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-large")) {
                    String ccPreviewWinColor = getWebElement("CC_PREVIEW_WIN_COLOR").getCssValue("background-color");
                    logger.info("\t Window color of Preview Text Selected :" + ccPreviewWinColor);
                    flag = flag && bgColorsCode[i].equalsIgnoreCase(ccPreviewWinColor);
                }
            }
            if(!flag){
                logger.error("Error in Windows color selection");
                return false;
            }
            logger.info("verified CC Windows color selection is working fine");

            return flag;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyCCOpacityPanel(String testName) {
        try {
            // verify CC Opacity Panel
            clickOnIndependentElement("CAPTION_OPACITY_PANEL");
            logger.info("\n*----------------------Verify Caption Opacity Panel--------------------*\n");
            Thread.sleep(2000);

            // select text Opacity
            WebElement slider = getWebElement("CC_TEXT_OPACITY_SELECTOR");
            slideSliderCaptionOpacity(slider);
            String ccTextOpacity = getWebElement("CC_TEXT_OPACITY").getText();
            logger.info("\t Text Opacity Selected :" + ccTextOpacity);
            String ccPreviewTextOpacity = getWebElement("CC_PREVIEW_TEXT").getCssValue("color");
            logger.info("\t Preview Text Opacity Selected :" + ccPreviewTextOpacity);
            if(!testName.contains("PlaybackFCCDefaultSettingTests")){
                    if (!validateCaptionOpacity(ccPreviewTextOpacity)){return false;}
            }
            logger.info("verified text opacity selection is working fine");

            // select Background Opacity
            WebElement slider1 = getWebElement("CC_BACKGROUND_OPACITY_SELECTOR");
            slideSliderCaptionOpacity(slider1);
            Thread.sleep(2000);
            String ccBgOpacity = getWebElement("CC_BACKGROUND_OPACITY").getText();
            logger.info("\t Background Opacity Selected :" + ccBgOpacity);
            String ccPreviewBgOpacity = getWebElement("CC_PREVIEW_TEXT_BG").getCssValue("background-color");
            logger.info("\t Preview Text Background Opacity Selected :" + ccPreviewBgOpacity);

            if(!testName.contains("PlaybackFCCDefaultSettingTests")){
                if (!validateCaptionOpacity(ccPreviewBgOpacity)){return false;}
            }

            logger.info("verified Background opacity selection is working fine");

            // select Windows Opacity
            WebElement slider3 = getWebElement("CC_WINDOW_OPACITY_SELECTOR");
            slideSliderCaptionOpacity(slider3);
            String ccWinOpacity = getWebElement("CC_WINDOW_OPACITY").getText();
            logger.info("\t Window Color Selected :" + ccWinOpacity);
            String ccPreviewWinOpacity = getWebElement("CC_PREVIEW_WIN_COLOR").getCssValue("background-color");
            logger.info("\t Window Opacity of Preview Text Selected :" + ccPreviewWinOpacity);

            if(!testName.contains("PlaybackFCCDefaultSettingTests")){
                if (!validateCaptionOpacity(ccPreviewWinOpacity)){return false;}
            }
            logger.info("verified Windows opacity selection is working fine");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyCCFonttypePanel() {
        try {
            // verify CC Font Type Panel
            clickOnIndependentElement("CC_FONT_TYPE_PANEL");
            logger.info("\n*--------------Verify Font Type Panel-------------------------*\n");
            Thread.sleep(2000);

            List<WebElement> ccFontType = getWebElementsList("CC_FONT_TYPE");
            logger.info("\t Font Type Count Value :" + ccFontType.size());

            while(isElementPresent("RIGHT_ARROW")){
                List<WebElement> ccFontType1 = getWebElementsList("CC_FONT_TYPE");
                for (int i = 0; i < ccFontType1.size(); i++) {
                    ccFontType1.get(i).click();
                    String ccFontTypeSelected = ccFontType1.get(i).getText();
                    logger.info("\n Language Selected - "+ccFontTypeSelected);
                    Thread.sleep(1000);
                    String ccPreviewTextFont = getWebElement("CC_PREVIEW_TEXT").getCssValue("font-family");
                    logger.info("\t Font type selected for CC Preview Text :" + ccPreviewTextFont);
                }

                if(isElementPresent("TOGGLING_ARROW")){
                    break;
                }
                clickOnIndependentElement("RIGHT_ARROW");
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
            boolean flag1 = true;

            clickOnIndependentElement("CC_FONT_SIZE_PANEL");
            logger.info("\n*--------------Verify CC Font Size Panel---------------------*\n");
            Thread.sleep(2000);
            ccFontSize = getWebElementsList("CC_FONT_SIZE_SELECTOR");
            logger.info("\t \t \t Font Size Count Value :" + ccFontSize.size());

            for (int i = 0; i < ccFontSize.size(); i++) {
                if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall")){
                    WebElement element = ccFontSize.get(i);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                    ccFontSize.get(i).click();
                }else {
                    ccFontSize.get(i).click();
                }
                String ccTextFontSize = getWebElement("CC_FONT_SIZE_SELECTED").getText();
                logger.info("\t Text Font Size Selected :" + ccTextFontSize);
                String ccPreviewTextFontSize[] = getWebElement("CC_PREVIEW_TEXT").getAttribute("style").split(";");
                String fontSizeInEm = null;
                int flag = 0;
                for (String cc : ccPreviewTextFontSize) {
                    if (!getBrowser().equalsIgnoreCase("safari")) {
                        if (cc.contains("font-variant-caps:")) {
                            String fontSize[] = ccPreviewTextFontSize[4].split(":");
                            fontSizeInEm = fontSize[1].trim();
                            flag = 1;
                            break;
                        }
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

                    if (getBrowser().equalsIgnoreCase("safari")){
                        fontSize = ccPreviewTextFontSize[8].split(":");
                        fontSizeInEm = fontSize[1].trim();
                    }
                }
                logger.info("\t font size in em : " + fontSizeInEm);
             //   Assert.assertEquals(ccFonts[i], ccTextFontSize); //verify font size selected
                flag1 = flag1 && ccFonts[i].equalsIgnoreCase(ccTextFontSize);

                if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-large")) {
                    if (!font_size_large[i].equals(fontSizeInEm)) {return false;}
                }
                if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-medium")) {
                    if (!font_size_medium[i].equals(fontSizeInEm)){return false;}
                }
                if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-small")) {
                    if (!font_size_small[i].equalsIgnoreCase(fontSizeInEm)){return false;}
                }
                if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall")) {
                    if (!font_size_xsmall[i].equals(fontSizeInEm)){return false;}
                }
            }

            logger.info("verified Font Size selection is working fine");
            return flag1;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyCCTextEnhancementPanel() {
        try {
            String textEnName[] = {"Uniform", "Depressed", "Raised", "Shadow"};
            String textEnCode[] = {"none", "rgb(255, 255, 255) 1px 1px 0px", "rgb(255, 255, 255) -1px -1px 0px, rgb(0, 0, 0) -3px 0px 5px", "rgb(26, 26, 26) 2px 2px 2px"};
            String textEnCodeForIE[]={"none","1px 1px white","-1px -1px white, -3px 0px 5px black","2px 2px 2px #1a1a1a"};
            clickOnIndependentElement("CC_TEXT_ENHANCEMENT");
            logger.info("\n*---------------Verify CC Text Enhancement Panel--------------*\n");
            Thread.sleep(2000);

            ccTextEnhancement = getWebElementsList("CC_TEXT_ENHANCEMENT_SELECTOR");
            logger.info("\t Text Enhancement Type Count Value :" + ccTextEnhancement.size());
            for (int i = 0; i < ccTextEnhancement.size(); i++) {

                if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall")){
                    WebElement element = ccTextEnhancement.get(i);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                    ccTextEnhancement.get(i).click();
                }else {
                    ccTextEnhancement.get(i).click();
                }


                String ccTextEnh = getWebElement("CC_FONT_SIZE_SELECTED").getText();
                logger.info("\t Text Enhancement Selected :" + ccTextEnh);
                String ccPreviewTextEnh = getWebElement("CC_PREVIEW_TEXT").getCssValue("text-shadow");
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

    public boolean clearCache() throws Exception {
        for (int i = 0; i < 20; i++) {
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
            logger.error("Horizontal cc option is not present");
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
            if(!getBrowser().equalsIgnoreCase("safari")){
                result = result && setCCOpacity();
            }
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
            logger.error("Error while setting cc color selection");
            return false;
        }
    }

    public HashMap<String,String> getCCColorSelection(){
        waitOnElement("COLOR_SELECTION_PANEL", 30000);
        clickOnIndependentElement("COLOR_SELECTION_PANEL");

        String ccTextColor, ccBgColor, ccWinColor;
        HashMap<String,String> ccColorMap = new HashMap<String,String>();

        ccTextColor = getWebElement("CC_TEXT_COLOR").getText();
        ccBgColor = getWebElement("CC_BACKGROUND_COLOR").getText();
        ccWinColor = getWebElement("CC_WINDOW_COLOR").getText();

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
            slider = getWebElement("CC_TEXT_OPACITY_SELECTOR");
            width = slider.getSize().getWidth();
            move.dragAndDropBy(slider,(width*20)/100,0).build().perform();

            slider = getWebElement("CC_BACKGROUND_OPACITY_SELECTOR");
            width = slider.getSize().getWidth();
            move.dragAndDropBy(slider,(width*20)/100,0).build().perform();

            slider = getWebElement("CC_WINDOW_OPACITY_SELECTOR");
            width = slider.getSize().getWidth();
            move.dragAndDropBy(slider,(width*20)/100,0).build().perform();
            return true;
        }catch (Exception e){
            logger.error("Error while setting CC opacity");
            e.printStackTrace();
            return false;
        }
    }

    public boolean setFontSize(){
        try{
            ccFontSize.get(2).click();
            return true;
        }catch (Exception e){
            logger.error("Error while setting CC font size");
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
            logger.error("Error while setting CC Text Enhancement");
            e.printStackTrace();
            return false;
        }
    }

    public String getTextEnhancement() throws Exception{
        clickOnIndependentElement("CC_TEXT_ENHANCEMENT");
        Thread.sleep(2000);
        String ccPreviewTextEnh = getWebElement("CC_PREVIEW_TEXT").getCssValue("text-shadow");
        logger.info("\t Text Enhancement Selected :" + ccPreviewTextEnh);
        return ccPreviewTextEnh;
    }

    public boolean setFontType(){
        try{
            List<WebElement> ccFontType = getWebElementsList("CC_FONT_TYPE");
            Thread.sleep(1000);
            ccFontType.get(1).click();
            return true;
        }catch (Exception e){
            logger.error("Error while setting CC font type");
            e.printStackTrace();
            return false;
        }
    }

    public String getFontType() throws Exception {
        clickOnIndependentElement("CC_FONT_TYPE_PANEL");
        Thread.sleep(2000);
        String ccPreviewTextFont = getWebElement("CC_PREVIEW_TEXT").getCssValue("font-family");
        return ccPreviewTextFont;
    }

    public String getCCLanguagePreviewText() {
        String previewTextDefault = getWebElement("CC_PREVIEW_TEXT").getText();
        return previewTextDefault;
    }

    public String getCCFontSizePreviewText() throws Exception{
        clickOnIndependentElement("CC_FONT_SIZE_PANEL");
        Thread.sleep(2000);
        String ccTextFontSize = getWebElement("CC_FONT_SIZE_SELECTED").getText();
        logger.info("\t Text Font Size Selected :" + ccTextFontSize);
        return ccTextFontSize;
    }

    public HashMap<String,String> getCCOpacityValues(){
        clickOnIndependentElement("CAPTION_OPACITY_PANEL");

        String ccTextOpacity, ccBgOpacity, ccWinOpacity;
        HashMap<String,String> ccOpacityMap = new HashMap<String,String>();

        clickOnIndependentElement("CAPTION_OPACITY_PANEL");
        ccTextOpacity = getWebElement("CC_PREVIEW_TEXT").getCssValue("color");
        ccBgOpacity = getWebElement("CC_PREVIEW_TEXT_BG").getCssValue("background-color");
        ccWinOpacity = getWebElement("CC_PREVIEW_WIN_COLOR").getCssValue("background-color");

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
            lang = getWebElementsList("LANG_LIST");
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
            e.printStackTrace();
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


    public void slideSliderCaptionOpacity(WebElement slider){
        if (getBrowser().equalsIgnoreCase("safari")) {
            ((JavascriptExecutor) driver).executeScript("function simulate(f,c,d,e){var b,a=null;for(b in eventMatchers)if(eventMatchers[b].test(c)){a=b;break}if(!a)return!1;document.createEvent?(b=document.createEvent(a),a==\"HTMLEvents\"?b.initEvent(c,!0,!0):b.initMouseEvent(c,!0,!0,document.defaultView,0,d,e,d,e,!1,!1,!1,!1,0,null),f.dispatchEvent(b)):(a=document.createEventObject(),a.detail=0,a.screenX=d,a.screenY=e,a.clientX=d,a.clientY=e,a.ctrlKey=!1,a.altKey=!1,a.shiftKey=!1,a.metaKey=!1,a.button=1,f.fireEvent(\"on\"+c,a));return!0} var eventMatchers={HTMLEvents:/^(?:load|unload|abort|error|select|change|submit|reset|focus|blur|resize|scroll)$/,MouseEvents:/^(?:click|dblclick|mouse(?:down|up|over|move|out))$/}; " +
                            "simulate(arguments[0],\"mousedown\",0,0); simulate(arguments[0],\"mousemove\",arguments[1],arguments[2]); simulate(arguments[0],\"mouseup\",arguments[1],arguments[2]); ",
                    slider);
        }else {
            Actions move = new Actions(driver);
            int width = slider.getSize().getWidth();
            move.dragAndDropBy(slider, (width * 25) / 100, 0).build().perform();
        }
    }

    public boolean validateCaptionOpacity(String opacityValue){
        if (!getBrowser().equalsIgnoreCase("safari")){
            if (opacityValue.equals("rgba(0, 0, 0, 0.8)")){
                return true;
            }else {
                logger.error(opacityValue + "is not matching with rgba(0, 0, 0, 0.8)");
                return false;
            }
        }else{
            if (opacityValue.equals("rgba(0, 0, 0, 0)")){
                return true;
            }
        }
        logger.error(opacityValue + "value is not matching with rgba(0, 0, 0, 0)");
        return false;
    }

    public boolean discoveryCheck(){
        try{
            if (isElementPresent("DISCOVERY_CLOSE")){
                Thread.sleep(5000);
                clickOnIndependentElement("DISCOVERY_CLOSE");
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

}