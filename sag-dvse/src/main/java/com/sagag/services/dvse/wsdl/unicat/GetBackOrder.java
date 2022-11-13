package com.sagag.services.dvse.wsdl.unicat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetBackOrder", propOrder = {
    "item",
    "orderedItems"
})
public class GetBackOrder {

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
