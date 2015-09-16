/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ooyala.faclile.page;

import org.openqa.selenium.WebDriver;

// TODO: Auto-generated Javadoc
/**
 * The Interface ContextAwareDriver.
 * 
 * @author pkumar
 */
public interface ContextAwareDriver {

	/**
	 * Gets the base target locator.
	 *
	 * @return the base target locator
	 */
	public WebDriver.TargetLocator getBaseTargetLocator();

	/**
	 * Adds the context.
	 *
	 * @param ctx the ctx
	 */
	public void addContext(Object ctx);

	/**
	 * Clear context.
	 */
	public void clearContext();

}
