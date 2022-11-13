
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetCustomerResponseBody complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="GetCustomerResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetCustomerResult" type="{DVSE.WebApp.CISService}OutCustomer" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetCustomerResponseBody", propOrder = {
    "getCustomerResult"
})
@XmlRootElement
public class GetCustomerResponseBody {

    @XmlElementRef(name = "GetCustomerResult", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<OutCustomer> getCustomerResult;

    /**
     * Gets the value of the getCustomerResult property.
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link OutCustomer }{@code >}
     *
     */
    public JAXBElement<OutCustomer> getGetCustomerResult() {
        return getCustomerResult;
    }

    /**
     * Sets the value of the getCustomerResult property.
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link OutCustomer }{@code >}
     *
     */
    public void setGetCustomerResult(JAXBElement<OutCustomer> value) {
        this.getCustomerResult = value;
    }

}
