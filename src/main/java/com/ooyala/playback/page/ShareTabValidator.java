package com.ooyala.playback.page;

import com.ooyala.playback.page.BaseValidator;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

/**
 * Created by soundarya on 11/8/16.
 */
public class ShareTabValidator extends BaseValidator {
    public static Logger Log = Logger.getLogger(ShareTabValidator.class);

    public ShareTabValidator(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("sharetab");
    }
    @Override
    public void validate(String element,int timeout)throws Exception {
        try {
            waitOnElement("shareBtn", 60);
            clickOnIndependentElement("shareBtn");
            Thread.sleep(5000);
        } catch (Exception e) {
            System.out.println("exception \n" + e.getMessage());
            clickOnIndependentElement("moreOptionItem");
            waitOnElement("shareBtn", 60);
            clickOnIndependentElement("shareBtn");
            if(!(isElementPresent("shareTab")))
                clickOnIndependentElement("shareBtn");
        }
        waitOnElement("contentScreen", 60);
        if(!(isElementPresent("shareTab")))
            clickOnIndependentElement("shareBtn");
        Thread.sleep(2000);

        String shareTab = readTextFromElement("shareTab");
        Log.info("Share Tab value "+ shareTab);
        Thread.sleep(1000);

        String embedTab = readTextFromElement("embedTab");
        Log.info("Text in Embed Tab  "+ embedTab);

        Assert.assertTrue(shareTab.equalsIgnoreCase("Compartir"), "Localization Failing");
        Assert.assertTrue(embedTab.equalsIgnoreCase("Insertar"),"Localization Failing");
        clickOnIndependentElement("shareClose");
        Log.info("ShareTab and Embed Tab localization verified");

    }

}
