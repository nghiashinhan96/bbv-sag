
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
@XmlType(name = "", propOrder = {
    "getItemsCollectionsResult"
})
@XmlRootElement(name = "GetItemsCollectionsResponse")
public class GetItemsCollectionsResponse {

    @XmlElement(name = "GetItemsCollectionsResult")
    protected GetItemsCollectionReply getItemsCollectionsResult;

    /**
     * Gets the value of the getItemsCollectionsResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetItemsCollectionReply }
     *     
     */
    public GetItemsCollectionReply getGetItemsCollectionsResult() {
        return getItemsCollectionsResult;
    }

    /**
     * Sets the value of the getItemsCollectionsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetItemsCollectionReply }
     *     
     */
    public void setGetItemsCollectionsResult(GetItemsCollectionReply value) {
        this.getItemsCollectionsResult = value;
    }

}
