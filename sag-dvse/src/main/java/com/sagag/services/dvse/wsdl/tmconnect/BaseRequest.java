
package com.sagag.services.dvse.wsdl.tmconnect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for BaseRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SessionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ExternalSessionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AdditionalIds" type="{http://topmotive.eu/TMConnect}ArrayOfKeyValueItem" minOccurs="0"/>
 *         &lt;element name="Credentials" type="{http://topmotive.eu/TMConnect}Credentials" minOccurs="0"/>
 *         &lt;element name="ClientIp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LanguageCodeIso639_1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ContextId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TimeStamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseRequest", propOrder = {
    "sessionId",
    "externalSessionId",
    "additionalIds",
    "credentials",
    "clientIp",
    "languageCodeIso6391",
    "contextId",
    "timeStamp"
})
@XmlSeeAlso({
    SendOrderRequest.class,
    GetItemsCollectionRequest.class,
    GetErpInformationRequest.class,
    SendOrderDocumentRequest.class,
    FindItemsRequest.class,
    GetNotificationRequest.class
})
public class BaseRequest {

    @XmlElement(name = "SessionId")
    protected String sessionId;
    @XmlElement(name = "ExternalSessionId")
    protected String externalSessionId;
    @XmlElement(name = "AdditionalIds")
    protected ArrayOfKeyValueItem additionalIds;
    @XmlElement(name = "Credentials")
    protected Credentials credentials;
    @XmlElement(name = "ClientIp")
    protected String clientIp;
    @XmlElement(name = "LanguageCodeIso639_1")
    protected String languageCodeIso6391;
    @XmlElement(name = "ContextId")
    protected int contextId;
    @XmlElement(name = "TimeStamp", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timeStamp;

    /**
     * Gets the value of the sessionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Sets the value of the sessionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionId(String value) {
        this.sessionId = value;
    }

    /**
     * Gets the value of the externalSessionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalSessionId() {
        return externalSessionId;
    }

    /**
     * Sets the value of the externalSessionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalSessionId(String value) {
        this.externalSessionId = value;
    }

    /**
     * Gets the value of the additionalIds property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfKeyValueItem }
     *     
     */
    public ArrayOfKeyValueItem getAdditionalIds() {
        return additionalIds;
    }

    /**
     * Sets the value of the additionalIds property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfKeyValueItem }
     *     
     */
    public void setAdditionalIds(ArrayOfKeyValueItem value) {
        this.additionalIds = value;
    }

    /**
     * Gets the value of the credentials property.
     * 
     * @return
     *     possible object is
     *     {@link Credentials }
     *     
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * Sets the value of the credentials property.
     * 
     * @param value
     *     allowed object is
     *     {@link Credentials }
     *     
     */
    public void setCredentials(Credentials value) {
        this.credentials = value;
    }

    /**
     * Gets the value of the clientIp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientIp() {
        return clientIp;
    }

    /**
     * Sets the value of the clientIp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientIp(String value) {
        this.clientIp = value;
    }

    /**
     * Gets the value of the languageCodeIso6391 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguageCodeIso6391() {
        return languageCodeIso6391;
    }

    /**
     * Sets the value of the languageCodeIso6391 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguageCodeIso6391(String value) {
        this.languageCodeIso6391 = value;
    }

    /**
     * Gets the value of the contextId property.
     * 
     */
    public int getContextId() {
        return contextId;
    }

    /**
     * Sets the value of the contextId property.
     * 
     */
    public void setContextId(int value) {
        this.contextId = value;
    }

    /**
     * Gets the value of the timeStamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the value of the timeStamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTimeStamp(XMLGregorianCalendar value) {
        this.timeStamp = value;
    }

}
