
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetReturnItemOptionsResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetReturnItemOptionsResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetReturnItemOptionsResult" type="{DVSE.WebApp.CISService}OutReturnItemOptions" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetReturnItemOptionsResponseBody", propOrder = {
    "getReturnItemOptionsResult"
})
public class GetReturnItemOptionsResponseBody {

    @XmlElementRef(name = "GetReturnItemOptionsResult", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<OutReturnItemOptions> getReturnItemOptionsResult;

    /**
     * Gets the value of the getReturnItemOptionsResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link OutReturnItemOptions }{@code >}
     *     
     */
    public JAXBElement<OutReturnItemOptions> getGetReturnItemOptionsResult() {
        return getReturnItemOptionsResult;
    }

    /**
     * Sets the value of the getReturnItemOptionsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link OutReturnItemOptions }{@code >}
     *     
     */
    public void setGetReturnItemOptionsResult(JAXBElement<OutReturnItemOptions> value) {
        this.getReturnItemOptionsResult = value;
    }

}
