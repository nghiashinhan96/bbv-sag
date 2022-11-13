
package com.sagag.services.dvse.wsdl.tmconnect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for BaseResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StatusId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ErrorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ErrorMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Notifications" type="{http://topmotive.eu/TMConnect}ArrayOfNotification" minOccurs="0"/>
 *         &lt;element name="ValidationMessages" type="{http://topmotive.eu/TMConnect}ArrayOfValidationMessage" minOccurs="0"/>
 *         &lt;element name="ControlIndicators" type="{http://topmotive.eu/TMConnect}ArrayOfControlIndicator" minOccurs="0"/>
 *         &lt;element name="TimeStamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="AdditionalIds" type="{http://topmotive.eu/TMConnect}ArrayOfKeyValueItem" minOccurs="0"/>
 *         &lt;element name="Credentials" type="{http://topmotive.eu/TMConnect}Credentials" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseResponse", propOrder = {
    "statusId",
    "errorCode",
    "errorMessage",
    "notifications",
    "validationMessages",
    "controlIndicators",
    "timeStamp",
    "additionalIds",
    "credentials"
})
@XmlSeeAlso({
    SendOrderReply.class,
    GetErpInformationReply.class,
    GetNotificationReply.class,
    GetItemsCollectionReply.class,
    GetAccountInformationReply.class,
    FindItemsReply.class,
    GetSessionReply.class
})
public class BaseResponse {

    @XmlElement(name = "StatusId")
    protected int statusId;
    @XmlElement(name = "ErrorCode")
    protected String errorCode;
    @XmlElement(name = "ErrorMessage")
    protected String errorMessage;
    @XmlElement(name = "Notifications")
    protected ArrayOfNotification notifications;
    @XmlElement(name = "ValidationMessages")
    protected ArrayOfValidationMessage validationMessages;
    @XmlElement(name = "ControlIndicators")
    protected ArrayOfControlIndicator controlIndicators;
    @XmlElement(name = "TimeStamp", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timeStamp;
    @XmlElement(name = "AdditionalIds")
    protected ArrayOfKeyValueItem additionalIds;
    @XmlElement(name = "Credentials")
    protected Credentials credentials;

    /**
     * Gets the value of the statusId property.
     * 
     */
    public int getStatusId() {
        return statusId;
    }

    /**
     * Sets the value of the statusId property.
     * 
     */
    public void setStatusId(int value) {
        this.statusId = value;
    }

    /**
     * Gets the value of the errorCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the value of the errorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorCode(String value) {
        this.errorCode = value;
    }

    /**
     * Gets the value of the errorMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the value of the errorMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorMessage(String value) {
        this.errorMessage = value;
    }

    /**
     * Gets the value of the notifications property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfNotification }
     *     
     */
    public ArrayOfNotification getNotifications() {
        return notifications;
    }

    /**
     * Sets the value of the notifications property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfNotification }
     *     
     */
    public void setNotifications(ArrayOfNotification value) {
        this.notifications = value;
    }

    /**
     * Gets the value of the validationMessages property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfValidationMessage }
     *     
     */
    public ArrayOfValidationMessage getValidationMessages() {
        return validationMessages;
    }

    /**
     * Sets the value of the validationMessages property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfValidationMessage }
     *     
     */
    public void setValidationMessages(ArrayOfValidationMessage value) {
        this.validationMessages = value;
    }

    /**
     * Gets the value of the controlIndicators property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfControlIndicator }
     *     
     */
    public ArrayOfControlIndicator getControlIndicators() {
        return controlIndicators;
    }

    /**
     * Sets the value of the controlIndicators property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfControlIndicator }
     *     
     */
    public void setControlIndicators(ArrayOfControlIndicator value) {
        this.controlIndicators = value;
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

}
