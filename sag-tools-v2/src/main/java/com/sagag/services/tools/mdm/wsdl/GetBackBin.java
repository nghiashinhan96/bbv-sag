//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2017.05.26 at 09:50:19 AM ICT
//


package com.sagag.services.tools.mdm.wsdl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetBackBin complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="GetBackBin"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://schemas.datacontract.org/2004/07/WebServiceV4.GetBack}GetBackBase"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetBackBin", namespace = "http://schemas.datacontract.org/2004/07/WebServiceV4.GetBack", propOrder = {
    "data"
})
public class GetBackBin
    extends GetBackBase {

    @XmlElementRef(name = "Data", namespace = "http://schemas.datacontract.org/2004/07/WebServiceV4.GetBack", type = JAXBElement.class, required = false)
    protected JAXBElement<byte[]> data;

    /**
     * Gets the value of the data property.
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *
     */
    public JAXBElement<byte[]> getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *
     */
    public void setData(JAXBElement<byte[]> value) {
        this.data = value;
    }

}
