
package com.sagag.services.stakis.erp.wsdl.cis;

import java.math.BigDecimal;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OutInvoices complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutInvoices">
 *   &lt;complexContent>
 *     &lt;extension base="{DVSE.WebApp.CISService}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="Invoices" type="{DVSE.WebApp.CISService}ArrayOfInvoice" minOccurs="0"/>
 *         &lt;element name="States" type="{DVSE.WebApp.CISService}ArrayOfState" minOccurs="0"/>
 *         &lt;element name="CurrentPage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TotalItems" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PageSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TotalInvoiced" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="TotalPaid" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="CurrencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutInvoices", propOrder = {
    "invoices",
    "states",
    "currentPage",
    "totalItems",
    "pageSize",
    "totalInvoiced",
    "totalPaid",
    "currencyCode"
})
public class OutInvoices
    extends BaseResponse
{

    @XmlElementRef(name = "Invoices", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfInvoice> invoices;
    @XmlElementRef(name = "States", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfState> states;
    @XmlElement(name = "CurrentPage")
    protected int currentPage;
    @XmlElement(name = "TotalItems")
    protected int totalItems;
    @XmlElement(name = "PageSize")
    protected int pageSize;
    @XmlElement(name = "TotalInvoiced", required = true)
    protected BigDecimal totalInvoiced;
    @XmlElement(name = "TotalPaid", required = true)
    protected BigDecimal totalPaid;
    @XmlElementRef(name = "CurrencyCode", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> currencyCode;

    /**
     * Gets the value of the invoices property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfInvoice }{@code >}
     *     
     */
    public JAXBElement<ArrayOfInvoice> getInvoices() {
        return invoices;
    }

    /**
     * Sets the value of the invoices property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfInvoice }{@code >}
     *     
     */
    public void setInvoices(JAXBElement<ArrayOfInvoice> value) {
        this.invoices = value;
    }

    /**
     * Gets the value of the states property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfState }{@code >}
     *     
     */
    public JAXBElement<ArrayOfState> getStates() {
        return states;
    }

    /**
     * Sets the value of the states property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfState }{@code >}
     *     
     */
    public void setStates(JAXBElement<ArrayOfState> value) {
        this.states = value;
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

    /**
     * Gets the value of the totalInvoiced property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalInvoiced() {
        return totalInvoiced;
    }

    /**
     * Sets the value of the totalInvoiced property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalInvoiced(BigDecimal value) {
        this.totalInvoiced = value;
    }

    /**
     * Gets the value of the totalPaid property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    /**
     * Sets the value of the totalPaid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalPaid(BigDecimal value) {
        this.totalPaid = value;
    }

    /**
     * Gets the value of the currencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCurrencyCode(JAXBElement<String> value) {
        this.currencyCode = value;
    }

}
