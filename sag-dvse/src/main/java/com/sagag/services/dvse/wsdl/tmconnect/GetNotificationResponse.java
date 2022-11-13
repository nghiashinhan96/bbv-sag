
package com.sagag.services.dvse.wsdl.tmconnect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
@XmlType(name = "", propOrder = {
    "getNotificationResult"
})
@XmlRootElement(name = "GetNotificationResponse")
public class GetNotificationResponse {

    @XmlElement(name = "GetNotificationResult")
    protected GetNotificationReply getNotificationResult;

    /**
     * Gets the value of the getNotificationResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetNotificationReply }
     *     
     */
    public GetNotificationReply getGetNotificationResult() {
        return getNotificationResult;
    }

    /**
     * Sets the value of the getNotificationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetNotificationReply }
     *     
     */
    public void setGetNotificationResult(GetNotificationReply value) {
        this.getNotificationResult = value;
    }

}
