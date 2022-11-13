
package com.sagag.services.stakis.erp.wsdl.cis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfSaleSubType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfSaleSubType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SaleSubType" type="{DVSE.WebApp.CISService}SaleSubType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfSaleSubType", propOrder = {
    "saleSubType"
})
public class ArrayOfSaleSubType {

    @XmlElement(name = "SaleSubType", nillable = true)
    protected List<SaleSubType> saleSubType;

    /**
     * Gets the value of the saleSubType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the saleSubType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSaleSubType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SaleSubType }
     * 
     * 
     */
    public List<SaleSubType> getSaleSubType() {
        if (saleSubType == null) {
            saleSubType = new ArrayList<SaleSubType>();
        }
        return this.saleSubType;
    }

}
