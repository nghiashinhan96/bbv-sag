
package com.sagag.services.autonet.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.sagag.services.autonet.erp.wsdl.user.SetSidRequestType;


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
 *         &lt;element name="sidInfo" type="{http://schemas.datacontract.org/2004/07/}SetSidRequest" minOccurs="0"/>
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
    "sidInfo"
})
@XmlRootElement(name = "SetSid")
public class SetSidElement {

    @XmlElementRef(name = "sidInfo", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<SetSidRequestType> sidInfo;

    /**
     * Gets the value of the sidInfo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link SetSidRequestType }{@code >}
     *     
     */
    public JAXBElement<SetSidRequestType> getSidInfo() {
        return sidInfo;
    }

    /**
     * Sets the value of the sidInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link SetSidRequestType }{@code >}
     *     
     */
    public void setSidInfo(JAXBElement<SetSidRequestType> value) {
        this.sidInfo = value;
    }

}
