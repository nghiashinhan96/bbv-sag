
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OutCustomer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutCustomer">
 *   &lt;complexContent>
 *     &lt;extension base="{DVSE.WebApp.CISService}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="Customer" type="{DVSE.WebApp.CISService}CustomerDetails" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutCustomer", propOrder = {
    "customer"
})
public class OutCustomer
    extends BaseResponse
{

    @XmlElementRef(name = "Customer", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<CustomerDetails> customer;

    /**
     * Gets the value of the customer property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CustomerDetails }{@code >}
     *     
     */
    public JAXBElement<CustomerDetails> getCustomer() {
        return customer;
    }

    /**
     * Sets the value of the customer property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link CustomerDetails }{@code >}
     *     
     */
    public void setCustomer(JAXBElement<CustomerDetails> value) {
        this.customer = value;
    }

}
