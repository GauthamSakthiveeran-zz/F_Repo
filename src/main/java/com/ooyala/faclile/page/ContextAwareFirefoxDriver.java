package com.ooyala.faclile.page;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

// TODO: Auto-generated Javadoc
/**
 * A wrapper class around a normal WebDriver object that allows us to plug into
 * the various methods that can be called. Specifically, we can store the
 * context of the underlying driver instance so that we can ensure it isn't
 * lost.
 * 
 * @author pkumar
 */
public class ContextAwareFirefoxDriver extends FirefoxDriver implements
		ContextAwareDriver {

	/** The context stack. */
	List<Object> contextStack = new ArrayList<Object>();

	/**
	 * Instantiates a new context aware firefox driver.
	 */
	public ContextAwareFirefoxDriver() {
	}

	/**
	 * Instantiates a new context aware firefox driver.
	 * 
	 * @param profile
	 *            the profile
	 */
	public ContextAwareFirefoxDriver(FirefoxProfile profile) {
		super(profile);
	}

	/**
	 * Instantiates a new context aware firefox driver.
	 * 
	 * @param desiredCapabilities
	 *            the desired capabilities
	 */
	public ContextAwareFirefoxDriver(Capabilities desiredCapabilities) {
		super(desiredCapabilities);
	}

	/**
	 * Instantiates a new context aware firefox driver.
	 * 
	 * @param desiredCapabilities
	 *            the desired capabilities
	 * @param requiredCapabilities
	 *            the required capabilities
	 */
	public ContextAwareFirefoxDriver(Capabilities desiredCapabilities,
			Capabilities requiredCapabilities) {
		super(desiredCapabilities, requiredCapabilities);
	}

	/**
	 * Instantiates a new context aware firefox driver.
	 * 
	 * @param binary
	 *            the binary
	 * @param profile
	 *            the profile
	 */
	public ContextAwareFirefoxDriver(FirefoxBinary binary,
			FirefoxProfile profile) {
		super(binary, profile);
	}

	/**
	 * Instantiates a new context aware firefox driver.
	 * 
	 * @param binary
	 *            the binary
	 * @param profile
	 *            the profile
	 * @param capabilities
	 *            the capabilities
	 */
	public ContextAwareFirefoxDriver(FirefoxBinary binary,
			FirefoxProfile profile, Capabilities capabilities) {
		super(binary, profile, capabilities);
	}

	/**
	 * Instantiates a new context aware firefox driver.
	 * 
	 * @param binary
	 *            the binary
	 * @param profile
	 *            the profile
	 * @param desiredCapabilities
	 *            the desired capabilities
	 * @param requiredCapabilities
	 *            the required capabilities
	 */
	public ContextAwareFirefoxDriver(FirefoxBinary binary,
			FirefoxProfile profile, Capabilities desiredCapabilities,
			Capabilities requiredCapabilities) {
		super(binary, profile, desiredCapabilities, requiredCapabilities);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openqa.selenium.remote.RemoteWebDriver#findElements(org.openqa.selenium
	 * .By)
	 */
	@Override
	public List<WebElement> findElements(By by) {
		try {
			return super.findElements(by);
		} catch (WebDriverException ex) {
			// Retry setting the context
			resetContext();
			return super.findElements(by);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openqa.selenium.remote.RemoteWebDriver#findElement(org.openqa.selenium
	 * .By)
	 */
	@Override
	public WebElement findElement(By by) {
		try {
			return super.findElement(by);
		} catch (WebDriverException ex) {
			// Retry setting the context
			resetContext();
			return super.findElement(by);
		}
	}

	/**
	 * Reset context.
	 */
	protected void resetContext() {
		super.switchTo().defaultContent();
		for (int i = 0; i < contextStack.size(); i++) {
			Object webdriverContext = contextStack.get(i);

			if (webdriverContext instanceof Integer) {
				super.switchTo().frame((Integer) webdriverContext);
			} else if (webdriverContext instanceof WebElement) {
				super.switchTo().frame((WebElement) webdriverContext);
			} else if (webdriverContext instanceof String) {
				super.switchTo().frame((String) webdriverContext);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.remote.RemoteWebDriver#switchTo()
	 */
	@Override
	public TargetLocator switchTo() {
		return new ContextAwareTargetLocator(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.intuit.webdriver.root.ContextAwareDriver#getBaseTargetLocator()
	 */
	public TargetLocator getBaseTargetLocator() {
		return super.switchTo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.intuit.webdriver.root.ContextAwareDriver#addContext(java.lang.Object)
	 */
	public void addContext(Object ctx) {
		this.contextStack.add(ctx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.intuit.webdriver.root.ContextAwareDriver#clearContext()
	 */
	public void clearContext() {
		this.contextStack.clear();
	}
}
