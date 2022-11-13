
package com.sagag.services.dvse.wsdl.tmconnect;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfDispatchType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfDispatchType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DispatchType" type="{http://topmotive.eu/TMConnect}DispatchType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfDispatchType", propOrder = {
    "dispatchType"
})
public class ArrayOfDispatchType {

    @XmlElement(name = "DispatchType", nillable = true)
    protected List<DispatchType> dispatchType;

    /**
     * Gets the value of the dispatchType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dispatchType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDispatchType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DispatchType }
     * 
     * 
     */
    public List<DispatchType> getDispatchType() {
        if (dispatchType == null) {
            dispatchType = new ArrayList<DispatchType>();
        }
        return this.dispatchType;
    }

}
