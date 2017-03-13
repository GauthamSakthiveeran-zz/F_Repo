package com.ooyala.playback.updateSpreadSheet;

import java.util.HashMap;

public class TestCaseData {

	private int headerRowNumber = -1;
	private int headerColumnNumber = -1;
	private int testCaseColumnNumber = -1;

	private HashMap<String, Integer> testCaseMap = new HashMap<>();
	
	private int sheetId;
	
	public int getSheetId(){
		return sheetId;
	}
	
	public void setSheetId(int sheetId){
		this.sheetId = sheetId;
	}

	public int getHeaderRowNumber() {
		return this.headerRowNumber;
	}

	public void setHeaderRowNumber(int headerRowNumber) {
		this.headerRowNumber = headerRowNumber;
	}

	public int getHeaderColumnNumber() {
		return headerColumnNumber;
	}

	public void setHeaderColumnNumber(int headerColumnNumber) {
		this.headerColumnNumber = headerColumnNumber;
	}

	public int getTestCaseColumnNumber() {
		return testCaseColumnNumber;
	}

	public void setTestCaseColumnNumber(int testCaseColumnNumber) {
		this.testCaseColumnNumber = testCaseColumnNumber;
	}

	public HashMap<String, Integer> getTestCaseMap() {
		return testCaseMap;
	}

	public void setTestCaseMap(HashMap<String, Integer> testCaseMap) {
		this.testCaseMap = testCaseMap;
	}

}
