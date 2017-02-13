package com.ooyala.playback.page;

import com.ooyala.qe.common.util.PropertyReader;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.Map;

/**
 * Created by snehal on 23/11/16.
 */

public class SaasPortValidator extends PlayBackPage implements
		PlaybackValidator {

	private static Logger logger = Logger.getLogger(SaasPortValidator.class);
	private String embedCode;
	private String pCode;
	private String accountId;
	private String sasportUrl;
	Map<String, String> data;

	public SaasPortValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("sasport");
	}

	@Override
	public boolean validate(String element, int timeout) {
		// Create an entitlement for an asset.
		try {
			data = parseURL();
			embedCode = data.get("ec");
			pCode = data.get("pcode");
			PropertyReader properties = PropertyReader.getInstance("urlData.properties");
			sasportUrl = properties.getProperty("sasport_url");
			accountId = properties.getProperty("account_id");
			if (!searchEntitlement())
				return false;
			deleteDevices();
			Thread.sleep(2000);

			// Checking the presence of Delete button
			if (isElementPresent(By.xpath("//button[@contentid='" + embedCode + "']"))) {
				if (!clickOnIndependentElement(By.xpath("//button[@contentid='" + embedCode + "']")))
					return false;
				logger.info("Deleted asset from entitlement");
				Thread.sleep(2000);
				if (!createEntitlement())
					return false;
				logger.info("Created the entitlement");
			} else {
				if (!createEntitlement())
					return false;
				logger.info("Created the entitlement");
			}
			return true;
		} catch (Exception e) {
			logger.error("Error in creating entitlement" + e.getMessage());
			return false;
		}
	}

	// Checking if device is registered or not for that particular entitlement
	public boolean checkDeviceRegistration(){
		try{
			if (!searchEntitlement())
				return false;

			if (!waitOnElement(By.xpath(".//button[@contentid='"+embedCode+"']/../../../div[7]//a[contains(text(),'Display')]"),20000)){
				throw new Exception(
						"Device is not registered for entitlement on sasport.");
			}

			if (!clickOnIndependentElement(By.xpath(".//button[@contentid='"+embedCode+"']/../../../div[7]//a[contains(text(),'Display')]")))
				return false;

			if (!waitOnElement(By.xpath(".//button[@contentid='"+embedCode+"']/../../../div[7]//a[contains(text(),'Display')]/../div"), 10000))
				return false;

			logger.info("Device gets registered for entitlement on sasport.");
			return true;
		}catch(Exception e){
			logger.error("Error in serching device not sasport "+ e);
			return false;
		}
	}

	// Search Entitlements data for given pcode and account id.
	public boolean searchEntitlement() {
		try{
			driver.get(sasportUrl);
			waitOnElement("PCODE", 30000);
			writeTextIntoTextBox("PCODE",pCode);
			writeTextIntoTextBox("ACCOUNT_ID",accountId);
			selectDropDownByVisibleText("ENVIRONMENT","Production");
			Thread.sleep(2000);
			return waitOnElement("SEARCH_BTN", 30000)
					&& clickOnIndependentElement("SEARCH_BTN") && waitOnElement("CREATE_ENTITLEMENT_BTN", 30000);
		}catch (Exception e){
			logger.error("Error while serching entitlement"+e.getMessage());
			return false;
		}
	}

	// Fill the details after click on 'Create entitlement' button
	private boolean createEntitlement() {
		try{
			if(!waitOnElement("CREATE_ENTITLEMENT_BTN", 30000))
				return false;
			clickOnIndependentElement("CREATE_ENTITLEMENT_BTN");
			waitOnElement("CREATE_ENTITLEMENT_ID", 30000);
			writeTextIntoTextBox("CREATE_ENTITLEMENT_ID", embedCode);
			writeTextIntoTextBox("EXTERNAL_PRODUCT_ID", "abc");
			writeTextIntoTextBox("MAX_DEVICES", "2");
			clickOnIndependentElement("CREATE_BTN");;
			Thread.sleep(5000);
			if(!waitOnElement("CREATE_ENTITLEMENT_BTN", 30000)){
				logger.error("Entitlement is not getting created");
				return false;
			}
			logger.info("Entitlement created suceessfully");
			return true;
		}catch (Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}

	// Delete devices from Account Devices
	public boolean deleteDevices(){
		int noOfRegisteredDevices = 0;
		if(isElementPresent("NO_DEVICES_REGISTERED")) {
			logger.info("No Devices registered");
			return true;
		}
		else{
			noOfRegisteredDevices = getWebElementsList("ACCOUNT_DEVICES_LIST").size();
			for(int i = 1; i<=noOfRegisteredDevices; i++){
				try{
					clickOnHiddenElement("DELETE_REGISTERED_DEVICE");
					Thread.sleep(1000);

				}catch(Exception e){
					logger.info("Error While deleting registered devices");
					return false;
				}
			}
			logger.info("Registered Devices deleted : "+noOfRegisteredDevices);
		}
		return true;
	}
}