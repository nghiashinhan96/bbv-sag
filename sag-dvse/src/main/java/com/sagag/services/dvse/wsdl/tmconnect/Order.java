
package com.sagag.services.dvse.wsdl.tmconnect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
public class Order
    extends BaseDto
{

    @XmlElement(name = "StatusInformation")
    protected KeyValueItem statusInformation;
    @XmlElement(name = "OrderIds")
    protected ArrayOfOrderId orderIds;
    @XmlElement(name = "UserDefinedData")
    protected ArrayOfUserDefinedData userDefinedData;
    @XmlElement(name = "WarehouseInformation")
    protected String warehouseInformation;
    @XmlElement(name = "TourInformation")
    protected String tourInformation;
    @XmlElement(name = "ExpectedDelivery")
    protected String expectedDelivery;
    @XmlElement(name = "Items")
    protected ArrayOfOrderPosition items;
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
     * Gets the value of the orderIds property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfOrderId }
     *     
     */
    public ArrayOfOrderId getOrderIds() {
        return orderIds;
    }

    /**
     * Sets the value of the orderIds property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfOrderId }
     *     
     */
    public void setOrderIds(ArrayOfOrderId value) {
        this.orderIds = value;
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
     * Gets the value of the warehouseInformation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarehouseInformation() {
        return warehouseInformation;
    }

    /**
     * Sets the value of the warehouseInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarehouseInformation(String value) {
        this.warehouseInformation = value;
    }

    /**
     * Gets the value of the tourInformation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTourInformation() {
        return tourInformation;
    }

    /**
     * Sets the value of the tourInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTourInformation(String value) {
        this.tourInformation = value;
    }

    /**
     * Gets the value of the expectedDelivery property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpectedDelivery() {
        return expectedDelivery;
    }

    /**
     * Sets the value of the expectedDelivery property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpectedDelivery(String value) {
        this.expectedDelivery = value;
    }

    /**
     * Gets the value of the items property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfOrderPosition }
     *     
     */
    public ArrayOfOrderPosition getItems() {
        return items;
    }

    /**
     * Sets the value of the items property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfOrderPosition }
     *     
     */
    public void setItems(ArrayOfOrderPosition value) {
        this.items = value;
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
