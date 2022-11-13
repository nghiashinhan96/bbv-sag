
package com.sagag.services.dvse.wsdl.tmconnect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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

    @XmlElement(name = "Articles")
    protected ArrayOfArticle articles;
    @XmlElement(name = "ArticleTmfs")
    protected ArrayOfArticleTmf articleTmfs;
    @XmlElement(name = "RepairTimes")
    protected ArrayOfRepairTime repairTimes;
    @XmlElement(name = "ItemsCollections")
    protected ArrayOfItemsCollection itemsCollections;
    @XmlElement(name = "Tours")
    protected ArrayOfTour tours;
    @XmlElement(name = "Vehicles")
    protected ArrayOfVehicle vehicles;
    @XmlElement(name = "Customers")
    protected ArrayOfCustomer customers;
    @XmlElement(name = "TextBlocks")
    protected ArrayOfTextBlock textBlocks;
    @XmlElement(name = "DispatchTypes")
    protected ArrayOfDispatchType dispatchTypes;
    @XmlElement(name = "SelectionListItems")
    protected ArrayOfSelectionListItem selectionListItems;
    @XmlElement(name = "AvailabilityStates")
    protected ArrayOfAvailabilityState availabilityStates;
    @XmlElement(name = "Icons")
    protected ArrayOfIcon icons;
    @XmlElement(name = "Addresses")
    protected ArrayOfAddress addresses;

    /**
     * Gets the value of the articles property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfArticle }
     *     
     */
    public ArrayOfArticle getArticles() {
        return articles;
    }

    /**
     * Sets the value of the articles property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfArticle }
     *     
     */
    public void setArticles(ArrayOfArticle value) {
        this.articles = value;
    }

    /**
     * Gets the value of the articleTmfs property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfArticleTmf }
     *     
     */
    public ArrayOfArticleTmf getArticleTmfs() {
        return articleTmfs;
    }

    /**
     * Sets the value of the articleTmfs property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfArticleTmf }
     *     
     */
    public void setArticleTmfs(ArrayOfArticleTmf value) {
        this.articleTmfs = value;
    }

    /**
     * Gets the value of the repairTimes property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfRepairTime }
     *     
     */
    public ArrayOfRepairTime getRepairTimes() {
        return repairTimes;
    }

    /**
     * Sets the value of the repairTimes property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfRepairTime }
     *     
     */
    public void setRepairTimes(ArrayOfRepairTime value) {
        this.repairTimes = value;
    }

    /**
     * Gets the value of the itemsCollections property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfItemsCollection }
     *     
     */
    public ArrayOfItemsCollection getItemsCollections() {
        return itemsCollections;
    }

    /**
     * Sets the value of the itemsCollections property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfItemsCollection }
     *     
     */
    public void setItemsCollections(ArrayOfItemsCollection value) {
        this.itemsCollections = value;
    }

    /**
     * Gets the value of the tours property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTour }
     *     
     */
    public ArrayOfTour getTours() {
        return tours;
    }

    /**
     * Sets the value of the tours property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTour }
     *     
     */
    public void setTours(ArrayOfTour value) {
        this.tours = value;
    }

    /**
     * Gets the value of the vehicles property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfVehicle }
     *     
     */
    public ArrayOfVehicle getVehicles() {
        return vehicles;
    }

    /**
     * Sets the value of the vehicles property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfVehicle }
     *     
     */
    public void setVehicles(ArrayOfVehicle value) {
        this.vehicles = value;
    }

    /**
     * Gets the value of the customers property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCustomer }
     *     
     */
    public ArrayOfCustomer getCustomers() {
        return customers;
    }

    /**
     * Sets the value of the customers property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCustomer }
     *     
     */
    public void setCustomers(ArrayOfCustomer value) {
        this.customers = value;
    }

    /**
     * Gets the value of the textBlocks property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTextBlock }
     *     
     */
    public ArrayOfTextBlock getTextBlocks() {
        return textBlocks;
    }

    /**
     * Sets the value of the textBlocks property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTextBlock }
     *     
     */
    public void setTextBlocks(ArrayOfTextBlock value) {
        this.textBlocks = value;
    }

    /**
     * Gets the value of the dispatchTypes property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfDispatchType }
     *     
     */
    public ArrayOfDispatchType getDispatchTypes() {
        return dispatchTypes;
    }

    /**
     * Sets the value of the dispatchTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfDispatchType }
     *     
     */
    public void setDispatchTypes(ArrayOfDispatchType value) {
        this.dispatchTypes = value;
    }

    /**
     * Gets the value of the selectionListItems property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSelectionListItem }
     *     
     */
    public ArrayOfSelectionListItem getSelectionListItems() {
        return selectionListItems;
    }

    /**
     * Sets the value of the selectionListItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSelectionListItem }
     *     
     */
    public void setSelectionListItems(ArrayOfSelectionListItem value) {
        this.selectionListItems = value;
    }

    /**
     * Gets the value of the availabilityStates property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAvailabilityState }
     *     
     */
    public ArrayOfAvailabilityState getAvailabilityStates() {
        return availabilityStates;
    }

    /**
     * Sets the value of the availabilityStates property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAvailabilityState }
     *     
     */
    public void setAvailabilityStates(ArrayOfAvailabilityState value) {
        this.availabilityStates = value;
    }

    /**
     * Gets the value of the icons property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfIcon }
     *     
     */
    public ArrayOfIcon getIcons() {
        return icons;
    }

    /**
     * Sets the value of the icons property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfIcon }
     *     
     */
    public void setIcons(ArrayOfIcon value) {
        this.icons = value;
    }

    /**
     * Gets the value of the addresses property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAddress }
     *     
     */
    public ArrayOfAddress getAddresses() {
        return addresses;
    }

    /**
     * Sets the value of the addresses property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAddress }
     *     
     */
    public void setAddresses(ArrayOfAddress value) {
        this.addresses = value;
    }

}
