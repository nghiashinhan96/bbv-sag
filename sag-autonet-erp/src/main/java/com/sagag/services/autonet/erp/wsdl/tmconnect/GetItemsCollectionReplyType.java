
package com.sagag.services.autonet.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetItemsCollectionReply complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetItemsCollectionReply">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="TypeId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Items" type="{http://topmotive.eu/TMConnect}ArrayOfItemsCollection" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetItemsCollectionReply", propOrder = {
    "typeId",
    "items"
})
public class GetItemsCollectionReplyType
    extends BaseResponseType
{

    @XmlElement(name = "TypeId")
    protected int typeId;
    @XmlElementRef(name = "Items", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfItemsCollectionType> items;

    /**
     * Gets the value of the typeId property.
     * 
     */
    public int getTypeId() {
        return typeId;
    }

    /**
     * Sets the value of the typeId property.
     * 
     */
    public void setTypeId(int value) {
        this.typeId = value;
    }

    /**
     * Gets the value of the items property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfItemsCollectionType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfItemsCollectionType> getItems() {
        return items;
    }

    /**
     * Sets the value of the items property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfItemsCollectionType }{@code >}
     *     
     */
    public void setItems(JAXBElement<ArrayOfItemsCollectionType> value) {
        this.items = value;
    }

}
