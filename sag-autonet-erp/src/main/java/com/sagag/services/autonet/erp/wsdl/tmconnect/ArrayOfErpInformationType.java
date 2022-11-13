
package com.sagag.services.autonet.erp.wsdl.tmconnect;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfErpInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfErpInformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ErpInformation" type="{http://topmotive.eu/TMConnect}ErpInformation" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfErpInformation", propOrder = {
    "erpInformation"
})
public class ArrayOfErpInformationType {

    @XmlElement(name = "ErpInformation", nillable = true)
    protected List<ErpInformationType> erpInformation;

    /**
     * Gets the value of the erpInformation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the erpInformation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErpInformation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ErpInformationType }
     * 
     * 
     */
    public List<ErpInformationType> getErpInformation() {
        if (erpInformation == null) {
            erpInformation = new ArrayList<ErpInformationType>();
        }
        return this.erpInformation;
    }

}
