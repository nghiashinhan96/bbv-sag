
package com.sagag.services.stakis.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetItemsCollectionsResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetItemsCollectionsResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetItemsCollectionsResult" type="{http://topmotive.eu/TMConnect}GetItemsCollectionReply" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetItemsCollectionsResponseBody", propOrder = {
    "getItemsCollectionsResult"
})
public class GetItemsCollectionsResponseBody {

    @XmlElementRef(name = "GetItemsCollectionsResult", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<GetItemsCollectionReply> getItemsCollectionsResult;

    /**
     * Gets the value of the getItemsCollectionsResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GetItemsCollectionReply }{@code >}
     *     
     */
    public JAXBElement<GetItemsCollectionReply> getGetItemsCollectionsResult() {
        return getItemsCollectionsResult;
    }

    /**
     * Sets the value of the getItemsCollectionsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GetItemsCollectionReply }{@code >}
     *     
     */
    public void setGetItemsCollectionsResult(JAXBElement<GetItemsCollectionReply> value) {
        this.getItemsCollectionsResult = value;
    }

}
