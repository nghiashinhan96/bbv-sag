
package com.sagag.services.stakis.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BaseDto complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseDto">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Guid" type="{http://schemas.microsoft.com/2003/10/Serialization/}guid"/>
 *         &lt;element name="AdditionalIdentifiers" type="{http://topmotive.eu/TMConnect}ArrayOfAdditionalIdentifier" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseDto", propOrder = {
    "guid",
    "additionalIdentifiers"
})
@XmlSeeAlso({
    Address.class,
    Customer.class,
    LinkedItemsCollection.class,
    Notification.class,
    AccountData.class,
    AvailabilityState.class,
    Tour.class,
    ErpInformation.class,
    ItemsCollection.class,
    Memo.class,
    Vehicle.class,
    Order.class,
    SelectionListItem.class,
    OrderPosition.class,
    SelectionList.class,
    Icon.class,
    TextBlock.class,
    Warehouse.class,
    OrderCollection.class,
    Article.class,
    RepairTime.class
})
public class BaseDto {

    @XmlElement(name = "Guid", required = true)
    protected String guid;
    @XmlElementRef(name = "AdditionalIdentifiers", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfAdditionalIdentifier> additionalIdentifiers;

    /**
     * Gets the value of the guid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuid() {
        return guid;
    }

    /**
     * Sets the value of the guid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuid(String value) {
        this.guid = value;
    }

    /**
     * Gets the value of the additionalIdentifiers property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfAdditionalIdentifier }{@code >}
     *     
     */
    public JAXBElement<ArrayOfAdditionalIdentifier> getAdditionalIdentifiers() {
        return additionalIdentifiers;
    }

    /**
     * Sets the value of the additionalIdentifiers property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfAdditionalIdentifier }{@code >}
     *     
     */
    public void setAdditionalIdentifiers(JAXBElement<ArrayOfAdditionalIdentifier> value) {
        this.additionalIdentifiers = value;
    }

}
