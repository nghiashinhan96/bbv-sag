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
 * <p>Java class for GetBackWarehouses complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="GetBackWarehouses"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{DVSE}BaseGetBack"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Items" type="{DVSE}ArrayOfWarehouse" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetBackWarehouses", propOrder = {
    "items"
})
public class GetBackWarehouses extends BaseGetBack {

    @XmlElement(name = "Items")
    protected ArrayOfWarehouse items;

    /**
     * Gets the value of the items property.
     *
     * @return
     *     possible object is
     *     {@link ArrayOfWarehouse }
     *
     */
    public ArrayOfWarehouse getItems() {
        return items;
    }

    /**
     * Sets the value of the items property.
     *
     * @param value
     *     allowed object is
     *     {@link ArrayOfWarehouse }
     *
     */
    public void setItems(ArrayOfWarehouse value) {
        this.items = value;
    }

}
