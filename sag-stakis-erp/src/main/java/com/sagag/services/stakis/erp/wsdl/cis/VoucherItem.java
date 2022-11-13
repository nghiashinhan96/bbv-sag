
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
 * <p>Java class for VoucherItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VoucherItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VoucherId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OrderNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OrderDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="GenericArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GenericArticleId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WholesalerArticleNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SupplierArticleNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SupplierName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SupplierId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ArticleDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="QuantityUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Discount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Prices" type="{DVSE.WebApp.CISService}ArrayOfPrice" minOccurs="0"/>
 *         &lt;element name="TotalGross" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="TotalNet" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Taxes" type="{DVSE.WebApp.CISService}ArrayOfTax" minOccurs="0"/>
 *         &lt;element name="CurrencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Information" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ShippingMode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PlateId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="KTypeId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="VehicleType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="VehicleInfo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CanReturn" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="ReturnState" type="{DVSE.WebApp.CISService}State" minOccurs="0"/>
 *         &lt;element name="ReturnQuantity" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ReturnReason" type="{DVSE.WebApp.CISService}ReturnReason" minOccurs="0"/>
 *         &lt;element name="ReturnInfo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HasConnectedVouchers" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="BackOrderQuantity" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="ComplaintDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VoucherItem", propOrder = {
    "id",
    "voucherId",
    "orderNo",
    "orderDate",
    "genericArticle",
    "genericArticleId",
    "wholesalerArticleNumber",
    "supplierArticleNumber",
    "supplierName",
    "supplierId",
    "articleDescription",
    "quantity",
    "quantityUnit",
    "discount",
    "prices",
    "totalGross",
    "totalNet",
    "taxes",
    "currencyCode",
    "information",
    "shippingMode",
    "plateId",
    "kTypeId",
    "vehicleType",
    "vehicleInfo",
    "canReturn",
    "returnState",
    "returnQuantity",
    "returnReason",
    "returnInfo",
    "hasConnectedVouchers",
    "backOrderQuantity",
    "complaintDate"
})
public class VoucherItem {

    @XmlElementRef(name = "Id", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> id;
    @XmlElementRef(name = "VoucherId", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> voucherId;
    @XmlElementRef(name = "OrderNo", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> orderNo;
    @XmlElement(name = "OrderDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar orderDate;
    @XmlElementRef(name = "GenericArticle", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> genericArticle;
    @XmlElementRef(name = "GenericArticleId", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> genericArticleId;
    @XmlElementRef(name = "WholesalerArticleNumber", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> wholesalerArticleNumber;
    @XmlElementRef(name = "SupplierArticleNumber", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> supplierArticleNumber;
    @XmlElementRef(name = "SupplierName", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> supplierName;
    @XmlElementRef(name = "SupplierId", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> supplierId;
    @XmlElementRef(name = "ArticleDescription", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> articleDescription;
    @XmlElement(name = "Quantity", required = true)
    protected BigDecimal quantity;
    @XmlElementRef(name = "QuantityUnit", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> quantityUnit;
    @XmlElement(name = "Discount", required = true)
    protected BigDecimal discount;
    @XmlElementRef(name = "Prices", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfPrice> prices;
    @XmlElement(name = "TotalGross", required = true)
    protected BigDecimal totalGross;
    @XmlElement(name = "TotalNet", required = true)
    protected BigDecimal totalNet;
    @XmlElementRef(name = "Taxes", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfTax> taxes;
    @XmlElementRef(name = "CurrencyCode", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> currencyCode;
    @XmlElementRef(name = "Information", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> information;
    @XmlElementRef(name = "ShippingMode", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> shippingMode;
    @XmlElementRef(name = "PlateId", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> plateId;
    @XmlElement(name = "KTypeId", required = true, type = Long.class, nillable = true)
    protected Long kTypeId;
    @XmlElement(name = "VehicleType", required = true, type = Integer.class, nillable = true)
    protected Integer vehicleType;
    @XmlElementRef(name = "VehicleInfo", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> vehicleInfo;
    @XmlElement(name = "CanReturn")
    protected boolean canReturn;
    @XmlElementRef(name = "ReturnState", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<State> returnState;
    @XmlElement(name = "ReturnQuantity")
    protected int returnQuantity;
    @XmlElementRef(name = "ReturnReason", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ReturnReason> returnReason;
    @XmlElementRef(name = "ReturnInfo", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> returnInfo;
    @XmlElement(name = "HasConnectedVouchers")
    protected boolean hasConnectedVouchers;
    @XmlElement(name = "BackOrderQuantity")
    protected BigDecimal backOrderQuantity;
    @XmlElement(name = "ComplaintDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar complaintDate;

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
     * Gets the value of the voucherId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getVoucherId() {
        return voucherId;
    }

    /**
     * Sets the value of the voucherId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setVoucherId(JAXBElement<String> value) {
        this.voucherId = value;
    }

    /**
     * Gets the value of the orderNo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOrderNo() {
        return orderNo;
    }

    /**
     * Sets the value of the orderNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOrderNo(JAXBElement<String> value) {
        this.orderNo = value;
    }

    /**
     * Gets the value of the orderDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOrderDate() {
        return orderDate;
    }

    /**
     * Sets the value of the orderDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOrderDate(XMLGregorianCalendar value) {
        this.orderDate = value;
    }

    /**
     * Gets the value of the genericArticle property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getGenericArticle() {
        return genericArticle;
    }

    /**
     * Sets the value of the genericArticle property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGenericArticle(JAXBElement<String> value) {
        this.genericArticle = value;
    }

    /**
     * Gets the value of the genericArticleId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getGenericArticleId() {
        return genericArticleId;
    }

    /**
     * Sets the value of the genericArticleId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGenericArticleId(JAXBElement<String> value) {
        this.genericArticleId = value;
    }

    /**
     * Gets the value of the wholesalerArticleNumber property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getWholesalerArticleNumber() {
        return wholesalerArticleNumber;
    }

    /**
     * Sets the value of the wholesalerArticleNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setWholesalerArticleNumber(JAXBElement<String> value) {
        this.wholesalerArticleNumber = value;
    }

    /**
     * Gets the value of the supplierArticleNumber property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSupplierArticleNumber() {
        return supplierArticleNumber;
    }

    /**
     * Sets the value of the supplierArticleNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSupplierArticleNumber(JAXBElement<String> value) {
        this.supplierArticleNumber = value;
    }

    /**
     * Gets the value of the supplierName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSupplierName() {
        return supplierName;
    }

    /**
     * Sets the value of the supplierName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSupplierName(JAXBElement<String> value) {
        this.supplierName = value;
    }

    /**
     * Gets the value of the supplierId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSupplierId() {
        return supplierId;
    }

    /**
     * Sets the value of the supplierId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSupplierId(JAXBElement<String> value) {
        this.supplierId = value;
    }

    /**
     * Gets the value of the articleDescription property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getArticleDescription() {
        return articleDescription;
    }

    /**
     * Sets the value of the articleDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setArticleDescription(JAXBElement<String> value) {
        this.articleDescription = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantity(BigDecimal value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the quantityUnit property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getQuantityUnit() {
        return quantityUnit;
    }

    /**
     * Sets the value of the quantityUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setQuantityUnit(JAXBElement<String> value) {
        this.quantityUnit = value;
    }

    /**
     * Gets the value of the discount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDiscount() {
        return discount;
    }

    /**
     * Sets the value of the discount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDiscount(BigDecimal value) {
        this.discount = value;
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
     * Gets the value of the taxes property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTax }{@code >}
     *     
     */
    public JAXBElement<ArrayOfTax> getTaxes() {
        return taxes;
    }

    /**
     * Sets the value of the taxes property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTax }{@code >}
     *     
     */
    public void setTaxes(JAXBElement<ArrayOfTax> value) {
        this.taxes = value;
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
     * Gets the value of the shippingMode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getShippingMode() {
        return shippingMode;
    }

    /**
     * Sets the value of the shippingMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setShippingMode(JAXBElement<String> value) {
        this.shippingMode = value;
    }

    /**
     * Gets the value of the plateId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPlateId() {
        return plateId;
    }

    /**
     * Sets the value of the plateId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPlateId(JAXBElement<String> value) {
        this.plateId = value;
    }

    /**
     * Gets the value of the kTypeId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getKTypeId() {
        return kTypeId;
    }

    /**
     * Sets the value of the kTypeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setKTypeId(Long value) {
        this.kTypeId = value;
    }

    /**
     * Gets the value of the vehicleType property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getVehicleType() {
        return vehicleType;
    }

    /**
     * Sets the value of the vehicleType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setVehicleType(Integer value) {
        this.vehicleType = value;
    }

    /**
     * Gets the value of the vehicleInfo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getVehicleInfo() {
        return vehicleInfo;
    }

    /**
     * Sets the value of the vehicleInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setVehicleInfo(JAXBElement<String> value) {
        this.vehicleInfo = value;
    }

    /**
     * Gets the value of the canReturn property.
     * 
     */
    public boolean isCanReturn() {
        return canReturn;
    }

    /**
     * Sets the value of the canReturn property.
     * 
     */
    public void setCanReturn(boolean value) {
        this.canReturn = value;
    }

    /**
     * Gets the value of the returnState property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link State }{@code >}
     *     
     */
    public JAXBElement<State> getReturnState() {
        return returnState;
    }

    /**
     * Sets the value of the returnState property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link State }{@code >}
     *     
     */
    public void setReturnState(JAXBElement<State> value) {
        this.returnState = value;
    }

    /**
     * Gets the value of the returnQuantity property.
     * 
     */
    public int getReturnQuantity() {
        return returnQuantity;
    }

    /**
     * Sets the value of the returnQuantity property.
     * 
     */
    public void setReturnQuantity(int value) {
        this.returnQuantity = value;
    }

    /**
     * Gets the value of the returnReason property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ReturnReason }{@code >}
     *     
     */
    public JAXBElement<ReturnReason> getReturnReason() {
        return returnReason;
    }

    /**
     * Sets the value of the returnReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ReturnReason }{@code >}
     *     
     */
    public void setReturnReason(JAXBElement<ReturnReason> value) {
        this.returnReason = value;
    }

    /**
     * Gets the value of the returnInfo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getReturnInfo() {
        return returnInfo;
    }

    /**
     * Sets the value of the returnInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setReturnInfo(JAXBElement<String> value) {
        this.returnInfo = value;
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
     * Gets the value of the backOrderQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBackOrderQuantity() {
        return backOrderQuantity;
    }

    /**
     * Sets the value of the backOrderQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBackOrderQuantity(BigDecimal value) {
        this.backOrderQuantity = value;
    }

    /**
     * Gets the value of the complaintDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getComplaintDate() {
        return complaintDate;
    }

    /**
     * Sets the value of the complaintDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setComplaintDate(XMLGregorianCalendar value) {
        this.complaintDate = value;
    }

}
