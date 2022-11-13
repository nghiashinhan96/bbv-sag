
package com.sagag.services.dvse.wsdl.tmconnect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="Guid" type="{http://microsoft.com/wsdl/types/}guid"/>
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
    Vehicle.class,
    Order.class,
    LinkedItemsCollection.class,
    SelectionListItem.class,
    OrderPosition.class,
    SelectionList.class,
    Icon.class,
    TextBlock.class,
    Warehouse.class,
    Notification.class,
    AccountData.class,
    AvailabilityState.class,
    Tour.class,
    Article.class,
    ErpInformation.class,
    OrderCollection.class,
    ItemsCollection.class,
    RepairTime.class,
    Memo.class
})
public class BaseDto {

    @XmlElement(name = "Guid", required = true)
    protected String guid;
    @XmlElement(name = "AdditionalIdentifiers")
    protected ArrayOfAdditionalIdentifier additionalIdentifiers;

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
     *     {@link ArrayOfAdditionalIdentifier }
     *     
     */
    public ArrayOfAdditionalIdentifier getAdditionalIdentifiers() {
        return additionalIdentifiers;
    }

    /**
     * Sets the value of the additionalIdentifiers property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAdditionalIdentifier }
     *     
     */
    public void setAdditionalIdentifiers(ArrayOfAdditionalIdentifier value) {
        this.additionalIdentifiers = value;
    }

}
