package com.sagag.services.dvse.wsdl.unicat;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Price", propOrder = {
    "id",
    "description",
    "value",
    "vat",
    "taxIncluded",
    "currencyCode",
    "rebate",
    "batchSize",
    "priceType",
    "memo",
    "priceUnit"
})
public class Price {

    @XmlElement(name = "Id")
    protected String id;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "Value", required = true)
    protected BigDecimal value;
    @XmlElement(name = "VAT", required = true)
    protected BigDecimal vat;
    @XmlElement(name = "TaxIncluded")
    protected boolean taxIncluded;
    @XmlElement(name = "CurrencyCode")
    protected String currencyCode;
    @XmlElement(name = "Rebate", required = true)
    protected BigDecimal rebate;
    @XmlElement(name = "BatchSize")
    protected Quantity batchSize;
    @XmlElement(name = "PriceType")
    protected String priceType;
    @XmlElement(name = "Memo")
    protected ArrayOfString memo;
    @XmlElement(name = "PriceUnit", required = true)
    protected BigDecimal priceUnit;

    /**
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the value property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * Gets the value of the vat property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getVAT() {
        return vat;
    }

    /**
     * Sets the value of the vat property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setVAT(BigDecimal value) {
        this.vat = value;
    }

    /**
     * Gets the value of the taxIncluded property.
     *
     */
    public boolean isTaxIncluded() {
        return taxIncluded;
    }

    /**
     * Sets the value of the taxIncluded property.
     *
     */
    public void setTaxIncluded(boolean value) {
        this.taxIncluded = value;
    }

    /**
     * Gets the value of the currencyCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCurrencyCode(String value) {
        this.currencyCode = value;
    }

    /**
     * Gets the value of the rebate property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getRebate() {
        return rebate;
    }

    /**
     * Sets the value of the rebate property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setRebate(BigDecimal value) {
        this.rebate = value;
    }

    /**
     * Gets the value of the batchSize property.
     *
     * @return
     *     possible object is
     *     {@link Quantity }
     *
     */
    public Quantity getBatchSize() {
        return batchSize;
    }

    /**
     * Sets the value of the batchSize property.
     *
     * @param value
     *     allowed object is
     *     {@link Quantity }
     *
     */
    public void setBatchSize(Quantity value) {
        this.batchSize = value;
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
     * Gets the value of the priceUnit property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getPriceUnit() {
        return priceUnit;
    }

    /**
     * Sets the value of the priceUnit property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setPriceUnit(BigDecimal value) {
        this.priceUnit = value;
    }

    public BigDecimal getVat() {
      return vat;
    }

    public void setVat(BigDecimal vat) {
      this.vat = vat;
    }

    public String getPriceType() {
      return priceType;
    }

    public void setPriceType(String priceType) {
      this.priceType = priceType;
    }


}
