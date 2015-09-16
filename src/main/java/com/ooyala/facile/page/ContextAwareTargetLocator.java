package com.ooyala.facile.page;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

// TODO: Auto-generated Javadoc
/**
 * The Class ContextAwareTargetLocator.
 * 
 * @author pkumar
 */
public class ContextAwareTargetLocator implements WebDriver.TargetLocator {

	/** The ctx driver. */
	private ContextAwareDriver ctxDriver = null;

	/**
	 * Instantiates a new context aware target locator.
	 * 
	 * @param d
	 *            the d
	 */
	public ContextAwareTargetLocator(ContextAwareDriver d) {
		ctxDriver = d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.WebDriver.TargetLocator#frame(int)
	 */
	public WebDriver frame(int i) {
		ctxDriver.addContext(new Integer(i));
		return ctxDriver.getBaseTargetLocator().frame(i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.WebDriver.TargetLocator#frame(java.lang.String)
	 */
	public WebDriver frame(String string) {
		ctxDriver.addContext(string);
		return ctxDriver.getBaseTargetLocator().frame(string);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openqa.selenium.WebDriver.TargetLocator#frame(org.openqa.selenium
	 * .WebElement)
	 */
	public WebDriver frame(WebElement we) {
		ctxDriver.addContext(we);
		return ctxDriver.getBaseTargetLocator().frame(we);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.WebDriver.TargetLocator#window(java.lang.String)
	 */
	public WebDriver window(String string) {
		ctxDriver.clearContext();
		return ctxDriver.getBaseTargetLocator().window(string);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.WebDriver.TargetLocator#defaultContent()
	 */
	public WebDriver defaultContent() {
		ctxDriver.clearContext();
		return ctxDriver.getBaseTargetLocator().defaultContent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.WebDriver.TargetLocator#activeElement()
	 */
	public WebElement activeElement() {
		return ctxDriver.getBaseTargetLocator().activeElement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.WebDriver.TargetLocator#alert()
	 */
	public Alert alert() {
		return ctxDriver.getBaseTargetLocator().alert();
	}

	public WebDriver parentFrame() {
		// TODO Auto-generated method stub
		return null;
	}
}
