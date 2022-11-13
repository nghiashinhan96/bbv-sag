
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OutVoucherDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutVoucherDetails">
 *   &lt;complexContent>
 *     &lt;extension base="{DVSE.WebApp.CISService}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="VoucherItems" type="{DVSE.WebApp.CISService}ArrayOfVoucherItem" minOccurs="0"/>
 *         &lt;element name="CurrentPage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TotalItems" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PageSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutVoucherDetails", propOrder = {
    "voucherItems",
    "currentPage",
    "totalItems",
    "pageSize"
})
public class OutVoucherDetails
    extends BaseResponse
{

    @XmlElementRef(name = "VoucherItems", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfVoucherItem> voucherItems;
    @XmlElement(name = "CurrentPage")
    protected int currentPage;
    @XmlElement(name = "TotalItems")
    protected int totalItems;
    @XmlElement(name = "PageSize")
    protected int pageSize;

    /**
     * Gets the value of the voucherItems property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfVoucherItem }{@code >}
     *     
     */
    public JAXBElement<ArrayOfVoucherItem> getVoucherItems() {
        return voucherItems;
    }

    /**
     * Sets the value of the voucherItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfVoucherItem }{@code >}
     *     
     */
    public void setVoucherItems(JAXBElement<ArrayOfVoucherItem> value) {
        this.voucherItems = value;
    }

    /**
     * Gets the value of the currentPage property.
     * 
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Sets the value of the currentPage property.
     * 
     */
    public void setCurrentPage(int value) {
        this.currentPage = value;
    }

    /**
     * Gets the value of the totalItems property.
     * 
     */
    public int getTotalItems() {
        return totalItems;
    }

    /**
     * Sets the value of the totalItems property.
     * 
     */
    public void setTotalItems(int value) {
        this.totalItems = value;
    }

    /**
     * Gets the value of the pageSize property.
     * 
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Sets the value of the pageSize property.
     * 
     */
    public void setPageSize(int value) {
        this.pageSize = value;
    }

}
