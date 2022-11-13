
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
@XmlType(name = "", propOrder = {
    "getAccountDataResult"
})
@XmlRootElement(name = "GetAccountDataResponse")
public class GetAccountDataResponse {

    @XmlElement(name = "GetAccountDataResult")
    protected GetAccountInformationReply getAccountDataResult;

    /**
     * Gets the value of the getAccountDataResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetAccountInformationReply }
     *     
     */
    public GetAccountInformationReply getGetAccountDataResult() {
        return getAccountDataResult;
    }

    /**
     * Sets the value of the getAccountDataResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetAccountInformationReply }
     *     
     */
    public void setGetAccountDataResult(GetAccountInformationReply value) {
        this.getAccountDataResult = value;
    }

}
