
package com.sagag.services.autonet.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ControlIndicator complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ControlIndicator">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="LinkedEntity" type="{http://topmotive.eu/TMConnect}EntityLink" minOccurs="0"/>
 *         &lt;element name="Parameters" type="{http://topmotive.eu/TMConnect}ArrayOfKeyValueItem" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ControlIndicator", propOrder = {
    "type",
    "linkedEntity",
    "parameters"
})
public class ControlIndicatorType {

    @XmlElement(name = "Type")
    protected int type;
    @XmlElementRef(name = "LinkedEntity", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<EntityLinkType> linkedEntity;
    @XmlElementRef(name = "Parameters", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfKeyValueItemType> parameters;

    /**
     * Gets the value of the type property.
     * 
     */
    public int getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     */
    public void setType(int value) {
        this.type = value;
    }

    /**
     * Gets the value of the linkedEntity property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}
     *     
     */
    public JAXBElement<EntityLinkType> getLinkedEntity() {
        return linkedEntity;
    }

    /**
     * Sets the value of the linkedEntity property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}
     *     
     */
    public void setLinkedEntity(JAXBElement<EntityLinkType> value) {
        this.linkedEntity = value;
    }

    /**
     * Gets the value of the parameters property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfKeyValueItemType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfKeyValueItemType> getParameters() {
        return parameters;
    }

    /**
     * Sets the value of the parameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfKeyValueItemType }{@code >}
     *     
     */
    public void setParameters(JAXBElement<ArrayOfKeyValueItemType> value) {
        this.parameters = value;
    }

}
