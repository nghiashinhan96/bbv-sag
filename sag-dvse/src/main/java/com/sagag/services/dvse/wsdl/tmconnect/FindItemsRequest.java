
package com.sagag.services.dvse.wsdl.tmconnect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FindItemsRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FindItemsRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}BaseRequest">
 *       &lt;sequence>
 *         &lt;element name="TypeId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SearchTerm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindItemsRequest", propOrder = {
    "typeId",
    "searchTerm"
})
public class FindItemsRequest
    extends BaseRequest
{

    @XmlElement(name = "TypeId")
    protected int typeId;
    @XmlElement(name = "SearchTerm")
    protected String searchTerm;

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
     * Gets the value of the searchTerm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSearchTerm() {
        return searchTerm;
    }

    /**
     * Sets the value of the searchTerm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSearchTerm(String value) {
        this.searchTerm = value;
    }

}
