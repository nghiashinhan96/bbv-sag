
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetQueryTypesCustomerResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetQueryTypesCustomerResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetQueryTypesCustomerResult" type="{DVSE.WebApp.CISService}OutQueryTypes" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetQueryTypesCustomerResponseBody", propOrder = {
    "getQueryTypesCustomerResult"
})
public class GetQueryTypesCustomerResponseBody {

    @XmlElementRef(name = "GetQueryTypesCustomerResult", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<OutQueryTypes> getQueryTypesCustomerResult;

    /**
     * Gets the value of the getQueryTypesCustomerResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link OutQueryTypes }{@code >}
     *     
     */
    public JAXBElement<OutQueryTypes> getGetQueryTypesCustomerResult() {
        return getQueryTypesCustomerResult;
    }

    /**
     * Sets the value of the getQueryTypesCustomerResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link OutQueryTypes }{@code >}
     *     
     */
    public void setGetQueryTypesCustomerResult(JAXBElement<OutQueryTypes> value) {
        this.getQueryTypesCustomerResult = value;
    }

}
