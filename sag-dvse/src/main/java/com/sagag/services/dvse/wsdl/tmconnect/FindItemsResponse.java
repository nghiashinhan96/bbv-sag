
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
@XmlType(name = "", propOrder = {
    "findItemsResult"
})
@XmlRootElement(name = "FindItemsResponse")
public class FindItemsResponse {

    @XmlElement(name = "FindItemsResult")
    protected FindItemsReply findItemsResult;

    /**
     * Gets the value of the findItemsResult property.
     * 
     * @return
     *     possible object is
     *     {@link FindItemsReply }
     *     
     */
    public FindItemsReply getFindItemsResult() {
        return findItemsResult;
    }

    /**
     * Sets the value of the findItemsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link FindItemsReply }
     *     
     */
    public void setFindItemsResult(FindItemsReply value) {
        this.findItemsResult = value;
    }

}
