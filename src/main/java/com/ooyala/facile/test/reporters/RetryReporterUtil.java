package com.ooyala.facile.test.reporters;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;

import com.ooyala.facile.exception.ExemptTestFailureException;

// TODO: Auto-generated Javadoc
/**
 * The Class RetryReporterUtil.
 *
 * @author pkumar
 */
public class RetryReporterUtil {

	//This is a SINGLETON utility.  Since ALL of the data manipulated with it is
	// a singleton, we only want to modify it once.  This boolean controls that
	/** The has run. */
	private static boolean HAS_RUN = false;

	/**
	 * Update suite results for retry.
	 *
	 * @param suites the suites
	 */
	public static void updateSuiteResultsForRetry(List<ISuite> suites) {
		if (HAS_RUN) {
			return;
		}

		for(ISuite suite : suites) {
			Map<String, ISuiteResult> suiteResults = suite.getResults();

			for(String suiteResultKey : suite.getResults().keySet()) {
				ISuiteResult suiteResult = suite.getResults().get(suiteResultKey);
				updateSuiteWithRetryResults(suiteResult);
			}
		}

		HAS_RUN = true;
	}

	/**
	 * Update suite with retry results.
	 *
	 * @param suiteResult the suite result
	 */
	private static void updateSuiteWithRetryResults(ISuiteResult suiteResult) {
		ITestContext context = suiteResult.getTestContext();

		Set<ITestResult> passedTests = context.getPassedTests().getAllResults();
		Set<ITestResult> failedTests = context.getFailedTests().getAllResults();

		Set<ITestResult> testsThatPassOnRetry = calcRetryPasses(passedTests, failedTests);

		for(ITestResult retryPass : testsThatPassOnRetry) {
			context.getFailedTests().removeResult(retryPass.getMethod());
			context.getFailedButWithinSuccessPercentageTests().addResult(retryPass, retryPass.getMethod());
			retryPass.setStatus(ITestResult.SUCCESS_PERCENTAGE_FAILURE);
		}

		Set<ITestResult> testsThatShouldntFail = calcAcceptableFailures(failedTests);
		for(ITestResult acceptable : testsThatShouldntFail) {
			context.getFailedTests().removeResult(acceptable.getMethod());
			context.getSkippedTests().addResult(acceptable, acceptable.getMethod());
			acceptable.setStatus(ITestResult.SKIP);
		}
	}

	/**
	 * Calc retry passes.
	 *
	 * @param passes the passes
	 * @param failures the failures
	 * @return the sets the
	 */
	private static Set<ITestResult> calcRetryPasses(Set<ITestResult> passes, Set<ITestResult> failures) {
		Set<ITestResult> retryPasses = new HashSet<ITestResult>();

		//For each test failure...
		for(ITestResult currFail : failures) {
			//Get the test name...
			String failedTestName = currFail.getName();
			Object[] failedParams = currFail.getParameters();
			//Add see if it is contained in the set of passed tests...
			for(ITestResult currPass : passes) {
				Object[] passedParams = currPass.getParameters();
				//If it is, then add it to the tests that passed on retry...
				if (failedTestName.equals(currPass.getName()) && areParamsEqual(passedParams, failedParams)) {
					retryPasses.add(currFail);
					break;
				}
			}
		}

		return retryPasses;
	}

	/**
	 * Are params equal.
	 *
	 * @param left the left
	 * @param right the right
	 * @return true, if successful
	 */
	public static boolean areParamsEqual(Object[] left, Object[] right) {
		//If both are null, that is technically equal
		if (left == null && right == null) {
			return true;
		}

		//If one, but not the other is null, they are NOT equal
		if (left == null || right == null) {
			return false;
		}

		//If the sizes are different, they are not equal
		if (left.length != right.length) {
			return false;
		}

		//Now iterate through each element and compare
		for(int i = 0; i < left.length; i++) {
			if (!left[i].equals(right[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Calc acceptable failures.
	 *
	 * @param failures the failures
	 * @return the sets the
	 */
	private static Set<ITestResult> calcAcceptableFailures(Set<ITestResult> failures) {
		Set<ITestResult> acceptables = new HashSet<ITestResult>();

		for(ITestResult currFail : failures) {
			if (isAcceptableFailure(currFail)) {
				acceptables.add(currFail);
			}
		}

		return acceptables;
	}

	/**
	 * Checks if is acceptable failure.
	 *
	 * @param result the result
	 * @return true, if is acceptable failure
	 */
	public static boolean isAcceptableFailure(ITestResult result) {
		return (result.getThrowable() == null ||
				result.getThrowable() instanceof ExemptTestFailureException);
	}
}
