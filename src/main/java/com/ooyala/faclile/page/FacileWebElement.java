/**
 * 
 */
package com.ooyala.faclile.page;

// TODO: Auto-generated Javadoc
/**
 * A simple bean class to represent Elements on a web Page.
 * 
 * @author pkumar
 * 
 */
public class FacileWebElement {

	/** The element key. */
	private String elementKey = null; // The unique key associated with the
										// element eg: Page_Body_Title

	// Ways in which the element can be found.
	/** The element id. */
	private String elementID = null;

	/** The element name. */
	private String elementName = null;

	/** The element class. */
	private String elementClass = null;

	/** The element text. */
	private String elementText = null;

	/** The element x path. */
	private String elementXPath = null;

	/** The element tag. */
	private String elementTag = null;

	/** The element i ex path. */
	private String elementIExPath = null;

	/** The element css selector. */
	private String elementCssSelector = null;

	// Preferred way of finding an element.
	/** The find by. */
	private String findBy = null;

	/** The xml template. */
	private String XML_TEMPLATE = "<element key=\"%s\" findBy=\"%s\" id=\"%s\" tag=\"%s\" name=\"%s\" text=\"%s\" class=\"%s\" xPath=\"%s\" ieXPath=\"%s\"  cssSelector=\"%s\"/>"
			.toLowerCase();

	/**
	 * Instantiates a new Facile web element.
	 * 
	 * @param elementKey
	 *            the element key
	 * @param elementID
	 *            the element id
	 * @param elementName
	 *            the element name
	 * @param elementClass
	 *            the element class
	 * @param elementText
	 *            the element text
	 * @param elementXPath
	 *            the element x path
	 * @param findBy
	 *            the find by
	 * @param elementTag
	 *            the element tag
	 * @param elementIExPath
	 *            the element i ex path
	 * @param elementCssSelector
	 *            the element css selector
	 */
	public FacileWebElement(String elementKey, String elementID,
			String elementName, String elementClass, String elementText,
			String elementXPath, String findBy, String elementTag,
			String elementIExPath, String elementCssSelector) {

		this.elementKey = elementKey;
		this.elementID = elementID;
		this.elementName = elementName;
		this.elementClass = elementClass;
		this.elementText = elementText;
		this.elementXPath = elementXPath;
		this.findBy = findBy;
		this.elementTag = elementTag;
		this.elementIExPath = elementIExPath;
		this.elementCssSelector = elementCssSelector;

	}

	/**
	 * Instantiates a new facile web element.
	 * 
	 * @param aFacileWebElement
	 *            the a facile web element
	 */
	public FacileWebElement(FacileWebElement aFacileWebElement) {
		this.elementKey = aFacileWebElement.elementKey;
		this.elementID = aFacileWebElement.elementID;
		this.elementName = aFacileWebElement.elementName;
		this.elementClass = aFacileWebElement.elementClass;
		this.elementText = aFacileWebElement.elementText;
		this.elementXPath = aFacileWebElement.elementXPath;
		this.findBy = aFacileWebElement.findBy;
		this.elementTag = aFacileWebElement.elementTag;
		this.elementIExPath = aFacileWebElement.elementIExPath;
		this.elementCssSelector = aFacileWebElement.elementCssSelector;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FacileWebElement [elementKey=" + elementKey + ", findBy="
				+ findBy + ", elementID=" + elementID + ", elementClass="
				+ elementClass + ", elementName=" + elementName
				+ ", elementTag=" + elementTag + ", elementText=" + elementText
				+ ", elementXPath=" + elementXPath + ", elementIEXPath="
				+ elementIExPath + ", elementCssSelector=" + elementCssSelector
				+ "]";
	}

	/**
	 * Gets the element tag.
	 * 
	 * @return the element tag
	 */
	public String getElementTag() {
		return elementTag;
	}

	/**
	 * Sets the element tag.
	 * 
	 * @param elementTag
	 *            the new element tag
	 */
	public void setElementTag(String elementTag) {
		this.elementTag = elementTag;
	}

	/**
	 * Gets the element key.
	 * 
	 * @return the element key
	 */
	public String getElementKey() {
		return elementKey;
	}

	/**
	 * Gets the element id.
	 * 
	 * @return the element id
	 */
	public String getElementID() {
		return elementID;
	}

	/**
	 * Gets the element name.
	 * 
	 * @return the element name
	 */
	public String getElementName() {
		return elementName;
	}

	/**
	 * Gets the element class.
	 * 
	 * @return the element class
	 */
	public String getElementClass() {
		return elementClass;
	}

	/**
	 * Gets the element text.
	 * 
	 * @return the element text
	 */
	public String getElementText() {
		return elementText;
	}

	/**
	 * Gets the element x path.
	 * 
	 * @return the element x path
	 */
	public String getElementXPath() {
		return elementXPath;
	}

	/**
	 * Gets the find by.
	 * 
	 * @return the find by
	 */
	public String getFindBy() {
		return findBy;
	}

	/**
	 * Gets the element i ex path.
	 * 
	 * @return the element i ex path
	 */
	public String getElementIExPath() {
		return elementIExPath;
	}

	/**
	 * Sets the element id.
	 * 
	 * @param elementID
	 *            the new element id
	 */
	public void setElementID(String elementID) {
		this.elementID = elementID;
	}

	/**
	 * Sets the element name.
	 * 
	 * @param elementName
	 *            the new element name
	 */
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	/**
	 * Sets the element class.
	 * 
	 * @param elementClass
	 *            the new element class
	 */
	public void setElementClass(String elementClass) {
		this.elementClass = elementClass;
	}

	/**
	 * Sets the element text.
	 * 
	 * @param elementText
	 *            the new element text
	 */
	public void setElementText(String elementText) {
		this.elementText = elementText;
	}

	/**
	 * Sets the element x path.
	 * 
	 * @param elementXPath
	 *            the new element x path
	 */
	public void setElementXPath(String elementXPath) {
		this.elementXPath = elementXPath;
	}

	/**
	 * Sets the find by.
	 * 
	 * @param findBy
	 *            the new find by
	 */
	public void setFindBy(String findBy) {
		this.findBy = findBy;
	}

	/**
	 * Sets the element i ex path.
	 * 
	 * @param elementIExPath
	 *            the new element i ex path
	 */
	public void setElementIExPath(String elementIExPath) {
		this.elementIExPath = elementIExPath;
	}

	/**
	 * Gets the element css selector.
	 * 
	 * @return the element css selector
	 */
	public String getElementCssSelector() {
		return elementCssSelector;
	}

	/**
	 * Sets the element css selector.
	 * 
	 * @param elementCssSelector
	 *            the new element css selector
	 */
	public void setElementCssSelector(String elementCssSelector) {
		this.elementCssSelector = elementCssSelector;
	}

	/**
	 * To xml.
	 * 
	 * @return the string
	 */
	public String toXML() {
		return String.format(XML_TEMPLATE, elementKey, findBy, elementID,
				elementTag, elementName, elementText, elementClass,
				elementXPath, elementIExPath, elementCssSelector);
	}

}
