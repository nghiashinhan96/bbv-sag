
package com.sagag.services.dvse.wsdl.tmconnect;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AccountData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccountData">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}BaseDto">
 *       &lt;sequence>
 *         &lt;element name="CreditLimit" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="UsedCreditLimit" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ShortDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Memos" type="{http://topmotive.eu/TMConnect}ArrayOfMemo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountData", propOrder = {
    "creditLimit",
    "usedCreditLimit",
    "shortDescription",
    "description",
    "memos"
})
public class AccountData
    extends BaseDto
{

    @XmlElement(name = "CreditLimit", required = true, nillable = true)
    protected BigDecimal creditLimit;
    @XmlElement(name = "UsedCreditLimit", required = true, nillable = true)
    protected BigDecimal usedCreditLimit;
    @XmlElement(name = "ShortDescription")
    protected String shortDescription;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "Memos")
    protected ArrayOfMemo memos;

    /**
     * Gets the value of the creditLimit property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    /**
     * Sets the value of the creditLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCreditLimit(BigDecimal value) {
        this.creditLimit = value;
    }

    /**
     * Gets the value of the usedCreditLimit property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUsedCreditLimit() {
        return usedCreditLimit;
    }

    /**
     * Sets the value of the usedCreditLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUsedCreditLimit(BigDecimal value) {
        this.usedCreditLimit = value;
    }

    /**
     * Gets the value of the shortDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * Sets the value of the shortDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortDescription(String value) {
        this.shortDescription = value;
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
     * Gets the value of the memos property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfMemo }
     *     
     */
    public ArrayOfMemo getMemos() {
        return memos;
    }

    /**
     * Sets the value of the memos property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfMemo }
     *     
     */
    public void setMemos(ArrayOfMemo value) {
        this.memos = value;
    }

}
