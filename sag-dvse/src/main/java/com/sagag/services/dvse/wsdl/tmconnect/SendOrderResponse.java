
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
 *         &lt;element name="SendOrderResult" type="{http://topmotive.eu/TMConnect}SendOrderReply" minOccurs="0"/>
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
    "sendOrderResult"
})
@XmlRootElement(name = "SendOrderResponse")
public class SendOrderResponse {

    @XmlElement(name = "SendOrderResult")
    protected SendOrderReply sendOrderResult;

    /**
     * Gets the value of the sendOrderResult property.
     * 
     * @return
     *     possible object is
     *     {@link SendOrderReply }
     *     
     */
    public SendOrderReply getSendOrderResult() {
        return sendOrderResult;
    }

    /**
     * Sets the value of the sendOrderResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link SendOrderReply }
     *     
     */
    public void setSendOrderResult(SendOrderReply value) {
        this.sendOrderResult = value;
    }

}
