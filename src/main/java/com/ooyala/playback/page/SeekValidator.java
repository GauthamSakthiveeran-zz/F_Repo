package com.ooyala.playback.page;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by soundarya on 10/27/16.
 */
public class SeekValidator extends BaseValidator {

	public SeekValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("seek");
	}

	public void validate(String element, int timeout) throws Exception {
		while (true) {
			if (Double.parseDouble(((JavascriptExecutor) driver).executeScript(
					"return pp.getPlayheadTime();").toString()) > 5) {
				((JavascriptExecutor) driver)
						.executeScript("pp.seek(pp.getDuration()-7);");
				loadingSpinner();
				((JavascriptExecutor) driver).executeScript("pp.pause();");
				Thread.sleep(2000);
				((JavascriptExecutor) driver).executeScript("pp.play();");
				break;
			}
		}
		waitOnElement(element, timeout);
	}
}
