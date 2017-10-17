package com.ooyala.playback.apps.actions;

import org.apache.log4j.Logger;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.facile.page.FacileWebElement;
import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.factory.PlayBackFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

public class SeekAction extends PlaybackApps implements Actions {

	private static Logger logger = Logger.getLogger(SeekAction.class);
	private PauseAction tapAction;

	public SeekAction(AppiumDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("seekbar");
		tapAction = new PlayBackFactory(driver, extentTest).getPauseAction();
	}

	private String slider;
	boolean seekFrwd;

	public SeekAction setSlider(String slider) {
		this.slider = slider;
		return this;
	}

	public SeekAction seekforward() {
		seekFrwd = true;
		return this;
	}

	@Override
	public boolean startAction(String seek) throws Exception {
		if (getPlatform().equalsIgnoreCase("ios")) {
			if (getPlatformVersion().startsWith("11") && !isV4) {
				seek += "_IOS11";
				slider += "_IOS11";
			}
			if (seekFrwd) {
				// seekFrwd = false;
				tapAction.tapScreenIfRequired();
				if (!seekVideoForward(slider, seek)) {
					logger.error("Unable to seek forward video.");
					tapAction.tapScreenIfRequired();
					if (!seekVideoForward(slider, seek)) {
						logger.error("Unable to seek forward video");
						return false;
					}
				}
			} else {
				tapAction.tapScreen();
				if (!seekVideoBack(slider, seek)) {
					logger.error("Unable to seek video.");
					tapAction.tapScreenIfRequired();
					if (!seekVideoBack(slider, seek)) {
						logger.error("Unable to seek forward video");
						return false;
					}
				}
			}
		} else {
			try {
				if (!seekVideo(seek)) {
					logger.error("Unable to seek Video, Seekbar was not present");
					return false;
				}
			} catch (Exception e) {
				logger.info("Seekbar is not present , let's tap on screen");
				tapOnScreen();
				if (!seekVideo(seek)) {
					logger.error("Unable to click on play pause.");
					return false;
				}
			}
		}
		return true;

	}

	private boolean seekVideoForward(String slider, String seekbar) throws InterruptedException {
		if (!waitOnElement(seekbar, 1000) || !waitOnElement(slider, 1000)) {
			tapAction.tapScreen();
		}
		int startx = getSliderPosition(slider);
		if (!waitOnElement(seekbar, 1000) || !waitOnElement(slider, 1000)) {
			tapAction.tapScreen();
		}
		Element seekbarElement = getSeekBarPosition(seekbar);
		logger.info("Seeking forward -------------------------  ");
		tapAction.tapScreenIfRequired();
		int seekForwardLength = (seekbarElement.getEndXPosition() - (startx + 1)) - 30;
		TouchAction touch = new TouchAction(driver);
		if (getPlatform().equalsIgnoreCase("ios"))
			touch.press((startx + 1), seekbarElement.getYposition()).moveTo(((startx + 1) + (seekForwardLength)),
					seekbarElement.getYposition() + seekbarElement.getYposition()).release().perform();
		else
			touch.longPress((startx + 1), seekbarElement.getYposition()).moveTo(((startx + 1) + (seekForwardLength)),
					seekbarElement.getYposition() + seekbarElement.getYposition()).release().perform();
		return true;
	}

	private Element getSeekBarPosition(String seekbar) throws InterruptedException {
		tapAction.waitAndTap();
		WebElement SEEK = this.getWebElement(seekbar);
		Point seekbarElementPos = SEEK.getLocation();
		Element seekbarElement = new Element();
		seekbarElement.setStartXPosition(seekbarElementPos.getX());
		seekbarElement.setYposition(seekbarElementPos.getY());
		seekbarElement.setWidth(SEEK.getSize().getWidth());
		seekbarElement.setEndXPosition(seekbarElement.getWidth() + seekbarElement.getStartXPosition());
		logger.info("SeekBarPosition : StartXPosition > " + seekbarElement.getStartXPosition() + ", "
				+ " EndXPosition > " + seekbarElement.getEndXPosition() + ", Width > " + seekbarElement.getWidth()
				+ " Yposition > " + seekbarElement.getYposition());
		return seekbarElement;

	}

	private boolean seekVideoBack(String slider, String seekbar) throws InterruptedException {

		if (!waitOnElement(slider, 1000)) {
			tapAction.tapScreen();
		}

		int startx = getSliderPosition(slider);

		if (!waitOnElement(seekbar, 1000)) {
			tapAction.tapScreen();
		}

		Element seekbarElement = getSeekBarPosition(seekbar);
		logger.info("Seeking Back------------");
		
		int seekBackLength = ((startx + 1) - seekbarElement.getStartXPosition()) / 2;
		TouchAction touch = new TouchAction(driver);
		tapAction.waitAndTap();
		if (getPlatform().equalsIgnoreCase("ios"))
			touch.press((startx + 1), seekbarElement.getYposition()).moveTo(((startx + 1) - seekBackLength),
					seekbarElement.getYposition() + seekbarElement.getYposition()).release().perform();
		else
			touch.longPress((startx + 1), seekbarElement.getYposition()).moveTo(((startx + 1) - seekBackLength),
					seekbarElement.getYposition() + seekbarElement.getYposition()).release().perform();
		return true;
	}

	private int getSliderPosition(String slider) throws InterruptedException {
		WebElement slide;
		try {
			slide = getWebElement(slider);
		} catch (Exception ex) {
			logger.info("Retry tapping.");
			tapAction.tapScreen();
			slide = getWebElement(slider);
		}
		
		int sliderXPosition = slide.getLocation().getX();
		logger.info("Slider X Position >> : " + sliderXPosition);
		return sliderXPosition;
	}

	private boolean seekVideo(String element) {
		WebElement seekbar = getWebElement(element);
		int seekBarFieldWidth = seekbar.getLocation().getX();
		int seekBarFieldHeigth = seekbar.getLocation().getY();
		TouchAction touch = new TouchAction(driver);
		touch.longPress(seekBarFieldWidth + 20, seekBarFieldHeigth).moveTo(seekBarFieldWidth + 100, seekBarFieldHeigth)
				.release().perform();
		return true;
	}

}
