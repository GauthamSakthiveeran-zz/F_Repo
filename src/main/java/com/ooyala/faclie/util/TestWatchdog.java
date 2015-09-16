package com.ooyala.faclie.util;

import org.openqa.selenium.WebDriver;

import com.ooyala.facile.listners.IWebPageListener;

// TODO: Auto-generated Javadoc
/**
 * The Class TestWatchdog.
 *
 * @author pkumar
 */
public class TestWatchdog implements Runnable, IWebPageListener {

	/** The driver. */
	protected WebDriver driver = null;

	/** The is running. */
	protected boolean isRunning = false;

	/** The terminated browser. */
	protected boolean terminatedBrowser = false;

	/** The killswitch. */
	protected long killswitch = 0L;

	// Listener used as a callback for various events within this thread.
	/** The listener. */
	private WatchdogListener listener = null;

	// Use this object as a lock on the isRunning value
	/** The running lock. */
	private final Object runningLock = new Object();

	// Use a default timeout of 60 seconds after each notification.
	/** The Constant DEFAULT_TIMEOUT. */
	public static final long DEFAULT_TIMEOUT = 1000 * 60 * 4;

	/**
	 * Instantiates a new test watchdog.
	 *
	 * @param d
	 *            the d
	 */
	public TestWatchdog(WebDriver d) {
		if (d == null) {
			throw new IllegalArgumentException(
					"Cannot start the test watchdog with a null WebDriver instance");
		}
		driver = d;
	}

	/**
	 * Sets the listener.
	 *
	 * @param l
	 *            the new listener
	 */
	public void setListener(WatchdogListener l) {
		listener = l;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		synchronized (runningLock) {
			isRunning = true;
		}

		killswitch = System.currentTimeMillis() + DEFAULT_TIMEOUT;

		while (isRunning) {
			if (System.currentTimeMillis() > killswitch) {
				killWebDriver();
			}

			try {
				Thread.sleep(1000);
			} catch (Exception ex) {
				// If the sleep is interrupted, we're ok with that.
			}
		}
	}

	/**
	 * Checks if is running.
	 *
	 * @return true, if is running
	 */
	public boolean isRunning() {
		synchronized (runningLock) {
			return isRunning;
		}
	}

	/**
	 * Was terminated.
	 *
	 * @return true, if successful
	 */
	public boolean wasTerminated() {
		synchronized (runningLock) {
			return terminatedBrowser;
		}
	}

	/**
	 * Ping.
	 *
	 * @return true, if successful
	 */
	public boolean ping() {
		return ping(DEFAULT_TIMEOUT);
	}

	/**
	 * Ping.
	 *
	 * @param timeout
	 *            the timeout
	 * @return true, if successful
	 */
	public synchronized boolean ping(long timeout) {
		if (isRunning()) {
			killswitch = System.currentTimeMillis() + timeout;
			return true;
		}

		return false;
	}

	/**
	 * Terminate.
	 */
	public void terminate() {
		synchronized (runningLock) {
			isRunning = false;
		}
	}

	/**
	 * Kill web driver.
	 */
	protected void killWebDriver() {
		if (driver != null) {
			listener.beforeTestKilled();
			terminate();
			try {
				driver.quit();
				terminatedBrowser = true;
			} catch (Exception ex) {
				// Purposely do nothing here.
			}
			listener.afterTestKilled();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.intuit.webdriver.root.IWebPageListener#onEvent(java.lang.String)
	 */
	public void onEvent(String event) {
		this.ping();
	}

	/**
	 * The listener interface for receiving watchdog events. The class that is
	 * interested in processing a watchdog event implements this interface, and
	 * the object created with that class is registered with a component using
	 * the component's <code>addWatchdogListener<code> method. When
	 * the watchdog event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see WatchdogEvent
	 */
	public static interface WatchdogListener {

		/**
		 * Before test killed.
		 */
		public void beforeTestKilled();

		/**
		 * After test killed.
		 */
		public void afterTestKilled();
	}
}
