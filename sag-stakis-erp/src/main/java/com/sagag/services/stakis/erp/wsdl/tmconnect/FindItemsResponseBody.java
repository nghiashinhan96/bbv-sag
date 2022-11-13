
package com.sagag.services.stakis.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FindItemsResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FindItemsResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FindItemsResult" type="{http://topmotive.eu/TMConnect}FindItemsReply" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindItemsResponseBody", propOrder = {
    "findItemsResult"
})
public class FindItemsResponseBody {

    @XmlElementRef(name = "FindItemsResult", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<FindItemsReply> findItemsResult;

    /**
     * Gets the value of the findItemsResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link FindItemsReply }{@code >}
     *     
     */
    public JAXBElement<FindItemsReply> getFindItemsResult() {
        return findItemsResult;
    }

    /**
     * Sets the value of the findItemsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link FindItemsReply }{@code >}
     *     
     */
    public void setFindItemsResult(JAXBElement<FindItemsReply> value) {
        this.findItemsResult = value;
    }

}
