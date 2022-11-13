
package com.sagag.services.stakis.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetNotificationResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetNotificationResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetNotificationResult" type="{http://topmotive.eu/TMConnect}GetNotificationReply" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetNotificationResponseBody", propOrder = {
    "getNotificationResult"
})
public class GetNotificationResponseBody {

    @XmlElementRef(name = "GetNotificationResult", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<GetNotificationReply> getNotificationResult;

    /**
     * Gets the value of the getNotificationResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GetNotificationReply }{@code >}
     *     
     */
    public JAXBElement<GetNotificationReply> getGetNotificationResult() {
        return getNotificationResult;
    }

    /**
     * Sets the value of the getNotificationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GetNotificationReply }{@code >}
     *     
     */
    public void setGetNotificationResult(JAXBElement<GetNotificationReply> value) {
        this.getNotificationResult = value;
    }

}
