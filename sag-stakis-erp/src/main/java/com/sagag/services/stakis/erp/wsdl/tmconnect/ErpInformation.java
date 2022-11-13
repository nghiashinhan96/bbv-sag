
package com.sagag.services.stakis.erp.wsdl.tmconnect;

import java.math.BigDecimal;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
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

    @XmlElementRef(name = "Item", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<EntityLink> item;
    @XmlElementRef(name = "Vehicle", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<EntityLink> vehicle;
    @XmlElement(name = "RequestedQuantity", required = true)
    protected BigDecimal requestedQuantity;
    @XmlElement(name = "ConfirmedQuantity", required = true, nillable = true)
    protected BigDecimal confirmedQuantity;
    @XmlElement(name = "SortId")
    protected int sortId;
    @XmlElementRef(name = "AvailabilityState", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<EntityLink> availabilityState;
    @XmlElementRef(name = "StatusInformation", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfKeyValueItem> statusInformation;
    @XmlElementRef(name = "Prices", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfPrice> prices;
    @XmlElementRef(name = "AdditionalDescription", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> additionalDescription;
    @XmlElementRef(name = "AdditionalDescriptionExtended", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> additionalDescriptionExtended;
    @XmlElementRef(name = "LinkedItemsCollections", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfLinkedItemsCollection> linkedItemsCollections;
    @XmlElementRef(name = "Warehouses", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfWarehouse> warehouses;
    @XmlElementRef(name = "Tour", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<EntityLink> tour;
    @XmlElementRef(name = "Memos", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfMemo> memos;
    @XmlElementRef(name = "SpecialIcons", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfEntityLink> specialIcons;
    @XmlElementRef(name = "ControlIndicators", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfControlIndicator> controlIndicators;

    /**
     * Gets the value of the item property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link EntityLink }{@code >}
     *     
     */
    public JAXBElement<EntityLink> getItem() {
        return item;
    }

    /**
     * Sets the value of the item property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link EntityLink }{@code >}
     *     
     */
    public void setItem(JAXBElement<EntityLink> value) {
        this.item = value;
    }

    /**
     * Gets the value of the vehicle property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link EntityLink }{@code >}
     *     
     */
    public JAXBElement<EntityLink> getVehicle() {
        return vehicle;
    }

    /**
     * Sets the value of the vehicle property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link EntityLink }{@code >}
     *     
     */
    public void setVehicle(JAXBElement<EntityLink> value) {
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
     *     {@link JAXBElement }{@code <}{@link EntityLink }{@code >}
     *     
     */
    public JAXBElement<EntityLink> getAvailabilityState() {
        return availabilityState;
    }

    /**
     * Sets the value of the availabilityState property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link EntityLink }{@code >}
     *     
     */
    public void setAvailabilityState(JAXBElement<EntityLink> value) {
        this.availabilityState = value;
    }

    /**
     * Gets the value of the statusInformation property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfKeyValueItem }{@code >}
     *     
     */
    public JAXBElement<ArrayOfKeyValueItem> getStatusInformation() {
        return statusInformation;
    }

    /**
     * Sets the value of the statusInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfKeyValueItem }{@code >}
     *     
     */
    public void setStatusInformation(JAXBElement<ArrayOfKeyValueItem> value) {
        this.statusInformation = value;
    }

    /**
     * Gets the value of the prices property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfPrice }{@code >}
     *     
     */
    public JAXBElement<ArrayOfPrice> getPrices() {
        return prices;
    }

    /**
     * Sets the value of the prices property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfPrice }{@code >}
     *     
     */
    public void setPrices(JAXBElement<ArrayOfPrice> value) {
        this.prices = value;
    }

    /**
     * Gets the value of the additionalDescription property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAdditionalDescription() {
        return additionalDescription;
    }

    /**
     * Sets the value of the additionalDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAdditionalDescription(JAXBElement<String> value) {
        this.additionalDescription = value;
    }

    /**
     * Gets the value of the additionalDescriptionExtended property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAdditionalDescriptionExtended() {
        return additionalDescriptionExtended;
    }

    /**
     * Sets the value of the additionalDescriptionExtended property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAdditionalDescriptionExtended(JAXBElement<String> value) {
        this.additionalDescriptionExtended = value;
    }

    /**
     * Gets the value of the linkedItemsCollections property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfLinkedItemsCollection }{@code >}
     *     
     */
    public JAXBElement<ArrayOfLinkedItemsCollection> getLinkedItemsCollections() {
        return linkedItemsCollections;
    }

    /**
     * Sets the value of the linkedItemsCollections property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfLinkedItemsCollection }{@code >}
     *     
     */
    public void setLinkedItemsCollections(JAXBElement<ArrayOfLinkedItemsCollection> value) {
        this.linkedItemsCollections = value;
    }

    /**
     * Gets the value of the warehouses property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfWarehouse }{@code >}
     *     
     */
    public JAXBElement<ArrayOfWarehouse> getWarehouses() {
        return warehouses;
    }

    /**
     * Sets the value of the warehouses property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfWarehouse }{@code >}
     *     
     */
    public void setWarehouses(JAXBElement<ArrayOfWarehouse> value) {
        this.warehouses = value;
    }

    /**
     * Gets the value of the tour property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link EntityLink }{@code >}
     *     
     */
    public JAXBElement<EntityLink> getTour() {
        return tour;
    }

    /**
     * Sets the value of the tour property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link EntityLink }{@code >}
     *     
     */
    public void setTour(JAXBElement<EntityLink> value) {
        this.tour = value;
    }

    /**
     * Gets the value of the memos property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfMemo }{@code >}
     *     
     */
    public JAXBElement<ArrayOfMemo> getMemos() {
        return memos;
    }

    /**
     * Sets the value of the memos property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfMemo }{@code >}
     *     
     */
    public void setMemos(JAXBElement<ArrayOfMemo> value) {
        this.memos = value;
    }

    /**
     * Gets the value of the specialIcons property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfEntityLink }{@code >}
     *     
     */
    public JAXBElement<ArrayOfEntityLink> getSpecialIcons() {
        return specialIcons;
    }

    /**
     * Sets the value of the specialIcons property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfEntityLink }{@code >}
     *     
     */
    public void setSpecialIcons(JAXBElement<ArrayOfEntityLink> value) {
        this.specialIcons = value;
    }

    /**
     * Gets the value of the controlIndicators property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfControlIndicator }{@code >}
     *     
     */
    public JAXBElement<ArrayOfControlIndicator> getControlIndicators() {
        return controlIndicators;
    }

    /**
     * Sets the value of the controlIndicators property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfControlIndicator }{@code >}
     *     
     */
    public void setControlIndicators(JAXBElement<ArrayOfControlIndicator> value) {
        this.controlIndicators = value;
    }

}
