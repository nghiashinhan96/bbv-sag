
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomerDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomerDetails">
 *   &lt;complexContent>
 *     &lt;extension base="{DVSE.WebApp.CISService}BaseCustomer">
 *       &lt;sequence>
 *         &lt;element name="ConceptCustomer" type="{DVSE.WebApp.CISService}ArrayOfConceptCustomer" minOccurs="0"/>
 *         &lt;element name="CustomerGroup" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Contacts" type="{DVSE.WebApp.CISService}ArrayOfContact" minOccurs="0"/>
 *         &lt;element name="Account" type="{DVSE.WebApp.CISService}Account" minOccurs="0"/>
 *         &lt;element name="TourInfo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DispatchTypeList" type="{DVSE.WebApp.CISService}DispatchTypeList" minOccurs="0"/>
 *         &lt;element name="CooperationGroup" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ErpCredentials" type="{DVSE.WebApp.CISService}ArrayOfErpCredential" minOccurs="0"/>
 *         &lt;element name="ForeignCustomer" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Notes" type="{DVSE.WebApp.CISService}ArrayOfNote" minOccurs="0"/>
 *         &lt;element name="SalesOutletList" type="{DVSE.WebApp.CISService}SalesOutletList" minOccurs="0"/>
 *         &lt;element name="CustomerItems" type="{DVSE.WebApp.CISService}ArrayOfCustomerItem" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomerDetails", propOrder = {
    "conceptCustomer",
    "customerGroup",
    "contacts",
    "account",
    "tourInfo",
    "dispatchTypeList",
    "cooperationGroup",
    "erpCredentials",
    "foreignCustomer",
    "notes",
    "salesOutletList",
    "customerItems"
})
public class CustomerDetails
    extends BaseCustomer
{

    @XmlElementRef(name = "ConceptCustomer", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfConceptCustomer> conceptCustomer;
    @XmlElementRef(name = "CustomerGroup", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> customerGroup;
    @XmlElementRef(name = "Contacts", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfContact> contacts;
    @XmlElementRef(name = "Account", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<Account> account;
    @XmlElementRef(name = "TourInfo", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> tourInfo;
    @XmlElementRef(name = "DispatchTypeList", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<DispatchTypeList> dispatchTypeList;
    @XmlElementRef(name = "CooperationGroup", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> cooperationGroup;
    @XmlElementRef(name = "ErpCredentials", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfErpCredential> erpCredentials;
    @XmlElement(name = "ForeignCustomer")
    protected boolean foreignCustomer;
    @XmlElementRef(name = "Notes", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfNote> notes;
    @XmlElementRef(name = "SalesOutletList", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<SalesOutletList> salesOutletList;
    @XmlElementRef(name = "CustomerItems", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfCustomerItem> customerItems;

    /**
     * Gets the value of the conceptCustomer property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfConceptCustomer }{@code >}
     *     
     */
    public JAXBElement<ArrayOfConceptCustomer> getConceptCustomer() {
        return conceptCustomer;
    }

    /**
     * Sets the value of the conceptCustomer property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfConceptCustomer }{@code >}
     *     
     */
    public void setConceptCustomer(JAXBElement<ArrayOfConceptCustomer> value) {
        this.conceptCustomer = value;
    }

    /**
     * Gets the value of the customerGroup property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustomerGroup() {
        return customerGroup;
    }

    /**
     * Sets the value of the customerGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustomerGroup(JAXBElement<String> value) {
        this.customerGroup = value;
    }

    /**
     * Gets the value of the contacts property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfContact }{@code >}
     *     
     */
    public JAXBElement<ArrayOfContact> getContacts() {
        return contacts;
    }

    /**
     * Sets the value of the contacts property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfContact }{@code >}
     *     
     */
    public void setContacts(JAXBElement<ArrayOfContact> value) {
        this.contacts = value;
    }

    /**
     * Gets the value of the account property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Account }{@code >}
     *     
     */
    public JAXBElement<Account> getAccount() {
        return account;
    }

    /**
     * Sets the value of the account property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Account }{@code >}
     *     
     */
    public void setAccount(JAXBElement<Account> value) {
        this.account = value;
    }

    /**
     * Gets the value of the tourInfo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTourInfo() {
        return tourInfo;
    }

    /**
     * Sets the value of the tourInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTourInfo(JAXBElement<String> value) {
        this.tourInfo = value;
    }

    /**
     * Gets the value of the dispatchTypeList property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DispatchTypeList }{@code >}
     *     
     */
    public JAXBElement<DispatchTypeList> getDispatchTypeList() {
        return dispatchTypeList;
    }

    /**
     * Sets the value of the dispatchTypeList property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DispatchTypeList }{@code >}
     *     
     */
    public void setDispatchTypeList(JAXBElement<DispatchTypeList> value) {
        this.dispatchTypeList = value;
    }

    /**
     * Gets the value of the cooperationGroup property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCooperationGroup() {
        return cooperationGroup;
    }

    /**
     * Sets the value of the cooperationGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCooperationGroup(JAXBElement<String> value) {
        this.cooperationGroup = value;
    }

    /**
     * Gets the value of the erpCredentials property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfErpCredential }{@code >}
     *     
     */
    public JAXBElement<ArrayOfErpCredential> getErpCredentials() {
        return erpCredentials;
    }

    /**
     * Sets the value of the erpCredentials property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfErpCredential }{@code >}
     *     
     */
    public void setErpCredentials(JAXBElement<ArrayOfErpCredential> value) {
        this.erpCredentials = value;
    }

    /**
     * Gets the value of the foreignCustomer property.
     * 
     */
    public boolean isForeignCustomer() {
        return foreignCustomer;
    }

    /**
     * Sets the value of the foreignCustomer property.
     * 
     */
    public void setForeignCustomer(boolean value) {
        this.foreignCustomer = value;
    }

    /**
     * Gets the value of the notes property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfNote }{@code >}
     *     
     */
    public JAXBElement<ArrayOfNote> getNotes() {
        return notes;
    }

    /**
     * Sets the value of the notes property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfNote }{@code >}
     *     
     */
    public void setNotes(JAXBElement<ArrayOfNote> value) {
        this.notes = value;
    }

    /**
     * Gets the value of the salesOutletList property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link SalesOutletList }{@code >}
     *     
     */
    public JAXBElement<SalesOutletList> getSalesOutletList() {
        return salesOutletList;
    }

    /**
     * Sets the value of the salesOutletList property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link SalesOutletList }{@code >}
     *     
     */
    public void setSalesOutletList(JAXBElement<SalesOutletList> value) {
        this.salesOutletList = value;
    }

    /**
     * Gets the value of the customerItems property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfCustomerItem }{@code >}
     *     
     */
    public JAXBElement<ArrayOfCustomerItem> getCustomerItems() {
        return customerItems;
    }

    /**
     * Sets the value of the customerItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfCustomerItem }{@code >}
     *     
     */
    public void setCustomerItems(JAXBElement<ArrayOfCustomerItem> value) {
        this.customerItems = value;
    }

}
