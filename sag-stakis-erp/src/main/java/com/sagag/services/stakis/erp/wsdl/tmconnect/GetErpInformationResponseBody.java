
package com.sagag.services.stakis.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetErpInformationResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetErpInformationResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetErpInformationResult" type="{http://topmotive.eu/TMConnect}GetErpInformationReply" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetErpInformationResponseBody", propOrder = {
    "getErpInformationResult"
})
public class GetErpInformationResponseBody {

    @XmlElementRef(name = "GetErpInformationResult", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<GetErpInformationReply> getErpInformationResult;

    /**
     * Gets the value of the getErpInformationResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GetErpInformationReply }{@code >}
     *     
     */
    public JAXBElement<GetErpInformationReply> getGetErpInformationResult() {
        return getErpInformationResult;
    }

    /**
     * Sets the value of the getErpInformationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GetErpInformationReply }{@code >}
     *     
     */
    public void setGetErpInformationResult(JAXBElement<GetErpInformationReply> value) {
        this.getErpInformationResult = value;
    }

}
