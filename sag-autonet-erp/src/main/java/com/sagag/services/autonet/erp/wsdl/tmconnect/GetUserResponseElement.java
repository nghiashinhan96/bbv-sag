
package com.sagag.services.autonet.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.sagag.services.autonet.erp.wsdl.user.GetUserResponseType;


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
 *         &lt;element name="GetUserResult" type="{http://schemas.datacontract.org/2004/07/}GetUserResponse" minOccurs="0"/>
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
    "getUserResult"
})
@XmlRootElement(name = "GetUserResponse")
public class GetUserResponseElement {

    @XmlElementRef(name = "GetUserResult", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<GetUserResponseType> getUserResult;

    /**
     * Gets the value of the getUserResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GetUserResponseType }{@code >}
     *     
     */
    public JAXBElement<GetUserResponseType> getGetUserResult() {
        return getUserResult;
    }

    /**
     * Sets the value of the getUserResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GetUserResponseType }{@code >}
     *     
     */
    public void setGetUserResult(JAXBElement<GetUserResponseType> value) {
        this.getUserResult = value;
    }

}
