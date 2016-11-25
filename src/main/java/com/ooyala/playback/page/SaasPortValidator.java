package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by snehal on 23/11/16.
 */

public class SaasPortValidator extends PlayBackPage implements PlaybackValidator{

    String embedCode = "x5aDhnMzE6XQrMEzt_g5OeqMeX4tuvln";
    String sasportUrl= "http://sasport.us-east-1.atlantis.services.ooyala.com/static/?tab=rights_locker&pcode=BjcWYyOu1KK2DiKOkF41Z2k0X57l&accountId=dulari_qa&rlEnv=Production";

    public static Logger Log = Logger.getLogger(SaasPortValidator.class);

    public SaasPortValidator(WebDriver webDriver){
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        /**
         * Here we will tell Facile to add the page elements of our Login Page
         */
        addElementToPageElements("sasport");
    }

    @Override
    public void validate(String element, int timeout) throws Exception {
            if(element.contains("CREATE_ENTITLEMENT")){
            try{
                boolean entitlementPresent;
                searchEntitlement();
                try {
                    isElementVisible("ENTITLEMENT");
                    entitlementPresent = true;
                } catch(Exception e){
                    entitlementPresent = false;
                }
                if(entitlementPresent){
                    clickOnIndependentElement("DELETE_BTN");
                    logger.info("Deleted asset from entitlement");
                    createEntitlement();
                    logger.info("Created the entitlement");
                }else {
                    createEntitlement();
                    logger.info("Created the entitlement");
                }
            }catch (Exception e){
                e.getMessage();
            }
        }else{
                searchEntitlement();
                waitOnElement("DISPLAY_BTN",5);
                if (!isElementPresent("DISPLAY_BTN")){
                    throw new Exception("Device is not registered for entitlement on sasport.");
                }
                clickOnIndependentElement("DISPLAY_BTN");
                waitOnElement("PLAYREADY", 5);
                logger.info("Device gets registered for entitlement on sasport.");
        }
    }

    public void searchEntitlement() throws Exception {
        driver.get(sasportUrl);
        waitOnElement("SEARCH_BTN",10);
        clickOnIndependentElement("SEARCH_BTN");
    }

    public void createEntitlement() throws Exception {
        waitOnElement("CREATE_ENTITLEMENT_BTN",10);
        clickOnIndependentElement("CREATE_ENTITLEMENT_BTN");
        waitOnElement("CREATE_ENTITLEMENT_ID", 10);
        writeTextIntoTextBox("CREATE_ENTITLEMENT_ID", "embedCode");
        writeTextIntoTextBox("EXTERNAL_PRODUCT_ID", "abc");
        writeTextIntoTextBox("MAX_DEVICES", "2");
        clickOnIndependentElement("CREATE_BTN");
    }
}
