
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
 *         &lt;element name="GetSessionResult" type="{http://topmotive.eu/TMConnect}GetSessionReply" minOccurs="0"/>
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
    "getSessionResult"
})
@XmlRootElement(name = "GetSessionResponse")
public class GetSessionResponse {

    @XmlElement(name = "GetSessionResult")
    protected GetSessionReply getSessionResult;

    /**
     * Gets the value of the getSessionResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetSessionReply }
     *     
     */
    public GetSessionReply getGetSessionResult() {
        return getSessionResult;
    }

    /**
     * Sets the value of the getSessionResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetSessionReply }
     *     
     */
    public void setGetSessionResult(GetSessionReply value) {
        this.getSessionResult = value;
    }

}
