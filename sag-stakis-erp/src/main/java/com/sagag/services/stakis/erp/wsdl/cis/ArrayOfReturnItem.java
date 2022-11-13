
package com.sagag.services.stakis.erp.wsdl.cis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfReturnItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfReturnItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ReturnItem" type="{DVSE.WebApp.CISService}ReturnItem" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfReturnItem", propOrder = {
    "returnItem"
})
public class ArrayOfReturnItem {

    @XmlElement(name = "ReturnItem", nillable = true)
    protected List<ReturnItem> returnItem;

    /**
     * Gets the value of the returnItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the returnItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReturnItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReturnItem }
     * 
     * 
     */
    public List<ReturnItem> getReturnItem() {
        if (returnItem == null) {
            returnItem = new ArrayList<ReturnItem>();
        }
        return this.returnItem;
    }

}
