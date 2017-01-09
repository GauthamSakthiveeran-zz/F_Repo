package com.ooyala.playback.amf.ima;

import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.ControlBarValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class OrderTest extends PlaybackWebTest {

	public OrderTest() throws OoyalaException {
		super();
	}

	private PlayValidator playValidator;
	private ControlBarValidator control;
	private PlayAction playAction;
	private PauseAction pause;

	@Test(groups = {"amf","customInteraction"}, dataProvider = "testUrls", enabled=false)
	public void validate(String testName, String url)
			throws Exception {

		try {

			driver.get(url);
			playValidator.waitForPage();
			playAction.startAction();
			Thread.sleep(20000);
			pause.startAction();
			control.validateOrderingOfElements();

		} catch (Exception e) {
			e.printStackTrace();
			extentTest.log(LogStatus.ERROR, e.getMessage());
		}

	}


}
