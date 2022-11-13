
package com.sagag.services.autonet.erp.wsdl.user;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.GetUserReplyType;


/**
 * <p>Java class for GetUserResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetUserResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetUserResult" type="{http://topmotive.eu/TMConnect}GetUserReply" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetUserResponseBody", propOrder = {
    "getUserResult"
})
public class GetUserResponseBodyType {

    @XmlElementRef(name = "GetUserResult", namespace = "http://schemas.datacontract.org/2004/07/", type = JAXBElement.class, required = false)
    protected JAXBElement<GetUserReplyType> getUserResult;

    /**
     * Gets the value of the getUserResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GetUserReplyType }{@code >}
     *     
     */
    public JAXBElement<GetUserReplyType> getGetUserResult() {
        return getUserResult;
    }

    /**
     * Sets the value of the getUserResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GetUserReplyType }{@code >}
     *     
     */
    public void setGetUserResult(JAXBElement<GetUserReplyType> value) {
        this.getUserResult = value;
    }

}
