
package com.sagag.services.dvse.wsdl.tmconnect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArticleTmf complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArticleTmf">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}Article">
 *       &lt;sequence>
 *         &lt;element name="ArticleIdSupplier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ProductGroups" type="{http://topmotive.eu/TMConnect}ArrayOfProductGroupTmf" minOccurs="0"/>
 *         &lt;element name="Supplier" type="{http://topmotive.eu/TMConnect}SupplierTmf" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArticleTmf", propOrder = {
    "articleIdSupplier",
    "productGroups",
    "supplier"
})
public class ArticleTmf
    extends Article
{

    @XmlElement(name = "ArticleIdSupplier")
    protected String articleIdSupplier;
    @XmlElement(name = "ProductGroups")
    protected ArrayOfProductGroupTmf productGroups;
    @XmlElement(name = "Supplier")
    protected SupplierTmf supplier;

    /**
     * Gets the value of the articleIdSupplier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArticleIdSupplier() {
        return articleIdSupplier;
    }

    /**
     * Sets the value of the articleIdSupplier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArticleIdSupplier(String value) {
        this.articleIdSupplier = value;
    }

    /**
     * Gets the value of the productGroups property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfProductGroupTmf }
     *     
     */
    public ArrayOfProductGroupTmf getProductGroups() {
        return productGroups;
    }

    /**
     * Sets the value of the productGroups property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfProductGroupTmf }
     *     
     */
    public void setProductGroups(ArrayOfProductGroupTmf value) {
        this.productGroups = value;
    }

    /**
     * Gets the value of the supplier property.
     * 
     * @return
     *     possible object is
     *     {@link SupplierTmf }
     *     
     */
    public SupplierTmf getSupplier() {
        return supplier;
    }

    /**
     * Sets the value of the supplier property.
     * 
     * @param value
     *     allowed object is
     *     {@link SupplierTmf }
     *     
     */
    public void setSupplier(SupplierTmf value) {
        this.supplier = value;
    }

}
