package com.ooyala.playback.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ExtentManager {

	private static Map<String, ExtentTest> extentTestMap;
	private static PBWExtentReports extentReports;
	private static Logger logger = Logger.getLogger(ExtentManager.class);

	static {
		extentTestMap = new HashMap<String, ExtentTest>();
		if (extentReports == null) {
			
			extentReports = new PBWExtentReports("./ExtentReport.html", true);

			extentReports.addSystemInfo("Host Name", "Jenkins-Dallas-Slave")
					.addSystemInfo("Environment", "QA");
			extentReports.addSystemInfo("browser", System.getProperty("browser"));

		}
	}

	public synchronized static PBWExtentReports getReporter() {
		return extentReports;
	}

	public synchronized static PBWExtentReports sharedInstance() {
		return extentReports;
	}

	public static synchronized void endTest(ExtentTest test) {
		extentReports.endTest(test);

	}

	public static synchronized ExtentTest startTest(String testName) {
		boolean retry = false;
		ExtentTest test = extentTestMap.get(testName);
		if (test != null) {
			extentReports.removeTest(test);
			extentTestMap.remove(test);
			logger.info(testName+ " is being removed from the report as it is being retried.");
			retry = true;
		}
		
		test = extentReports.startTest(testName);
		if(retry)
			test.log(LogStatus.INFO, "Test being retried.");
		extentTestMap.put(testName, test);
		return test;
	}

	public static synchronized void flush() {
		extentReports.flush();
	}
}
