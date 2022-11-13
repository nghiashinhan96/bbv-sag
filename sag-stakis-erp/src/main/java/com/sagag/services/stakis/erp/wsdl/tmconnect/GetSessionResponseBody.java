
package com.sagag.services.stakis.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetSessionResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetSessionResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetSessionResult" type="{http://topmotive.eu/TMConnect}GetSessionReply" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetSessionResponseBody", propOrder = {
    "getSessionResult"
})
public class GetSessionResponseBody {

    @XmlElementRef(name = "GetSessionResult", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<GetSessionReply> getSessionResult;

    /**
     * Gets the value of the getSessionResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GetSessionReply }{@code >}
     *     
     */
    public JAXBElement<GetSessionReply> getGetSessionResult() {
        return getSessionResult;
    }

    /**
     * Sets the value of the getSessionResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GetSessionReply }{@code >}
     *     
     */
    public void setGetSessionResult(JAXBElement<GetSessionReply> value) {
        this.getSessionResult = value;
    }

}
