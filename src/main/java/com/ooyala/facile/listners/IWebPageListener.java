package com.ooyala.facile.listners;

// TODO: Auto-generated Javadoc
/**
 * This interface defines methods that can be used outside of the main Facile
 * source to be notified of actions that occur on ANY Facile page.
 * 
 * @author pkumar
 */
public interface IWebPageListener {

	/** The generic event. */
	public static String GENERIC_EVENT = "generic";

	/**
	 * On event.
	 *
	 * @param event
	 *            the event
	 */
	public void onEvent(String event);

}
