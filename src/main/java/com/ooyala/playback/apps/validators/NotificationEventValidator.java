package com.ooyala.playback.apps.validators;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.utils.CommandLineParameters;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;

public class NotificationEventValidator extends PlaybackApps implements Validators {

	private static Logger logger = Logger.getLogger(NotificationEventValidator.class);
	int eventVerificationCount=0;
	private int count;

	public NotificationEventValidator(AppiumDriver driver) {
		super(driver);
		count = 0;
		PageFactory.initElements(driver, this);
		addElementToPageElements("event");		
	}


	public boolean validateEvent(Events event, int timeout) throws Exception {
		if (System.getProperty(CommandLineParameters.PLATFORM).equalsIgnoreCase("ios")) {
			int returncount = 0;
			boolean status = false;
			long startTime = System.currentTimeMillis();

			while ((System.currentTimeMillis() - startTime) < timeout) {
				logger.info("Waiting for notification >>>> : " + event.getEvent());
				String notifiationEvents = getNotificationEvents();
				returncount = verifyNotificationEventPresence(notifiationEvents, event.getEvent(), eventVerificationCount);

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
		if (!status) {
			logger.error("Event not found !!!. Expected Event : " + event.getEvent());
			logger.error("ACTUAL notification : " + getNotificationEvents());
			return false;
		}
		return status;
		} else {
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
		                //extentTest.log(LogStatus.PASS, consoleMessage);
		                //logger.info(consoleMessage);
		            }
		        }
		        if(!status){
		            Assert.assertTrue(status);
		        }
		        return status;
		    
		}
	}

	private String getNotificationEvents() {
		int counter = 0;
		int timeout = 10;
		String notifications = null;
		while (counter < timeout) {
			if(waitOnElement("NOTIFICATION_AREA"))
				notifications = getWebElement("NOTIFICATION_AREA").getText();
			if (notifications != null)
				return notifications;
			counter++;
		}
		throw new NullPointerException("Notification events is NULL !!!");
	}

	private int verifyNotificationEventPresence(String notificationEvents, String eventToBeVerified, int count) {
		try {

			String lines[] = parseNotificationEvents(notificationEvents);
			if(lines.length<count){
				count=0;
				eventVerificationCount=0;
			}
			for (String line : lines) {

				if (line.contains("state: ERROR")) {
					logger.error("APP CRASHED !!!!! ");
					Assert.fail("App is crashed during playback");

				}
				if (line.contains(eventToBeVerified)) {
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

	private int getLatestCount(String line) {
		int count;
		String[] tokens = line.split(":");
		String trimToken = tokens[4].trim();
		count = Integer.parseInt(trimToken);
		return count;
	}

	public boolean verifyEvent(Events event, int timeout) {
		try {
			if (validate(event.getEvent(), timeout)) {
				extentTest.log(LogStatus.PASS, event.getEvent());
				return true;
			} else {
				extentTest.log(LogStatus.FAIL, event.getFailureMessage());
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			extentTest.log(LogStatus.FAIL, event.getEvent(), e);
			return false;
		}
	}


	@Override
	public boolean validate(String element, int timeout) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
