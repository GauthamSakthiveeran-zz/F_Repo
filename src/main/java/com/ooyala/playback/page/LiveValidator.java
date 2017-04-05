package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.url.UrlObject;

public class LiveValidator extends PlayBackPage implements PlaybackValidator {

	private static Logger logger = Logger.getLogger(LiveValidator.class);

	public LiveValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		return true;
	}

    /*
        isChannelIdPresent method return true if for particular asset Channel needs in Live state.
     */
	public boolean isChannelIdPresent(UrlObject url){
		if (url.getChannelId()!=null){
			return true;
		}
		return false;
	}

    public boolean isGettingError(){
        if (driver.executeScript("return pp.getErrorCode()").toString()==null){
            return true;
        }
        return false;
    }
}
