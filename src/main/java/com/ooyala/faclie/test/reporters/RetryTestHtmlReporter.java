
package com.ooyala.faclie.test.reporters;
import java.util.ArrayList;
import java.util.List;

import org.testng.ITestResult;
import org.testng.reporters.TestHTMLReporter;

// TODO: Auto-generated Javadoc
/**
 * The Class RetryTestHtmlReporter.
 *
 * @author pkumar
 */
public class RetryTestHtmlReporter extends TestHTMLReporter {

    /* (non-Javadoc)
     * @see org.testng.TestListenerAdapter#getFailedTests()
     */
    @Override
    public List<ITestResult> getFailedTests() {
        List <ITestResult> failed = super.getFailedTests();
        List <ITestResult> passed = super.getPassedTests();

        List<ITestResult> results = new ArrayList<ITestResult>();

        for(ITestResult failure :failed) {

            boolean found = false;
			Object[] failedParams = failure.getParameters();
            for(ITestResult pass : passed) {
				Object[] passedParams = pass.getParameters();
				if(failedParams != null && passedParams != null){
					if (pass.getName().equals(failure.getName()) && RetryReporterUtil.areParamsEqual(failedParams, passedParams)) {
						found = true;
					}
				}
            }

            if (!found) {
                results.add(failure);
            }
        }

        return results;
    }

    /* (non-Javadoc)
     * @see org.testng.TestListenerAdapter#getFailedButWithinSuccessPercentageTests()
     */
    @Override
    public List<ITestResult> getFailedButWithinSuccessPercentageTests() {
        List <ITestResult> failed = super.getFailedTests();
        List <ITestResult> passed = super.getPassedTests();

        List<ITestResult> results = new ArrayList<ITestResult>();

        for(ITestResult failure :failed) {

            boolean found = false;
			Object[] failedParams = failure.getParameters();
            for(ITestResult pass : passed) {
				Object[] passedParams = pass.getParameters();
                if (pass.getName().equals(failure.getName()) && RetryReporterUtil.areParamsEqual(failedParams, passedParams)) {
                    found = true;
                }
            }

            if (found) {
                results.add(failure);
            }
        }

        return results;
    }
}
