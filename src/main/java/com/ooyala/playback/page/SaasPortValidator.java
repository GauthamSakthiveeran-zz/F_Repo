package com.ooyala.playback.page;

import com.ooyala.qe.common.util.PropertyReader;
import org.apache.log4j.Logger;
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
	public boolean validate(String element, int timeout) throws Exception {
		if (element.contains("CREATE_ENTITLEMENT")) {
			try {
				data = parseURL();
				embedCode=data.get("ec");
				PropertyReader properties = PropertyReader.getInstance("urlData.properties");
				sasportUrl=properties.getProperty("sasport_url");
				if (!searchEntitlement())
					return false;
				deleteDevices();
				Thread.sleep(5000);

				if (isElementVisible("ENTITLEMENT")) {
					if (!clickOnIndependentElement("DELETE_BTN"))
						return false;
					logger.info("Deleted asset from entitlement");
					Thread.sleep(5000);
					if (!createEntitlement())
						return false;
					logger.info("Created the entitlement");
				} else {
					if (!createEntitlement())
						return false;
					logger.info("Created the entitlement");
				}
			} catch (Exception e) {
				e.getMessage();
				return false;
			}
		} else {
			if (!searchEntitlement())
				return false;
			if (!waitOnElement("DISPLAY_BTN", 15000)){
				logger.error("Device is not registered");
				return false;
			}

			if (!isElementPresent("DISPLAY_BTN")) {
				throw new Exception(
						"Device is not registered for entitlement on sasport.");
			}
			if (!clickOnIndependentElement("DISPLAY_BTN"))
				return false;
			if (!waitOnElement("DRM_POLICY", 10000))
				return false;
			logger.info(getWebElement("DRM_POLICY").getText());

			logger.info("Device gets registered for entitlement on sasport.");
		}
		return true;
	}

	public boolean searchEntitlement() throws Exception {
		driver.get(sasportUrl);
		return waitOnElement("SEARCH_BTN", 30000)
				&& clickOnIndependentElement("SEARCH_BTN") && waitOnElement("CREATE_ENTITLEMENT_BTN", 30000);
	}

	private boolean createEntitlement() throws Exception {
		waitOnElement("CREATE_ENTITLEMENT_BTN", 30000);
		Thread.sleep(2000);
		clickOnIndependentElement("CREATE_ENTITLEMENT_BTN");
		Thread.sleep(5000);
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
	}

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
					Thread.sleep(5000);

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