
package com.sagag.services.dvse.wsdl.tmconnect;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Quantity complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Quantity">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="QuantityUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PackagingUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MinQuantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="MaxQuantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ExpectedDeliveryTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AvailabilityState" type="{http://topmotive.eu/TMConnect}EntityLink" minOccurs="0"/>
 *         &lt;element name="LotSizes" type="{http://topmotive.eu/TMConnect}ArrayOfKeyValueItem" minOccurs="0"/>
 *         &lt;element name="Division" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="QuantityPackingUnit" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Tour" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Quantity", propOrder = {
    "description",
    "value",
    "quantityUnit",
    "packagingUnit",
    "minQuantity",
    "maxQuantity",
    "expectedDeliveryTime",
    "availabilityState",
    "lotSizes",
    "division",
    "quantityPackingUnit",
    "tour"
})
public class Quantity {

    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "Value", required = true)
    protected BigDecimal value;
    @XmlElement(name = "QuantityUnit")
    protected String quantityUnit;
    @XmlElement(name = "PackagingUnit")
    protected String packagingUnit;
    @XmlElement(name = "MinQuantity", required = true, nillable = true)
    protected BigDecimal minQuantity;
    @XmlElement(name = "MaxQuantity", required = true, nillable = true)
    protected BigDecimal maxQuantity;
    @XmlElement(name = "ExpectedDeliveryTime")
    protected String expectedDeliveryTime;
    @XmlElement(name = "AvailabilityState")
    protected EntityLink availabilityState;
    @XmlElement(name = "LotSizes")
    protected ArrayOfKeyValueItem lotSizes;
    @XmlElement(name = "Division", required = true, nillable = true)
    protected BigDecimal division;
    @XmlElement(name = "QuantityPackingUnit", required = true, nillable = true)
    protected BigDecimal quantityPackingUnit;
    @XmlElement(name = "Tour")
    protected String tour;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * Gets the value of the quantityUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuantityUnit() {
        return quantityUnit;
    }

    /**
     * Sets the value of the quantityUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuantityUnit(String value) {
        this.quantityUnit = value;
    }

    /**
     * Gets the value of the packagingUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPackagingUnit() {
        return packagingUnit;
    }

    /**
     * Sets the value of the packagingUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackagingUnit(String value) {
        this.packagingUnit = value;
    }

    /**
     * Gets the value of the minQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMinQuantity() {
        return minQuantity;
    }

    /**
     * Sets the value of the minQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMinQuantity(BigDecimal value) {
        this.minQuantity = value;
    }

    /**
     * Gets the value of the maxQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMaxQuantity() {
        return maxQuantity;
    }

    /**
     * Sets the value of the maxQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMaxQuantity(BigDecimal value) {
        this.maxQuantity = value;
    }

    /**
     * Gets the value of the expectedDeliveryTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpectedDeliveryTime() {
        return expectedDeliveryTime;
    }

    /**
     * Sets the value of the expectedDeliveryTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpectedDeliveryTime(String value) {
        this.expectedDeliveryTime = value;
    }

    /**
     * Gets the value of the availabilityState property.
     * 
     * @return
     *     possible object is
     *     {@link EntityLink }
     *     
     */
    public EntityLink getAvailabilityState() {
        return availabilityState;
    }

    /**
     * Sets the value of the availabilityState property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntityLink }
     *     
     */
    public void setAvailabilityState(EntityLink value) {
        this.availabilityState = value;
    }

    /**
     * Gets the value of the lotSizes property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfKeyValueItem }
     *     
     */
    public ArrayOfKeyValueItem getLotSizes() {
        return lotSizes;
    }

    /**
     * Sets the value of the lotSizes property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfKeyValueItem }
     *     
     */
    public void setLotSizes(ArrayOfKeyValueItem value) {
        this.lotSizes = value;
    }

    /**
     * Gets the value of the division property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDivision() {
        return division;
    }

    /**
     * Sets the value of the division property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDivision(BigDecimal value) {
        this.division = value;
    }

    /**
     * Gets the value of the quantityPackingUnit property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQuantityPackingUnit() {
        return quantityPackingUnit;
    }

    /**
     * Sets the value of the quantityPackingUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantityPackingUnit(BigDecimal value) {
        this.quantityPackingUnit = value;
    }

    /**
     * Gets the value of the tour property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTour() {
        return tour;
    }

    /**
     * Sets the value of the tour property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTour(String value) {
        this.tour = value;
    }

}
