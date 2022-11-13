
package com.sagag.services.dvse.wsdl.tmconnect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
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
@XmlType(name = "", propOrder = {
    "getErpInformationResult"
})
@XmlRootElement(name = "GetErpInformationResponse")
public class GetErpInformationResponse {

    @XmlElement(name = "GetErpInformationResult")
    protected GetErpInformationReply getErpInformationResult;

    /**
     * Gets the value of the getErpInformationResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetErpInformationReply }
     *     
     */
    public GetErpInformationReply getGetErpInformationResult() {
        return getErpInformationResult;
    }

    /**
     * Sets the value of the getErpInformationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetErpInformationReply }
     *     
     */
    public void setGetErpInformationResult(GetErpInformationReply value) {
        this.getErpInformationResult = value;
    }

}
