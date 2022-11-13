
package com.sagag.services.stakis.erp.wsdl.cis;

import java.math.BigDecimal;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Voucher complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Voucher">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VoucherType" type="{DVSE.WebApp.CISService}VoucherType" minOccurs="0"/>
 *         &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="Contacts" type="{DVSE.WebApp.CISService}ArrayOfContact" minOccurs="0"/>
 *         &lt;element name="DeliveryAddress" type="{DVSE.WebApp.CISService}Address" minOccurs="0"/>
 *         &lt;element name="InvoiceAddress" type="{DVSE.WebApp.CISService}Address" minOccurs="0"/>
 *         &lt;element name="ItemsCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TotalGross" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="TotalNet" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Information" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HasConnectedVouchers" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="CurrencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PaymentInformation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DeliveryType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VoucherSummaryItems" type="{DVSE.WebApp.CISService}ArrayOfVoucherSummaryItem" minOccurs="0"/>
 *         &lt;element name="Salespoint" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VoucherDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Tour" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Voucher", propOrder = {
    "id",
    "voucherType",
    "date",
    "contacts",
    "deliveryAddress",
    "invoiceAddress",
    "itemsCount",
    "totalGross",
    "totalNet",
    "information",
    "hasConnectedVouchers",
    "currencyCode",
    "paymentInformation",
    "deliveryType",
    "voucherSummaryItems",
    "salespoint",
    "voucherDescription",
    "tour"
})
public class Voucher {

    @XmlElementRef(name = "Id", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> id;
    @XmlElementRef(name = "VoucherType", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<VoucherType> voucherType;
    @XmlElement(name = "Date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar date;
    @XmlElementRef(name = "Contacts", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfContact> contacts;
    @XmlElementRef(name = "DeliveryAddress", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<Address> deliveryAddress;
    @XmlElementRef(name = "InvoiceAddress", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<Address> invoiceAddress;
    @XmlElement(name = "ItemsCount")
    protected int itemsCount;
    @XmlElement(name = "TotalGross", required = true)
    protected BigDecimal totalGross;
    @XmlElement(name = "TotalNet", required = true)
    protected BigDecimal totalNet;
    @XmlElementRef(name = "Information", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> information;
    @XmlElement(name = "HasConnectedVouchers")
    protected boolean hasConnectedVouchers;
    @XmlElementRef(name = "CurrencyCode", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> currencyCode;
    @XmlElementRef(name = "PaymentInformation", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> paymentInformation;
    @XmlElementRef(name = "DeliveryType", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> deliveryType;
    @XmlElementRef(name = "VoucherSummaryItems", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfVoucherSummaryItem> voucherSummaryItems;
    @XmlElementRef(name = "Salespoint", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> salespoint;
    @XmlElementRef(name = "VoucherDescription", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> voucherDescription;
    @XmlElementRef(name = "Tour", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> tour;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setId(JAXBElement<String> value) {
        this.id = value;
    }

    /**
     * Gets the value of the voucherType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link VoucherType }{@code >}
     *     
     */
    public JAXBElement<VoucherType> getVoucherType() {
        return voucherType;
    }

    /**
     * Sets the value of the voucherType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link VoucherType }{@code >}
     *     
     */
    public void setVoucherType(JAXBElement<VoucherType> value) {
        this.voucherType = value;
    }

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

    /**
     * Gets the value of the contacts property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfContact }{@code >}
     *     
     */
    public JAXBElement<ArrayOfContact> getContacts() {
        return contacts;
    }

    /**
     * Sets the value of the contacts property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfContact }{@code >}
     *     
     */
    public void setContacts(JAXBElement<ArrayOfContact> value) {
        this.contacts = value;
    }

    /**
     * Gets the value of the deliveryAddress property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Address }{@code >}
     *     
     */
    public JAXBElement<Address> getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * Sets the value of the deliveryAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Address }{@code >}
     *     
     */
    public void setDeliveryAddress(JAXBElement<Address> value) {
        this.deliveryAddress = value;
    }

    /**
     * Gets the value of the invoiceAddress property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Address }{@code >}
     *     
     */
    public JAXBElement<Address> getInvoiceAddress() {
        return invoiceAddress;
    }

    /**
     * Sets the value of the invoiceAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Address }{@code >}
     *     
     */
    public void setInvoiceAddress(JAXBElement<Address> value) {
        this.invoiceAddress = value;
    }

    /**
     * Gets the value of the itemsCount property.
     * 
     */
    public int getItemsCount() {
        return itemsCount;
    }

    /**
     * Sets the value of the itemsCount property.
     * 
     */
    public void setItemsCount(int value) {
        this.itemsCount = value;
    }

    /**
     * Gets the value of the totalGross property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalGross() {
        return totalGross;
    }

    /**
     * Sets the value of the totalGross property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalGross(BigDecimal value) {
        this.totalGross = value;
    }

    /**
     * Gets the value of the totalNet property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalNet() {
        return totalNet;
    }

    /**
     * Sets the value of the totalNet property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalNet(BigDecimal value) {
        this.totalNet = value;
    }

    /**
     * Gets the value of the information property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getInformation() {
        return information;
    }

    /**
     * Sets the value of the information property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setInformation(JAXBElement<String> value) {
        this.information = value;
    }

    /**
     * Gets the value of the hasConnectedVouchers property.
     * 
     */
    public boolean isHasConnectedVouchers() {
        return hasConnectedVouchers;
    }

    /**
     * Sets the value of the hasConnectedVouchers property.
     * 
     */
    public void setHasConnectedVouchers(boolean value) {
        this.hasConnectedVouchers = value;
    }

    /**
     * Gets the value of the currencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCurrencyCode(JAXBElement<String> value) {
        this.currencyCode = value;
    }

    /**
     * Gets the value of the paymentInformation property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPaymentInformation() {
        return paymentInformation;
    }

    /**
     * Sets the value of the paymentInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPaymentInformation(JAXBElement<String> value) {
        this.paymentInformation = value;
    }

    /**
     * Gets the value of the deliveryType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDeliveryType() {
        return deliveryType;
    }

    /**
     * Sets the value of the deliveryType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDeliveryType(JAXBElement<String> value) {
        this.deliveryType = value;
    }

    /**
     * Gets the value of the voucherSummaryItems property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfVoucherSummaryItem }{@code >}
     *     
     */
    public JAXBElement<ArrayOfVoucherSummaryItem> getVoucherSummaryItems() {
        return voucherSummaryItems;
    }

    /**
     * Sets the value of the voucherSummaryItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfVoucherSummaryItem }{@code >}
     *     
     */
    public void setVoucherSummaryItems(JAXBElement<ArrayOfVoucherSummaryItem> value) {
        this.voucherSummaryItems = value;
    }

    /**
     * Gets the value of the salespoint property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSalespoint() {
        return salespoint;
    }

    /**
     * Sets the value of the salespoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSalespoint(JAXBElement<String> value) {
        this.salespoint = value;
    }

    /**
     * Gets the value of the voucherDescription property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getVoucherDescription() {
        return voucherDescription;
    }

    /**
     * Sets the value of the voucherDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setVoucherDescription(JAXBElement<String> value) {
        this.voucherDescription = value;
    }

    /**
     * Gets the value of the tour property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTour() {
        return tour;
    }

    /**
     * Sets the value of the tour property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTour(JAXBElement<String> value) {
        this.tour = value;
    }

}
