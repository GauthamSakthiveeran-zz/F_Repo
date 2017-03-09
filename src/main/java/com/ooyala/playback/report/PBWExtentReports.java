package com.ooyala.playback.report;

import java.util.List;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.model.Log;

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
	
	public static String getFeatureFailedLogList(ExtentTest test){
		
		List<Log> list = test.getTest().getLogList();
		
		for(Log log : list){
			if(log.getLogStatus()==LogStatus.FAIL && log.getDetails().contains("TEST FAILED")){
				return log.getDetails().split("TEST FAILED: ")[1];
			}
		}
		return "";
	}

	
}
