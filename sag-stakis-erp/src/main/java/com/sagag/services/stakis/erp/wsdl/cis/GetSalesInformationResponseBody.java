
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetSalesInformationResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetSalesInformationResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetSalesInformationResult" type="{DVSE.WebApp.CISService}OutSalesInformation" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetSalesInformationResponseBody", propOrder = {
    "getSalesInformationResult"
})
public class GetSalesInformationResponseBody {

    @XmlElementRef(name = "GetSalesInformationResult", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<OutSalesInformation> getSalesInformationResult;

    /**
     * Gets the value of the getSalesInformationResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link OutSalesInformation }{@code >}
     *     
     */
    public JAXBElement<OutSalesInformation> getGetSalesInformationResult() {
        return getSalesInformationResult;
    }

    /**
     * Sets the value of the getSalesInformationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link OutSalesInformation }{@code >}
     *     
     */
    public void setGetSalesInformationResult(JAXBElement<OutSalesInformation> value) {
        this.getSalesInformationResult = value;
    }

}
