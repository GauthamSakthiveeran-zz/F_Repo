//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.08.21 at 11:25:31 AM IST 
//


package com.ooyala.playback.url;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="priority">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="skinJson">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="embed_code">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="pcode">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="plugins">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="adPlugins">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="adFirstPlay" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="adFrequency" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="adPlayTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="overlayPlayTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="clickthrough" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="additionalPlugins">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="pbid">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="secret" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="apiKey" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="browsersSupported" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="browserSupportedVersion" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="sslEnabled" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="browser" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="live" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="channelId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="provider" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="ingestUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="error" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="platformsSupported" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="playerParameter" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="streamType" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="supportedMuxFormat" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "description",
    "priority",
    "skinJson",
    "embedCode",
    "pcode",
    "plugins",
    "adPlugins",
    "additionalPlugins",
    "pbid",
    "secret",
    "apiKey",
    "browsersSupported",
    "browserSupportedVersion",
    "sslEnabled",
    "live",
    "error",
    "platformsSupported",
    "playerParameter",
    "streamType"
})
public class Url {

    @XmlElement(required = true)
    protected Description description;
    @XmlElement(required = true)
    protected Priority priority;
    @XmlElement(required = true)
    protected SkinJson skinJson;
    @XmlElement(name = "embed_code", required = true)
    protected EmbedCode embedCode;
    @XmlElement(required = true)
    protected Pcode pcode;
    @XmlElement(required = true)
    protected Plugins plugins;
    @XmlElement(required = true)
    protected AdPlugins adPlugins;
    @XmlElement(required = true)
    protected AdditionalPlugins additionalPlugins;
    @XmlElement(required = true)
    protected Pbid pbid;
    protected Secret secret;
    protected ApiKey apiKey;
    protected BrowsersSupported browsersSupported;
    protected BrowserSupportedVersion browserSupportedVersion;
    protected SslEnabled sslEnabled;
    protected Live live;
    protected Error error;
    protected PlatformsSupported platformsSupported;
    @XmlElement(required = true)
    protected String playerParameter;
    protected StreamType streamType;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link Description }
     *     
     */
    public Description getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link Description }
     *     
     */
    public void setDescription(Description value) {
        this.description = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link Priority }
     *     
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link Priority }
     *     
     */
    public void setPriority(Priority value) {
        this.priority = value;
    }

    /**
     * Gets the value of the skinJson property.
     * 
     * @return
     *     possible object is
     *     {@link SkinJson }
     *     
     */
    public SkinJson getSkinJson() {
        return skinJson;
    }

    /**
     * Sets the value of the skinJson property.
     * 
     * @param value
     *     allowed object is
     *     {@link SkinJson }
     *     
     */
    public void setSkinJson(SkinJson value) {
        this.skinJson = value;
    }

    /**
     * Gets the value of the embedCode property.
     * 
     * @return
     *     possible object is
     *     {@link EmbedCode }
     *     
     */
    public EmbedCode getEmbedCode() {
        return embedCode;
    }

    /**
     * Sets the value of the embedCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmbedCode }
     *     
     */
    public void setEmbedCode(EmbedCode value) {
        this.embedCode = value;
    }

    /**
     * Gets the value of the pcode property.
     * 
     * @return
     *     possible object is
     *     {@link Pcode }
     *     
     */
    public Pcode getPcode() {
        return pcode;
    }

    /**
     * Sets the value of the pcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Pcode }
     *     
     */
    public void setPcode(Pcode value) {
        this.pcode = value;
    }

    /**
     * Gets the value of the plugins property.
     * 
     * @return
     *     possible object is
     *     {@link Plugins }
     *     
     */
    public Plugins getPlugins() {
        return plugins;
    }

    /**
     * Sets the value of the plugins property.
     * 
     * @param value
     *     allowed object is
     *     {@link Plugins }
     *     
     */
    public void setPlugins(Plugins value) {
        this.plugins = value;
    }

    /**
     * Gets the value of the adPlugins property.
     * 
     * @return
     *     possible object is
     *     {@link AdPlugins }
     *     
     */
    public AdPlugins getAdPlugins() {
        return adPlugins;
    }

    /**
     * Sets the value of the adPlugins property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdPlugins }
     *     
     */
    public void setAdPlugins(AdPlugins value) {
        this.adPlugins = value;
    }

    /**
     * Gets the value of the additionalPlugins property.
     * 
     * @return
     *     possible object is
     *     {@link AdditionalPlugins }
     *     
     */
    public AdditionalPlugins getAdditionalPlugins() {
        return additionalPlugins;
    }

    /**
     * Sets the value of the additionalPlugins property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdditionalPlugins }
     *     
     */
    public void setAdditionalPlugins(AdditionalPlugins value) {
        this.additionalPlugins = value;
    }

    /**
     * Gets the value of the pbid property.
     * 
     * @return
     *     possible object is
     *     {@link Pbid }
     *     
     */
    public Pbid getPbid() {
        return pbid;
    }

    /**
     * Sets the value of the pbid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Pbid }
     *     
     */
    public void setPbid(Pbid value) {
        this.pbid = value;
    }

    /**
     * Gets the value of the secret property.
     * 
     * @return
     *     possible object is
     *     {@link Secret }
     *     
     */
    public Secret getSecret() {
        return secret;
    }

    /**
     * Sets the value of the secret property.
     * 
     * @param value
     *     allowed object is
     *     {@link Secret }
     *     
     */
    public void setSecret(Secret value) {
        this.secret = value;
    }

    /**
     * Gets the value of the apiKey property.
     * 
     * @return
     *     possible object is
     *     {@link ApiKey }
     *     
     */
    public ApiKey getApiKey() {
        return apiKey;
    }

    /**
     * Sets the value of the apiKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApiKey }
     *     
     */
    public void setApiKey(ApiKey value) {
        this.apiKey = value;
    }

    /**
     * Gets the value of the browsersSupported property.
     * 
     * @return
     *     possible object is
     *     {@link BrowsersSupported }
     *     
     */
    public BrowsersSupported getBrowsersSupported() {
        return browsersSupported;
    }

    /**
     * Sets the value of the browsersSupported property.
     * 
     * @param value
     *     allowed object is
     *     {@link BrowsersSupported }
     *     
     */
    public void setBrowsersSupported(BrowsersSupported value) {
        this.browsersSupported = value;
    }

    /**
     * Gets the value of the browserSupportedVersion property.
     * 
     * @return
     *     possible object is
     *     {@link BrowserSupportedVersion }
     *     
     */
    public BrowserSupportedVersion getBrowserSupportedVersion() {
        return browserSupportedVersion;
    }

    /**
     * Sets the value of the browserSupportedVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link BrowserSupportedVersion }
     *     
     */
    public void setBrowserSupportedVersion(BrowserSupportedVersion value) {
        this.browserSupportedVersion = value;
    }

    /**
     * Gets the value of the sslEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link SslEnabled }
     *     
     */
    public SslEnabled getSslEnabled() {
        return sslEnabled;
    }

    /**
     * Sets the value of the sslEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link SslEnabled }
     *     
     */
    public void setSslEnabled(SslEnabled value) {
        this.sslEnabled = value;
    }

    /**
     * Gets the value of the live property.
     * 
     * @return
     *     possible object is
     *     {@link Live }
     *     
     */
    public Live getLive() {
        return live;
    }

    /**
     * Sets the value of the live property.
     * 
     * @param value
     *     allowed object is
     *     {@link Live }
     *     
     */
    public void setLive(Live value) {
        this.live = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link Error }
     *     
     */
    public Error getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link Error }
     *     
     */
    public void setError(Error value) {
        this.error = value;
    }

    /**
     * Gets the value of the platformsSupported property.
     * 
     * @return
     *     possible object is
     *     {@link PlatformsSupported }
     *     
     */
    public PlatformsSupported getPlatformsSupported() {
        return platformsSupported;
    }

    /**
     * Sets the value of the platformsSupported property.
     * 
     * @param value
     *     allowed object is
     *     {@link PlatformsSupported }
     *     
     */
    public void setPlatformsSupported(PlatformsSupported value) {
        this.platformsSupported = value;
    }

    /**
     * Gets the value of the playerParameter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlayerParameter() {
        return playerParameter;
    }

    /**
     * Sets the value of the playerParameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlayerParameter(String value) {
        this.playerParameter = value;
    }

    /**
     * Gets the value of the streamType property.
     * 
     * @return
     *     possible object is
     *     {@link StreamType }
     *     
     */
    public StreamType getStreamType() {
        return streamType;
    }

    /**
     * Sets the value of the streamType property.
     * 
     * @param value
     *     allowed object is
     *     {@link StreamType }
     *     
     */
    public void setStreamType(StreamType value) {
        this.streamType = value;
    }

}
