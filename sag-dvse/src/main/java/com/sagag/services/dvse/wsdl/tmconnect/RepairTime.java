
package com.sagag.services.dvse.wsdl.tmconnect;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RepairTime complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RepairTime">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}BaseDto">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="CalculatedValue" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ContextId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ProviderId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TypeOfCostRate" type="{http://topmotive.eu/TMConnect}RepairTimesCostRate" minOccurs="0"/>
 *         &lt;element name="Price" type="{http://topmotive.eu/TMConnect}Price" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RepairTime", propOrder = {
    "id",
    "description",
    "value",
    "calculatedValue",
    "contextId",
    "providerId",
    "typeOfCostRate",
    "price"
})
public class RepairTime
    extends BaseDto
{

    @XmlElement(name = "Id")
    protected String id;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "Value", required = true)
    protected BigDecimal value;
    @XmlElement(name = "CalculatedValue", required = true)
    protected BigDecimal calculatedValue;
    @XmlElement(name = "ContextId")
    protected int contextId;
    @XmlElement(name = "ProviderId")
    protected int providerId;
    @XmlElement(name = "TypeOfCostRate")
    protected RepairTimesCostRate typeOfCostRate;
    @XmlElement(name = "Price")
    protected Price price;

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
     * Gets the value of the calculatedValue property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCalculatedValue() {
        return calculatedValue;
    }

    /**
     * Sets the value of the calculatedValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCalculatedValue(BigDecimal value) {
        this.calculatedValue = value;
    }

    /**
     * Gets the value of the contextId property.
     * 
     */
    public int getContextId() {
        return contextId;
    }

    /**
     * Sets the value of the contextId property.
     * 
     */
    public void setContextId(int value) {
        this.contextId = value;
    }

    /**
     * Gets the value of the providerId property.
     * 
     */
    public int getProviderId() {
        return providerId;
    }

    /**
     * Sets the value of the providerId property.
     * 
     */
    public void setProviderId(int value) {
        this.providerId = value;
    }

    /**
     * Gets the value of the typeOfCostRate property.
     * 
     * @return
     *     possible object is
     *     {@link RepairTimesCostRate }
     *     
     */
    public RepairTimesCostRate getTypeOfCostRate() {
        return typeOfCostRate;
    }

    /**
     * Sets the value of the typeOfCostRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link RepairTimesCostRate }
     *     
     */
    public void setTypeOfCostRate(RepairTimesCostRate value) {
        this.typeOfCostRate = value;
    }

    /**
     * Gets the value of the price property.
     * 
     * @return
     *     possible object is
     *     {@link Price }
     *     
     */
    public Price getPrice() {
        return price;
    }

    /**
     * Sets the value of the price property.
     * 
     * @param value
     *     allowed object is
     *     {@link Price }
     *     
     */
    public void setPrice(Price value) {
        this.price = value;
    }

}
