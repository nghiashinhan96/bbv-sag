//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2017.05.29 at 10:18:58 AM ICT
//


package com.sagag.services.dvse.wsdl.dvse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetBackOrder complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="GetBackOrder"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{DVSE}BaseGetBack"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Item" type="{DVSE}Order" minOccurs="0"/&gt;
 *         &lt;element name="OrderedItems" type="{DVSE}ArrayOfItem" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetBackOrder", propOrder = {
    "item",
    "orderedItems"
})
public class GetBackOrder extends BaseGetBack {

    @XmlElement(name = "Item")
    protected Order item;
    @XmlElement(name = "OrderedItems")
    protected ArrayOfItem orderedItems;

    /**
     * Gets the value of the item property.
     *
     * @return
     *     possible object is
     *     {@link Order }
     *
     */
    public Order getItem() {
        return item;
    }

    /**
     * Sets the value of the item property.
     *
     * @param value
     *     allowed object is
     *     {@link Order }
     *
     */
    public void setItem(Order value) {
        this.item = value;
    }

    /**
     * Gets the value of the orderedItems property.
     *
     * @return
     *     possible object is
     *     {@link ArrayOfItem }
     *
     */
    public ArrayOfItem getOrderedItems() {
        return orderedItems;
    }

    /**
     * Sets the value of the orderedItems property.
     *
     * @param value
     *     allowed object is
     *     {@link ArrayOfItem }
     *
     */
    public void setOrderedItems(ArrayOfItem value) {
        this.orderedItems = value;
    }

}
