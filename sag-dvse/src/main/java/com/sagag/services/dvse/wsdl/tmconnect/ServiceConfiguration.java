
package com.sagag.services.dvse.wsdl.tmconnect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ServiceConfiguration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ServiceConfiguration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UpperLimitRequestItems" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ControlIndicators" type="{http://topmotive.eu/TMConnect}ArrayOfControlIndicator" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceConfiguration", propOrder = {
    "upperLimitRequestItems",
    "controlIndicators"
})
public class ServiceConfiguration {

    @XmlElement(name = "UpperLimitRequestItems", required = true, type = Integer.class, nillable = true)
    protected Integer upperLimitRequestItems;
    @XmlElement(name = "ControlIndicators")
    protected ArrayOfControlIndicator controlIndicators;

    /**
     * Gets the value of the upperLimitRequestItems property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUpperLimitRequestItems() {
        return upperLimitRequestItems;
    }

    /**
     * Sets the value of the upperLimitRequestItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUpperLimitRequestItems(Integer value) {
        this.upperLimitRequestItems = value;
    }

    /**
     * Gets the value of the controlIndicators property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfControlIndicator }
     *     
     */
    public ArrayOfControlIndicator getControlIndicators() {
        return controlIndicators;
    }

    /**
     * Sets the value of the controlIndicators property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfControlIndicator }
     *     
     */
    public void setControlIndicators(ArrayOfControlIndicator value) {
        this.controlIndicators = value;
    }

}
