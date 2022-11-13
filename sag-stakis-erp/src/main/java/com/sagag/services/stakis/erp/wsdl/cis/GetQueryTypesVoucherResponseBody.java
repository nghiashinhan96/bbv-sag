
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetQueryTypesVoucherResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetQueryTypesVoucherResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetQueryTypesVoucherResult" type="{DVSE.WebApp.CISService}OutQueryTypes" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetQueryTypesVoucherResponseBody", propOrder = {
    "getQueryTypesVoucherResult"
})
public class GetQueryTypesVoucherResponseBody {

    @XmlElementRef(name = "GetQueryTypesVoucherResult", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<OutQueryTypes> getQueryTypesVoucherResult;

    /**
     * Gets the value of the getQueryTypesVoucherResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link OutQueryTypes }{@code >}
     *     
     */
    public JAXBElement<OutQueryTypes> getGetQueryTypesVoucherResult() {
        return getQueryTypesVoucherResult;
    }

    /**
     * Sets the value of the getQueryTypesVoucherResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link OutQueryTypes }{@code >}
     *     
     */
    public void setGetQueryTypesVoucherResult(JAXBElement<OutQueryTypes> value) {
        this.getQueryTypesVoucherResult = value;
    }

}
