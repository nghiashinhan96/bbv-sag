
package com.sagag.services.autonet.erp.wsdl.tmconnect;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfLinkedItemsCollection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfLinkedItemsCollection">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LinkedItemsCollection" type="{http://topmotive.eu/TMConnect}LinkedItemsCollection" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfLinkedItemsCollection", propOrder = {
    "linkedItemsCollection"
})
public class ArrayOfLinkedItemsCollectionType {

    @XmlElement(name = "LinkedItemsCollection", nillable = true)
    protected List<LinkedItemsCollectionType> linkedItemsCollection;

    /**
     * Gets the value of the linkedItemsCollection property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the linkedItemsCollection property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLinkedItemsCollection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LinkedItemsCollectionType }
     * 
     * 
     */
    public List<LinkedItemsCollectionType> getLinkedItemsCollection() {
        if (linkedItemsCollection == null) {
            linkedItemsCollection = new ArrayList<LinkedItemsCollectionType>();
        }
        return this.linkedItemsCollection;
    }

}
