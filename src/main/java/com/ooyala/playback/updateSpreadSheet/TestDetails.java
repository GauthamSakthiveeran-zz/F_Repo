package com.ooyala.playback.updateSpreadSheet;

import org.testng.ITestResult;

import com.relevantcodes.extentreports.ExtentTest;

public class TestDetails {

//	private String testName = "";
	private ExtentTest extentTest;
	private ITestResult result;

//	public String getTestName() {
//		return testName;
//	}
//
//	public void setTestName(String testName) {
//		this.testName = testName;
//	}

	public ExtentTest getExtentTest() {
		return extentTest;
	}

	public void setExtentTest(ExtentTest extentTest) {
		this.extentTest = extentTest;
	}

	public ITestResult getITestResult() {
		return result;
	}

	public void setITestResult(ITestResult result) {
		this.result = result;
	}

}
