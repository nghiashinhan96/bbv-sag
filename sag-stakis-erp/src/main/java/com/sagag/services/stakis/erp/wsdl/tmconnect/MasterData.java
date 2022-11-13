
package com.sagag.services.stakis.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MasterData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MasterData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Articles" type="{http://topmotive.eu/TMConnect}ArrayOfArticle" minOccurs="0"/>
 *         &lt;element name="ArticleTmfs" type="{http://topmotive.eu/TMConnect}ArrayOfArticleTmf" minOccurs="0"/>
 *         &lt;element name="RepairTimes" type="{http://topmotive.eu/TMConnect}ArrayOfRepairTime" minOccurs="0"/>
 *         &lt;element name="ItemsCollections" type="{http://topmotive.eu/TMConnect}ArrayOfItemsCollection" minOccurs="0"/>
 *         &lt;element name="Tours" type="{http://topmotive.eu/TMConnect}ArrayOfTour" minOccurs="0"/>
 *         &lt;element name="Vehicles" type="{http://topmotive.eu/TMConnect}ArrayOfVehicle" minOccurs="0"/>
 *         &lt;element name="Customers" type="{http://topmotive.eu/TMConnect}ArrayOfCustomer" minOccurs="0"/>
 *         &lt;element name="TextBlocks" type="{http://topmotive.eu/TMConnect}ArrayOfTextBlock" minOccurs="0"/>
 *         &lt;element name="DispatchTypes" type="{http://topmotive.eu/TMConnect}ArrayOfDispatchType" minOccurs="0"/>
 *         &lt;element name="SelectionListItems" type="{http://topmotive.eu/TMConnect}ArrayOfSelectionListItem" minOccurs="0"/>
 *         &lt;element name="AvailabilityStates" type="{http://topmotive.eu/TMConnect}ArrayOfAvailabilityState" minOccurs="0"/>
 *         &lt;element name="Icons" type="{http://topmotive.eu/TMConnect}ArrayOfIcon" minOccurs="0"/>
 *         &lt;element name="Addresses" type="{http://topmotive.eu/TMConnect}ArrayOfAddress" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MasterData", propOrder = {
    "articles",
    "articleTmfs",
    "repairTimes",
    "itemsCollections",
    "tours",
    "vehicles",
    "customers",
    "textBlocks",
    "dispatchTypes",
    "selectionListItems",
    "availabilityStates",
    "icons",
    "addresses"
})
public class MasterData {

    @XmlElementRef(name = "Articles", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfArticle> articles;
    @XmlElementRef(name = "ArticleTmfs", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfArticleTmf> articleTmfs;
    @XmlElementRef(name = "RepairTimes", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfRepairTime> repairTimes;
    @XmlElementRef(name = "ItemsCollections", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfItemsCollection> itemsCollections;
    @XmlElementRef(name = "Tours", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfTour> tours;
    @XmlElementRef(name = "Vehicles", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfVehicle> vehicles;
    @XmlElementRef(name = "Customers", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfCustomer> customers;
    @XmlElementRef(name = "TextBlocks", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfTextBlock> textBlocks;
    @XmlElementRef(name = "DispatchTypes", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfDispatchType> dispatchTypes;
    @XmlElementRef(name = "SelectionListItems", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfSelectionListItem> selectionListItems;
    @XmlElementRef(name = "AvailabilityStates", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfAvailabilityState> availabilityStates;
    @XmlElementRef(name = "Icons", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfIcon> icons;
    @XmlElementRef(name = "Addresses", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfAddress> addresses;

    /**
     * Gets the value of the articles property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfArticle }{@code >}
     *     
     */
    public JAXBElement<ArrayOfArticle> getArticles() {
        return articles;
    }

    /**
     * Sets the value of the articles property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfArticle }{@code >}
     *     
     */
    public void setArticles(JAXBElement<ArrayOfArticle> value) {
        this.articles = value;
    }

    /**
     * Gets the value of the articleTmfs property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfArticleTmf }{@code >}
     *     
     */
    public JAXBElement<ArrayOfArticleTmf> getArticleTmfs() {
        return articleTmfs;
    }

    /**
     * Sets the value of the articleTmfs property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfArticleTmf }{@code >}
     *     
     */
    public void setArticleTmfs(JAXBElement<ArrayOfArticleTmf> value) {
        this.articleTmfs = value;
    }

    /**
     * Gets the value of the repairTimes property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRepairTime }{@code >}
     *     
     */
    public JAXBElement<ArrayOfRepairTime> getRepairTimes() {
        return repairTimes;
    }

    /**
     * Sets the value of the repairTimes property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRepairTime }{@code >}
     *     
     */
    public void setRepairTimes(JAXBElement<ArrayOfRepairTime> value) {
        this.repairTimes = value;
    }

    /**
     * Gets the value of the itemsCollections property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfItemsCollection }{@code >}
     *     
     */
    public JAXBElement<ArrayOfItemsCollection> getItemsCollections() {
        return itemsCollections;
    }

    /**
     * Sets the value of the itemsCollections property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfItemsCollection }{@code >}
     *     
     */
    public void setItemsCollections(JAXBElement<ArrayOfItemsCollection> value) {
        this.itemsCollections = value;
    }

    /**
     * Gets the value of the tours property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTour }{@code >}
     *     
     */
    public JAXBElement<ArrayOfTour> getTours() {
        return tours;
    }

    /**
     * Sets the value of the tours property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTour }{@code >}
     *     
     */
    public void setTours(JAXBElement<ArrayOfTour> value) {
        this.tours = value;
    }

    /**
     * Gets the value of the vehicles property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfVehicle }{@code >}
     *     
     */
    public JAXBElement<ArrayOfVehicle> getVehicles() {
        return vehicles;
    }

    /**
     * Sets the value of the vehicles property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfVehicle }{@code >}
     *     
     */
    public void setVehicles(JAXBElement<ArrayOfVehicle> value) {
        this.vehicles = value;
    }

    /**
     * Gets the value of the customers property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfCustomer }{@code >}
     *     
     */
    public JAXBElement<ArrayOfCustomer> getCustomers() {
        return customers;
    }

    /**
     * Sets the value of the customers property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfCustomer }{@code >}
     *     
     */
    public void setCustomers(JAXBElement<ArrayOfCustomer> value) {
        this.customers = value;
    }

    /**
     * Gets the value of the textBlocks property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTextBlock }{@code >}
     *     
     */
    public JAXBElement<ArrayOfTextBlock> getTextBlocks() {
        return textBlocks;
    }

    /**
     * Sets the value of the textBlocks property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTextBlock }{@code >}
     *     
     */
    public void setTextBlocks(JAXBElement<ArrayOfTextBlock> value) {
        this.textBlocks = value;
    }

    /**
     * Gets the value of the dispatchTypes property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfDispatchType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfDispatchType> getDispatchTypes() {
        return dispatchTypes;
    }

    /**
     * Sets the value of the dispatchTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfDispatchType }{@code >}
     *     
     */
    public void setDispatchTypes(JAXBElement<ArrayOfDispatchType> value) {
        this.dispatchTypes = value;
    }

    /**
     * Gets the value of the selectionListItems property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSelectionListItem }{@code >}
     *     
     */
    public JAXBElement<ArrayOfSelectionListItem> getSelectionListItems() {
        return selectionListItems;
    }

    /**
     * Sets the value of the selectionListItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSelectionListItem }{@code >}
     *     
     */
    public void setSelectionListItems(JAXBElement<ArrayOfSelectionListItem> value) {
        this.selectionListItems = value;
    }

    /**
     * Gets the value of the availabilityStates property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfAvailabilityState }{@code >}
     *     
     */
    public JAXBElement<ArrayOfAvailabilityState> getAvailabilityStates() {
        return availabilityStates;
    }

    /**
     * Sets the value of the availabilityStates property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfAvailabilityState }{@code >}
     *     
     */
    public void setAvailabilityStates(JAXBElement<ArrayOfAvailabilityState> value) {
        this.availabilityStates = value;
    }

    /**
     * Gets the value of the icons property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfIcon }{@code >}
     *     
     */
    public JAXBElement<ArrayOfIcon> getIcons() {
        return icons;
    }

    /**
     * Sets the value of the icons property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfIcon }{@code >}
     *     
     */
    public void setIcons(JAXBElement<ArrayOfIcon> value) {
        this.icons = value;
    }

    /**
     * Gets the value of the addresses property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfAddress }{@code >}
     *     
     */
    public JAXBElement<ArrayOfAddress> getAddresses() {
        return addresses;
    }

    /**
     * Sets the value of the addresses property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfAddress }{@code >}
     *     
     */
    public void setAddresses(JAXBElement<ArrayOfAddress> value) {
        this.addresses = value;
    }

}
