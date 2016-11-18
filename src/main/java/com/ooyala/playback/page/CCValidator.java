package com.ooyala.playback.page;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.ooyala.facile.page.WebPage;

/**
 * Created by soundarya on 11/3/16.
 */
public class CCValidator extends BaseValidator {

	public static Logger logger = Logger.getLogger(CCValidator.class);
	
	public CCValidator(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("cc");
        addElementToPageElements("play");
        addElementToPageElements("controlBar");
	}

    public void validate(String element,int timeout)throws Exception {
        try { try {
            waitOnElement("ccBtn", 60);
        }catch (Exception e) {
            clickOnIndependentElement("moreOptionIcon");
            waitOnElement("ccBtn", 60);
        }
            boolean ccbutton = isElementPresent("ccBtn");
            Assert.assertEquals(ccbutton, true, "ClosedCaption button is not present on player");
            logger.info("Verified the presence of ClosedCaption button ");
            if (isElementPresent("ccPanelClose")) {
                clickOnIndependentElement("ccPanelClose");
                clickOnIndependentElement("playButton");
            }

        } catch (Exception e) {
            System.out.println("closedCaption button is not  present\n");
        }
        checkClosedCaptionLanguages();
        logger.info("Verified the ClosedCaption button languages");
        Thread.sleep(1000);
        try {
            clickOnIndependentElement("ccBtn");
        } catch (Exception e) {
            clickOnIndependentElement("moreOptionIcon");
            waitOnElement("ccBtn", 60);
            clickOnIndependentElement("ccBtn");
        }
        Thread.sleep(1000);

        closedCaptionMicroPanel();
        logger.info("Verified  ClosedCaption button Micropanel ");

        if (!(isElementPresent("closedCaptionPanel"))) {
            clickOnIndependentElement("ccBtn");
        }

        boolean ccpanel = isElementVisible("closedCaptionPanel");
        Assert.assertEquals(ccpanel, true, "closedCaption languages panel is not present");
        Thread.sleep(1000);
        waitOnElement("ccsSwitch", 60);

        clickOnIndependentElement("ccSwitchContainer");
        clickOnIndependentElement("ccPanelClose");

        logger.info("Verified ClosedCaption ");

        if (isElementPresent("playButton"))
            clickOnIndependentElement("playButton");
        Thread.sleep(2000);

        Assert.assertEquals(isElementPresent("ccmode_disabled"), true, "ClosedCaption is not disabled");
        Thread.sleep(2000);

        Assert.assertEquals(isElementPresent("ccmode_showing"), true, "ClosedCaption is not showing");
    }

    protected void closedCaptionMicroPanel() throws Exception
    {
        try {
            waitOnElement("ccPopoverHorizontal", 10);
            boolean horizontal_CC_Option = isElementPresent("ccPopoverHorizontal");
            System.out.println(horizontal_CC_Option);
            if (horizontal_CC_Option) {
                waitOnElement("ccSwitchContainerHorizontal", 20);
                waitOnElement("ccMoreCaptions", 10);
                waitOnElement("ccCloseButton", 10);
                clickOnIndependentElement( "ccMoreCaptions");
                logger.info("Verified presence of closedCaptionMicroPanel ");
            }
        } catch (Exception e) {
            System.out.println("Horizontal cc option is not present");
        }

    }

    protected  void checkClosedCaptionLanguages() throws  Exception{
        ArrayList<String> langlist = ((ArrayList<String>)(((JavascriptExecutor) driver).executeScript("var attrb = pp.getCurrentItemClosedCaptionsLanguages().languages;" +"{return attrb;}")));
        System.out.println("Closed Caption Available Languages: " + langlist);
        for (int i = 0; i < langlist.size(); i++) {
            ((JavascriptExecutor) driver).executeScript("pp.setClosedCaptionsLanguage(\"" + langlist.get(i) + "\")");
            WebElement ccElement1 = (new WebDriverWait(driver, 60)).until(ExpectedConditions.presenceOfElementLocated(By.id("cclanguage_" + langlist.get(i))));
        }
    }
}
