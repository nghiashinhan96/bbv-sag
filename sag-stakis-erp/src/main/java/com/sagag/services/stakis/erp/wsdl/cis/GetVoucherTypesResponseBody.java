
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetVoucherTypesResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetVoucherTypesResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetVoucherTypesResult" type="{DVSE.WebApp.CISService}OutVoucherTypes" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetVoucherTypesResponseBody", propOrder = {
    "getVoucherTypesResult"
})
public class GetVoucherTypesResponseBody {

    @XmlElementRef(name = "GetVoucherTypesResult", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<OutVoucherTypes> getVoucherTypesResult;

    /**
     * Gets the value of the getVoucherTypesResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link OutVoucherTypes }{@code >}
     *     
     */
    public JAXBElement<OutVoucherTypes> getGetVoucherTypesResult() {
        return getVoucherTypesResult;
    }

    /**
     * Sets the value of the getVoucherTypesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link OutVoucherTypes }{@code >}
     *     
     */
    public void setGetVoucherTypesResult(JAXBElement<OutVoucherTypes> value) {
        this.getVoucherTypesResult = value;
    }

}
