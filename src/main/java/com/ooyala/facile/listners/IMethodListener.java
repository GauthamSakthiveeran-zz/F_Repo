package com.ooyala.facile.listners;

import org.apache.log4j.Logger;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

public class IMethodListener implements IInvokedMethodListener {

	protected static Logger logger = Logger.getLogger(IMethodListener.class);

	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		logger.info("**********************************************************************************");
		logger.info(testResult.getMethod().getMethodName());
		logger.info("**********************************************************************************");

	}

	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		logger.info("**********************************************************************************");
		logger.info(testResult.getMethod().getMethodName());
		logger.info("**********************************************************************************");
	}
}
