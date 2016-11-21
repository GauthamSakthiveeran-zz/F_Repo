package com.ooyala.playback.page;

import static java.lang.System.out;
import static java.lang.Thread.sleep;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

public class PlayPauseOnScreenValidator extends BaseValidator{

	public PlayPauseOnScreenValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
	    addElementToPageElements("startscreen");
	    addElementToPageElements("pause");
	    addElementToPageElements("play");
	}

	@Override
	public void validate(String element, int timeout) throws Exception {
		//Pause the video
        try {
            waitOnElement("stateScreens", 50);
            clickOnIndependentElement("stateScreens");
            out.println("Clicked on screen to pause the video");
        } catch (Exception e) {
            Actions action = new Actions(driver);
            action.moveToElement(getWebElement("pauseButton")).build().perform();
            sleep(5000);
            try {
            	clickOnIndependentElement("pauseButton");
                out.println("Clicked on Pause button to pause the video");
            } catch (Exception e1) {
            	clickOnIndependentElement("stateScreenSelectable");
                out.println("Clicked on screen which is selectable to pause the video");
            }
        }

        sleep(5000);

        // Play the video
        try {
        	 waitOnElement("stateScreenSelectable", 50);
        	 clickOnIndependentElement("stateScreenSelectable");
        } catch (Exception e) {
            Actions action = new Actions(driver);
            action.moveToElement(getWebElement("playButton")).build().perform();
            action.moveToElement(getWebElement("playButton")).build().perform();
            sleep(5000);
            clickOnIndependentElement("playButton");
        }

        sleep(5000);
		
	}

}
