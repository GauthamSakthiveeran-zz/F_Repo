package com.ooyala.playback.report;

import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager {
	private static ExtentReports extent;

	public synchronized static ExtentReports getReporter() {
		if (extent == null) {
			extent = new ExtentReports("./ExtentReport.html", true);

			extent.addSystemInfo("Host Name", "Jenkins-Dallas-Slave")
					.addSystemInfo("Environment", "QA");
		}

		return extent;
	}

	public synchronized static ExtentReports sharedInstance() {
		return extent;
	}
}
