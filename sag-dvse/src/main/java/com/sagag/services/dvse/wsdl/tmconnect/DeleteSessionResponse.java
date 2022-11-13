
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
 *         &lt;element name="DeleteSessionResult" type="{http://topmotive.eu/TMConnect}BaseResponse" minOccurs="0"/>
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
    "deleteSessionResult"
})
@XmlRootElement(name = "DeleteSessionResponse")
public class DeleteSessionResponse {

    @XmlElement(name = "DeleteSessionResult")
    protected BaseResponse deleteSessionResult;

    /**
     * Gets the value of the deleteSessionResult property.
     * 
     * @return
     *     possible object is
     *     {@link BaseResponse }
     *     
     */
    public BaseResponse getDeleteSessionResult() {
        return deleteSessionResult;
    }

    /**
     * Sets the value of the deleteSessionResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link BaseResponse }
     *     
     */
    public void setDeleteSessionResult(BaseResponse value) {
        this.deleteSessionResult = value;
    }

}