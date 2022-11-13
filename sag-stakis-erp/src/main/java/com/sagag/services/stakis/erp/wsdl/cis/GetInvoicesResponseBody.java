
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetInvoicesResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetInvoicesResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetInvoicesResult" type="{DVSE.WebApp.CISService}OutInvoices" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetInvoicesResponseBody", propOrder = {
    "getInvoicesResult"
})
public class GetInvoicesResponseBody {

    @XmlElementRef(name = "GetInvoicesResult", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<OutInvoices> getInvoicesResult;

    /**
     * Gets the value of the getInvoicesResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link OutInvoices }{@code >}
     *     
     */
    public JAXBElement<OutInvoices> getGetInvoicesResult() {
        return getInvoicesResult;
    }

    /**
     * Sets the value of the getInvoicesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link OutInvoices }{@code >}
     *     
     */
    public void setGetInvoicesResult(JAXBElement<OutInvoices> value) {
        this.getInvoicesResult = value;
    }

}
