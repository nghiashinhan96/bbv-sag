
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetVouchersResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetVouchersResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetVouchersResult" type="{DVSE.WebApp.CISService}OutVouchers" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetVouchersResponseBody", propOrder = {
    "getVouchersResult"
})
public class GetVouchersResponseBody {

    @XmlElementRef(name = "GetVouchersResult", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<OutVouchers> getVouchersResult;

    /**
     * Gets the value of the getVouchersResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link OutVouchers }{@code >}
     *     
     */
    public JAXBElement<OutVouchers> getGetVouchersResult() {
        return getVouchersResult;
    }

    /**
     * Sets the value of the getVouchersResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link OutVouchers }{@code >}
     *     
     */
    public void setGetVouchersResult(JAXBElement<OutVouchers> value) {
        this.getVouchersResult = value;
    }

}
