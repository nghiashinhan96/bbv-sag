
package com.sagag.services.autonet.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetUserRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetUserRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}BaseRequest">
 *       &lt;sequence>
 *         &lt;element name="ANPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ANUserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Source" type="{http://www.w3.org/2001/XMLSchema}unsignedByte" minOccurs="0"/>
 *         &lt;element name="TDLanguageCode" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetUserRequest", propOrder = {
    "anPassword",
    "anUserName",
    "source",
    "tdLanguageCode"
})
public class GetUserRequestType
    extends BaseRequestType
{

    @XmlElementRef(name = "ANPassword", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> anPassword;
    @XmlElementRef(name = "ANUserName", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> anUserName;
    @XmlElement(name = "Source")
    @XmlSchemaType(name = "unsignedByte")
    protected Short source;
    @XmlElement(name = "TDLanguageCode")
    protected Integer tdLanguageCode;

    /**
     * Gets the value of the anPassword property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getANPassword() {
        return anPassword;
    }

    /**
     * Sets the value of the anPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setANPassword(JAXBElement<String> value) {
        this.anPassword = value;
    }

    /**
     * Gets the value of the anUserName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getANUserName() {
        return anUserName;
    }

    /**
     * Sets the value of the anUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setANUserName(JAXBElement<String> value) {
        this.anUserName = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setSource(Short value) {
        this.source = value;
    }

    /**
     * Gets the value of the tdLanguageCode property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTDLanguageCode() {
        return tdLanguageCode;
    }

    /**
     * Sets the value of the tdLanguageCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTDLanguageCode(Integer value) {
        this.tdLanguageCode = value;
    }

}
