
package com.sagag.services.autonet.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LinkedItemsCollection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LinkedItemsCollection">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}BaseDto">
 *       &lt;sequence>
 *         &lt;element name="Role" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Memos" type="{http://topmotive.eu/TMConnect}ArrayOfMemo" minOccurs="0"/>
 *         &lt;element name="LinkedItems" type="{http://topmotive.eu/TMConnect}ArrayOfErpInformation" minOccurs="0"/>
 *         &lt;element name="ControlIndicator" type="{http://topmotive.eu/TMConnect}ControlIndicator" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LinkedItemsCollection", propOrder = {
    "role",
    "description",
    "memos",
    "linkedItems",
    "controlIndicator"
})
public class LinkedItemsCollectionType
    extends BaseDtoType
{

    @XmlElement(name = "Role")
    protected int role;
    @XmlElementRef(name = "Description", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> description;
    @XmlElementRef(name = "Memos", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfMemoType> memos;
    @XmlElementRef(name = "LinkedItems", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfErpInformationType> linkedItems;
    @XmlElementRef(name = "ControlIndicator", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ControlIndicatorType> controlIndicator;

    /**
     * Gets the value of the role property.
     * 
     */
    public int getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     * 
     */
    public void setRole(int value) {
        this.role = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescription(JAXBElement<String> value) {
        this.description = value;
    }

    /**
     * Gets the value of the memos property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfMemoType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfMemoType> getMemos() {
        return memos;
    }

    /**
     * Sets the value of the memos property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfMemoType }{@code >}
     *     
     */
    public void setMemos(JAXBElement<ArrayOfMemoType> value) {
        this.memos = value;
    }

    /**
     * Gets the value of the linkedItems property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfErpInformationType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfErpInformationType> getLinkedItems() {
        return linkedItems;
    }

    /**
     * Sets the value of the linkedItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfErpInformationType }{@code >}
     *     
     */
    public void setLinkedItems(JAXBElement<ArrayOfErpInformationType> value) {
        this.linkedItems = value;
    }

    /**
     * Gets the value of the controlIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ControlIndicatorType }{@code >}
     *     
     */
    public JAXBElement<ControlIndicatorType> getControlIndicator() {
        return controlIndicator;
    }

    /**
     * Sets the value of the controlIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ControlIndicatorType }{@code >}
     *     
     */
    public void setControlIndicator(JAXBElement<ControlIndicatorType> value) {
        this.controlIndicator = value;
    }

}
