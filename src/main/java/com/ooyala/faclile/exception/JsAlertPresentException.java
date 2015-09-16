package com.ooyala.faclile.exception;

import org.openqa.selenium.Alert;

public class JsAlertPresentException extends RuntimeException {

	// private String jsAlert = "";
	/** The alert. */
	private Alert theAlert = null;

	/** The cause. */
	private String cause = "";

	/**
	 * Instantiates a new js alert present exception.
	 *
	 * @param alert
	 *            the alert
	 * @param cause
	 *            the cause
	 */
	public JsAlertPresentException(Alert alert, String cause) {
		// jsAlert = alert.getText();
		theAlert = alert;
		this.cause = cause;
	}

	/**
	 * Return the actual contents of the JS alert that appeared during runtime.
	 * 
	 * @return A String of the JS alert's message or "" if it couldn't be
	 *         parsed.
	 */
	public String getAlertMessage() {
		// return jsAlert;
		return theAlert.getText();
	}

	/**
	 * This method can be populated by Facile to notify the runtime of the
	 * action that caused the JS alert to appear.
	 * 
	 * @return Human readable string of what action caused the JS alert to
	 *         appear.
	 */
	public String getPreviousAction() {
		return cause;
	}

	/**
	 * Gets the alert.
	 *
	 * @return the alert
	 */
	public Alert getAlert() {
		return theAlert;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString() {
		return super.toString() + " caused by action: " + cause;
	}

}