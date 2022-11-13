
package com.sagag.services.dvse.wsdl.tmconnect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
public class LinkedItemsCollection
    extends BaseDto
{

    @XmlElement(name = "Role")
    protected int role;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "Memos")
    protected ArrayOfMemo memos;
    @XmlElement(name = "LinkedItems")
    protected ArrayOfErpInformation linkedItems;
    @XmlElement(name = "ControlIndicator")
    protected ControlIndicator controlIndicator;

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
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the memos property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfMemo }
     *     
     */
    public ArrayOfMemo getMemos() {
        return memos;
    }

    /**
     * Sets the value of the memos property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfMemo }
     *     
     */
    public void setMemos(ArrayOfMemo value) {
        this.memos = value;
    }

    /**
     * Gets the value of the linkedItems property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfErpInformation }
     *     
     */
    public ArrayOfErpInformation getLinkedItems() {
        return linkedItems;
    }

    /**
     * Sets the value of the linkedItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfErpInformation }
     *     
     */
    public void setLinkedItems(ArrayOfErpInformation value) {
        this.linkedItems = value;
    }

    /**
     * Gets the value of the controlIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link ControlIndicator }
     *     
     */
    public ControlIndicator getControlIndicator() {
        return controlIndicator;
    }

    /**
     * Sets the value of the controlIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link ControlIndicator }
     *     
     */
    public void setControlIndicator(ControlIndicator value) {
        this.controlIndicator = value;
    }

}
