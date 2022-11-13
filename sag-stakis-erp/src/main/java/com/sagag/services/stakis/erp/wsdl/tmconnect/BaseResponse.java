
package com.sagag.services.stakis.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
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
    FindItemsReply.class,
    SendOrderReply.class,
    GetErpInformationReply.class,
    GetAccountInformationReply.class,
    GetNotificationReply.class,
    GetSessionReply.class,
    GetItemsCollectionReply.class
})
public class BaseResponse {

    @XmlElement(name = "StatusId")
    protected int statusId;
    @XmlElementRef(name = "ErrorCode", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> errorCode;
    @XmlElementRef(name = "ErrorMessage", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> errorMessage;
    @XmlElementRef(name = "Notifications", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfNotification> notifications;
    @XmlElementRef(name = "ValidationMessages", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfValidationMessage> validationMessages;
    @XmlElementRef(name = "ControlIndicators", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfControlIndicator> controlIndicators;
    @XmlElement(name = "TimeStamp", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timeStamp;
    @XmlElementRef(name = "AdditionalIds", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfKeyValueItem> additionalIds;
    @XmlElementRef(name = "Credentials", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<Credentials> credentials;

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
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the value of the errorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setErrorCode(JAXBElement<String> value) {
        this.errorCode = value;
    }

    /**
     * Gets the value of the errorMessage property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the value of the errorMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setErrorMessage(JAXBElement<String> value) {
        this.errorMessage = value;
    }

    /**
     * Gets the value of the notifications property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfNotification }{@code >}
     *     
     */
    public JAXBElement<ArrayOfNotification> getNotifications() {
        return notifications;
    }

    /**
     * Sets the value of the notifications property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfNotification }{@code >}
     *     
     */
    public void setNotifications(JAXBElement<ArrayOfNotification> value) {
        this.notifications = value;
    }

    /**
     * Gets the value of the validationMessages property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfValidationMessage }{@code >}
     *     
     */
    public JAXBElement<ArrayOfValidationMessage> getValidationMessages() {
        return validationMessages;
    }

    /**
     * Sets the value of the validationMessages property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfValidationMessage }{@code >}
     *     
     */
    public void setValidationMessages(JAXBElement<ArrayOfValidationMessage> value) {
        this.validationMessages = value;
    }

    /**
     * Gets the value of the controlIndicators property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfControlIndicator }{@code >}
     *     
     */
    public JAXBElement<ArrayOfControlIndicator> getControlIndicators() {
        return controlIndicators;
    }

    /**
     * Sets the value of the controlIndicators property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfControlIndicator }{@code >}
     *     
     */
    public void setControlIndicators(JAXBElement<ArrayOfControlIndicator> value) {
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
     *     {@link JAXBElement }{@code <}{@link ArrayOfKeyValueItem }{@code >}
     *     
     */
    public JAXBElement<ArrayOfKeyValueItem> getAdditionalIds() {
        return additionalIds;
    }

    /**
     * Sets the value of the additionalIds property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfKeyValueItem }{@code >}
     *     
     */
    public void setAdditionalIds(JAXBElement<ArrayOfKeyValueItem> value) {
        this.additionalIds = value;
    }

    /**
     * Gets the value of the credentials property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Credentials }{@code >}
     *     
     */
    public JAXBElement<Credentials> getCredentials() {
        return credentials;
    }

    /**
     * Sets the value of the credentials property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Credentials }{@code >}
     *     
     */
    public void setCredentials(JAXBElement<Credentials> value) {
        this.credentials = value;
    }

}
