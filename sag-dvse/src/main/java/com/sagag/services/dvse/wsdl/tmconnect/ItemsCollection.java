
package com.sagag.services.dvse.wsdl.tmconnect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ItemsCollection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ItemsCollection">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}BaseDto">
 *       &lt;sequence>
 *         &lt;element name="ArticleIdErp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LinkedItems" type="{http://topmotive.eu/TMConnect}ArrayOfEntityLink" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItemsCollection", propOrder = {
    "articleIdErp",
    "description",
    "linkedItems"
})
public class ItemsCollection
    extends BaseDto
{

    @XmlElement(name = "ArticleIdErp")
    protected String articleIdErp;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "LinkedItems")
    protected ArrayOfEntityLink linkedItems;

    /**
     * Gets the value of the articleIdErp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArticleIdErp() {
        return articleIdErp;
    }

    /**
     * Sets the value of the articleIdErp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArticleIdErp(String value) {
        this.articleIdErp = value;
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
     * Gets the value of the linkedItems property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfEntityLink }
     *     
     */
    public ArrayOfEntityLink getLinkedItems() {
        return linkedItems;
    }

    /**
     * Sets the value of the linkedItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfEntityLink }
     *     
     */
    public void setLinkedItems(ArrayOfEntityLink value) {
        this.linkedItems = value;
    }

}
