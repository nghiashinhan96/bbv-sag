package com.sagag.services.dvse.wsdl.unicat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
