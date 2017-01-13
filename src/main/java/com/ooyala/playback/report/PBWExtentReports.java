package com.ooyala.playback.report;

import java.util.List;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class PBWExtentReports extends ExtentReports{

	private static final long serialVersionUID = 2543311573206081367L;

	public PBWExtentReports(String filePath, Boolean replaceExisting) {
		super(filePath,replaceExisting);
	}
	
	public List<ExtentTest> getTestList() {
        return testList;
    } 
	
	public synchronized boolean removeTest(ExtentTest extentTest){
		return testList.remove(extentTest);
	}

	
}
