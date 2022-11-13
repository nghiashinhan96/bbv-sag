
package com.sagag.services.dvse.wsdl.tmconnect;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DispatchType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DispatchType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}SelectionListItem">
 *       &lt;sequence>
 *         &lt;element name="Costs" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="CurrencySymbol" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CurrencyCode_Iso_4217" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DefaultDispatchCosts" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="CarriageLimit" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ExpressCosts" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="IsExpressDelivery" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="DispatchConditions" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DispatchType", propOrder = {
    "costs",
    "currencySymbol",
    "currencyCodeIso4217",
    "defaultDispatchCosts",
    "carriageLimit",
    "expressCosts",
    "isExpressDelivery",
    "dispatchConditions"
})
public class DispatchType
    extends SelectionListItem
{

    @XmlElement(name = "Costs", required = true)
    protected BigDecimal costs;
    @XmlElement(name = "CurrencySymbol")
    protected String currencySymbol;
    @XmlElement(name = "CurrencyCode_Iso_4217")
    protected String currencyCodeIso4217;
    @XmlElement(name = "DefaultDispatchCosts", required = true, nillable = true)
    protected BigDecimal defaultDispatchCosts;
    @XmlElement(name = "CarriageLimit", required = true, nillable = true)
    protected BigDecimal carriageLimit;
    @XmlElement(name = "ExpressCosts", required = true, nillable = true)
    protected BigDecimal expressCosts;
    @XmlElement(name = "IsExpressDelivery")
    protected boolean isExpressDelivery;
    @XmlElement(name = "DispatchConditions")
    protected String dispatchConditions;

    /**
     * Gets the value of the costs property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCosts() {
        return costs;
    }

    /**
     * Sets the value of the costs property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCosts(BigDecimal value) {
        this.costs = value;
    }

    /**
     * Gets the value of the currencySymbol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencySymbol() {
        return currencySymbol;
    }

    /**
     * Sets the value of the currencySymbol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencySymbol(String value) {
        this.currencySymbol = value;
    }

    /**
     * Gets the value of the currencyCodeIso4217 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCodeIso4217() {
        return currencyCodeIso4217;
    }

    /**
     * Sets the value of the currencyCodeIso4217 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCodeIso4217(String value) {
        this.currencyCodeIso4217 = value;
    }

    /**
     * Gets the value of the defaultDispatchCosts property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDefaultDispatchCosts() {
        return defaultDispatchCosts;
    }

    /**
     * Sets the value of the defaultDispatchCosts property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDefaultDispatchCosts(BigDecimal value) {
        this.defaultDispatchCosts = value;
    }

    /**
     * Gets the value of the carriageLimit property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCarriageLimit() {
        return carriageLimit;
    }

    /**
     * Sets the value of the carriageLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCarriageLimit(BigDecimal value) {
        this.carriageLimit = value;
    }

    /**
     * Gets the value of the expressCosts property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getExpressCosts() {
        return expressCosts;
    }

    /**
     * Sets the value of the expressCosts property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setExpressCosts(BigDecimal value) {
        this.expressCosts = value;
    }

    /**
     * Gets the value of the isExpressDelivery property.
     * 
     */
    public boolean isIsExpressDelivery() {
        return isExpressDelivery;
    }

    /**
     * Sets the value of the isExpressDelivery property.
     * 
     */
    public void setIsExpressDelivery(boolean value) {
        this.isExpressDelivery = value;
    }

    /**
     * Gets the value of the dispatchConditions property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDispatchConditions() {
        return dispatchConditions;
    }

    /**
     * Sets the value of the dispatchConditions property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDispatchConditions(String value) {
        this.dispatchConditions = value;
    }

}
