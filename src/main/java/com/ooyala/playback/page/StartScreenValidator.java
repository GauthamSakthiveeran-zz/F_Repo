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
        addElementToPageElements("");
    }
    @Override
    public void validate(String element,int timeout)throws Exception {

        //Wait for startScreenPoster to load
      /*  waitOnElement("stateScreenPoster",60);

        //get the style attribute of class startScreenPoster which contailns preview image url so that we compare it.
        String value=driver.findElement(locators.getobjectLocator("stateScreenPoster")).getAttribute("style");
        String url=value.replaceAll(".*\\(|\\).*", "");
        url =url.replaceAll("^\"|\"$", "");
        Assert.assertEquals(url, "http://ak.c.ooyala.com/piMXdiczqydplt6ojmhNzdfAERdgVvaj/3Gduepif0T1UGY8H4xMDoxOjBiO1q_Vi", "Preview Image is not matching");


        //get title of video
        try {
            String startScreenTitle = driver.findElement(locators.getobjectLocator("stateScreenTitle")).getText();

            //get Discription of video
            String description = driver.findElement(locators.getobjectLocator("stateScreenDec")).getText();


            Object title = ((JavascriptExecutor) driver)
                    .executeScript("var title=pp.getTitle();" +
                            "{return title;}");
            String title1=(String)title;

            System.out.println("title1:"+title1);
            Object description1 = ((JavascriptExecutor) driver)
                    .executeScript("var description=pp.getDescription();" +
                            "{return description;}");
            String description12=description.toString();

            System.out.println("Description:"+description12);

            Assert.assertTrue(startScreenTitle.equalsIgnoreCase(title1),"Title is not matching on start screen");
            Assert.assertTrue(description.equalsIgnoreCase(description12),"Description is not matching on Start Screen");
        }catch(Exception e)
        {
            System.out.println("Title or description is failing");
        }*/

    }
}
