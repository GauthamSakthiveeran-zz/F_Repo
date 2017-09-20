package com.ooyala.playback.apps.validators;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import com.relevantcodes.extentreports.LogStatus;
import com.ooyala.playback.apps.PlaybackApps;

import io.appium.java_client.AppiumDriver;

//TODO:  add timeout variable to function signature
public class FileEventValidator  extends PlaybackApps {
    final static Logger logger = Logger.getLogger(FileEventValidator.class);
    
    private int count ;

    public FileEventValidator(AppiumDriver driver){
        super(driver);    
    		count=0;
        PageFactory.initElements(driver, this);
    }

    public boolean verifyEvent(Events event,String consoleMessage,int timeout){
         
        int returncount;         

        // Paused  Verification
        boolean status=false;
        long startTime = System.currentTimeMillis(); //fetch starting time
        while(!status && (System.currentTimeMillis()-startTime)<timeout) {


            returncount = ParseEventsFile.parseeventfile(event,count);


            if (returncount== -1){
            		extentTest.log(LogStatus.INFO, "Event not found in log file");
                status=false;
            }
            else{
                status=true;
                count=returncount;
            }            

            if (status == true) {
                extentTest.log(LogStatus.PASS, consoleMessage);
                logger.info(consoleMessage);
            }
        }
        if(!status){
            Assert.assertTrue(status);
        }
        return status;
    }
}
