package com.sagag.services.dvse.wsdl.unicat;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Quantity", propOrder = {
    "id",
    "description",
    "value",
    "quantityUnit",
    "packingUnit",
    "minQuantity",
    "maxQuantity",
    "expectedDeliveryTime",
    "availState",
    "memo",
    "lotSize1",
    "lotSize2",
    "division",
    "quantityPackingUnit"
})
public class Quantity {

    @XmlElement(name = "Id")
    protected String id;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "Value", required = true)
    protected BigDecimal value;
    @XmlElement(name = "QuantityUnit")
    protected String quantityUnit;
    @XmlElement(name = "PackingUnit")
    protected String packingUnit;
    @XmlElement(name = "MinQuantity", required = true)
    protected BigDecimal minQuantity;
    @XmlElement(name = "MaxQuantity", required = true)
    protected BigDecimal maxQuantity;
    @XmlElement(name = "ExpectedDeliveryTime")
    protected String expectedDeliveryTime;
    @XmlElement(name = "AvailState")
    protected AvailableState availState;
    @XmlElement(name = "Memo")
    protected ArrayOfString memo;
    @XmlElement(name = "LotSize1")
    protected int lotSize1;
    @XmlElement(name = "LotSize2")
    protected int lotSize2;
    @XmlElement(name = "Division", required = true)
    protected BigDecimal division;
    @XmlElement(name = "QuantityPackingUnit", required = true)
    protected BigDecimal quantityPackingUnit;

    /**
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setId(String value) {
        this.id = value;
    }

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
     * Gets the value of the packingUnit property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPackingUnit() {
        return packingUnit;
    }

    /**
     * Sets the value of the packingUnit property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPackingUnit(String value) {
        this.packingUnit = value;
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
     * Gets the value of the availState property.
     *
     * @return
     *     possible object is
     *     {@link AvailableState }
     *
     */
    public AvailableState getAvailState() {
        return availState;
    }

    /**
     * Sets the value of the availState property.
     *
     * @param value
     *     allowed object is
     *     {@link AvailableState }
     *
     */
    public void setAvailState(AvailableState value) {
        this.availState = value;
    }

    /**
     * Gets the value of the memo property.
     *
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *
     */
    public ArrayOfString getMemo() {
        return memo;
    }

    /**
     * Sets the value of the memo property.
     *
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *
     */
    public void setMemo(ArrayOfString value) {
        this.memo = value;
    }

    /**
     * Gets the value of the lotSize1 property.
     *
     */
    public int getLotSize1() {
        return lotSize1;
    }

    /**
     * Sets the value of the lotSize1 property.
     *
     */
    public void setLotSize1(int value) {
        this.lotSize1 = value;
    }

    /**
     * Gets the value of the lotSize2 property.
     *
     */
    public int getLotSize2() {
        return lotSize2;
    }

    /**
     * Sets the value of the lotSize2 property.
     *
     */
    public void setLotSize2(int value) {
        this.lotSize2 = value;
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

}
