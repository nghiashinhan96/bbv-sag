
package com.sagag.services.dvse.wsdl.tmconnect;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfRepairTime complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRepairTime">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RepairTime" type="{http://topmotive.eu/TMConnect}RepairTime" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfRepairTime", propOrder = {
    "repairTime"
})
public class ArrayOfRepairTime {

    @XmlElement(name = "RepairTime", nillable = true)
    protected List<RepairTime> repairTime;

    /**
     * Gets the value of the repairTime property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the repairTime property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRepairTime().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RepairTime }
     * 
     * 
     */
    public List<RepairTime> getRepairTime() {
        if (repairTime == null) {
            repairTime = new ArrayList<RepairTime>();
        }
        return this.repairTime;
    }

}
