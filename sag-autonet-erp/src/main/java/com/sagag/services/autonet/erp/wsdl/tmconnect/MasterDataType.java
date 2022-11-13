
package com.sagag.services.autonet.erp.wsdl.tmconnect;

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
public class MasterDataType {

    @XmlElementRef(name = "Articles", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfArticleType> articles;
    @XmlElementRef(name = "ArticleTmfs", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfArticleTmfType> articleTmfs;
    @XmlElementRef(name = "RepairTimes", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfRepairTimeType> repairTimes;
    @XmlElementRef(name = "ItemsCollections", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfItemsCollectionType> itemsCollections;
    @XmlElementRef(name = "Tours", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfTourType> tours;
    @XmlElementRef(name = "Vehicles", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfVehicleType> vehicles;
    @XmlElementRef(name = "Customers", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfCustomerType> customers;
    @XmlElementRef(name = "TextBlocks", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfTextBlockType> textBlocks;
    @XmlElementRef(name = "DispatchTypes", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfDispatchTypeType> dispatchTypes;
    @XmlElementRef(name = "SelectionListItems", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfSelectionListItemType> selectionListItems;
    @XmlElementRef(name = "AvailabilityStates", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfAvailabilityStateType> availabilityStates;
    @XmlElementRef(name = "Icons", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfIconType> icons;
    @XmlElementRef(name = "Addresses", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfAddressType> addresses;

    /**
     * Gets the value of the articles property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfArticleType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfArticleType> getArticles() {
        return articles;
    }

    /**
     * Sets the value of the articles property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfArticleType }{@code >}
     *     
     */
    public void setArticles(JAXBElement<ArrayOfArticleType> value) {
        this.articles = value;
    }

    /**
     * Gets the value of the articleTmfs property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfArticleTmfType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfArticleTmfType> getArticleTmfs() {
        return articleTmfs;
    }

    /**
     * Sets the value of the articleTmfs property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfArticleTmfType }{@code >}
     *     
     */
    public void setArticleTmfs(JAXBElement<ArrayOfArticleTmfType> value) {
        this.articleTmfs = value;
    }

    /**
     * Gets the value of the repairTimes property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRepairTimeType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfRepairTimeType> getRepairTimes() {
        return repairTimes;
    }

    /**
     * Sets the value of the repairTimes property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRepairTimeType }{@code >}
     *     
     */
    public void setRepairTimes(JAXBElement<ArrayOfRepairTimeType> value) {
        this.repairTimes = value;
    }

    /**
     * Gets the value of the itemsCollections property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfItemsCollectionType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfItemsCollectionType> getItemsCollections() {
        return itemsCollections;
    }

    /**
     * Sets the value of the itemsCollections property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfItemsCollectionType }{@code >}
     *     
     */
    public void setItemsCollections(JAXBElement<ArrayOfItemsCollectionType> value) {
        this.itemsCollections = value;
    }

    /**
     * Gets the value of the tours property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTourType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfTourType> getTours() {
        return tours;
    }

    /**
     * Sets the value of the tours property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTourType }{@code >}
     *     
     */
    public void setTours(JAXBElement<ArrayOfTourType> value) {
        this.tours = value;
    }

    /**
     * Gets the value of the vehicles property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfVehicleType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfVehicleType> getVehicles() {
        return vehicles;
    }

    /**
     * Sets the value of the vehicles property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfVehicleType }{@code >}
     *     
     */
    public void setVehicles(JAXBElement<ArrayOfVehicleType> value) {
        this.vehicles = value;
    }

    /**
     * Gets the value of the customers property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfCustomerType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfCustomerType> getCustomers() {
        return customers;
    }

    /**
     * Sets the value of the customers property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfCustomerType }{@code >}
     *     
     */
    public void setCustomers(JAXBElement<ArrayOfCustomerType> value) {
        this.customers = value;
    }

    /**
     * Gets the value of the textBlocks property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTextBlockType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfTextBlockType> getTextBlocks() {
        return textBlocks;
    }

    /**
     * Sets the value of the textBlocks property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTextBlockType }{@code >}
     *     
     */
    public void setTextBlocks(JAXBElement<ArrayOfTextBlockType> value) {
        this.textBlocks = value;
    }

    /**
     * Gets the value of the dispatchTypes property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfDispatchTypeType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfDispatchTypeType> getDispatchTypes() {
        return dispatchTypes;
    }

    /**
     * Sets the value of the dispatchTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfDispatchTypeType }{@code >}
     *     
     */
    public void setDispatchTypes(JAXBElement<ArrayOfDispatchTypeType> value) {
        this.dispatchTypes = value;
    }

    /**
     * Gets the value of the selectionListItems property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSelectionListItemType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfSelectionListItemType> getSelectionListItems() {
        return selectionListItems;
    }

    /**
     * Sets the value of the selectionListItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSelectionListItemType }{@code >}
     *     
     */
    public void setSelectionListItems(JAXBElement<ArrayOfSelectionListItemType> value) {
        this.selectionListItems = value;
    }

    /**
     * Gets the value of the availabilityStates property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfAvailabilityStateType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfAvailabilityStateType> getAvailabilityStates() {
        return availabilityStates;
    }

    /**
     * Sets the value of the availabilityStates property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfAvailabilityStateType }{@code >}
     *     
     */
    public void setAvailabilityStates(JAXBElement<ArrayOfAvailabilityStateType> value) {
        this.availabilityStates = value;
    }

    /**
     * Gets the value of the icons property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfIconType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfIconType> getIcons() {
        return icons;
    }

    /**
     * Sets the value of the icons property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfIconType }{@code >}
     *     
     */
    public void setIcons(JAXBElement<ArrayOfIconType> value) {
        this.icons = value;
    }

    /**
     * Gets the value of the addresses property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfAddressType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfAddressType> getAddresses() {
        return addresses;
    }

    /**
     * Sets the value of the addresses property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfAddressType }{@code >}
     *     
     */
    public void setAddresses(JAXBElement<ArrayOfAddressType> value) {
        this.addresses = value;
    }

}
