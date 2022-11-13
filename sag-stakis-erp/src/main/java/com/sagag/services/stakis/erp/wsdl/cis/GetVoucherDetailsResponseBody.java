
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetVoucherDetailsResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetVoucherDetailsResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetVoucherDetailsResult" type="{DVSE.WebApp.CISService}OutVoucherDetails" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetVoucherDetailsResponseBody", propOrder = {
    "getVoucherDetailsResult"
})
public class GetVoucherDetailsResponseBody {

    @XmlElementRef(name = "GetVoucherDetailsResult", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<OutVoucherDetails> getVoucherDetailsResult;

    /**
     * Gets the value of the getVoucherDetailsResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link OutVoucherDetails }{@code >}
     *     
     */
    public JAXBElement<OutVoucherDetails> getGetVoucherDetailsResult() {
        return getVoucherDetailsResult;
    }

    /**
     * Sets the value of the getVoucherDetailsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link OutVoucherDetails }{@code >}
     *     
     */
    public void setGetVoucherDetailsResult(JAXBElement<OutVoucherDetails> value) {
        this.getVoucherDetailsResult = value;
    }

}
