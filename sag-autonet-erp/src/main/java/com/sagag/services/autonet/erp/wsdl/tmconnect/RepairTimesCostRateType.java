
package com.sagag.services.autonet.erp.wsdl.tmconnect;

import java.math.BigDecimal;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RepairTimesCostRate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RepairTimesCostRate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Division" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="TypeOfRate" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TypeOfWork" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PricePerUnit" type="{http://topmotive.eu/TMConnect}Price" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RepairTimesCostRate", propOrder = {
    "division",
    "typeOfRate",
    "typeOfWork",
    "pricePerUnit"
})
public class RepairTimesCostRateType {

    @XmlElement(name = "Division", required = true)
    protected BigDecimal division;
    @XmlElement(name = "TypeOfRate")
    protected int typeOfRate;
    @XmlElement(name = "TypeOfWork")
    protected int typeOfWork;
    @XmlElementRef(name = "PricePerUnit", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<PriceType> pricePerUnit;

    /**
     * Gets the value of the division property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDivision() {
        return division;
    }

    /**
     * Sets the value of the division property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDivision(BigDecimal value) {
        this.division = value;
    }

    /**
     * Gets the value of the typeOfRate property.
     * 
     */
    public int getTypeOfRate() {
        return typeOfRate;
    }

    /**
     * Sets the value of the typeOfRate property.
     * 
     */
    public void setTypeOfRate(int value) {
        this.typeOfRate = value;
    }

    /**
     * Gets the value of the typeOfWork property.
     * 
     */
    public int getTypeOfWork() {
        return typeOfWork;
    }

    /**
     * Sets the value of the typeOfWork property.
     * 
     */
    public void setTypeOfWork(int value) {
        this.typeOfWork = value;
    }

    /**
     * Gets the value of the pricePerUnit property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PriceType }{@code >}
     *     
     */
    public JAXBElement<PriceType> getPricePerUnit() {
        return pricePerUnit;
    }

    /**
     * Sets the value of the pricePerUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PriceType }{@code >}
     *     
     */
    public void setPricePerUnit(JAXBElement<PriceType> value) {
        this.pricePerUnit = value;
    }

}
