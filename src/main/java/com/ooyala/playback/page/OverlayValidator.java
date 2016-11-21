package com.ooyala.playback.page;

import static java.lang.System.out;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;

public class OverlayValidator extends PlayBackPage implements PlaybackValidator {

	public OverlayValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("adoverlay");
		addElementToPageElements("fullscreen");
	}

	public void validate(String element, int timeout) throws Exception {
		try {
            waitOnElement("overlayCloseBtn", 40);
            clickOnIndependentElement("overlayCloseBtn");
            waitOnElement(element, timeout);

        } catch (Exception e) {
            out.print("No close button for Overlay");
            out.println("No close button seen in normal screen on Overlay....trying in Fullscreen \n");
            FullScreenValidator fullScreenValidator = PlayBackFactory.getInstance(driver).getFullScreenValidator(); // TODO can we move full screen to action
            fullScreenValidator.validate("",60);
            
            if (!getBrowser().equalsIgnoreCase("safari") && !getPlatform().equalsIgnoreCase("Android")) {
            	waitOnElement("overlayCloseBtn", 40);
                clickOnIndependentElement("overlayCloseBtn");
                System.out.println("Clicked on overlay close button in fullscreen screen \n");
                System.out.print("Overlay gets closed");
            }
            Thread.sleep(1000);
            waitOnElement("nonlinearAdPlayed_1",160);
            
            if (getBrowser().equalsIgnoreCase("safari")) {
            	clickOnIndependentElement("normalScreen");
            } else {
                try {
                	SeekValidator seek = PlayBackFactory.getInstance(driver).getSeekValidator();
                	seek.seek("15"); // TODO this is also as an action?
                } catch (Exception e1) {
                    clickOnHiddenElement("normalScreen");
                }
            }
            
            
            
        }

		
	}

}
