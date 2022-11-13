
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResetVoucherItemsCanReturnPropertyResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResetVoucherItemsCanReturnPropertyResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResetVoucherItemsCanReturnPropertyResult" type="{DVSE.WebApp.CISService}BaseResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResetVoucherItemsCanReturnPropertyResponseBody", propOrder = {
    "resetVoucherItemsCanReturnPropertyResult"
})
public class ResetVoucherItemsCanReturnPropertyResponseBody {

    @XmlElementRef(name = "ResetVoucherItemsCanReturnPropertyResult", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<BaseResponse> resetVoucherItemsCanReturnPropertyResult;

    /**
     * Gets the value of the resetVoucherItemsCanReturnPropertyResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BaseResponse }{@code >}
     *     
     */
    public JAXBElement<BaseResponse> getResetVoucherItemsCanReturnPropertyResult() {
        return resetVoucherItemsCanReturnPropertyResult;
    }

    /**
     * Sets the value of the resetVoucherItemsCanReturnPropertyResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BaseResponse }{@code >}
     *     
     */
    public void setResetVoucherItemsCanReturnPropertyResult(JAXBElement<BaseResponse> value) {
        this.resetVoucherItemsCanReturnPropertyResult = value;
    }

}
