
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OutVoucherTypes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutVoucherTypes">
 *   &lt;complexContent>
 *     &lt;extension base="{DVSE.WebApp.CISService}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="VoucherTypes" type="{DVSE.WebApp.CISService}ArrayOfVoucherType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutVoucherTypes", propOrder = {
    "voucherTypes"
})
public class OutVoucherTypes
    extends BaseResponse
{

    @XmlElementRef(name = "VoucherTypes", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfVoucherType> voucherTypes;

    /**
     * Gets the value of the voucherTypes property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfVoucherType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfVoucherType> getVoucherTypes() {
        return voucherTypes;
    }

    /**
     * Sets the value of the voucherTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfVoucherType }{@code >}
     *     
     */
    public void setVoucherTypes(JAXBElement<ArrayOfVoucherType> value) {
        this.voucherTypes = value;
    }

}
