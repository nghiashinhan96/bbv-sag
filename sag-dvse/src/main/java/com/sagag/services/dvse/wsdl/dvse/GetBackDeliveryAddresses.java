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
 * <p>Java class for GetBackDeliveryAddresses complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="GetBackDeliveryAddresses"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{DVSE}BaseGetBack"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Items" type="{DVSE}ArrayOfAddress" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetBackDeliveryAddresses", propOrder = {
    "items"
})
public class GetBackDeliveryAddresses extends BaseGetBack {

    @XmlElement(name = "Items")
    protected ArrayOfAddress items;

    /**
     * Gets the value of the items property.
     *
     * @return
     *     possible object is
     *     {@link ArrayOfAddress }
     *
     */
    public ArrayOfAddress getItems() {
        return items;
    }

    /**
     * Sets the value of the items property.
     *
     * @param value
     *     allowed object is
     *     {@link ArrayOfAddress }
     *
     */
    public void setItems(ArrayOfAddress value) {
        this.items = value;
    }

}
