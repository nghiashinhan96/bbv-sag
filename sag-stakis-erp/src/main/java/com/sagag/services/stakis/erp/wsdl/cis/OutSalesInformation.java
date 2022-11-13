
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OutSalesInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutSalesInformation">
 *   &lt;complexContent>
 *     &lt;extension base="{DVSE.WebApp.CISService}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="SaleTypes" type="{DVSE.WebApp.CISService}ArrayOfSaleType" minOccurs="0"/>
 *         &lt;element name="Intervals" type="{DVSE.WebApp.CISService}ArrayOfInt" minOccurs="0"/>
 *         &lt;element name="DisplayModes" type="{DVSE.WebApp.CISService}ArrayOfInt" minOccurs="0"/>
 *         &lt;element name="DefaultInterval" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="DefaultDisplayMode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutSalesInformation", propOrder = {
    "saleTypes",
    "intervals",
    "displayModes",
    "defaultInterval",
    "defaultDisplayMode"
})
public class OutSalesInformation
    extends BaseResponse
{

    @XmlElementRef(name = "SaleTypes", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfSaleType> saleTypes;
    @XmlElementRef(name = "Intervals", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfInt> intervals;
    @XmlElementRef(name = "DisplayModes", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfInt> displayModes;
    @XmlElement(name = "DefaultInterval")
    protected int defaultInterval;
    @XmlElement(name = "DefaultDisplayMode")
    protected int defaultDisplayMode;

    /**
     * Gets the value of the saleTypes property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSaleType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfSaleType> getSaleTypes() {
        return saleTypes;
    }

    /**
     * Sets the value of the saleTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSaleType }{@code >}
     *     
     */
    public void setSaleTypes(JAXBElement<ArrayOfSaleType> value) {
        this.saleTypes = value;
    }

    /**
     * Gets the value of the intervals property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfInt }{@code >}
     *     
     */
    public JAXBElement<ArrayOfInt> getIntervals() {
        return intervals;
    }

    /**
     * Sets the value of the intervals property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfInt }{@code >}
     *     
     */
    public void setIntervals(JAXBElement<ArrayOfInt> value) {
        this.intervals = value;
    }

    /**
     * Gets the value of the displayModes property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfInt }{@code >}
     *     
     */
    public JAXBElement<ArrayOfInt> getDisplayModes() {
        return displayModes;
    }

    /**
     * Sets the value of the displayModes property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfInt }{@code >}
     *     
     */
    public void setDisplayModes(JAXBElement<ArrayOfInt> value) {
        this.displayModes = value;
    }

    /**
     * Gets the value of the defaultInterval property.
     * 
     */
    public int getDefaultInterval() {
        return defaultInterval;
    }

    /**
     * Sets the value of the defaultInterval property.
     * 
     */
    public void setDefaultInterval(int value) {
        this.defaultInterval = value;
    }

    /**
     * Gets the value of the defaultDisplayMode property.
     * 
     */
    public int getDefaultDisplayMode() {
        return defaultDisplayMode;
    }

    /**
     * Sets the value of the defaultDisplayMode property.
     * 
     */
    public void setDefaultDisplayMode(int value) {
        this.defaultDisplayMode = value;
    }

}
