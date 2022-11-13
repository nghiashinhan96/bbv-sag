
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SalesOutletList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SalesOutletList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Default" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SalesOutlets" type="{DVSE.WebApp.CISService}ArrayOfSalesOutlet" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SalesOutletList", propOrder = {
    "_default",
    "salesOutlets"
})
public class SalesOutletList {

    @XmlElementRef(name = "Default", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> _default;
    @XmlElementRef(name = "SalesOutlets", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfSalesOutlet> salesOutlets;

    /**
     * Gets the value of the default property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDefault() {
        return _default;
    }

    /**
     * Sets the value of the default property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDefault(JAXBElement<String> value) {
        this._default = value;
    }

    /**
     * Gets the value of the salesOutlets property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSalesOutlet }{@code >}
     *     
     */
    public JAXBElement<ArrayOfSalesOutlet> getSalesOutlets() {
        return salesOutlets;
    }

    /**
     * Sets the value of the salesOutlets property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSalesOutlet }{@code >}
     *     
     */
    public void setSalesOutlets(JAXBElement<ArrayOfSalesOutlet> value) {
        this.salesOutlets = value;
    }

}
