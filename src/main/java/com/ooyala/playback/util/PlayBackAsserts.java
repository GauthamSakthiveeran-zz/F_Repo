package com.ooyala.playback.util;

import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class PlayBackAsserts extends SoftAssert {

	ExtentTest extentTest;

	public PlayBackAsserts(ExtentTest extentTest) {
		this.extentTest = extentTest;
	}

	@Override
	public void onAssertSuccess(IAssert assertCommand) {
		extentTest.log(LogStatus.PASS, assertCommand.getMessage());
	}
	
	@Override
	public void onAssertFailure(IAssert assertCommand) {
		extentTest.log(LogStatus.FAIL, assertCommand.getMessage());
	}

}
