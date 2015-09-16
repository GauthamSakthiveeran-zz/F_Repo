
package com.ooyala.facile.test.reporters;
import java.util.List;

import org.testng.ISuite;
import org.testng.reporters.SuiteHTMLReporter;
import org.testng.xml.XmlSuite;

// TODO: Auto-generated Javadoc
/**
 * The Class RetrySuiteHTMLReporter.
 *
 * @author pkumar
 */
public class RetrySuiteHTMLReporter extends SuiteHTMLReporter {

	/* (non-Javadoc)
	 * @see org.testng.reporters.SuiteHTMLReporter#generateReport(java.util.List, java.util.List, java.lang.String)
	 */
	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		RetryReporterUtil.updateSuiteResultsForRetry(suites);

		super.generateReport(xmlSuites, suites, outputDirectory);
	}

}
