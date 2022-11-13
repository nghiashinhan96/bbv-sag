
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetConnectedVouchersResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetConnectedVouchersResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetConnectedVouchersResult" type="{DVSE.WebApp.CISService}OutVouchers" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetConnectedVouchersResponseBody", propOrder = {
    "getConnectedVouchersResult"
})
public class GetConnectedVouchersResponseBody {

    @XmlElementRef(name = "GetConnectedVouchersResult", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<OutVouchers> getConnectedVouchersResult;

    /**
     * Gets the value of the getConnectedVouchersResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link OutVouchers }{@code >}
     *     
     */
    public JAXBElement<OutVouchers> getGetConnectedVouchersResult() {
        return getConnectedVouchersResult;
    }

    /**
     * Sets the value of the getConnectedVouchersResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link OutVouchers }{@code >}
     *     
     */
    public void setGetConnectedVouchersResult(JAXBElement<OutVouchers> value) {
        this.getConnectedVouchersResult = value;
    }

}
