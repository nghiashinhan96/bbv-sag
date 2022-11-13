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
 * <p>Java class for ArticleAction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArticleAction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PicUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LinkUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ArticleActions" type="{DVSE}ArrayOfArticleAction" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArticleAction", propOrder = {
    "picUrl",
    "linkUrl",
    "description",
    "articleActions"
})
public class ArticleAction {

    @XmlElement(name = "PicUrl")
    protected String picUrl;
    @XmlElement(name = "LinkUrl")
    protected String linkUrl;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "ArticleActions")
    protected ArrayOfArticleAction articleActions;

    /**
     * Gets the value of the picUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPicUrl() {
        return picUrl;
    }

    /**
     * Sets the value of the picUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPicUrl(String value) {
        this.picUrl = value;
    }

    /**
     * Gets the value of the linkUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkUrl() {
        return linkUrl;
    }

    /**
     * Sets the value of the linkUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkUrl(String value) {
        this.linkUrl = value;
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
     * Gets the value of the articleActions property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfArticleAction }
     *     
     */
    public ArrayOfArticleAction getArticleActions() {
        return articleActions;
    }

    /**
     * Sets the value of the articleActions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfArticleAction }
     *     
     */
    public void setArticleActions(ArrayOfArticleAction value) {
        this.articleActions = value;
    }

}
