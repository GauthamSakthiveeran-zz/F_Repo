package com.ooyala.facile.test.reporters;

import java.util.List;

import org.testng.ISuite;
import org.testng.reporters.XMLReporter;
import org.testng.xml.XmlSuite;

// TODO: Auto-generated Javadoc
/**
 * The Class RetryXmlReporter.
 *
 * @author pkumar
 */
public class RetryXmlReporter extends XMLReporter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.reporters.XMLReporter#generateReport(java.util.List,
	 * java.util.List, java.lang.String)
	 */
	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
			String outputDirectory) {

		RetryReporterUtil.updateSuiteResultsForRetry(suites);

		super.generateReport(xmlSuites, suites, outputDirectory);
	}
}
