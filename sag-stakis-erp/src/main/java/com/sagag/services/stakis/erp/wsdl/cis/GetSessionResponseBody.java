
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetSessionResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetSessionResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetSessionResult" type="{DVSE.WebApp.CISService}OutSession" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetSessionResponseBody", propOrder = {
    "getSessionResult"
})
public class GetSessionResponseBody {

    @XmlElementRef(name = "GetSessionResult", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<OutSession> getSessionResult;

    /**
     * Gets the value of the getSessionResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link OutSession }{@code >}
     *     
     */
    public JAXBElement<OutSession> getGetSessionResult() {
        return getSessionResult;
    }

    /**
     * Sets the value of the getSessionResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link OutSession }{@code >}
     *     
     */
    public void setGetSessionResult(JAXBElement<OutSession> value) {
        this.getSessionResult = value;
    }

}
