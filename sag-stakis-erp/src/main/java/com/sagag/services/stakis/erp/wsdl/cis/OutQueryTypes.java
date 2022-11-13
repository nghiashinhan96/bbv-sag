
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OutQueryTypes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutQueryTypes">
 *   &lt;complexContent>
 *     &lt;extension base="{DVSE.WebApp.CISService}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="QueryTypes" type="{DVSE.WebApp.CISService}ArrayOfQueryType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutQueryTypes", propOrder = {
    "queryTypes"
})
public class OutQueryTypes
    extends BaseResponse
{

    @XmlElementRef(name = "QueryTypes", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfQueryType> queryTypes;

    /**
     * Gets the value of the queryTypes property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfQueryType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfQueryType> getQueryTypes() {
        return queryTypes;
    }

    /**
     * Sets the value of the queryTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfQueryType }{@code >}
     *     
     */
    public void setQueryTypes(JAXBElement<ArrayOfQueryType> value) {
        this.queryTypes = value;
    }

}
