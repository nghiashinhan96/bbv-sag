
package com.sagag.services.stakis.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SendOrderResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SendOrderResponseBody">
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
@XmlType(name = "SendOrderResponseBody", propOrder = {
    "sendOrderResult"
})
public class SendOrderResponseBody {

    @XmlElementRef(name = "SendOrderResult", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<SendOrderReply> sendOrderResult;

    /**
     * Gets the value of the sendOrderResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link SendOrderReply }{@code >}
     *     
     */
    public JAXBElement<SendOrderReply> getSendOrderResult() {
        return sendOrderResult;
    }

    /**
     * Sets the value of the sendOrderResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link SendOrderReply }{@code >}
     *     
     */
    public void setSendOrderResult(JAXBElement<SendOrderReply> value) {
        this.sendOrderResult = value;
    }

}
