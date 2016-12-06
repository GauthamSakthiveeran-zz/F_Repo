package com.ooyala.playback.asserts;

import java.util.List;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class PlayBackAsserts {

	List<String> failures;
	ExtentTest extentTest;

	/**
	 * failures are a list of failures - used for soft asserts. ExtentTest is
	 * for reporting. The class needs to be instantiated at @BeforeMethod in
	 * PlaybackWebTest
	 * 
	 * @param failures
	 * @param extentTest
	 */
	public PlayBackAsserts(List<String> failures, ExtentTest extentTest) {
		this.failures = failures;
		this.extentTest = extentTest;
	}

	public PlayBackAsserts assertTrue(boolean condition, String stepstepMessage) {
		if (!condition) {
			failures.add(stepstepMessage);
		}
		return this;
	}

	public PlayBackAsserts assertFalse(boolean condition, String stepMessage) {
		if (condition) {
			failures.add(stepMessage);
		}
		return this;
	}

	public PlayBackAsserts equals(String actual, String expected,
			String stepMessage) { // can add a similar notEquals when needed.

		if (actual == null || expected == null) {
			failures.add(stepMessage + " Actual:" + actual + " Expected:"
					+ expected);
			extentTest.log(LogStatus.FAIL, stepMessage);
			return this;
		} else if (!actual.equals(expected)) {
			String msg = stepMessage + " Actual:" + actual + " not equals "
					+ " Expected:" + expected;
			failures.add(msg);
			extentTest.log(LogStatus.FAIL, stepMessage);
		} else {
			extentTest.log(LogStatus.PASS, stepMessage);
		}
		return this;

	}

	public PlayBackAsserts fail(String stepMessage) { // completely fail
		extentTest.log(LogStatus.FAIL, stepMessage);
		assert false;
		return this;
	}

	public PlayBackAsserts end() {
		if (failures != null && extentTest != null) {
			for (String stepMessage : failures) {
				extentTest.log(LogStatus.FAIL, stepMessage);
			}
		}
		return this;
	}
}
