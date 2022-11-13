
package com.sagag.services.dvse.wsdl.tmconnect;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderPosition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderPosition">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}BaseDto">
 *       &lt;sequence>
 *         &lt;element name="StatusInformation" type="{http://topmotive.eu/TMConnect}KeyValueItem" minOccurs="0"/>
 *         &lt;element name="Item" type="{http://topmotive.eu/TMConnect}EntityLink" minOccurs="0"/>
 *         &lt;element name="Vehicle" type="{http://topmotive.eu/TMConnect}EntityLink" minOccurs="0"/>
 *         &lt;element name="RequestedQuantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ConfirmedQuantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Prices" type="{http://topmotive.eu/TMConnect}ArrayOfPrice" minOccurs="0"/>
 *         &lt;element name="SortId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="GroupId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="UserDefinedData" type="{http://topmotive.eu/TMConnect}ArrayOfUserDefinedData" minOccurs="0"/>
 *         &lt;element name="LinkedEntities" type="{http://topmotive.eu/TMConnect}ArrayOfEntityLink" minOccurs="0"/>
 *         &lt;element name="Tour" type="{http://topmotive.eu/TMConnect}EntityLink" minOccurs="0"/>
 *         &lt;element name="SelectionLists" type="{http://topmotive.eu/TMConnect}ArrayOfSelectionList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderPosition", propOrder = {
    "statusInformation",
    "item",
    "vehicle",
    "requestedQuantity",
    "confirmedQuantity",
    "prices",
    "sortId",
    "groupId",
    "userDefinedData",
    "linkedEntities",
    "tour",
    "selectionLists"
})
public class OrderPosition
    extends BaseDto
{

    @XmlElement(name = "StatusInformation")
    protected KeyValueItem statusInformation;
    @XmlElement(name = "Item")
    protected EntityLink item;
    @XmlElement(name = "Vehicle")
    protected EntityLink vehicle;
    @XmlElement(name = "RequestedQuantity", required = true)
    protected BigDecimal requestedQuantity;
    @XmlElement(name = "ConfirmedQuantity", required = true, nillable = true)
    protected BigDecimal confirmedQuantity;
    @XmlElement(name = "Prices")
    protected ArrayOfPrice prices;
    @XmlElement(name = "SortId")
    protected int sortId;
    @XmlElement(name = "GroupId")
    protected int groupId;
    @XmlElement(name = "UserDefinedData")
    protected ArrayOfUserDefinedData userDefinedData;
    @XmlElement(name = "LinkedEntities")
    protected ArrayOfEntityLink linkedEntities;
    @XmlElement(name = "Tour")
    protected EntityLink tour;
    @XmlElement(name = "SelectionLists")
    protected ArrayOfSelectionList selectionLists;

    /**
     * Gets the value of the statusInformation property.
     * 
     * @return
     *     possible object is
     *     {@link KeyValueItem }
     *     
     */
    public KeyValueItem getStatusInformation() {
        return statusInformation;
    }

    /**
     * Sets the value of the statusInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link KeyValueItem }
     *     
     */
    public void setStatusInformation(KeyValueItem value) {
        this.statusInformation = value;
    }

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
     * Gets the value of the groupId property.
     * 
     */
    public int getGroupId() {
        return groupId;
    }

    /**
     * Sets the value of the groupId property.
     * 
     */
    public void setGroupId(int value) {
        this.groupId = value;
    }

    /**
     * Gets the value of the userDefinedData property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfUserDefinedData }
     *     
     */
    public ArrayOfUserDefinedData getUserDefinedData() {
        return userDefinedData;
    }

    /**
     * Sets the value of the userDefinedData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfUserDefinedData }
     *     
     */
    public void setUserDefinedData(ArrayOfUserDefinedData value) {
        this.userDefinedData = value;
    }

    /**
     * Gets the value of the linkedEntities property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfEntityLink }
     *     
     */
    public ArrayOfEntityLink getLinkedEntities() {
        return linkedEntities;
    }

    /**
     * Sets the value of the linkedEntities property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfEntityLink }
     *     
     */
    public void setLinkedEntities(ArrayOfEntityLink value) {
        this.linkedEntities = value;
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
     * Gets the value of the selectionLists property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSelectionList }
     *     
     */
    public ArrayOfSelectionList getSelectionLists() {
        return selectionLists;
    }

    /**
     * Sets the value of the selectionLists property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSelectionList }
     *     
     */
    public void setSelectionLists(ArrayOfSelectionList value) {
        this.selectionLists = value;
    }

}
