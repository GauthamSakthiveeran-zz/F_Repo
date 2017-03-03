package com.ooyala.playback.updateSpreadSheet;

public class TestDetails {

	private String testCaseName = "";
	private String sheetName = "";
	private TestResult testResult;
	private String bugID = "";

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public TestResult getTestResult() {
		return testResult;
	}

	public void setTestResult(TestResult testResult) {
		this.testResult = testResult;
	}

	public String getBugID() {
		return bugID;
	}

	public void setBugID(String bugID) {
		this.bugID = bugID;
	}

}
