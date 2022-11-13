
package com.sagag.services.dvse.wsdl.tmconnect;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ErpInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ErpInformation">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}BaseDto">
 *       &lt;sequence>
 *         &lt;element name="Item" type="{http://topmotive.eu/TMConnect}EntityLink" minOccurs="0"/>
 *         &lt;element name="Vehicle" type="{http://topmotive.eu/TMConnect}EntityLink" minOccurs="0"/>
 *         &lt;element name="RequestedQuantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ConfirmedQuantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="SortId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="AvailabilityState" type="{http://topmotive.eu/TMConnect}EntityLink" minOccurs="0"/>
 *         &lt;element name="StatusInformation" type="{http://topmotive.eu/TMConnect}ArrayOfKeyValueItem" minOccurs="0"/>
 *         &lt;element name="Prices" type="{http://topmotive.eu/TMConnect}ArrayOfPrice" minOccurs="0"/>
 *         &lt;element name="AdditionalDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AdditionalDescriptionExtended" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LinkedItemsCollections" type="{http://topmotive.eu/TMConnect}ArrayOfLinkedItemsCollection" minOccurs="0"/>
 *         &lt;element name="Warehouses" type="{http://topmotive.eu/TMConnect}ArrayOfWarehouse" minOccurs="0"/>
 *         &lt;element name="Tour" type="{http://topmotive.eu/TMConnect}EntityLink" minOccurs="0"/>
 *         &lt;element name="Memos" type="{http://topmotive.eu/TMConnect}ArrayOfMemo" minOccurs="0"/>
 *         &lt;element name="SpecialIcons" type="{http://topmotive.eu/TMConnect}ArrayOfEntityLink" minOccurs="0"/>
 *         &lt;element name="ControlIndicators" type="{http://topmotive.eu/TMConnect}ArrayOfControlIndicator" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ErpInformation", propOrder = {
    "item",
    "vehicle",
    "requestedQuantity",
    "confirmedQuantity",
    "sortId",
    "availabilityState",
    "statusInformation",
    "prices",
    "additionalDescription",
    "additionalDescriptionExtended",
    "linkedItemsCollections",
    "warehouses",
    "tour",
    "memos",
    "specialIcons",
    "controlIndicators"
})
public class ErpInformation
    extends BaseDto
{

    @XmlElement(name = "Item")
    protected EntityLink item;
    @XmlElement(name = "Vehicle")
    protected EntityLink vehicle;
    @XmlElement(name = "RequestedQuantity", required = true)
    protected BigDecimal requestedQuantity;
    @XmlElement(name = "ConfirmedQuantity", required = true, nillable = true)
    protected BigDecimal confirmedQuantity;
    @XmlElement(name = "SortId")
    protected int sortId;
    @XmlElement(name = "AvailabilityState")
    protected EntityLink availabilityState;
    @XmlElement(name = "StatusInformation")
    protected ArrayOfKeyValueItem statusInformation;
    @XmlElement(name = "Prices")
    protected ArrayOfPrice prices;
    @XmlElement(name = "AdditionalDescription")
    protected String additionalDescription;
    @XmlElement(name = "AdditionalDescriptionExtended")
    protected String additionalDescriptionExtended;
    @XmlElement(name = "LinkedItemsCollections")
    protected ArrayOfLinkedItemsCollection linkedItemsCollections;
    @XmlElement(name = "Warehouses")
    protected ArrayOfWarehouse warehouses;
    @XmlElement(name = "Tour")
    protected EntityLink tour;
    @XmlElement(name = "Memos")
    protected ArrayOfMemo memos;
    @XmlElement(name = "SpecialIcons")
    protected ArrayOfEntityLink specialIcons;
    @XmlElement(name = "ControlIndicators")
    protected ArrayOfControlIndicator controlIndicators;

    /**
     * Gets the value of the item property.
     * 
     * @return
     *     possible object is
     *     {@link EntityLink }
     *     
     */
    public EntityLink getItem() {
        return item;
    }

    /**
     * Sets the value of the item property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntityLink }
     *     
     */
    public void setItem(EntityLink value) {
        this.item = value;
    }

    /**
     * Gets the value of the vehicle property.
     * 
     * @return
     *     possible object is
     *     {@link EntityLink }
     *     
     */
    public EntityLink getVehicle() {
        return vehicle;
    }

    /**
     * Sets the value of the vehicle property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntityLink }
     *     
     */
    public void setVehicle(EntityLink value) {
        this.vehicle = value;
    }

    /**
     * Gets the value of the requestedQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRequestedQuantity() {
        return requestedQuantity;
    }

    /**
     * Sets the value of the requestedQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRequestedQuantity(BigDecimal value) {
        this.requestedQuantity = value;
    }

    /**
     * Gets the value of the confirmedQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getConfirmedQuantity() {
        return confirmedQuantity;
    }

    /**
     * Sets the value of the confirmedQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setConfirmedQuantity(BigDecimal value) {
        this.confirmedQuantity = value;
    }

    /**
     * Gets the value of the sortId property.
     * 
     */
    public int getSortId() {
        return sortId;
    }

    /**
     * Sets the value of the sortId property.
     * 
     */
    public void setSortId(int value) {
        this.sortId = value;
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
     * Gets the value of the statusInformation property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfKeyValueItem }
     *     
     */
    public ArrayOfKeyValueItem getStatusInformation() {
        return statusInformation;
    }

    /**
     * Sets the value of the statusInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfKeyValueItem }
     *     
     */
    public void setStatusInformation(ArrayOfKeyValueItem value) {
        this.statusInformation = value;
    }

    /**
     * Gets the value of the prices property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfPrice }
     *     
     */
    public ArrayOfPrice getPrices() {
        return prices;
    }

    /**
     * Sets the value of the prices property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfPrice }
     *     
     */
    public void setPrices(ArrayOfPrice value) {
        this.prices = value;
    }

    /**
     * Gets the value of the additionalDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalDescription() {
        return additionalDescription;
    }

    /**
     * Sets the value of the additionalDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalDescription(String value) {
        this.additionalDescription = value;
    }

    /**
     * Gets the value of the additionalDescriptionExtended property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalDescriptionExtended() {
        return additionalDescriptionExtended;
    }

    /**
     * Sets the value of the additionalDescriptionExtended property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalDescriptionExtended(String value) {
        this.additionalDescriptionExtended = value;
    }

    /**
     * Gets the value of the linkedItemsCollections property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfLinkedItemsCollection }
     *     
     */
    public ArrayOfLinkedItemsCollection getLinkedItemsCollections() {
        return linkedItemsCollections;
    }

    /**
     * Sets the value of the linkedItemsCollections property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfLinkedItemsCollection }
     *     
     */
    public void setLinkedItemsCollections(ArrayOfLinkedItemsCollection value) {
        this.linkedItemsCollections = value;
    }

    /**
     * Gets the value of the warehouses property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfWarehouse }
     *     
     */
    public ArrayOfWarehouse getWarehouses() {
        return warehouses;
    }

    /**
     * Sets the value of the warehouses property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfWarehouse }
     *     
     */
    public void setWarehouses(ArrayOfWarehouse value) {
        this.warehouses = value;
    }

    /**
     * Gets the value of the tour property.
     * 
     * @return
     *     possible object is
     *     {@link EntityLink }
     *     
     */
    public EntityLink getTour() {
        return tour;
    }

    /**
     * Sets the value of the tour property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntityLink }
     *     
     */
    public void setTour(EntityLink value) {
        this.tour = value;
    }

    /**
     * Gets the value of the memos property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfMemo }
     *     
     */
    public ArrayOfMemo getMemos() {
        return memos;
    }

    /**
     * Sets the value of the memos property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfMemo }
     *     
     */
    public void setMemos(ArrayOfMemo value) {
        this.memos = value;
    }

    /**
     * Gets the value of the specialIcons property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfEntityLink }
     *     
     */
    public ArrayOfEntityLink getSpecialIcons() {
        return specialIcons;
    }

    /**
     * Sets the value of the specialIcons property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfEntityLink }
     *     
     */
    public void setSpecialIcons(ArrayOfEntityLink value) {
        this.specialIcons = value;
    }

    /**
     * Gets the value of the controlIndicators property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfControlIndicator }
     *     
     */
    public ArrayOfControlIndicator getControlIndicators() {
        return controlIndicators;
    }

    /**
     * Sets the value of the controlIndicators property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfControlIndicator }
     *     
     */
    public void setControlIndicators(ArrayOfControlIndicator value) {
        this.controlIndicators = value;
    }

}
