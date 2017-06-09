package com.ooyala.playback.updateSpreadSheet;

import java.util.HashMap;
import java.util.List;

public class TestCaseData {

	private int headerRowNumber = -1;
	private int headerColumnNumber = -1;
	private int testCaseColumnNumber = -1;
	private int lastColumnForTestCase = -1;
	private List<List<Object>> values;

	private HashMap<String, Integer> testCaseMap = new HashMap<>();
	
	private int sheetId;
	
	
	public List<List<Object>> getValues() {
		return values;
	}
	
	public void setValues(List<List<Object>> values) {
		this.values = values;
	}
	
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

	public int getLastColumnForTestCase() {
		return lastColumnForTestCase;
	}

	public void setLastColumnForTestCase(int lastColumnForTestCase) {
		this.lastColumnForTestCase = lastColumnForTestCase;
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
