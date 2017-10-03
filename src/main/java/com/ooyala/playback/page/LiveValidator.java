package com.ooyala.playback.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.url.UrlObject;

public class LiveValidator extends PlayBackPage implements PlaybackValidator {


	public LiveValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("live");
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		return isElementPresent("LIVE");
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
        if (new PlayBackFactory(driver, extentTest).getPlayerAPIAction().getErrorCode()==null){
            return true;
        }
        return false;
    }
}
