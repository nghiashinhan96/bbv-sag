
package com.sagag.services.stakis.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetAccountDataResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetAccountDataResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetAccountDataResult" type="{http://topmotive.eu/TMConnect}GetAccountInformationReply" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetAccountDataResponseBody", propOrder = {
    "getAccountDataResult"
})
public class GetAccountDataResponseBody {

    @XmlElementRef(name = "GetAccountDataResult", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<GetAccountInformationReply> getAccountDataResult;

    /**
     * Gets the value of the getAccountDataResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GetAccountInformationReply }{@code >}
     *     
     */
    public JAXBElement<GetAccountInformationReply> getGetAccountDataResult() {
        return getAccountDataResult;
    }

    /**
     * Sets the value of the getAccountDataResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GetAccountInformationReply }{@code >}
     *     
     */
    public void setGetAccountDataResult(JAXBElement<GetAccountInformationReply> value) {
        this.getAccountDataResult = value;
    }

}
