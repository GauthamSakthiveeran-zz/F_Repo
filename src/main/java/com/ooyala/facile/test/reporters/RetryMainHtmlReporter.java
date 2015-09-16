package com.ooyala.facile.test.reporters;

import java.util.List;

import org.testng.ISuite;
import org.testng.xml.XmlSuite;
import org.testng.reporters.jq.Main;

// TODO: Auto-generated Javadoc
/**
 * Retry reporter implementation for TestNG's new reporting.
 * 
 * @author pkumar
 */
public class RetryMainHtmlReporter extends Main {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.reporters.jq.Main#generateReport(java.util.List,
	 * java.util.List, java.lang.String)
	 */
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
			String outputDirectory) {
		RetryReporterUtil.updateSuiteResultsForRetry(suites);

		super.generateReport(xmlSuites, suites, outputDirectory);
	}
}
