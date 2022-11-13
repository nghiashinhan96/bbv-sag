
package com.sagag.services.dvse.wsdl.tmconnect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SelectionListItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SelectionListItem">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}BaseDto">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Text" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SortId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Style" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="IsSelected" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Memos" type="{http://topmotive.eu/TMConnect}ArrayOfMemo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SelectionListItem", propOrder = {
    "id",
    "text",
    "sortId",
    "style",
    "isSelected",
    "memos"
})
@XmlSeeAlso({
    DispatchType.class
})
public class SelectionListItem
    extends BaseDto
{

    @XmlElement(name = "Id")
    protected int id;
    @XmlElement(name = "Text")
    protected String text;
    @XmlElement(name = "SortId")
    protected int sortId;
    @XmlElement(name = "Style")
    protected int style;
    @XmlElement(name = "IsSelected", required = true, type = Boolean.class, nillable = true)
    protected Boolean isSelected;
    @XmlElement(name = "Memos")
    protected ArrayOfMemo memos;

    /**
     * Gets the value of the id property.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * Gets the value of the sortId property.
     * 
     */
    public int getSortId() {
        return sortId;
    }

    /**
     * Sets the value of the sortId property.
     * 
     */
    public void setSortId(int value) {
        this.sortId = value;
    }

    /**
     * Gets the value of the style property.
     * 
     */
    public int getStyle() {
        return style;
    }

    /**
     * Sets the value of the style property.
     * 
     */
    public void setStyle(int value) {
        this.style = value;
    }

    /**
     * Gets the value of the isSelected property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsSelected() {
        return isSelected;
    }

    /**
     * Sets the value of the isSelected property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsSelected(Boolean value) {
        this.isSelected = value;
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

}
