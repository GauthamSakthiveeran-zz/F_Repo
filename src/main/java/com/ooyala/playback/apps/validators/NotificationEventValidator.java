package com.ooyala.playback.apps.validators;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.ooyala.playback.apps.PlaybackApps;

import io.appium.java_client.AppiumDriver;

public class NotificationEventValidator extends PlaybackApps implements Validators {
	
	private static Logger logger = Logger.getLogger(NotificationEventValidator.class);

	public NotificationEventValidator(AppiumDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("event");
	}
	
	int eventVerificationCount= 0 ;

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		
		if(!waitOnElement("NOTIFICATION_AREA", 10)) {
			logger.error("NOTIFICATION_AREA not found");
			return false;
		}
		
		String notifiationEvents = getWebElement("NOTIFICATION_AREA").getText();
		
		
		int returncount = 0;
        boolean status = false;
        long startTime = System.currentTimeMillis();
        

        while((System.currentTimeMillis() - startTime) < timeout) {
        	logger.info("Waiting for notification >>>> : " + element);
            returncount = verifyNotificationEventPresence(notifiationEvents, element, eventVerificationCount);

            if (returncount == -1)
                status = false;
            
            else {
                status = true;
                eventVerificationCount = returncount;
            }            

            if (status == true) {
            	return true;
            }
        }
        if(!status) {
        	logger.error("ACTUAL notification : "  + getNotificationEvents());
            return false;
        }
        return false;
	}
	
	private String getNotificationEvents() {
    	int counter = 0;
    	int timeout = 10;
    	String notifications = null;
    	while (counter < timeout) {
    		notifications = getWebElement("NOTIFICATION_AREA").getText();
    		if (notifications != null)
    			return notifications;
    		counter ++;
    	}
    	throw new NullPointerException("Notification events is NULL !!!");
    }
	
	private int verifyNotificationEventPresence(String notificationEvents, String eventToBeVerified, int count) {
        try {
        	
        	String lines[] = parseNotificationEvents(notificationEvents);
            for (String line : lines){

                if(line.contains("state: ERROR")) {
                	logger.error("APP CRASHED !!!!! ");
                    Assert.fail("App is crashed during playback");
                    
                }
                if(line.contains(eventToBeVerified)) {
                  if (getLatestCount(line) > count) {
                	  	logger.info("Event Recieved From SDK AND Sample App :- " + line);
                        count = getLatestCount(line);
                        return count;
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Exception while parsing notification events " + e);
            e.printStackTrace();
        }

        return -1;
    }

	private String[] parseNotificationEvents(String notificationEvents) {
    	String[] lines = notificationEvents.split("::::::::::");
    	return lines;
    	
    }
    
    
    private int getLatestCount(String line){
        int count;
        String[] tokens = line.split(":");
        String trimToken = tokens[4].trim();
        count = Integer.parseInt(trimToken);
        return count;
    }



}