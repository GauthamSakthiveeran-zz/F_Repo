package com.ooyala.facile.page;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.SystemClock;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ooyala.facile.exception.JsAlertPresentException;
import com.ooyala.facile.listners.IWebPageListener;
import com.ooyala.facile.util.ReadPropertyFile;

// TODO: Auto-generated Javadoc
/**
 * This class represents a general web page in the WebDriver world. It also
 * provides some useful methods to use on the page.
 * 
 * @author pkumar
 * 
 */
public abstract class WebPage {

	/** The logger. */
	public static Logger logger = Logger.getLogger(WebPage.class);

	/** The driver. */
	protected RemoteWebDriver driver = null;

	/** The type. */
	protected KeyboardCommands type = new KeyboardCommands();
	// Static list of listeners that are attached to the QBOWebPage object.
	/** The listeners. */
	protected static List<IWebPageListener> listeners = new ArrayList<IWebPageListener>();
	/** Marker for tracking localization strings */
	protected static String BEGIN_MARKER = "$";
	/** Marker for tracking localization strings */
	protected static String END_MARKER = "$";
	// TODO can we make it protected or private

	/** The Constant WAIT_TIME. */
	public static final int WAIT_TIME = 100000; // 100 seconds this is really
	// high should not be more than
	// 10 seconds or 20 max
	/** The Constant WAIT_INCR. */
	public static final int WAIT_INCR = 500;

	/** The page elements. */
	protected HashMap<String, FacileWebElement> pageElements = null;

	/** The clock. */
	private static Clock clock = new SystemClock();

	/**
	 * Assumes we are only running one instance of a driver at one time.
	 *
	 * @param driver
	 *            the driver
	 */
	public WebPage(WebDriver driver) {
		this.driver = (RemoteWebDriver) driver;
		pageElements = new HashMap<String, FacileWebElement>();
	}

	/**
	 * Sets the page driver.
	 *
	 * @param driver
	 *            the new page driver
	 */
	public void setPageDriver(RemoteWebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Sets the page driver.
	 *
	 * @param driver
	 *            the new page driver
	 */
	@Deprecated
	public void setPageDriver(WebDriver driver) {
		this.driver = (RemoteWebDriver) driver;
	}

	/**
	 * Instantiates a new web page.
	 */
	@Deprecated
	public WebPage() {

	}

	/**
	 * This method "isTextElementThere" verifies visibility of a element with
	 * the given ID. Note : This method is valid only for elements that can
	 * receive text input.
	 *
	 * @param id
	 *            the id
	 * @return (boolean) true : In case the element is present and ready to
	 *         receive text input. written by kbhalla, it is not a sure shot
	 *         pill for all element verifications. Its kind of workaround.
	 */
	protected boolean isTextElementThere(String id) {

		WebElement targetElement = null;
		if (driver == null || id == null)
			return false;

		try {
			targetElement = driver.findElement(By.id(id));
			if (targetElement == null) {
				logger.info("Web element to recieve text input not found");
				return false;
			}

			if (!targetElement.isEnabled()) {
				logger.info("Web element to recieve text input is not enabled");
				return false;
			}

			// Sending keys to the element. This is a workaround to verify that
			// the element is visible.
			targetElement.sendKeys(" ");
		} catch (org.openqa.selenium.ElementNotVisibleException ex) {
			logger.error("Caught exception" + ex);
			return false;
		} catch (org.openqa.selenium.NoSuchElementException ex) {
			logger.error("Caught exception" + ex);
			return false;
		}
		return true;
	}

	/**
	 * Get browser url from WebDriver.
	 *
	 * @return the browser url
	 */
	public String getBrowserUrl() {
		logger.info("Current URL is : " + driver.getCurrentUrl());
		return driver.getCurrentUrl();
	}

	/**
	 * Waits until webdriver can find an element with the specified name
	 * attribute.
	 * 
	 * @param name
	 *            the 'name' attribute value
	 * @param maxMilliseconds
	 *            how long we should wait
	 * @param frame
	 *            the frame the element is located on
	 * @param ignoreRendering
	 *            do we care if the element is visible or not
	 * @return true if WebDriver is able to find a web element with the given
	 *         name attribute value
	 */
	public boolean waitOnName(String name, int maxMilliseconds, String frame,
			boolean ignoreRendering) {

		WebElement identifier = null;
		int secondsPassed = 0;

		while (secondsPassed < maxMilliseconds) {
			logger.info("Trying to find a web element with the specified name: "
					+ name);
			try {
				if (frame == null)
					identifier = driver.findElement(By.name(name));
				else {
					driver.switchTo().defaultContent();
					identifier = driver.switchTo().frame(frame)
							.findElement(By.name(name));
				}
			} catch (Exception e) {
				logger.error("Caught exception" + e);
				identifier = null;
			}

			if (identifier != null) { // && !(driver instanceof SafariDriver)) {
				// // can't cast SafariWebElement to
				// RenderedWebElement
				if (identifier.isDisplayed())
					System.out.print("   Object name Found: " + name);
				else {
					logger.info("   ...looking for object name (" + name + ")");
					if (driver instanceof InternetExplorerDriver) {
						wait(500); // prevent too much checking if IE (it seems
						// to crash on these alot)
					}
				}
			}
			logger.info("identifier currently:" + identifier + " and driver: "
					+ driver);
			if (identifier != null
					&& (/* driver instanceof SafariDriver || */ignoreRendering || (identifier
							.isDisplayed()))) {
				logger.info("Found name in: " + secondsPassed / 1000 + " sec");
				return true;
			}
			secondsPassed += WAIT_INCR;
			wait(WAIT_INCR);
		}
		logger.info("Waiting for object name: " + name + " timed out after "
				+ maxMilliseconds / 1000 + " sec");
		return false;
	}

	/**
	 * Sleeps the specified amount of milliseconds.
	 *
	 * @param milliseconds
	 *            the milliseconds
	 * @deprecated The reason I am deprecating this is that we should really
	 *             avoid using waits. We should rely more on ait for page and
	 *             only use this when no other solution works.
	 */
	@Deprecated
	public static void wait(int milliseconds) {

		long endTime = clock.laterBy(milliseconds);
		while (clock.isNowBefore(endTime)) {
			try {
				Thread.sleep(WAIT_INCR);
			} catch (InterruptedException e) {
				logger.error("Caught exception" + e);
			}
		}
	}

	/**
	 * This method waits the specified time, and also prints a message out.
	 * Useful for when you want to debug what you're waiting on and see in real
	 * time when waits occur
	 *
	 * @param milliseconds
	 *            the milliseconds
	 * @param message
	 *            the message
	 */
	public static void wait(int milliseconds, String message) {
		logger.info(message);
		wait(milliseconds);
	}

	/**
	 * Waits until webdriver can find an element with the specified name
	 * attribute.
	 *
	 * @param name
	 *            the name
	 * @param frame
	 *            the frame
	 * @return true, if successful
	 */
	public boolean waitOnName(String name, String frame) {
		return waitOnName(name, WAIT_TIME, frame, false);
	}

	/**
	 * Waits until webdriver can find an element with the specified name
	 * attribute.
	 *
	 * @param name
	 *            the name
	 * @return true, if successful
	 */
	public boolean waitOnName(String name) {
		return waitOnName(name, WAIT_TIME, null, false);
	}

	/**
	 * Waits until webdriver can find an element with the specified name
	 * attribute.
	 *
	 * @param name
	 *            the name
	 * @param ignoreRendering
	 *            the ignore rendering
	 * @return true, if successful
	 */
	public boolean waitOnName(String name, boolean ignoreRendering) {
		return waitOnName(name, WAIT_TIME, null, ignoreRendering);
	}

	/**
	 * Waits until webdriver can find the specified header text. The header
	 * title is the common ui header text that spans all pages (part of the new
	 * qbo ui refresh)
	 *
	 * @param id
	 *            the id
	 * @param maxMilliseconds
	 *            the max milliseconds
	 * @param frame
	 *            the frame
	 * @param ignoreRendering
	 *            the ignore rendering
	 * @return true if WebDriver is able to find an element with the given id
	 */
	public boolean waitOnId(String id, int maxMilliseconds, String frame,
			boolean ignoreRendering) {

		WebElement identifier = null;
		int secondsPassed = 0;

		while (secondsPassed < maxMilliseconds) {
			logger.info("Trying to find web element with specified id: " + id);
			try {
				if (frame == null)
					identifier = driver.findElement(By.id(id));
				else {
					driver.switchTo().defaultContent();
					identifier = driver.switchTo().frame(frame)
							.findElement(By.id(id));
				}
			} catch (Exception e) {
				if (isSafariDummyPlug()) {
					frame = null;
					logger.error("Caught exception: " + e);
				}
				identifier = null;
			}

			try {
				if (identifier != null && !isSafariDummyPlug()) {
					if (identifier.isDisplayed())
						logger.info("   Id Found: " + id);
					else {
						logger.info("   ...looking for id (" + id + ")");
						if (driver instanceof InternetExplorerDriver) {
							wait(500); // prevent too much checking if IE (it
							// seems to crash on these alot)
							// 6.23.2009 snguyen1 - this may be obsolete now
							// since the driver is more mature.
							// This isn't detrimental, will leave for awhile.
						}
					}
				}
			} catch (RuntimeException e) {
				wait(WAIT_INCR);
				identifier = null;

				if (isSafariDummyPlug()) {
					e.printStackTrace();
					frame = null;
				}
			}

			if (identifier != null
					&& (isSafariDummyPlug() || ignoreRendering || (identifier
							.isDisplayed()))) {
				logger.info("   Found id in: " + secondsPassed / 1000 + " sec");
				return true;
			}
			secondsPassed += WAIT_INCR;
			wait(WAIT_INCR);
			// logger.info("Waiting on identifier(" + id + ")...");
		}
		logger.info("Waiting for id: " + id + " timed out after "
				+ maxMilliseconds / 1000 + " sec");
		return false;
	}

	/**
	 * Wait on id.
	 *
	 * @param id
	 *            the id
	 * @param frame
	 *            the frame
	 * @return true, if successful
	 */
	public boolean waitOnId(String id, String frame) {
		return waitOnId(id, WAIT_TIME, frame, false);
	}

	/**
	 * Wait on id.
	 *
	 * @param id
	 *            the id
	 * @return true, if successful
	 */
	public boolean waitOnId(String id) {
		return waitOnId(id, WAIT_TIME, null, false);
	}

	/**
	 * Wait on id.
	 *
	 * @param id
	 *            the id
	 * @param ignoreRendering
	 *            the ignore rendering
	 * @return true, if successful
	 */
	public boolean waitOnId(String id, boolean ignoreRendering) {
		return waitOnId(id, WAIT_TIME, null, ignoreRendering);
	}

	/**
	 * Wait on id.
	 *
	 * @param id
	 *            the id
	 * @param maxTime
	 *            the max time
	 * @param ignoreRendering
	 *            the ignore rendering
	 * @return true, if successful
	 */
	public boolean waitOnId(String id, int maxTime, boolean ignoreRendering) {
		return waitOnId(id, maxTime, null, ignoreRendering);
	}

	/**
	 * Wait on id.
	 *
	 * @param id
	 *            the id
	 * @param maxTime
	 *            the max time
	 * @return true, if successful
	 */
	public boolean waitOnId(String id, int maxTime) {
		return waitOnId(id, maxTime, null, false);
	}

	/**
	 * Wait on id.
	 *
	 * @param id
	 *            the id
	 * @param frame
	 *            the frame
	 * @param ignoreRendering
	 *            the ignore rendering
	 * @return true, if successful
	 */
	public boolean waitOnId(String id, String frame, boolean ignoreRendering) {
		return waitOnId(id, WAIT_TIME, frame, ignoreRendering);
	}

	/**
	 * Waits until webdriver can find the element specified by xpath.
	 *
	 * @param path
	 *            the path
	 * @param maxMilliseconds
	 *            the max milliseconds
	 * @param frame
	 *            the frame
	 * @param ignoreRendering
	 *            the ignore rendering
	 * @return true if WebDriver is able to find an element with the given id
	 */
	public boolean waitOnXPath(String path, int maxMilliseconds, String frame,
			boolean ignoreRendering) {

		WebElement identifier = null;
		int secondsPassed = 0;

		while (secondsPassed < maxMilliseconds) {
			logger.info("Trying to find web element with specified xpath: "
					+ path);
			try {
				if (frame == null)
					identifier = driver.findElement(By.xpath(path));
				else {
					driver.switchTo().defaultContent();
					identifier = driver.switchTo().frame(frame)
							.findElement(By.xpath(path));
				}
			} catch (Exception e) {
				if (isSafariDummyPlug()) {
					frame = null;
					logger.error("Caught exception" + e);
				}
				identifier = null;
			}

			try {
				if (identifier != null && !isSafariDummyPlug()) {
					if (identifier.isDisplayed())
						logger.info("   XPath Found: " + path);
					else {
						logger.info("   ...looking for xpath (" + path + ")");
						if (driver instanceof InternetExplorerDriver) {
							wait(500); // prevent too much checking if IE (it
							// seems to crash on these alot)
							// 6.23.2009 snguyen1 - this may be obsolete now
							// since the driver is more mature.
							// This isn't detrimental, will leave for awhile.
						}
					}
				}
			} catch (RuntimeException e) {
				wait(WAIT_INCR);
				identifier = null;

				if (isSafariDummyPlug()) {
					logger.error("Caught exception" + e);
					frame = null;
				}
			}

			if (identifier != null
					&& (isSafariDummyPlug() || ignoreRendering || identifier
							.isDisplayed())) {
				logger.info("   Found id in: " + secondsPassed / 1000 + " sec");
				return true;
			}
			secondsPassed += WAIT_INCR;
			wait(WAIT_INCR);
			// logger.info("Waiting on identifier(" + id + ")...");
		}
		logger.info("Waiting for xpath: " + path + " timed out after "
				+ maxMilliseconds / 1000 + " sec");
		return false;
	}

	/**
	 * Wait on x path.
	 *
	 * @param path
	 *            the path
	 * @param frame
	 *            the frame
	 * @return true, if successful
	 */
	public boolean waitOnXPath(String path, String frame) {
		return waitOnXPath(path, WAIT_TIME, frame, false);
	}

	/**
	 * Wait on x path.
	 *
	 * @param path
	 *            the path
	 * @return true, if successful
	 */
	public boolean waitOnXPath(String path) {
		return waitOnXPath(path, WAIT_TIME, null, false);
	}

	/**
	 * Wait on x path.
	 *
	 * @param path
	 *            the path
	 * @param ignoreRendering
	 *            the ignore rendering
	 * @return true, if successful
	 */
	public boolean waitOnXPath(String path, boolean ignoreRendering) {
		return waitOnXPath(path, WAIT_TIME, null, ignoreRendering);
	}

	/**
	 * Wait on x path.
	 *
	 * @param path
	 *            the path
	 * @param maxTime
	 *            the max time
	 * @param ignoreRendering
	 *            the ignore rendering
	 * @return true, if successful
	 */
	public boolean waitOnXPath(String path, int maxTime, boolean ignoreRendering) {
		return waitOnXPath(path, maxTime, null, ignoreRendering);
	}

	/**
	 * Wait on x path.
	 *
	 * @param path
	 *            the path
	 * @param maxTime
	 *            the max time
	 * @return true, if successful
	 */
	public boolean waitOnXPath(String path, int maxTime) {
		return waitOnXPath(path, maxTime, null, false);
	}

	/**
	 * Wait on x path.
	 *
	 * @param path
	 *            the path
	 * @param frame
	 *            the frame
	 * @param ignoreRendering
	 *            the ignore rendering
	 * @return true, if successful
	 */
	public boolean waitOnXPath(String path, String frame,
			boolean ignoreRendering) {
		return waitOnXPath(path, WAIT_TIME, frame, ignoreRendering);
	}

	/**
	 * Returns the coordinates of the top lefthand corner of the browser window.
	 *
	 * @return the window location
	 */
	private Point getWindowLocation() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Point windowLocation = new Point();

		// TODO ensure this works in safari on mac
		if (driver instanceof InternetExplorerDriver) {
			windowLocation.x = ((Long) js
					.executeScript("return parent.window.screenLeft;"))
					.intValue();
			windowLocation.y = ((Long) js
					.executeScript("return parent.window.screenTop;"))
					.intValue();
		} else {
			windowLocation.x = ((Long) js
					.executeScript("return window.screenX;")).intValue();
			windowLocation.y = ((Long) js
					.executeScript("return window.screenY;")).intValue();
		}
		logger.info("Returning browser window's top lefthand x-coordinate: "
				+ windowLocation.x + " and y-coordinate: " + windowLocation.y);
		return windowLocation;
	}

	/**
	 * Gets the browser size.
	 *
	 * @return the browser size
	 */
	private Point getBrowserSize() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Point browserSize = new Point();

		// TODO ensure this works in IE 7 and 8
		if (driver instanceof InternetExplorerDriver) {
			// IE 4 compatible, but seems to work for IE7
			browserSize.x = ((Long) js
					.executeScript("return parent.document.body.clientWidth;"))
					.intValue();
			browserSize.y = ((Long) js
					.executeScript("return parent.document.body.clientHeight;"))
					.intValue();

			// for IE 6+ in 'standards compliant mode'
			// supposedly uses (unverified):
			// wWidth = document.documentElement.clientWidth;
			// wHeight = document.documentElement.clientHeight;
		} else if (driver instanceof FirefoxDriver) {
			browserSize.x = ((Long) js
					.executeScript("return window.outerWidth;")).intValue();
			browserSize.y = ((Long) js
					.executeScript("return window.outerHeight;")).intValue();
		} else {
			throw new UnsupportedOperationException();
		}

		logger.info("Returning browser width: " + browserSize.x
				+ " and height: " + browserSize.y);
		return browserSize;
	}

	/**
	 * Gets the browser rect.
	 *
	 * @return the browser rect
	 */
	private Rectangle getBrowserRect() {
		Point location = getWindowLocation();
		Point size = getBrowserSize();
		return new Rectangle(location.x, location.y, size.x, size.y);
	}

	/**
	 * Resize to.
	 *
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public void resizeTo(int width, int height) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String script = "window.resizeTo(" + width + "," + height + ")";
		if (driver instanceof InternetExplorerDriver) { // ie executes this
			// within the frame...
			// we want on the parent
			// frame
			script = "parent." + script;
		}
		logger.info("Trying to execute script to rezise window");
		js.executeScript(script);
		WebPage.wait(1000);
	}

	/**
	 * Move to.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void moveTo(int x, int y) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String script = "window.moveTo(" + x + "," + y + ")";
		if (driver instanceof InternetExplorerDriver) {
			script = "parent." + script;
		}
		logger.info("Trying to execute script to move window to new location");
		js.executeScript(script);
		WebPage.wait(1000);
	}

	/**
	 * Takes a screenshot of the browser window. With the Firefox Driver, the
	 * firefox window does not need to be visible or on screen, the IE driver
	 * however does not implement this feature yet. Follow the webdriver issue
	 * http://code.google.com/p/webdriver/issues/detail?id=12
	 *
	 * @param fileName
	 *            the file name
	 * @param destinationDir
	 *            the destination dir
	 */
	@SuppressWarnings("deprecation")
	public void takeBrowserScreenshot(String fileName, String destinationDir) {
		if (!(new File(destinationDir).exists())) {
			throw new RuntimeException("Directory does not exist: "
					+ destinationDir);
		}

		// save captured image to PNG file
		try {
			String imageFileName = destinationDir + fileName;

			int count = 0;
			String currentImageFilePath = null;
			File f;
			do {
				currentImageFilePath = imageFileName
						+ ((count == 0) ? "" : count) + ".png";
				f = new File(currentImageFilePath);
				count++;
			} while (f.exists());
			if (driver instanceof FirefoxDriver) {
				// ((FirefoxDriver) driver).saveScreenshot(new
				// File(currentImageFilePath));
				File ss = ((FirefoxDriver) driver)
						.getScreenshotAs(OutputType.FILE);
				ss.renameTo(new File(currentImageFilePath));
			} else { // as soon as the IE driver has it's own native saveScreen
				// shot method, we have to use Robot
				Robot robot = null;
				try {
					robot = new Robot();
				} catch (AWTException e1) {
					logger.error("Caught exception: " + e1);
				}
				// create screen shot
				BufferedImage image = robot
						.createScreenCapture(getBrowserRect());
				ImageIO.write(image, "png", new File(currentImageFilePath));
			}
			logger.info("Saved screenshot titled " + fileName + " to: "
					+ imageFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * A temporary method used to identify Safari in it's current dumb
	 * implementation. SafariDriver is not as mature as the other drivers and
	 * lacks some functionality key to some of the methods written
	 *
	 * @return true, if is safari dummy plug
	 */
	protected boolean isSafariDummyPlug() {
		return false;// (driver instanceof SafariDriver && true);
	}

	/**
	 * Executes the onclick handler for an elemen given an id. This method was
	 * created as an alternative to the hack to open up modal dialogs, which was
	 * sending focus to the element and pressing the 'Enter' key from Robot.
	 * That hack does not work if the window does not have focus and thus the
	 * reason for this method.
	 *
	 */
	/*
	 * public void jsClick(String id) { JavascriptExecutor js =
	 * (JavascriptExecutor) driver;
	 * 
	 * String script =
	 * "setTimeout( \"var elem = parent.bodyframe.document.getElementById('" +
	 * id +
	 * "'); if (document.fireEvent) {elem.fireEvent('onclick'); } else  { var evt = document.createEvent('MouseEvents'); evt.initEvent('click', true, true); elem.dispatchEvent(evt); }\", 1000);"
	 * ; js.executeScript(script); WebPage.wait(1000); }
	 */

	/**
	 * Overrides the javascript alert so that we can capture its output
	 */
	public void overrideAlert() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.alertDone = false; window.alert = function(msg) {window.alertDone = true; window.alertMsg = msg}");
	}

	/**
	 * This method reads an xml files with Web Element Meta data and returns a
	 * HashMap containing information stored in all the elements.
	 *
	 * @param filename
	 *            the filename
	 * @param areaSeeked
	 *            the area seeked
	 * @return the web elements from xml
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws SAXException
	 *             the sAX exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static HashMap<String, FacileWebElement> getWebElementsFromXML(
			String filename, String areaSeeked)
			throws ParserConfigurationException, SAXException, IOException {
		logger.info("Trying to get web elements from xml file: " + filename);
		FileInputStream fis = new FileInputStream(filename);
		HashMap<String, FacileWebElement> webElements = getWebElementsFromXML(
				fis, areaSeeked);
		logger.info("Closing the XML file: " + filename);
		fis.close();
		return webElements;
	}

	/**
	 * Gets the web elements from xml.
	 *
	 * @param is
	 *            the is
	 * @param areaSeeked
	 *            the area seeked
	 * @return the web elements from xml
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws SAXException
	 *             the sAX exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static HashMap<String, FacileWebElement> getWebElementsFromXML(
			InputStream is, String areaSeeked)
			throws ParserConfigurationException, SAXException, IOException {
		HashMap<String, FacileWebElement> elementHashMap = new HashMap<String, FacileWebElement>();

		// 1. Open the XML File.
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(is);

		// 2. Read contents from the XML File One Element at a time and Populate
		// the HashMap.
		Node rootNode = doc.getDocumentElement();
		NodeList listOfElements = rootNode.getChildNodes(); // Get all the Child
		// nodes of the root
		// node.

		if (rootNode.hasAttributes()) { // The root node contains the area name
			// of QBO as attribute value.
			NamedNodeMap attributesOfRoot = rootNode.getAttributes();

			// If the elements values seeked by the calling method are not of
			// the same area as in the XML file return null.
			if (!attributesOfRoot.getNamedItem("area").getNodeValue()
					.equalsIgnoreCase(areaSeeked))
				return null;
		}

		/* Getting values out of each element from the XML file. */
		for (int i = 0; i < listOfElements.getLength(); i++) {
			Node currentNode = listOfElements.item(i);
			boolean elementDataPopulated = false;
			// data values to extract from the XML File.
			String id = "";
			String key = "";
			String findBy = "";
			String tag = "";
			String name = "";
			String text = "";
			String classValue = "";
			String xPath = "";
			String ieXPath = "";
			String cssSelector = "";

			/* Getting values out of Tags */
			if (currentNode.hasChildNodes()) {
				NodeList elementFields = currentNode.getChildNodes();

				for (int k = 0; k < elementFields.getLength(); k++) {
					String nodeName = elementFields.item(k).getNodeName();
					String nodeValue = "";

					if (elementFields.item(k).hasChildNodes()) {
						if (elementFields.item(k).getFirstChild()
								.getNodeValue() != null) {
							nodeValue = elementFields.item(k).getFirstChild()
									.getNodeValue();
						}
					} else if (!elementFields.item(k).getNodeName()
							.equalsIgnoreCase("#text")) {
						nodeValue = "";
					}

					if (nodeName.equalsIgnoreCase("key"))
						key = nodeValue;
					else if (nodeName.equalsIgnoreCase("findby"))
						findBy = nodeValue;
					else if (nodeName.equalsIgnoreCase("tag"))
						tag = nodeValue;
					else if (nodeName.equalsIgnoreCase("name"))
						name = nodeValue;
					else if (nodeName.equalsIgnoreCase("text"))
						text = nodeValue;
					else if (nodeName.equalsIgnoreCase("class"))
						classValue = nodeValue;
					else if (nodeName.equalsIgnoreCase("xpath"))
						xPath = nodeValue;
					else if (nodeName.equalsIgnoreCase("id"))
						id = nodeValue;
					else if (nodeName.equalsIgnoreCase("iexpath"))
						ieXPath = nodeValue;
					else if (nodeName.equalsIgnoreCase("cssSelector")) {
						cssSelector = nodeValue;
					} else if (!nodeName.equalsIgnoreCase("#text")) { // The
																		// node
						// is not a
						// valid /
						// known
						// node in
						// the
						// framework.
						// print the
						// message.
						logger.info("\n Not a valid NODE in the XML File : "
								+ nodeName + " : " + nodeValue);
					}
				}

				elementDataPopulated = true;
			}

			/* Getting values out of attributes */
			if (currentNode.hasAttributes()) {
				NamedNodeMap elementAttributesMap = currentNode.getAttributes();

				if (elementAttributesMap.getNamedItem("key") != null)
					key = elementAttributesMap.getNamedItem("key")
							.getNodeValue();

				if (elementAttributesMap.getNamedItem("findBy") != null)
					findBy = elementAttributesMap.getNamedItem("findBy")
							.getNodeValue();

				if (elementAttributesMap.getNamedItem("tag") != null)
					tag = elementAttributesMap.getNamedItem("tag")
							.getNodeValue();

				if (elementAttributesMap.getNamedItem("name") != null)
					name = elementAttributesMap.getNamedItem("name")
							.getNodeValue();

				if (elementAttributesMap.getNamedItem("text") != null)
					text = elementAttributesMap.getNamedItem("text")
							.getNodeValue();

				if (elementAttributesMap.getNamedItem("id") != null)
					id = elementAttributesMap.getNamedItem("id").getNodeValue();

				if (elementAttributesMap.getNamedItem("class") != null)
					classValue = elementAttributesMap.getNamedItem("class")
							.getNodeValue();

				if (elementAttributesMap.getNamedItem("xPath") != null)
					xPath = elementAttributesMap.getNamedItem("xPath")
							.getNodeValue();

				if (elementAttributesMap.getNamedItem("iexpath") != null)
					ieXPath = elementAttributesMap.getNamedItem("iexpath")
							.getNodeValue();

				if (elementAttributesMap.getNamedItem("cssSelector") != null) {
					cssSelector = elementAttributesMap.getNamedItem(
							"cssSelector").getNodeValue();
				}

				elementDataPopulated = true;
			}

			// we create a new Facile element only when we find an element that
			// has attributes or nodes or combination of both.
			if (elementDataPopulated) {
				// We have all the data values to feed to FacileWebElemnt while
				// creating it
				FacileWebElement facileWebElement = new FacileWebElement(key,
						id, name, classValue, text, xPath, findBy, tag,
						ieXPath, cssSelector);
				elementDataPopulated = false;
				// Put the FacileWebElement Object in the hashmap.
				elementHashMap.put(facileWebElement.getElementKey(),
						facileWebElement);
			}
		}

		// 3. Return the HashMap.
		return elementHashMap;
	}

	/**
	 * Switches to the specified frame before overriding the javascript alert.
	 *
	 * @param frame
	 *            name of the frame
	 */
	public void overrideAlert(String frame) {
		logger.info("Switching to frame: " + frame);
		driver.switchTo().frame(frame);
		overrideAlert();
	}

	/**
	 * Append text into text box.
	 *
	 * @param elementKey
	 *            the element key
	 * @param textToWrite
	 *            the text to write
	 * @return true, if successful
	 */
	protected boolean appendTextIntoTextBox(String elementKey,
			String textToWrite) {
		return appendTextIntoTextBox(elementKey, textToWrite, -1);
	}

	/*
	 * This method does the following: 1. Retrieve the Element Information from
	 * the hash map using the elementKey passed as parameter to the method. 2.
	 * Finds the Element on the web page. If Element found it sends text to the
	 * element and returns TRUE. If element is not found or there is some error
	 * in sending text it returns False.
	 */
	/**
	 * Write text into text box.
	 *
	 * @param elementKey
	 *            the element key
	 * @param textToWrite
	 *            the text to write
	 * @param lineNumbers
	 *            the line numbers
	 * @return true, if successful
	 */
	protected boolean appendTextIntoTextBox(String elementKey,
			String textToWrite, int... lineNumbers) {
		logger.info("Trying to write text: " + textToWrite
				+ " in text element: " + elementKey);
		String action = "";
		if (textToWrite.length() > 0) {
			action = "Writing '" + textToWrite + "' to...";
		} else {
			action = "Focusing on...";
		}
		FacileWebElement anElement;

		try {
			// We need to create a clone of the element retrieved from the
			// hashmap as we can't make the hash map dirty.
			anElement = new FacileWebElement(pageElements.get(elementKey));
		} catch (NullPointerException ex) {
			throw new NullPointerException(
					"The element "
							+ elementKey
							+ " key-value pair is not present or is wrong in the xml file!");
		}
		// if the element extracted from the HashMap is a Line Item then we need
		// to replace the "#" character with the LINE NUMBER.
		if (lineNumbers.length > 0) {
			addLineNumberToElement(anElement, "#", lineNumbers);
		}

		waitOnElement(elementKey, 7500);
		if (!isElementVisible(elementKey, lineNumbers)) {
			throw new WebDriverException(
					"Unable to read from non-visible WebElement: "
							+ anElement.toString());
		}

		if (anElement == null) {
			logger.info("Failed to find element: " + elementKey
					+ " in XML file");
			return false; // check if element is not found in the hash map then
			// return FALSE.
		}

		// Log the action
		logger.info(action);

		WebElement elementOfInterest = getWebElementFromFacileWebElement(anElement);

		if (textToWrite == null)
			textToWrite = "";

		try {
			// sending keys.
			WebPage.wait(300);
			elementOfInterest.sendKeys(textToWrite);
		} catch (NullPointerException ex) {
			logger.error("Caught exception " + ex);
			return false;
		}

		// Hack prevent failures while running test cases in remote machine
		// type.tab(driver);

		// Check for JS alerts
		checkForJavascriptAlerts(action + " " + elementKey);

		return true;
	}

	/*
	 * This method Writes text in text elements like text box, text area.
	 */
	/**
	 * Write text into text box.
	 *
	 * @param elementKey
	 *            the element key
	 * @param textToWrite
	 *            the text to write
	 * @return true, if successful
	 */
	protected boolean writeTextIntoTextBox(String elementKey, String textToWrite) {
		return writeTextIntoTextBox(elementKey, textToWrite, -1);
	}

	/*
	 * This method does the following: 1. Retrieve the Element Information from
	 * the hash map using the elementKey passed as parameter to the method. 2.
	 * Finds the Element on the web page. If Element found it sends text to the
	 * element and returns TRUE. If element is not found or there is some error
	 * in sending text it returns False.
	 */
	/**
	 * Write text into text box.
	 *
	 * @param elementKey
	 *            the element key
	 * @param textToWrite
	 *            the text to write
	 * @param lineNumbers
	 *            the line numbers
	 * @return true, if successful
	 */
	protected boolean writeTextIntoTextBox(String elementKey,
			String textToWrite, int... lineNumbers) {
		logger.info("Trying to write text: " + textToWrite
				+ " in text element: " + elementKey);
		String action = "";
		if (textToWrite.length() > 0) {
			action = "Writing '" + textToWrite + "' to...";
		} else {
			action = "Focusing on...";
		}
		FacileWebElement anElement;

		try {
			// We need to create a clone of the element retrieved from the
			// hashmap as we can't make the hash map dirty.
			anElement = new FacileWebElement(pageElements.get(elementKey));
		} catch (NullPointerException ex) {
			throw new NullPointerException(
					"The element "
							+ elementKey
							+ " key-value pair is not present or is wrong in the xml file!");
		}
		// if the element extracted from the HashMap is a Line Item then we need
		// to replace the "#" character with the LINE NUMBER.
		if (lineNumbers.length > 0) {
			addLineNumberToElement(anElement, "#", lineNumbers);
		}

		waitOnElement(elementKey, 7500);
		if (!isElementVisible(elementKey, lineNumbers)) {
			throw new WebDriverException(
					"Unable to read from non-visible WebElement: "
							+ anElement.toString());
		}

		if (anElement == null) {
			logger.info("Failed to find element: " + elementKey
					+ " in XML file");
			return false; // check if element is not found in the hash map then
			// return FALSE.
		}

		// Log the action
		logger.info(action);

		WebElement elementOfInterest = getWebElementFromFacileWebElement(anElement);

		if (textToWrite == null)
			textToWrite = "";

		try {
			// This is needed for Chrome instance
			// The Firefox and InternetExplorer drivers are also RemoteWebDriver
			// so we need to explicitly exclude these drivers from the following
			// click call.
			// Chrome also doesn't need this click call now.
			// if (!(driver instanceof FirefoxDriver || driver instanceof
			// InternetExplorerDriver))
			// elementOfInterest.click();
			elementOfInterest.sendKeys("");
			elementOfInterest.clear(); // clears the text in the element before
			// sending keys.
			WebPage.wait(300);
			elementOfInterest.sendKeys(textToWrite);
		} catch (NullPointerException ex) {
			logger.error("Caught exception " + ex);
			return false;
		}

		// Now attempt to read the value from the field to make sure that it was
		// written
		// correctly. This should fix issues we were seeing where the first part
		// of the
		// text is cut off.
		try {
			logger.info("Trying to make sure that text is entered correctly in text field");
			if (textToWrite.length() > 0
					&& !elementOfInterest.getAttribute("value").equals(
							textToWrite)) {
				elementOfInterest.clear();
				// The Firefox and InternetExplorer drivers are also
				// RemoteWebDriver
				// so we need to explicitly exclude these drivers from the
				// following
				// click call.
				if (!(driver instanceof FirefoxDriver || driver instanceof InternetExplorerDriver))
					elementOfInterest.click();
				elementOfInterest.sendKeys(textToWrite);
			}
		} catch (Exception ex) {
			// Do nothing if this fails since it's just a fallback anyway.
		}

		// Hack prevent failures while running test cases in remote machine
		// type.tab(driver);

		// Check for JS alerts
		checkForJavascriptAlerts(action + " " + elementKey);

		return true;
	}

	/**
	 * Convenience method that will check for a Javascript alert window and
	 * throw a JsAlertPresentException if one is open.
	 *
	 * @param cause
	 *            the cause
	 */
	// TODO: Unit test not written as its not working correctly
	public void checkForJavascriptAlerts(String cause) {
		logger.info("Checking for a Javascript alert window");
		JsAlertPresentException ex = null;
		// TODO: This method is temporary commented, as it is not working
		// perfectly

		/*
		 * try {
		 * 
		 * //Problems exist if we try to look for alerts on a window that
		 * doesn't // exist anymore. This call makes sure that the window still
		 * exists. //String url = driver.getCurrentUrl();
		 * 
		 * // This call is required for IE Browser, otherwise browser hangs
		 * rather than throwing Exception. //TODO: This is not the best
		 * workAorund, will continue to see how best we can handle this.
		 * driver.getTitle();
		 * 
		 * Alert alert = driver.switchTo().alert(); if (alert != null &&
		 * alert.getText() != null) { if (alert.getText().contains(
		 * "Prevent this page from creating additional dialogs")) {
		 * alert.dismiss(); } // Condition for IE, any pop in IE is considered
		 * as JS Alert else if(driver instanceof InternetExplorerDriver &&
		 * alert.getText().equals("")){ // Do nothing } else if
		 * (alert.getText().equals("false")) { //Do nothing //This case is
		 * REQUIRED because sometimes there are ghost // alerts that float
		 * around webdriver. This elseif here // seems to catch any ghost alerts
		 * and dispose of them. } else { logger.info("Alert found: " +
		 * alert.getText()); ex = new JsAlertPresentException(alert, cause); } }
		 * } catch(NoAlertPresentException e) { //Exception will be thrown if
		 * alert doesn't actually exist so we'll // do nothing here. }
		 * catch(Exception exc) { //Do nothing here in case the window has
		 * already been closed because there's no // point looking for a JS
		 * alert on that window. }
		 * 
		 * //If we determined that an alert is visible, then we need to throw
		 * the // exception up the stack. if (ex != null) { throw ex; }
		 */
	}

	/*
	 * This method reads text from text elements like text box, text area.
	 */
	/**
	 * Read text from element.
	 *
	 * @param elementKey
	 *            the element key
	 * @return the string
	 */
	protected String readTextFromElement(String elementKey) {
		return readTextFromElement(elementKey, -1);
	}

	/**
	 * This method does the following: 1. Retrieve the Element Information from
	 * the hash map using the elementKey passed as parameter to the method. 2.
	 * Finds the Element on the web page.
	 *
	 * @param elementKey
	 *            the element key
	 * @param lineNumbers
	 *            the line numbers
	 * @return If Element is found it reads text from the element and returns
	 *         it. else reutnrs null.
	 */
	protected String readTextFromElement(String elementKey, int... lineNumbers) {
		// We need to create a clone of the element retrieved from the hashmap
		// as we cant make the hash map dirty.
		logger.info("Trying to read text from text element: " + elementKey);
		FacileWebElement anElement = new FacileWebElement(
				pageElements.get(elementKey));

		// if the element extracted from the HashMap is a Line Item then we need
		// to replace the "#" character with the LINE NUMBER.
		if (lineNumbers.length > 0) {
			addLineNumberToElement(anElement, "#", lineNumbers);
		}

		waitOnElement(elementKey, 7500);
		if (!isElementVisible(elementKey, lineNumbers)) {
			throw new WebDriverException(
					"Unable to read from non-visible WebElement: "
							+ anElement.toString());
		}

		if (anElement == null) {
			logger.info("Failed to find element: " + elementKey
					+ " in XML file");
			return null; // check if element is not found in the hash map then
			// return FALSE.
		}

		logger.info("Reading text from element...");
		WebElement elementOfInterest = getWebElementFromFacileWebElement(anElement);
		String returnText = null;
		try {
			// Due to an issue with the way that webdriver used to report
			// multiple "&nbsp;" as a single space
			// we have multiple tests that are designed around assuming there
			// are only one space in certain
			// messages. Therefore, we'll replace any instance of multiple
			// spaces with a single space
			// instead.
			returnText = elementOfInterest.getText().replaceAll(" +", " ");
		} catch (NullPointerException ex) {
			logger.error("Caught exception " + ex);
			return "";
		}
		logger.info("Getting text: " + returnText + "  from text element: "
				+ elementKey);
		return returnText;
	}

	/*
	 * This method corrects the elements meta data for the line item elements.
	 */
	/**
	 * Adds the line number to element.
	 *
	 * @param anElement
	 *            the an element
	 * @param toReplace
	 *            the to replace
	 * @param replaceWith
	 *            the replace with
	 */
	protected void addLineNumberToElement(FacileWebElement anElement,
			String toReplace, String replaceWith) {
		logger.info("Trying to validate the elements meta data for the line item elements");
		if (anElement.getElementID() != null)
			anElement.setElementID(anElement.getElementID().replace(toReplace,
					replaceWith));
		if (anElement.getElementName() != null)
			anElement.setElementName(anElement.getElementName().replace(
					toReplace, replaceWith));
		if (anElement.getElementXPath() != null)
			anElement.setElementXPath(anElement.getElementXPath().replace(
					toReplace, replaceWith));
	}

	/**
	 * Adds the line number to element.
	 *
	 * @param anElement
	 *            the an element
	 * @param toReplace
	 *            the to replace
	 * @param replacements
	 *            the replacements
	 */
	protected void addLineNumberToElement(FacileWebElement anElement,
			String toReplace, int[] replacements) {
		logger.info("Trying to validate the elements meta data for the line item elements");
		if (anElement.getElementID() != null)
			anElement.setElementID(replaceStringWithIndexes(
					anElement.getElementID(), toReplace, replacements));
		if (anElement.getElementName() != null)
			anElement.setElementName(replaceStringWithIndexes(
					anElement.getElementName(), toReplace, replacements));
		if (anElement.getElementXPath() != null)
			anElement.setElementXPath(replaceStringWithIndexes(
					anElement.getElementXPath(), toReplace, replacements));
	}

	/**
	 * Replace string with indexes.
	 *
	 * @param original
	 *            the original
	 * @param toReplace
	 *            the to replace
	 * @param replacements
	 *            the replacements
	 * @return the string
	 */
	protected String replaceStringWithIndexes(String original,
			String toReplace, int[] replacements) {
		String result = new String(original);
		for (int i = 0; i < replacements.length; i++) {
			if (replacements[i] == -1) {
				continue;
			}

			result = result.replaceFirst(toReplace, replacements[i] + "");
		}

		return result;
	}

	/**
	 * Checks if is element visible.
	 *
	 * @param elementKey
	 *            the element key
	 * @return true, if is element visible
	 */
	public boolean isElementVisible(String elementKey) {
		return isElementVisible(elementKey, -1);
	}

	/**
	 * Checks if is element visible.
	 *
	 * @param elementKey
	 *            the element key
	 * @param lineNumbers
	 *            the line numbers
	 * @return true, if is element visible
	 */
	public boolean isElementVisible(String elementKey, int... lineNumbers) {
		// WebElement may need to be changed to RenderedWebElement.
		logger.info("Verify visible...");
		try {
			WebElement elementOfInterest = (getWebElementFromFacileWebElement(
					elementKey, lineNumbers));
			return isElementVisible(elementOfInterest);
		} catch (NoSuchElementException ex) {
			logger.error("Caught exception: " + ex);
			return false;
		}
	}

	/**
	 * Checks if is element visible.
	 *
	 * @param element
	 *            the element
	 * @return true, if is element visible
	 */
	private boolean isElementVisible(WebElement element) {
		return element.isDisplayed();
	}

	/**
	 * Checks if is element enabled.
	 *
	 * @param elementKey
	 *            the element key
	 * @return true, if is element enabled
	 */
	public boolean isElementEnabled(String elementKey) {
		return isElementEnabled(elementKey, -1);
	}

	/**
	 * Checks if is element enabled.
	 *
	 * @param elementKey
	 *            the element key
	 * @param lineNumbers
	 *            the line numbers
	 * @return true, if is element enabled
	 */
	public boolean isElementEnabled(String elementKey, int... lineNumbers) {
		logger.info("Verify enabled...");
		WebElement elementOfInterest = getWebElementFromFacileWebElement(
				elementKey, lineNumbers);
		return elementOfInterest.isEnabled();
	}

	/**
	 * Function that should be called to determine whether or not an element is
	 * selected or not. No validation is performed to verify that the element
	 * specified is selectable.
	 *
	 * @param elementKey
	 *            The Facile key representing the particular element to verify
	 *            if it is selected.
	 * @param lineNumbers
	 *            the line numbers
	 * @return True if the element is selected. False otherwise. Execution is
	 *         undefined for elements that can not be selected.
	 */
	protected boolean isElementSelected(String elementKey, int... lineNumbers) {
		logger.info("Checking if the element: " + elementKey + "is selected");
		FacileWebElement checkBox = new FacileWebElement(
				pageElements.get(elementKey));
		if (lineNumbers.length > 0) {
			addLineNumberToElement(checkBox, "#", lineNumbers);
		}

		WebElement selectableElement = getWebElementFromFacileWebElement(checkBox);
		return selectableElement.isSelected();
	}

	/**
	 * Checks if is element selected.
	 *
	 * @param elementKey
	 *            the element key
	 * @return true, if is element selected
	 */
	protected boolean isElementSelected(String elementKey) {
		return isElementSelected(elementKey, -1);
	}

	/**
	 * Function that should be called on radio buttons or dropdowns in order to
	 * select a particular element from a list of elements. The element should
	 * be an "option" or "input" radio button in order to work successfully.
	 *
	 * @param elementKey
	 *            The Facile key representing the particular element to be set
	 *            as the selected option.
	 * @param lineNumbers
	 *            the line numbers
	 */
	protected void setSelected(String elementKey, int... lineNumbers) {
		logger.info("Trying to select element from the list of elements for :"
				+ elementKey);
		String action = "Selecting element...";
		FacileWebElement element = new FacileWebElement(
				pageElements.get(elementKey));
		if (lineNumbers.length > 0) {
			addLineNumberToElement(element, "#", lineNumbers);
		}

		// Log the action performed
		logger.info(action);

		WebElement selectableElement = getWebElementFromFacileWebElement(element);
		// selectableElement.setSelected();
		selectableElement.click();

		checkForJavascriptAlerts(action + " " + elementKey);
	}

	/**
	 * Sets the selected.
	 *
	 * @param elementKey
	 *            the new selected
	 */
	protected void setSelected(String elementKey) {
		setSelected(elementKey, -1);
	}

	/**
	 * Checks if is selected.
	 *
	 * @param elementKey
	 *            the element key
	 * @return true, if is selected
	 */
	protected boolean isSelected(String elementKey) {
		return isSelected(elementKey, -1);
	}

	/**
	 * Checks if is selected.
	 *
	 * @param elementKey
	 *            the element key
	 * @param lineNumbers
	 *            the line numbers
	 * @return true, if is selected
	 */
	protected boolean isSelected(String elementKey, int... lineNumbers) {
		logger.info("Is Element Selected...");
		WebElement webElement = getWebElementFromFacileWebElement(elementKey,
				lineNumbers);
		return webElement.isSelected();
	}

	/**
	 * Gets the select drop down options.
	 *
	 * @param elementKey
	 *            the element key
	 * @return the select drop down options
	 */
	protected List<WebElement> getSelectDropDownOptions(String elementKey) {
		logger.info("Trying to get list of options avalable in dropdown for: "
				+ elementKey);
		FacileWebElement selectDropDown = new FacileWebElement(
				pageElements.get(elementKey));
		WebElement wSelectDropDown = getWebElementFromFacileWebElement(selectDropDown);

		Select sSelectDropDown = new Select(wSelectDropDown);
		return sSelectDropDown.getOptions();
	}

	/**
	 * Gets the default selected option in the dropdown.
	 *
	 * @param elementKey
	 *            the element key
	 * @return the select drop down options
	 */
	public String getDefaultSelectedValueOfDropDown(String elementKey) {

		FacileWebElement selectDropDown = new FacileWebElement(
				pageElements.get(elementKey));
		WebElement wSelectDropDown = getWebElementFromFacileWebElement(selectDropDown);

		Select sSelectDropDown = new Select(wSelectDropDown);
		WebElement webElement = sSelectDropDown.getFirstSelectedOption();
		String currentValue = webElement.getText();
		return currentValue;
		// return "test";
	}

	/**
	 * Gets the select drop down options.
	 *
	 * @param elementKey
	 *            the element key
	 * @param lineNumbers
	 *            the line numbers
	 * @return the select drop down options
	 */
	protected List<WebElement> getSelectDropDownOptions(String elementKey,
			int... lineNumbers) {
		logger.info("Trying to get list of options avalable in dropdown for: "
				+ elementKey);
		FacileWebElement selectDropDown = new FacileWebElement(
				pageElements.get(elementKey));
		if (lineNumbers.length > 0) {
			addLineNumberToElement(selectDropDown, "#", lineNumbers);
		}

		WebElement selectElement = getWebElementFromFacileWebElement(selectDropDown);
		Select sSelectDropDown = new Select(selectElement);

		return sSelectDropDown.getOptions();
	}

	/*
	 * This method rips out the embedded WebElement inside the FacileWebElement
	 * by using meta information inside the FacileWebElement.
	 */
	/**
	 * Gets the web element from facile web element.
	 *
	 * @param aFacileWebElement
	 *            the a facile web element
	 * @return the web element from facile web element
	 */
	protected WebElement getWebElementFromFacileWebElement(
			FacileWebElement aFacileWebElement) {
		logger.info("\t" + aFacileWebElement);

		WebElement elementOfInterest = null;
		try {
			if (aFacileWebElement.getFindBy().equalsIgnoreCase("id")) {
				logger.info("Trying to find element by id");
				elementOfInterest = driver.findElement(By.id(aFacileWebElement
						.getElementID()));
			} else if (aFacileWebElement.getFindBy().equalsIgnoreCase("XPATH")) {
				logger.info("Trying to find element by xpath");
				if (driver instanceof InternetExplorerDriver
						&& aFacileWebElement.getElementIExPath() != null
						&& !aFacileWebElement.getElementIExPath().equals(""))
					elementOfInterest = driver.findElement(By
							.xpath(aFacileWebElement.getElementIExPath()));
				else
					elementOfInterest = driver.findElement(By
							.xpath(aFacileWebElement.getElementXPath()));
			} else if (aFacileWebElement.getFindBy().equalsIgnoreCase("name")) {
				logger.info("Trying to find element by name");
				elementOfInterest = driver.findElement(By
						.name(aFacileWebElement.getElementName()));
			} else if (aFacileWebElement.getFindBy().equalsIgnoreCase("tag")) {
				logger.info("Trying to find element by tag");
				elementOfInterest = driver.findElement(By
						.tagName(aFacileWebElement.getElementTag()));
			} else if (aFacileWebElement.getFindBy().equalsIgnoreCase("text")) {
				logger.info("Trying to find element by text");
				elementOfInterest = driver.findElement(By
						.partialLinkText(aFacileWebElement.getElementText()));
			} else if (aFacileWebElement.getFindBy().equalsIgnoreCase("class")) {
				logger.info("Trying to find element by class");
				elementOfInterest = driver.findElement(By
						.className(aFacileWebElement.getElementClass()));
			} else if (aFacileWebElement.getFindBy().equalsIgnoreCase(
					"cssSelector")) {
				logger.info("Trying to find element by css selector "
						+ aFacileWebElement.getElementCssSelector());
				elementOfInterest = driver
						.findElement(By.cssSelector(aFacileWebElement
								.getElementCssSelector()));
			}
		} catch (Exception ex) {
			// Package up the causing exception into a NoSuchElementException.
			throw new NoSuchElementException("Unable to find element: "
					+ aFacileWebElement, ex);
		}

		// If we can't find the element, attempt to fail fast by throwing an
		// exception.
		if (elementOfInterest == null) {
			throw new NoSuchElementException("Unable to find element: "
					+ aFacileWebElement);
		}

		// Ping the Watchdog that we're still alive.
		notifyListeners();

		return elementOfInterest;
	}

	/**
	 * Gets the web element from facile web element.
	 *
	 * @param elementKey
	 *            the element key
	 * @param lineNumbers
	 *            the line numbers
	 * @return the web element from facile web element
	 */
	protected WebElement getWebElementFromFacileWebElement(String elementKey,
			int... lineNumbers) {
		// waitOnElement(elementKey, WAIT_TIME, false, lineNumbers);
		logger.info(" Trying to find the web element inside the FacileWebElement by using meta information");
		FacileWebElement element = new FacileWebElement(
				pageElements.get(elementKey));
		if (lineNumbers.length > 0) {
			addLineNumberToElement(element, "#", lineNumbers);
		}

		return getWebElementFromFacileWebElement(element);
	}

	/**
	 * Notify listeners.
	 */
	protected void notifyListeners() {
		logger.info("Notifying the listeners that we are alive");
		for (IWebPageListener curr : listeners) {
			curr.onEvent(IWebPageListener.GENERIC_EVENT);
		}
	}

	/**
	 * Attach listener.
	 *
	 * @param l
	 *            the l
	 */
	public static void attachListener(IWebPageListener l) {
		listeners.add(l);
	}

	/**
	 * Detach listener.
	 *
	 * @param l
	 *            the l
	 */
	public static void detachListener(IWebPageListener l) {
		listeners.remove(l);
	}

	/**
	 * This method does the following: 1. Retrieve the Element Information from
	 * the hash map using the elementKey passed as parameter to the method. 2.
	 * Finds the Element on the web page.
	 *
	 * @param elementKey
	 *            the element key
	 * @param lineNumbers
	 *            the line numbers
	 * @return If Element is found it reads the VALUE from the element and
	 *         returns it. else reutnrs null. Input fields store their values in
	 *         the VALUE attribute instead of within innerText.
	 */
	protected String readValueFromElement(String elementKey, int... lineNumbers) {
		// We need to create a clone of the element retrieved from the hashmap
		// as we cant make the hash map dirty.
		logger.info("Trying to read the VALUE from the element: " + elementKey);
		FacileWebElement anElement = new FacileWebElement(
				pageElements.get(elementKey));

		// if the element extracted from the HashMap is a Line Item then we need
		// to replace the "#" character with the LINE NUMBER.
		if (lineNumbers.length > 0) {
			addLineNumberToElement(anElement, "#", lineNumbers);
		}

		if (!isElementVisible(elementKey, lineNumbers)) {
			throw new WebDriverException(
					"Unable to read from non-visible WebElement: "
							+ anElement.toString());
		}

		if (anElement == null) {
			logger.info("Failed to find element: " + elementKey
					+ " in XML file");
			return null; // check if element is not found in the hash map then
			// return FALSE.
		}

		logger.info("Reading value from element...");
		WebElement elementOfInterest = getWebElementFromFacileWebElement(anElement);
		String returnText = null;
		try {
			returnText = elementOfInterest.getAttribute("value");
			if (returnText == null) {
				returnText = elementOfInterest.getText();
			}
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			return "";
		}
		return returnText;
	}

	/**
	 * Read value from element.
	 *
	 * @param elementKey
	 *            the element key
	 * @param replacements
	 *            the replacements
	 * @return the string
	 */
	protected String readValueFromElement(String elementKey,
			String... replacements) {
		logger.info("Trying to read the TEXT VALUE from the element: "
				+ elementKey);
		// We need to create a clone of the element retrieved from the hashmap
		// as we cant make the hash map dirty.
		FacileWebElement anElement = new FacileWebElement(
				pageElements.get(elementKey));

		// if the element extracted from the HashMap is a Line Item then we need
		// to replace the "#" character with the LINE NUMBER.
		if (replacements.length > 0) {
			addLineNumberToElement(anElement, "#", replacements[0]);
		}

		WebElement actualElement = getWebElementFromFacileWebElement(anElement);

		if (!isElementVisible(actualElement)) {
			throw new WebDriverException(
					"Unable to read from non-visible WebElement: "
							+ anElement.toString());
		}

		if (anElement == null) {
			logger.info("Failed to find element: " + elementKey
					+ " in XML file");
			return null; // check if element is not found in the hash map then
			// return FALSE.
		}

		logger.info("Reading value from element...");
		WebElement elementOfInterest = getWebElementFromFacileWebElement(anElement);
		String returnText = null;
		try {
			returnText = elementOfInterest.getAttribute("value");
		} catch (NullPointerException ex) {
			logger.error("Caught exception " + ex);
			return "";
		}
		return returnText;
	}

	/**
	 * Read value from element.
	 *
	 * @param elementKey
	 *            the element key
	 * @param replacements
	 *            the replacements
	 * @return the string
	 */
	protected String readValueFromElement(String elementKey, String replacements) {
		logger.info("Trying to read the TEXT VALUE from the element: "
				+ elementKey);
		// We need to create a clone of the element retrieved from the hashmap
		// as we cant make the hash map dirty.
		FacileWebElement anElement = new FacileWebElement(
				pageElements.get(elementKey));
		if (anElement == null) {
			logger.info("Failed to find element: " + elementKey
					+ " in XML file");
			return null; // check if element is not found in the hash map then
			// return FALSE.
		}

		// if the element extracted from the HashMap is a Line Item then we need
		// to replace the "%" character with the LINE NUMBER.
		if (replacements != null) {
			addLineNumberToElement(anElement, "%", replacements);
		}

		WebElement actualElement = getWebElementFromFacileWebElement(anElement);

		if (!isElementVisible(actualElement)) {
			throw new WebDriverException(
					"Unable to read from non-visible WebElement: "
							+ anElement.toString());
		}

		logger.info("Reading value from element...");
		WebElement elementOfInterest = getWebElementFromFacileWebElement(anElement);
		String returnText = null;
		try {
			returnText = elementOfInterest.getAttribute("value");
		} catch (NullPointerException ex) {
			logger.error("Caught exception " + ex);
			return "";
		}
		return returnText;
	}

	/**
	 * Function that will toggle an onscreen checkbox depending on its current
	 * reported state and the desired ending state. If toChecked is TRUE, then
	 * this function will check the box, otherwise it will toggle the checkbox
	 * to uncheck it.
	 * 
	 * @param elementKey
	 *            The key defined in the XML locators file for the element to
	 *            interact with. Element must be a checkbox component.
	 * @param lineNumber
	 *            The particular line number that this component is part of.
	 *            This distinguishes elements in a list.
	 * @param tobeChecked
	 *            TRUE to check the box false to uncheck it.
	 */
	protected void setCheckBoxToggle(String elementKey, int lineNumber,
			boolean tobeChecked) {
		String action = "Toggling checkbox to "
				+ (tobeChecked ? "Checked..." : "Unchecked..."
						+ "for element: " + elementKey);
		FacileWebElement checkBox = new FacileWebElement(
				pageElements.get(elementKey));
		if (lineNumber > -1)
			addLineNumberToElement(checkBox, "#", "" + lineNumber);

		// Log the action
		logger.info(action);
		WebElement isCheckBox = getWebElementFromFacileWebElement(checkBox);
		// if the value to be set is true and the check box is not selected then
		// we CHECK it.
		// Using the WebElement toggle(...) method doesn't seem to trigger some
		// certain
		// events within QBO.
		if (isCheckBox.isSelected() && !tobeChecked)
			isCheckBox.click();
		// if the value to be set is false and the check box is selected then we
		// UNCHECK it.
		else if (!isCheckBox.isSelected() && tobeChecked)
			isCheckBox.click();
		// for other conditions the check box is in correct state.

		// Check for JS alerts
		checkForJavascriptAlerts(action + " " + elementKey);
	}

	/**
	 * Sets the check box toggle.
	 *
	 * @param elementKey
	 *            the element key
	 * @param toChecked
	 *            the to checked
	 */
	protected void setCheckBoxToggle(String elementKey, boolean toChecked) {
		setCheckBoxToggle(elementKey, -1, toChecked);
	}

	/**
	 * Read value from element.
	 *
	 * @param elementKey
	 *            the element key
	 * @return the string
	 */
	protected String readValueFromElement(String elementKey) {
		return readValueFromElement(elementKey, -1);
	}

	/**
	 * Select drop down by visible text.
	 *
	 * @param elementKey
	 *            the element key
	 * @param visibleText
	 *            the visible text
	 */
	protected void selectDropDownByVisibleText(String elementKey,
			String visibleText) {
		String action = "Selecting '" + visibleText
				+ "' from dropdown element..." + " for element: " + elementKey;

		// Log the action
		logger.info(action);

		List<WebElement> items = getSelectDropDownOptions(elementKey);
		boolean itemFound = false;
		for (WebElement item : items) {
			if (item.getText().trim().equalsIgnoreCase(visibleText.trim())) {
				logger.info(item.getText().trim()
						.equalsIgnoreCase(visibleText.trim()));
				item.click();
				itemFound = true;
				break;
			}
		}

		if (itemFound) {
			Select dropDown = getSelectWebElement(elementKey);
			String repString = null;
			clickOnElement(elementKey, repString);
			dropDown.selectByVisibleText(visibleText);
		} else {
			for (WebElement item : items) {
				if (item.getText().toLowerCase()
						.contains(visibleText.toLowerCase())) {
					item.click();
					break;
				}
			}
		}

		// Check for JS alerts
		checkForJavascriptAlerts(action + " " + elementKey);
	}

	/**
	 * Select drop down by visible text.
	 *
	 * @param elementKey
	 *            the element key
	 * @param visibleText
	 *            the visible text
	 */
	protected void selectDropDownWithWindowSwitching(String elementKey,
			String visibleText, String currentWindow) {
		String action = "Selecting '" + visibleText
				+ "' from dropdown element..." + " for element: " + elementKey;

		// Log the action
		logger.info(action);

		List<WebElement> items = getSelectDropDownOptions(elementKey);
		boolean itemFound = false;
		for (WebElement item : items) {
			if (item.getText().trim().equalsIgnoreCase(visibleText.trim())) {
				logger.info(item.getText().trim()
						.equalsIgnoreCase(visibleText.trim()));
				Actions actions = new Actions(driver);
				actions.click(item);
				item.click();
				itemFound = true;
				break;
			}
		}

		if (itemFound) {
			switchWindow(currentWindow);
			Select dropDown = getSelectWebElement(elementKey);
			String repString = null;
			clickOnElement(elementKey, repString);
			dropDown.selectByVisibleText(visibleText);
		} else {
			for (WebElement item : items) {
				if (item.getText().toLowerCase()
						.contains(visibleText.toLowerCase())) {
					item.click();
					break;
				}
			}
		}

		// Check for JS alerts
		checkForJavascriptAlerts(action + " " + elementKey);
	}

	/*
	 * Switch the windows
	 */

	public void switchWindow(String mainWindow) {

		Set<String> s = driver.getWindowHandles();
		Iterator<String> ite = s.iterator();
		while (ite.hasNext()) {
			String popup = ite.next();
			if (!popup.equalsIgnoreCase(mainWindow)) {
				driver.switchTo().window(popup);
				break;
			}
		}
	}

	public String getCurrentWindow() {
		Set<String> s = driver.getWindowHandles();
		Iterator<String> ite = s.iterator();
		while (ite.hasNext()) {
			return ite.next();
		}
		return null;
	}

	/*
	 * Getting selected dropDown
	 */

	private Select getSelectWebElement(String elementKey) {
		logger.info("Trying to get list of options avalable in dropdown for: "
				+ elementKey);
		FacileWebElement selectDropDown = new FacileWebElement(
				pageElements.get(elementKey));
		WebElement wSelectDropDown = getWebElementFromFacileWebElement(selectDropDown);

		Select sSelectDropDown = new Select(wSelectDropDown);
		return sSelectDropDown;
	}

	/**
	 * Select drop down by visible text.
	 *
	 * @param elementKey
	 *            the element key
	 * @param visibleText
	 *            the visible text
	 */
	protected void selectDropDownByIndex(String elementKey, int index) {
		String action = "Selecting '" + index + "' from dropdown element..."
				+ " for element: " + elementKey;

		// Log the action
		logger.info(action);

		List<WebElement> items = getSelectDropDownOptions(elementKey);
		boolean itemFound = false;
		int i = 1;
		for (WebElement item : items) {
			if (i == index) {
				item.click();
				itemFound = true;
				break;
			}
			i++;
		}

		// Default back to the normal way of searching for elements.
		if (!itemFound) {
			logger.info("Element with " + elementKey + " and index " + index
					+ " not found");
		}

		// Check for JS alerts
		checkForJavascriptAlerts(action + " " + elementKey);
	}

	/**
	 * Select drop down by visible text.
	 *
	 * @param elementKey
	 *            the element key
	 * @param visibleText
	 *            the visible text
	 * @param indexes
	 *            the indexes
	 */
	protected void selectDropDownByVisibleText(String elementKey,
			String visibleText, int... indexes) {
		String action = "Selecting '" + visibleText
				+ "' from dropdown element..." + " for element: " + elementKey;

		// Log the action
		logger.info(action);

		List<WebElement> items = getSelectDropDownOptions(elementKey, indexes);
		boolean itemFound = false;
		for (WebElement item : items) {
			if (item.getText().trim().equals(visibleText.trim())) {
				item.click();
				itemFound = true;
				break;
			}
		}

		// Default back to the normal way of searching for elements.
		if (!itemFound) {
			for (WebElement item : items) {
				if (item.getText().contains(visibleText)) {
					item.click();
					break;
				}
			}
		}

		// Check for JS alerts
		checkForJavascriptAlerts(action);
	}

	/**
	 * Gets the selection from drop down.
	 *
	 * @param elementKey
	 *            the element key
	 * @param indexes
	 *            the indexes
	 * @return the selection from drop down
	 */
	protected String getSelectionFromDropDown(String elementKey, int... indexes) {
		String action = "Reading from dropdown '" + elementKey + "' ";

		// Log the action
		logger.info(action);

		List<WebElement> items = getSelectDropDownOptions(elementKey, indexes);
		for (WebElement curr : items) {
			if (curr.isSelected()) {
				return curr.getText();
			}
		}

		return null;
	}

	/**
	 * Wait on element.
	 *
	 * @param elementKey
	 *            the element key
	 * @param timeout
	 *            the timeout
	 * @param ignoreRendering
	 *            the ignore rendering
	 * @param lineNumbers
	 *            the line numbers
	 * @return true, if successful
	 */
	public boolean waitOnElement(String elementKey, int timeout,
			boolean ignoreRendering, int... lineNumbers) {
		logger.info("Waiting on element: " + elementKey);
		FacileWebElement element = new FacileWebElement(
				pageElements.get(elementKey));
		if (lineNumbers.length > 0) {
			addLineNumberToElement(element, "#", lineNumbers);
		}

		if (element.getFindBy().equalsIgnoreCase("ID")) {
			return waitOnId(element.getElementID(), timeout, null,
					ignoreRendering);
		} else if (element.getFindBy().equalsIgnoreCase("NAME")) {
			return waitOnName(element.getElementName(), timeout, null,
					ignoreRendering);
		} else if (element.getFindBy().equalsIgnoreCase("XPATH")) {
			return waitOnXPath(elementKey, timeout, null, ignoreRendering);
		} else {
			return true;
		}
	}

	/**
	 * Wait on element.
	 *
	 * @param elementKey
	 *            the element key
	 * @return true, if successful
	 */
	public boolean waitOnElement(String elementKey) {
		return waitOnElement(elementKey, -1, WAIT_TIME, false);
	}

	/**
	 * Wait on element.
	 *
	 * @param elementKey
	 *            the element key
	 * @param timeout
	 *            the timeout
	 * @return true, if successful
	 */
	public boolean waitOnElement(String elementKey, int timeout) {
		return waitOnElement(elementKey, -1, timeout, false);
	}

	/**
	 * Wait for a particular Facile element to appear.
	 *
	 * @param elementyKey
	 *            the elementy key
	 * @param lineNumber
	 *            the line number
	 * @param timeout
	 *            the timeout
	 * @param ignoreRendering
	 *            the ignore rendering
	 * @return true, if successful
	 */
	public boolean waitOnElement(String elementyKey, int lineNumber,
			int timeout, boolean ignoreRendering) {
		logger.info("Waiting on element: " + elementyKey + " to appear");
		FacileWebElement element = new FacileWebElement(
				pageElements.get(elementyKey));
		if (lineNumber > -1)
			addLineNumberToElement(element, "#", "" + lineNumber);

		if (element.getFindBy().equalsIgnoreCase("ID")) {
			return waitOnId(element.getElementID(), timeout, ignoreRendering);
		} else if (element.getFindBy().equalsIgnoreCase("NAME")) {
			return waitOnName(element.getElementName(), timeout, null,
					ignoreRendering);
		} else if (element.getFindBy().equalsIgnoreCase("XPATH")) {
			return waitOnXPath(element.getElementXPath(), timeout, null,
					ignoreRendering);
		}

		return false;
	}

	/**
	 * This method uses the specified By object to click on an element that
	 * produces a popup window like a Javascript alert.
	 * 
	 * @param element
	 *            WebElement. A WebElement you want to click on.
	 */
	public void clickOnWindowProducingElement(WebElement element) {
		clickOnWindowProducingElement(element, element.getAttribute("ID"));
	}

	/**
	 * Click on window producing element.
	 *
	 * @param element
	 *            the element
	 * @param elementToken
	 *            the element token
	 */
	private void clickOnWindowProducingElement(WebElement element,
			String elementToken) {
		String action = "Click on element. Expecting new window...";
		logger.info(action);

		if (driver instanceof InternetExplorerDriver) {
			element.click();
		} else {
			element.sendKeys("");
			type.space();
		}

		// Ensure that no JS alerts are visible
		checkForJavascriptAlerts(action + " " + elementToken);
	}

	/**
	 * Click on window producing element.
	 *
	 * @param elementKey
	 *            the element key
	 */
	public void clickOnWindowProducingElement(String elementKey) {
		WebElement element = getWebElementFromFacileWebElement(elementKey, -1);
		clickOnWindowProducingElement(element, elementKey);
	}

	/*
	 * This method clicks on an element -whose KEY is passed- on the Web Page.
	 */
	/**
	 * Click on independent element.
	 *
	 * @param elementKey
	 *            the element key
	 * @return true, if successful
	 */
	protected boolean clickOnIndependentElement(String elementKey) {
		return clickOnElement(elementKey, -1);
	}

	/*
	 * This method clicks on an element -whose KEY is passed- on the Web Page.
	 */
	/**
	 * Click on independent element.
	 *
	 * @param elementKey
	 *            the element key
	 * @param replacementString
	 *            the replacement string
	 * @return true, if successful
	 */
	protected boolean clickOnIndependentElement(String elementKey,
			String replacementString) {
		return clickOnElement(elementKey, replacementString);
	}

	/*
	 * This method clicks on an element -Whose Generic KEY is passed containing
	 * # along with the line number where the element is present- on the web
	 * page.
	 */
	/**
	 * Click on element.
	 *
	 * @param elementKey
	 *            the element key
	 * @param replacementString
	 *            the replacement string
	 * @return true, if successful
	 */
	protected boolean clickOnElement(String elementKey, String replacementString) {
		String action = "Clicking on..." + elementKey;

		// We need to create a clone of the element retrieved from the hashmap
		// as we cant make the hash map dirty.
		FacileWebElement anElement = new FacileWebElement(
				pageElements.get(elementKey));

		// if the element extracted from the HashMap is a Line Item then we need
		// to replace the "#" character with the LINE NUMBER.
		if (replacementString != null) {
			addDynamicTextToElement(anElement, "%", replacementString);
		}

		if (anElement == null) {
			logger.info("Failed to find element: " + elementKey
					+ " in XML file");
			return false; // check if element is not found in the hash map then
			// return FALSE.
		}

		// Log the action
		logger.info(action);
		try {
			WebElement elementOfInterest = getWebElementFromFacileWebElement(anElement);
			elementOfInterest.click();
		} catch (TimeoutException ex) {
			// do nothing this behaviour is seen on IE. Exact reason is not
			// known as on 7 jan 2009
			logger.info("Exception while clicking an element : ");
			logger.error("Caught exception " + ex);
		} catch (NullPointerException ex) {
			logger.error("Caught exception " + ex);
			return false;
		}
		return true;

	}

	protected boolean mouseOverElementAndClick(String firstElementKey,
			String secondElementKey) {
		String action = "Clicking on..." + secondElementKey;

		// We need to create a clone of the element retrieved from the hashmap
		// as we cant make the hash map dirty.
		FacileWebElement firstElement = new FacileWebElement(
				pageElements.get(firstElementKey));
		FacileWebElement secondElement = new FacileWebElement(
				pageElements.get(secondElementKey));

		if (firstElement == null || secondElement == null) {
			logger.info("Failed to find element: " + firstElement + " and "
					+ secondElement + " in XML file");
			return false; // check if element is not found in the hash map then
			// return FALSE.
		}

		// Log the action
		logger.info(action);
		try {
			WebElement element1 = getWebElementFromFacileWebElement(firstElement);
			WebElement element2 = getWebElementFromFacileWebElement(secondElement);
			Actions actions = new Actions(driver);
			actions.moveToElement(element1).perform();
			actions.click(element2);

		} catch (TimeoutException ex) {
			// do nothing this behaviour is seen on IE. Exact reason is not
			// known as on 7 jan 2009
			logger.info("Exception while clicking an element : ");
			logger.error("Caught exception " + ex);
		} catch (NullPointerException ex) {
			logger.error("Caught exception " + ex);
			return false;
		}
		return true;
	}

	protected boolean mouseOverElement(String firstElementKey) {
		String action = "Clicking on..." + firstElementKey;

		// We need to create a clone of the element retrieved from the hashmap
		// as we cant make the hash map dirty.
		FacileWebElement firstElement = new FacileWebElement(
				pageElements.get(firstElementKey));

		if (firstElement == null) {
			logger.info("Failed to find element: " + firstElement
					+ " in XML file");
			return false; // check if element is not found in the hash map then
			// return FALSE.
		}

		// Log the action
		logger.info(action);
		try {
			WebElement elementOfInterest = getWebElementFromFacileWebElement(firstElement);
			Actions actions = new Actions(driver);
			actions.moveToElement(elementOfInterest).perform();

		} catch (TimeoutException ex) {
			// do nothing this behaviour is seen on IE. Exact reason is not
			// known as on 7 jan 2009
			logger.info("Exception while clicking an element : ");
			logger.error("Caught exception " + ex);
		} catch (NullPointerException ex) {
			logger.error("Caught exception " + ex);
			return false;
		}
		return true;
	}

	/*
	 * This method clicks on an element -Whose Generic KEY is passed containing
	 * # along with the line number where the element is present- on the web
	 * page.
	 */
	/**
	 * Click on element.
	 *
	 * @param elementKey
	 *            the element key
	 * @param lineNumbers
	 *            the line numbers
	 * @return true, if successful
	 */
	protected boolean clickOnElement(String elementKey, int... lineNumbers) {
		String action = "Clicking on..." + elementKey;
		// We need to create a clone of the element retrieved from the hashmap
		// as we cant make the hash map dirty.
		FacileWebElement anElement = new FacileWebElement(
				pageElements.get(elementKey));

		// if the element extracted from the HashMap is a Line Item then we need
		// to replace the "#" character with the LINE NUMBER.
		if (lineNumbers.length > 0) {
			addLineNumberToElement(anElement, "#", lineNumbers);
		}

		if (anElement == null) {
			logger.info("Failed to find element: " + elementKey
					+ " in XML file");
			return false; // check if element is not found in the hash map then
			// return FALSE.
		}

		// Log the action
		logger.info(action);
		try {
			WebElement elementOfInterest = getWebElementFromFacileWebElement(anElement);
			if (driver instanceof InternetExplorerDriver) {
				if (elementOfInterest.getAttribute("type") != null) {
					if (elementOfInterest.getTagName().equalsIgnoreCase("div")
					// || (elementOfInterest.getTagName().equalsIgnoreCase("a")
					// && elementOfInterest.getText().contains("FacileCompany"))
							|| elementOfInterest.getTagName().equalsIgnoreCase(
									"a")
							// ||
							// (elementOfInterest.getTagName().equalsIgnoreCase("img")
							// && elementOfInterest.getText().equals(""))
							|| elementOfInterest.getAttribute("type")
									.equalsIgnoreCase("radio")) {
						logger.info("elementOfInterest for IE click ---> "
								+ elementOfInterest.getText());
						elementOfInterest.click();
					} else {
						logger.info("elementOfInterest for IE sendKeys ---> "
								+ elementOfInterest.getText());
						elementOfInterest.sendKeys("\n");
					}
				} else {
					elementOfInterest.click();
				}
				WebPage.wait(10000); // TODO: to check whether we need this wait
				// for both click & sendKeys
			} else {
				elementOfInterest.click();
			}
		} catch (TimeoutException ex) {
			// do nothing this behaviour is seen on IE. Exact reason is not
			// known as on 7 jan 2009
			logger.info("Exception while clicking an element : ");
			logger.error("Caught exception " + ex);
		} catch (NullPointerException ex) {
			logger.error("Caught exception " + ex);
			return false;
		}

		// Check for JS alert
		checkForJavascriptAlerts(action + " " + elementKey);

		return true;
	}

	/**
	 * Checks if is modal window present.
	 *
	 * @return true, if is modal window present
	 */
	public boolean isModalWindowPresent() {
		logger.info("Looking for modal windows");
		checkForJavascriptAlerts("Looking for modal windows");

		int numberOfOpenWindows = driver.getWindowHandles().size();
		if (numberOfOpenWindows > 1)
			return true;
		else
			return false;
	}

	/**
	 * This method is used to set focus on a particular Web Element eg: button.
	 * This is a workaround as no existing API supports this in GWD.
	 * 
	 * @param elementKey
	 *            : key of the element to be interacted with on the Page.
	 * @return TRUE if Set Focus operation is done successfully. Otherwise
	 *         returns FALSE.
	 */
	protected boolean setFocusOnElement(String elementKey) {
		String action = "Focussing on Element..." + elementKey;
		// We need to create a clone of the element retrieved from the hashmap
		// as we can't make the hash map dirty.
		FacileWebElement anElement = new FacileWebElement(
				pageElements.get(elementKey));
		if (anElement == null) {
			logger.info("Failed to find element: " + elementKey
					+ " in XML file");
			return false; // check if element is not found in the hash map then
			// return FALSE.
		}

		// Log the action
		logger.info(action);
		WebElement elementOfInterest = getWebElementFromFacileWebElement(anElement);

		try {
			elementOfInterest.sendKeys("");
		} catch (NullPointerException ex) {
			logger.error("Caught exception " + ex);
			return false;
		}
		// Check for JS alerts
		checkForJavascriptAlerts(action + " " + elementKey);
		return true;
	}

	/**
	 * You would need to override this method in anypage which extends WebPage()
	 * to point to your custome XXXXXXIndex.xml file. This helps in de-coupling
	 * Facile from Live Something like this: protected String getIndexFileName()
	 * { return "/com/ooyala/webdriver/root/neoIndex.xml"; }
	 *
	 * @return the index file name
	 */
	abstract protected String getIndexFileName();

	/**
	 * Adds the element to page elements.
	 *
	 * @param qboAreaName
	 *            the qbo area name
	 */
	protected void addElementToPageElements(String qboAreaName) {
		// Read the root index file to get path of elements xml file.
		String indexFileName = getIndexFileName();
		logger.info("Trying to add element: " + qboAreaName + " to index file "
				+ indexFileName);

		String elementfile = ReadPropertyFile.getConfigurationParameter(
				indexFileName, qboAreaName);

		// read the element xml file for the desired area and populate the page
		// elements object.
		try {
			InputStream is = WebPage.class.getClassLoader()
					.getResourceAsStream(elementfile);
			if (is == null)
				is = new FileInputStream(elementfile);

			pageElements.putAll(getWebElementsFromXML(is, qboAreaName));
			logger.info("Retrieving elements from : " + elementfile
					+ " For area : " + qboAreaName);
		} catch (Exception ex) {
			throw new RuntimeException("Unable to load the xml for area: "
					+ qboAreaName, ex);
		}
		for (Map.Entry<String, FacileWebElement> entry : pageElements
				.entrySet()) {
			String extractedKey = "";
			if (entry.getValue().getElementID().contains(BEGIN_MARKER)) {
				entry.getValue().setElementID(
						replaceFromBundle(entry.getValue().getElementID()));
			} else if (entry.getValue().getElementName().contains(BEGIN_MARKER)) {
				entry.getValue().setElementName(
						replaceFromBundle(entry.getValue().getElementName()));
			} else if (entry.getValue().getElementText().contains(BEGIN_MARKER)) {
				entry.getValue().setElementText(
						replaceFromBundle(entry.getValue().getElementText()));
			} else if (entry.getValue().getElementClass()
					.contains(BEGIN_MARKER)) {
				entry.getValue().setElementClass(
						replaceFromBundle(entry.getValue().getElementClass()));
			} else if (entry.getValue().getElementXPath()
					.contains(BEGIN_MARKER)) {
				entry.getValue().setElementXPath(
						replaceFromBundle(entry.getValue().getElementXPath()));
			}
		}
	}

	/**
	 * Replaces the String of pattern $KEY$ with the correct value form the
	 * resource bundle
	 * 
	 * @param value
	 * @return
	 */
	private String replaceFromBundle(String value) {
		String extractedKey = StringUtils.substringBetween(value, BEGIN_MARKER,
				END_MARKER);
		String orignalString = value;
		String replacedString = orignalString.replace(orignalString.substring(
				orignalString.indexOf(BEGIN_MARKER),
				orignalString.lastIndexOf(END_MARKER) + 1),
				getLocalizedPageElementString(extractedKey));
		return replacedString;
	}

	/**
	 * Method that will be used to determine that the corresponding browser page
	 * has been completely loaded. It is expected that execution should wait
	 * inside this method until the page is ready.
	 * 
	 * True is only returned if the page is ready to be interacted with. False
	 * is usually returned if the thread has waited for a particular condition
	 * to become available and it does not within a particular timeout period.
	 * 
	 * @return True if the page is completely loaded. False otherwise.
	 */
	abstract public boolean waitForPage();

	/**
	 * Gets the web elements from web element by tr.
	 *
	 * @param webObj
	 *            the web obj
	 * @return the web elements from web element by tr
	 */
	protected List<WebElement> getWebElementsFromWebElementByTR(
			WebElement webObj) {
		logger.info("Trying to get web elements by table row tag for: "
				+ webObj);
		List<WebElement> elementOfInterest = null;

		try {

			elementOfInterest = webObj.findElements(By.tagName("tr"));
			logger.info("No of rows ->" + elementOfInterest);

		} catch (Exception ex) {

			throw new NoSuchElementException("Unable to find element: "
					+ webObj, ex);

		}

		return elementOfInterest;

	}

	/**
	 * Gets the web elements from web element by td.
	 *
	 * @param webObj
	 *            the web obj
	 * @return the web elements from web element by td
	 */
	protected List<WebElement> getWebElementsFromWebElementByTD(
			WebElement webObj) {
		logger.info("Trying to get web elements by table data tag for: "
				+ webObj);
		List<WebElement> elementOfInterest = null;

		try {

			elementOfInterest = webObj.findElements(By.tagName("td"));
			logger.info("No of columns ->" + elementOfInterest);

		} catch (Exception ex) {

			throw new NoSuchElementException("Unable to find element: "
					+ webObj, ex);

		}

		return elementOfInterest;

	}

	// This methos will return List of webElements

	/**
	 * Gets the web element from facile web elements.
	 *
	 * @param aFacileWebElement
	 *            the a facile web element
	 * @return the web element from facile web elements
	 */
	protected List<WebElement> getWebElementFromFacileWebElements(
			FacileWebElement aFacileWebElement) {
		logger.info("\t" + aFacileWebElement);

		List<WebElement> elementOfInterest = null;
		try {
			if (aFacileWebElement.getFindBy().equalsIgnoreCase("id")) {
				logger.info("Trying to find web elements by id");
				elementOfInterest = driver.findElements(By.id(aFacileWebElement
						.getElementID()));
			} else if (aFacileWebElement.getFindBy().equalsIgnoreCase("XPATH")) {
				logger.info("Trying to find web elements by xpath");
				if (driver instanceof InternetExplorerDriver
						&& aFacileWebElement.getElementIExPath() != null
						&& !aFacileWebElement.getElementIExPath().equals(""))
					elementOfInterest = driver.findElements(By
							.xpath(aFacileWebElement.getElementIExPath()));
				else
					elementOfInterest = driver.findElements(By
							.xpath(aFacileWebElement.getElementXPath()));
			} else if (aFacileWebElement.getFindBy().equalsIgnoreCase("name")) {
				logger.info("Trying to find web elements by name");
				elementOfInterest = driver.findElements(By
						.name(aFacileWebElement.getElementName()));
			} else if (aFacileWebElement.getFindBy().equalsIgnoreCase("tag")) {
				logger.info("Trying to find web elements by tag");
				elementOfInterest = driver.findElements(By
						.tagName(aFacileWebElement.getElementTag()));
			} else if (aFacileWebElement.getFindBy().equalsIgnoreCase("text")) {
				logger.info("Trying to find web elements by text");
				elementOfInterest = driver.findElements(By
						.partialLinkText(aFacileWebElement.getElementText()));
			}

		} catch (Exception ex) {
			// Package up the causing exception into a NoSuchElementException.
			throw new NoSuchElementException("Unable to find element: "
					+ aFacileWebElement, ex);
		}

		// If we can't find the element, attempt to fail fast by throwing an
		// exception.
		if (elementOfInterest == null) {
			throw new NoSuchElementException("Unable to find element: "
					+ aFacileWebElement);
		}

		// Ping the Watchdog that we're still alive.
		notifyListeners();

		return elementOfInterest;
	}

	// This method is to return webElements from webElement , we can iterate
	// inside a webElement

	/**
	 * Gets the web elements from web element.
	 *
	 * @param webObj
	 *            the web obj
	 * @param searchObj
	 *            the search obj
	 * @return the web elements from web element
	 */
	protected List<WebElement> getWebElementsFromWebElement(WebElement webObj,
			FacileWebElement searchObj) {

		logger.info("\t" + webObj);

		List<WebElement> elementOfInterest = null;
		try {
			if (searchObj.getFindBy().equalsIgnoreCase("id"))
				elementOfInterest = webObj.findElements(By.id(searchObj
						.getElementID()));

			else if (searchObj.getFindBy().equalsIgnoreCase("XPATH")) {
				if (driver instanceof InternetExplorerDriver
						&& searchObj.getElementIExPath() != null
						&& !searchObj.getElementIExPath().equals(""))
					elementOfInterest = webObj.findElements(By.xpath(searchObj
							.getElementIExPath()));
				else
					elementOfInterest = webObj.findElements(By.xpath(searchObj
							.getElementXPath()));
			} else if (searchObj.getFindBy().equalsIgnoreCase("name"))
				elementOfInterest = webObj.findElements(By.name(searchObj
						.getElementName()));

			else if (searchObj.getFindBy().equalsIgnoreCase("tag"))
				elementOfInterest = webObj.findElements(By.tagName(searchObj
						.getElementTag()));

			else if (searchObj.getFindBy().equalsIgnoreCase("text"))
				elementOfInterest = webObj.findElements(By
						.partialLinkText(searchObj.getElementText()));

			else if (searchObj.getFindBy().equalsIgnoreCase("class"))
				elementOfInterest = webObj.findElements(By.className(searchObj
						.getElementClass()));

		} catch (Exception ex) {
			// Package up the causing exception into a NoSuchElementException.
			throw new NoSuchElementException("Unable to find element: "
					+ webObj, ex);
		}

		// If we can't find the element, attempt to fail fast by throwing an
		// exception.
		if (elementOfInterest == null) {
			throw new NoSuchElementException("Unable to find element: "
					+ webObj);
		}

		// Ping the Watchdog that we're still alive.
		notifyListeners();

		return elementOfInterest;
	}

	/**
	 * Adds the dynamic text to element.
	 *
	 * @param anElement
	 *            the an element
	 * @param toReplace
	 *            the to replace
	 * @param replacements
	 *            the replacements
	 */
	protected void addDynamicTextToElement(FacileWebElement anElement,
			String toReplace, String replacements) {
		if (anElement.getElementXPath() != null)
			anElement.setElementXPath(replaceSpecialCharWithString(
					anElement.getElementXPath(), toReplace, replacements));
		if (anElement.getElementID() != null)
			anElement.setElementID(replaceSpecialCharWithString(
					anElement.getElementID(), toReplace, replacements));
		if (anElement.getElementName() != null)
			anElement.setElementName(replaceSpecialCharWithString(
					anElement.getElementName(), toReplace, replacements));
	}

	/**
	 * Replace special char with string.
	 *
	 * @param original
	 *            the original
	 * @param toReplace
	 *            the to replace
	 * @param replacements
	 *            the replacements
	 * @return the string
	 */
	protected String replaceSpecialCharWithString(String original,
			String toReplace, String replacements) {
		String result = new String(original);
		if (replacements == null && replacements.equals("")) {
			return null;
		}
		result = result.replaceFirst(toReplace, replacements);

		return result;
	}

	/**
	 * Gets the web element from facile web element.
	 *
	 * @param elementKey
	 *            the element key
	 * @param replacementString
	 *            the replacement string
	 * @return the web element from facile web element
	 */
	protected WebElement getWebElementFromFacileWebElement(String elementKey,
			String replacementString) {

		FacileWebElement element = new FacileWebElement(
				pageElements.get(elementKey));
		if (replacementString != null) {
			addDynamicTextToElement(element, "%", replacementString);
		}

		return getWebElementFromFacileWebElement(element);
	}

	/**
	 * Gets the localized string from the appropriate resource bundle to be
	 * replaced in the page element
	 * 
	 * @param extractedKey
	 * @return
	 */
	protected abstract String getLocalizedPageElementString(String extractedKey);

	/**
	 * Gets the localized String from the appropriate resource bundle for the
	 * page titles
	 * 
	 * Use this method in the implementation for waitForPage() method to wait
	 * for the localized page title
	 * 
	 * @param key
	 * @return
	 */
	// protected abstract String getLocalizedTitleForPage(String key);

}
