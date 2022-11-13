package com.sagag.services.dvse.wsdl.unicat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Order", propOrder = {
    "orderId",
    "ownOrderId",
    "memo",
    "deliveryAddress",
    "shipmentMode",
    "paymentMode",
    "billingMode",
    "warehouse",
    "tourId",
    "expectedDelivery",
    "wantedDelivery"
})
public class Order {

    @XmlElement(name = "OrderId")
    protected String orderId;
    @XmlElement(name = "OwnOrderId")
    protected String ownOrderId;
    @XmlElement(name = "Memo")
    protected ArrayOfString memo;
    @XmlElement(name = "DeliveryAddress")
    protected Address deliveryAddress;
    @XmlElement(name = "ShipmentMode")
    protected String shipmentMode;
    @XmlElement(name = "PaymentMode")
    protected String paymentMode;
    @XmlElement(name = "BillingMode")
    protected String billingMode;
    @XmlElement(name = "Warehouse")
    protected String warehouse;
    @XmlElement(name = "TourId")
    protected String tourId;
    @XmlElement(name = "ExpectedDelivery")
    protected String expectedDelivery;
    @XmlElement(name = "WantedDelivery")
    protected String wantedDelivery;

    /**
     * Gets the value of the orderId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Sets the value of the orderId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOrderId(String value) {
        this.orderId = value;
    }

    /**
     * Gets the value of the ownOrderId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOwnOrderId() {
        return ownOrderId;
    }

    /**
     * Sets the value of the ownOrderId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOwnOrderId(String value) {
        this.ownOrderId = value;
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
     * Gets the value of the deliveryAddress property.
     *
     * @return
     *     possible object is
     *     {@link Address }
     *
     */
    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * Sets the value of the deliveryAddress property.
     *
     * @param value
     *     allowed object is
     *     {@link Address }
     *
     */
    public void setDeliveryAddress(Address value) {
        this.deliveryAddress = value;
    }

    /**
     * Gets the value of the shipmentMode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getShipmentMode() {
        return shipmentMode;
    }

    /**
     * Sets the value of the shipmentMode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setShipmentMode(String value) {
        this.shipmentMode = value;
    }

    /**
     * Gets the value of the paymentMode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPaymentMode() {
        return paymentMode;
    }

    /**
     * Sets the value of the paymentMode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPaymentMode(String value) {
        this.paymentMode = value;
    }

    /**
     * Gets the value of the billingMode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBillingMode() {
        return billingMode;
    }

    /**
     * Sets the value of the billingMode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBillingMode(String value) {
        this.billingMode = value;
    }

    /**
     * Gets the value of the warehouse property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getWarehouse() {
        return warehouse;
    }

    /**
     * Sets the value of the warehouse property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setWarehouse(String value) {
        this.warehouse = value;
    }

    /**
     * Gets the value of the tourId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTourId() {
        return tourId;
    }

    /**
     * Sets the value of the tourId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTourId(String value) {
        this.tourId = value;
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
     * Gets the value of the wantedDelivery property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getWantedDelivery() {
        return wantedDelivery;
    }

    /**
     * Sets the value of the wantedDelivery property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setWantedDelivery(String value) {
        this.wantedDelivery = value;
    }

}
