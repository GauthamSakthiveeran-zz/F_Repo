package com.ooyala.playback.updateSpreadSheet;

import java.io.IOException;

import com.ooyala.qe.common.util.PropertyReader;

public class TestCaseSheetProperties {
	
	private static PropertyReader properties;
	protected static String email_address;
	protected static String spreadSheetId;
	protected static String dateFormat;
	protected static String delimiterForDifferentTests;
	protected static String delimiterForTabAndTest;
	protected static String sheetRangeForInitialReading;
	protected static String lastColumnForTestCase;
	protected static String testCaseDescriptionColumn;
	protected static String firstColumnForTestCase;
	
	
	static {
		try {
			properties = PropertyReader.getInstance("testcase.properties");
			
			if(properties!=null){
				email_address = properties.getProperty("email_address");
				spreadSheetId = properties.getProperty("spreadSheetId");
				dateFormat = properties.getProperty("dateFormat");
				delimiterForDifferentTests = properties.getProperty("delimiterForDifferentTests");
				delimiterForTabAndTest = properties.getProperty("delimiterForTabAndTest");
				sheetRangeForInitialReading = properties.getProperty("sheetRangeForInitialReading");
				lastColumnForTestCase = properties.getProperty("lastColumnForTestCase");
				testCaseDescriptionColumn = properties.getProperty("testCaseDescriptionColumn");
				firstColumnForTestCase = properties.getProperty("firstColumnForTestCase");
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
