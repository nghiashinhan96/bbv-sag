//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.29 at 10:18:58 AM ICT 
//


package com.sagag.services.dvse.wsdl.dvse;

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
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GetArticleInformationResult" type="{DVSE}GetBackItems" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getArticleInformationResult"
})
@XmlRootElement(name = "GetArticleInformationResponse")
public class GetArticleInformationResponse {

    @XmlElement(name = "GetArticleInformationResult")
    protected GetBackItems getArticleInformationResult;

    /**
     * Gets the value of the getArticleInformationResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetBackItems }
     *     
     */
    public GetBackItems getGetArticleInformationResult() {
        return getArticleInformationResult;
    }

    /**
     * Sets the value of the getArticleInformationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetBackItems }
     *     
     */
    public void setGetArticleInformationResult(GetBackItems value) {
        this.getArticleInformationResult = value;
    }

}
