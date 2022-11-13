
package com.sagag.services.stakis.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SendOrderReply complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SendOrderReply">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="MasterData" type="{http://topmotive.eu/TMConnect}MasterData" minOccurs="0"/>
 *         &lt;element name="OrderCollection" type="{http://topmotive.eu/TMConnect}OrderCollection" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SendOrderReply", propOrder = {
    "masterData",
    "orderCollection"
})
public class SendOrderReply
    extends BaseResponse
{

    @XmlElementRef(name = "MasterData", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<MasterData> masterData;
    @XmlElementRef(name = "OrderCollection", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<OrderCollection> orderCollection;

    /**
     * Gets the value of the masterData property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link MasterData }{@code >}
     *     
     */
    public JAXBElement<MasterData> getMasterData() {
        return masterData;
    }

    /**
     * Sets the value of the masterData property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link MasterData }{@code >}
     *     
     */
    public void setMasterData(JAXBElement<MasterData> value) {
        this.masterData = value;
    }

    /**
     * Gets the value of the orderCollection property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link OrderCollection }{@code >}
     *     
     */
    public JAXBElement<OrderCollection> getOrderCollection() {
        return orderCollection;
    }

    /**
     * Sets the value of the orderCollection property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link OrderCollection }{@code >}
     *     
     */
    public void setOrderCollection(JAXBElement<OrderCollection> value) {
        this.orderCollection = value;
    }

}
