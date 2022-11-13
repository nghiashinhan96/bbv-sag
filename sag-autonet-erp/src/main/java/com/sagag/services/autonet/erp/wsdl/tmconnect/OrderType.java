
package com.sagag.services.autonet.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Order complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Order">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}BaseDto">
 *       &lt;sequence>
 *         &lt;element name="StatusInformation" type="{http://topmotive.eu/TMConnect}KeyValueItem" minOccurs="0"/>
 *         &lt;element name="OrderIds" type="{http://topmotive.eu/TMConnect}ArrayOfOrderId" minOccurs="0"/>
 *         &lt;element name="UserDefinedData" type="{http://topmotive.eu/TMConnect}ArrayOfUserDefinedData" minOccurs="0"/>
 *         &lt;element name="WarehouseInformation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TourInformation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ExpectedDelivery" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Items" type="{http://topmotive.eu/TMConnect}ArrayOfOrderPosition" minOccurs="0"/>
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
@XmlType(name = "Order", propOrder = {
    "statusInformation",
    "orderIds",
    "userDefinedData",
    "warehouseInformation",
    "tourInformation",
    "expectedDelivery",
    "items",
    "selectionLists"
})
public class OrderType
    extends BaseDtoType
{

    @XmlElementRef(name = "StatusInformation", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<KeyValueItemType> statusInformation;
    @XmlElementRef(name = "OrderIds", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfOrderIdType> orderIds;
    @XmlElementRef(name = "UserDefinedData", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfUserDefinedDataType> userDefinedData;
    @XmlElementRef(name = "WarehouseInformation", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> warehouseInformation;
    @XmlElementRef(name = "TourInformation", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> tourInformation;
    @XmlElementRef(name = "ExpectedDelivery", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> expectedDelivery;
    @XmlElementRef(name = "Items", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfOrderPositionType> items;
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
     * Gets the value of the orderIds property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfOrderIdType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfOrderIdType> getOrderIds() {
        return orderIds;
    }

    /**
     * Sets the value of the orderIds property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfOrderIdType }{@code >}
     *     
     */
    public void setOrderIds(JAXBElement<ArrayOfOrderIdType> value) {
        this.orderIds = value;
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
     * Gets the value of the warehouseInformation property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getWarehouseInformation() {
        return warehouseInformation;
    }

    /**
     * Sets the value of the warehouseInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setWarehouseInformation(JAXBElement<String> value) {
        this.warehouseInformation = value;
    }

    /**
     * Gets the value of the tourInformation property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTourInformation() {
        return tourInformation;
    }

    /**
     * Sets the value of the tourInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTourInformation(JAXBElement<String> value) {
        this.tourInformation = value;
    }

    /**
     * Gets the value of the expectedDelivery property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getExpectedDelivery() {
        return expectedDelivery;
    }

    /**
     * Sets the value of the expectedDelivery property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setExpectedDelivery(JAXBElement<String> value) {
        this.expectedDelivery = value;
    }

    /**
     * Gets the value of the items property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfOrderPositionType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfOrderPositionType> getItems() {
        return items;
    }

    /**
     * Sets the value of the items property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfOrderPositionType }{@code >}
     *     
     */
    public void setItems(JAXBElement<ArrayOfOrderPositionType> value) {
        this.items = value;
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
