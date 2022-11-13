
package com.sagag.services.autonet.erp.wsdl.tmconnect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetUserReply complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetUserReply">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="CustomerAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomerFirstName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomerID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomerLastName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DVSEPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DVSEUserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ErrorMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ErrorOccured" type="{http://www.w3.org/2001/XMLSchema}unsignedByte" minOccurs="0"/>
 *         &lt;element name="HKat" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Privileges" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StartPage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StartPagePosition" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="TraderID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="UID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetUserReply", propOrder = {
    "rest"
})
public class GetUserReplyType
    extends BaseResponseType
{

    @XmlElementRefs({
        @XmlElementRef(name = "CustomerLastName", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Privileges", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "UID", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "StartPage", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "TraderID", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "CustomerFirstName", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "DVSEUserName", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "StartPagePosition", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "CustomerAddress", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "DVSEPassword", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "ErrorMessage", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "CustomerID", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "ErrorOccured", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "HKat", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<? extends Serializable>> rest;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "ErrorMessage" is used by two different parts of a schema. See: 
     * line 1 of https://connectwstest.autonet-group.com/TMConnectContract.svc?xsd=xsd0
     * line 1 of https://connectwstest.autonet-group.com/TMConnectContract.svc?xsd=xsd0
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the rest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link Integer }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link Integer }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link Short }{@code >}
     * {@link JAXBElement }{@code <}{@link Integer }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends Serializable>> getRest() {
        if (rest == null) {
            rest = new ArrayList<JAXBElement<? extends Serializable>>();
        }
        return this.rest;
    }

}
