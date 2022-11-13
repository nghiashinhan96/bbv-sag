
package com.sagag.services.dvse.wsdl.tmconnect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Notification complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Notification">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}BaseDto">
 *       &lt;sequence>
 *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="LinkedEntity" type="{http://topmotive.eu/TMConnect}EntityLink" minOccurs="0"/>
 *         &lt;element name="Memo" type="{http://topmotive.eu/TMConnect}Memo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Notification", propOrder = {
    "type",
    "linkedEntity",
    "memo"
})
public class Notification
    extends BaseDto
{

    @XmlElement(name = "Type")
    protected int type;
    @XmlElement(name = "LinkedEntity")
    protected EntityLink linkedEntity;
    @XmlElement(name = "Memo")
    protected Memo memo;

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
     *     {@link EntityLink }
     *     
     */
    public EntityLink getLinkedEntity() {
        return linkedEntity;
    }

    /**
     * Sets the value of the linkedEntity property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntityLink }
     *     
     */
    public void setLinkedEntity(EntityLink value) {
        this.linkedEntity = value;
    }

    /**
     * Gets the value of the memo property.
     * 
     * @return
     *     possible object is
     *     {@link Memo }
     *     
     */
    public Memo getMemo() {
        return memo;
    }

    /**
     * Sets the value of the memo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Memo }
     *     
     */
    public void setMemo(Memo value) {
        this.memo = value;
    }

}
