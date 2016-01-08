package com.ooyala.facile.listners;

import java.io.File;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import com.ooyala.facile.grid.saucelabs.SauceREST;
import com.ooyala.facile.util.NoRetry;
import com.ooyala.facile.util.ReadPropertyFile;
import com.ooyala.facile.util.ReadTriggerFile;
import com.ooyala.facile.util.TestDescription;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving facileTest events. The class that is
 * interested in processing a facileTest event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addFacileTestListener<code> method. When
 * the facileTest event occurs, that object's appropriate
 * method is invoked.
 * 
 * @author pkumar
 */

public class FacileTestListener extends TestListenerAdapter implements
		IRetryAnalyzer {

	/** The logger. */
	public static Logger logger = Logger.getLogger(FacileTestListener.class);

	/** The test case execution time. */
	private long testCaseExecutionTime = 0;

	/** The test start time. */
	private long testStartTime = 0;

	/** The test end time. */
	private long testEndTime = 0;

	/** The retry count. */
	protected static int retryCount = 0;

	/** The Constant DEFAULT_MAX_RETRY. */
	protected static final int DEFAULT_MAX_RETRY = 1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.TestListenerAdapter#onTestStart(org.testng.ITestResult)
	 */
	@Override
	public void onTestStart(ITestResult tr) {

		// Init the Reporter log with the correct
		Reporter.setCurrentTestResult(tr);
		String fullTestName = tr.getMethod().toString();
		testEndTime = 0;
		testCaseExecutionTime = 0;
		testStartTime = new java.util.Date().getTime();
		logger.info(now("MMddyyyy:hhmmss") + "=== Running... <" + fullTestName
				+ ">===");

		// Logging the test description in the report

		if (tr.getMethod()
				.getConstructorOrMethod()
				.getMethod()
				.isAnnotationPresent(
						(Class<? extends Annotation>) TestDescription.class)) {
			TestDescription testDescription = tr.getMethod()
					.getConstructorOrMethod().getMethod()
					.getAnnotation(TestDescription.class);
			logger.info("******************************************************************");
			logger.info("Test description is " + testDescription.description());
			logger.info("******************************************************************");
			Reporter.log(testDescription.description() + "<br>");
		}

		// Output the retry number so that we can correlate the TestNG results
		// with
		// screenshots that are taken.
		if (retryCount == 0) {
			//Reporter.log("Running Test for First Time<br>");
		} else {
			Reporter.log("Running Retry #" + retryCount + "<br>");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.TestListenerAdapter#onTestSuccess(org.testng.ITestResult)
	 */
	@Override
	public void onTestSuccess(ITestResult tr) {
		String fullTestName = getSuiteTestName(tr.getMethod().toString());
		testEndTime = new java.util.Date().getTime();
		testCaseExecutionTime = testEndTime - testStartTime;
		logger.info(now("MMddyyyy:hhmmss") + " Test Passed : " + fullTestName
				+ " in " + testCaseExecutionTime + "ms.");
		logger.info("");
		if (tr.getAttributeNames() != null && !tr.getAttributeNames().isEmpty()
				&& tr.getAttribute("jobId") != null)
			this.updateSauceTestJob(tr.getAttribute("jobId").toString(), true,
					tr);
		// Make sure to reset the retry count to 0 again
		retryCount = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.TestListenerAdapter#onTestSkipped(org.testng.ITestResult)
	 */
	@Override
	public void onTestSkipped(ITestResult tr) {
		String fullTestName = getSuiteTestName(tr.getMethod().toString());
		testEndTime = new java.util.Date().getTime();
		testCaseExecutionTime = testEndTime - testStartTime;
		logger.info("Test Skipped: " + fullTestName + " in "
				+ testCaseExecutionTime + "ms.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.TestListenerAdapter#onTestFailure(org.testng.ITestResult)
	 */
	@Override
	public void onTestFailure(ITestResult tr) {
		testEndTime = new java.util.Date().getTime();
		testCaseExecutionTime = testEndTime - testStartTime;
		logger.info(now("MMddyyyy:hhmmss") + " Test FAILED "
				+ getSuiteTestName(tr.getMethod().toString()) + "in "
				+ testCaseExecutionTime + " ms.");
		if (tr.getAttributeNames() != null && !tr.getAttributeNames().isEmpty()
				&& tr.getAttribute("jobId") != null)
			this.updateSauceTestJob(tr.getAttribute("jobId").toString(), false,
					tr);
	}

	/**
	 * Gets the suite test name.
	 * 
	 * @param fullTestName
	 *            the full test name
	 * @return the suite test name
	 */
	protected String getSuiteTestName(String fullTestName) {
		fullTestName = fullTestName.replaceAll("com.intuit.webdriver.tests.",
				"");
		return fullTestName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.IRetryAnalyzer#retry(org.testng.ITestResult)
	 */
	public boolean retry(ITestResult result) {

		// Check to see if the "NoRetry" annotation is present in which case the
		// test SHOULD NOT be retried.
		if (result
				.getMethod()
				.getMethod()
				.isAnnotationPresent(
						(Class<? extends Annotation>) NoRetry.class)) {
			return false;
		}

		/**
		 * In src/test/resources/facile.properties file, if retry=true, it will
		 * retry all the failed test if retry=false, it will not retry any
		 * failed test if retry is not set or any other value, it will retry all
		 * the tests except tests with @NoRetry annotation
		 */

		File confFile = new File("src/test/resources/facile.properties");
		if (confFile.exists()) {
			ReadTriggerFile propertiesFile = new ReadTriggerFile(
					"src/test/resources/facile.properties");
			String retry = propertiesFile.getParameter("retry", "");
			int retryCount = Integer.parseInt(propertiesFile.getParameter(
					"retryCount", ""));
			logger.info("Retry count is " + retryCount);
			logger.info("Is Retry is enabled? " + retry);
			if (retry != null) {
				if (retry.equalsIgnoreCase("true")) {
					return retryTracker(retryCount);
				} else if (retry.equalsIgnoreCase("false")) {
					return false;
				}
			}
		}

		return retryTracker(DEFAULT_MAX_RETRY);
	}

	/**
	 * Now.
	 * 
	 * @param format
	 *            the format
	 * @return the string
	 */
	private String now(String format) {
		Calendar cal = Calendar.getInstance();
		return (new SimpleDateFormat(format)).format(cal.getTime());
	}

	/**
	 * Retry tracker.
	 * 
	 * @return true, if successful
	 */
	private boolean retryTracker(int maxRetryCount) {
		logger.info("retry count is " + retryCount);
		logger.info("Max Retry Count is " + maxRetryCount);
		if (retryCount < maxRetryCount) {
			logger.error("Test failed, but Facile will try to rerun the test");
			retryCount++;
			return true;
		} else {
			retryCount = 0;
			return false;
		}
	}

	/**
	 * Update sauce test job.
	 * 
	 * @param jobID
	 *            the job id
	 * @param testResult
	 *            the test result
	 * @param result
	 *            the result
	 */
	private void updateSauceTestJob(String jobID, boolean testResult,
			ITestResult result) {

		String sauceConfigFile = "/config/sauce.properties";

		InputStream in = FacileTestListener.class
				.getResourceAsStream(sauceConfigFile);

		if (!(in == null)) {

			String isSauceEnabled = ReadPropertyFile.getConfigurationParameter(
					sauceConfigFile, "USE_SAUCELAB_GRID");

			if (System.getProperty("USE_SAUCELAB_GRID") != null
					&& System.getProperty("USE_SAUCELAB_GRID")
							.equalsIgnoreCase("true")) {
				logger.info("Sauce labs is enabled by Environment variable USE_SAUCELAB_GRID");
			} else if (isSauceEnabled != null) {
				if (isSauceEnabled.equalsIgnoreCase("true")) {
					logger.info("Sauce is enabled by property file "
							+ sauceConfigFile);
				} else {
					logger.debug("Sauce is not enabled.");
					return;
				}
			} else {
				logger.info("Please provide valid value for USE_SAUCELAB_GRID in "
						+ sauceConfigFile);
				return;
			}
			String sauceUserName = ReadPropertyFile.getConfigurationParameter(
					sauceConfigFile, "SAUCE_USERNAME");
			String sauceAPiKey = ReadPropertyFile.getConfigurationParameter(
					sauceConfigFile, "SAUCE_API_KEY");

			if (sauceUserName != null && sauceAPiKey != null) {
				logger.info("Saucelabs is enabled so updating the sauce labs job.");
				SauceREST client = new SauceREST(sauceUserName, sauceAPiKey);

				Map<String, Object> updates = new HashMap<String, Object>();
				// updates.put("name", result.getMethod().getMethodName());
				updates.put("passed", testResult);
				logger.info("Updating the status of method :'"
						+ result.getMethod().getMethodName() + " '" + " as :"
						+ testResult);

				// Temp fix, if the user name is not scmbuild (which is true if
				// the run from jenkins).
				// the current user name will be udpated to the job build
				// details
				if (!System.getProperty("user.name").equalsIgnoreCase(
						"scmbuild")) {
					updates.put(
							"build",
							"Test Started By :"
									+ System.getProperty("user.name"));
					logger.info("Since the job is not invkoed from Jenkins, updating the user who started the job");
				}

				JSONArray tags = new JSONArray();
				tags.add(result.getTestContext().getIncludedGroups());
				// updates.put("tags", tags);
				client.updateJobInfo(jobID, updates);
				logger.info("Updating Sauce Job Id :" + jobID + " Completed.");
			}
		} else {
			logger.info("Sauce config file does not exist at "
					+ sauceConfigFile);

		}

	}
}
