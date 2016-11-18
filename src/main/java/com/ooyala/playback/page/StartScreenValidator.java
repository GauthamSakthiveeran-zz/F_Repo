package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

/**
 * Created by soundarya on 11/16/16.
 */
public class StartScreenValidator extends BaseValidator {
    public static Logger Log = Logger.getLogger(StartScreenValidator.class);

    public StartScreenValidator(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("startscreen");
    }
    @Override
    public void validate(String element,int timeout)throws Exception {

        waitOnElement("stateScreenPoster",60);

        //get the style attribute of class startScreenPoster which contailns preview image url so that we compare it.
        String value=getWebElement("stateScreenPoster").getAttribute("style");
        String url=value.replaceAll(".*\\(|\\).*", "");
        url =url.replaceAll("^\"|\"$", "");
        Assert.assertEquals(url, "http://ak.c.ooyala.com/piMXdiczqydplt6ojmhNzdfAERdgVvaj/3Gduepif0T1UGY8H4xMDoxOjBiO1q_Vi", "Preview Image is not matching");

        //get title of video
        try {
            String startScreenTitle = getWebElement("stateScreenTitle").getText();

            //get Discription of video
            String description = getWebElement("stateScreenDec").getText();

            String title = ((JavascriptExecutor) driver).executeScript("var title=pp.getTitle();" +
                            "{return title;}").toString();

            System.out.println("title1:"+title);
            String desc = ((JavascriptExecutor) driver).executeScript("var description=pp.getDescription();" +
                    "{return description;}").toString();

            System.out.println("Description:"+desc);

            Assert.assertTrue(startScreenTitle.equalsIgnoreCase(title),"Title is not matching on start screen");
            Assert.assertTrue(description.equalsIgnoreCase(desc),"Description is not matching on Start Screen");
        }catch(Exception e)
        {
            System.out.println("Title or description is failing");
        }

    }
}
