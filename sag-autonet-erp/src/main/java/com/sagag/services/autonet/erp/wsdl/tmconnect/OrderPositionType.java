
package com.sagag.services.autonet.erp.wsdl.tmconnect;

import java.math.BigDecimal;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
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
public class OrderPositionType
    extends BaseDtoType
{

    @XmlElementRef(name = "StatusInformation", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<KeyValueItemType> statusInformation;
    @XmlElementRef(name = "Item", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<EntityLinkType> item;
    @XmlElementRef(name = "Vehicle", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<EntityLinkType> vehicle;
    @XmlElement(name = "RequestedQuantity", required = true)
    protected BigDecimal requestedQuantity;
    @XmlElement(name = "ConfirmedQuantity", required = true, nillable = true)
    protected BigDecimal confirmedQuantity;
    @XmlElementRef(name = "Prices", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfPriceType> prices;
    @XmlElement(name = "SortId")
    protected int sortId;
    @XmlElement(name = "GroupId")
    protected int groupId;
    @XmlElementRef(name = "UserDefinedData", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfUserDefinedDataType> userDefinedData;
    @XmlElementRef(name = "LinkedEntities", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfEntityLinkType> linkedEntities;
    @XmlElementRef(name = "Tour", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<EntityLinkType> tour;
    @XmlElementRef(name = "SelectionLists", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfSelectionListType> selectionLists;

    /**
     * Gets the value of the statusInformation property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link KeyValueItemType }{@code >}
     *     
     */
    public JAXBElement<KeyValueItemType> getStatusInformation() {
        return statusInformation;
    }

    /**
     * Sets the value of the statusInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link KeyValueItemType }{@code >}
     *     
     */
    public void setStatusInformation(JAXBElement<KeyValueItemType> value) {
        this.statusInformation = value;
    }

    /**
     * Gets the value of the item property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}
     *     
     */
    public JAXBElement<EntityLinkType> getItem() {
        return item;
    }

    /**
     * Sets the value of the item property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}
     *     
     */
    public void setItem(JAXBElement<EntityLinkType> value) {
        this.item = value;
    }

    /**
     * Gets the value of the vehicle property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}
     *     
     */
    public JAXBElement<EntityLinkType> getVehicle() {
        return vehicle;
    }

    /**
     * Sets the value of the vehicle property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}
     *     
     */
    public void setVehicle(JAXBElement<EntityLinkType> value) {
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
     *     {@link JAXBElement }{@code <}{@link ArrayOfPriceType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfPriceType> getPrices() {
        return prices;
    }

    /**
     * Sets the value of the prices property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfPriceType }{@code >}
     *     
     */
    public void setPrices(JAXBElement<ArrayOfPriceType> value) {
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
     *     {@link JAXBElement }{@code <}{@link ArrayOfUserDefinedDataType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfUserDefinedDataType> getUserDefinedData() {
        return userDefinedData;
    }

    /**
     * Sets the value of the userDefinedData property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfUserDefinedDataType }{@code >}
     *     
     */
    public void setUserDefinedData(JAXBElement<ArrayOfUserDefinedDataType> value) {
        this.userDefinedData = value;
    }

    /**
     * Gets the value of the linkedEntities property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfEntityLinkType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfEntityLinkType> getLinkedEntities() {
        return linkedEntities;
    }

    /**
     * Sets the value of the linkedEntities property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfEntityLinkType }{@code >}
     *     
     */
    public void setLinkedEntities(JAXBElement<ArrayOfEntityLinkType> value) {
        this.linkedEntities = value;
    }

    /**
     * Gets the value of the tour property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}
     *     
     */
    public JAXBElement<EntityLinkType> getTour() {
        return tour;
    }

    /**
     * Sets the value of the tour property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}
     *     
     */
    public void setTour(JAXBElement<EntityLinkType> value) {
        this.tour = value;
    }

    /**
     * Gets the value of the selectionLists property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSelectionListType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfSelectionListType> getSelectionLists() {
        return selectionLists;
    }

    /**
     * Sets the value of the selectionLists property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSelectionListType }{@code >}
     *     
     */
    public void setSelectionLists(JAXBElement<ArrayOfSelectionListType> value) {
        this.selectionLists = value;
    }

}
