
package com.sagag.services.stakis.erp.wsdl.cis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfErpCredential complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfErpCredential">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ErpCredential" type="{DVSE.WebApp.CISService}ErpCredential" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfErpCredential", propOrder = {
    "erpCredential"
})
public class ArrayOfErpCredential {

    @XmlElement(name = "ErpCredential", nillable = true)
    protected List<ErpCredential> erpCredential;

    /**
     * Gets the value of the erpCredential property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the erpCredential property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErpCredential().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ErpCredential }
     * 
     * 
     */
    public List<ErpCredential> getErpCredential() {
        if (erpCredential == null) {
            erpCredential = new ArrayList<ErpCredential>();
        }
        return this.erpCredential;
    }

}
