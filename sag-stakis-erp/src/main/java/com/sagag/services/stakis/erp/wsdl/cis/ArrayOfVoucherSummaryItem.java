
package com.sagag.services.stakis.erp.wsdl.cis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfVoucherSummaryItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfVoucherSummaryItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VoucherSummaryItem" type="{DVSE.WebApp.CISService}VoucherSummaryItem" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfVoucherSummaryItem", propOrder = {
    "voucherSummaryItem"
})
public class ArrayOfVoucherSummaryItem {

    @XmlElement(name = "VoucherSummaryItem", nillable = true)
    protected List<VoucherSummaryItem> voucherSummaryItem;

    /**
     * Gets the value of the voucherSummaryItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the voucherSummaryItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVoucherSummaryItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VoucherSummaryItem }
     * 
     * 
     */
    public List<VoucherSummaryItem> getVoucherSummaryItem() {
        if (voucherSummaryItem == null) {
            voucherSummaryItem = new ArrayList<VoucherSummaryItem>();
        }
        return this.voucherSummaryItem;
    }

}
