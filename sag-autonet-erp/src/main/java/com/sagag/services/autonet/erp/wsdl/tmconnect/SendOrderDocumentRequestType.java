
package com.sagag.services.autonet.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SendOrderDocumentRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SendOrderDocumentRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}BaseRequest">
 *       &lt;sequence>
 *         &lt;element name="MasterData" type="{http://topmotive.eu/TMConnect}MasterData" minOccurs="0"/>
 *         &lt;element name="OrderCollection" type="{http://topmotive.eu/TMConnect}OrderCollection" minOccurs="0"/>
 *         &lt;element name="DocType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Document" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SendOrderDocumentRequest", propOrder = {
    "masterData",
    "orderCollection",
    "docType",
    "document"
})
public class SendOrderDocumentRequestType
    extends BaseRequestType
{

    @XmlElementRef(name = "MasterData", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<MasterDataType> masterData;
    @XmlElementRef(name = "OrderCollection", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<OrderCollectionType> orderCollection;
    @XmlElement(name = "DocType")
    protected int docType;
    @XmlElementRef(name = "Document", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> document;

    /**
     * Gets the value of the masterData property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link MasterDataType }{@code >}
     *     
     */
    public JAXBElement<MasterDataType> getMasterData() {
        return masterData;
    }

    /**
     * Sets the value of the masterData property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link MasterDataType }{@code >}
     *     
     */
    public void setMasterData(JAXBElement<MasterDataType> value) {
        this.masterData = value;
    }

    /**
     * Gets the value of the orderCollection property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link OrderCollectionType }{@code >}
     *     
     */
    public JAXBElement<OrderCollectionType> getOrderCollection() {
        return orderCollection;
    }

    /**
     * Sets the value of the orderCollection property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link OrderCollectionType }{@code >}
     *     
     */
    public void setOrderCollection(JAXBElement<OrderCollectionType> value) {
        this.orderCollection = value;
    }

    /**
     * Gets the value of the docType property.
     * 
     */
    public int getDocType() {
        return docType;
    }

    /**
     * Sets the value of the docType property.
     * 
     */
    public void setDocType(int value) {
        this.docType = value;
    }

    /**
     * Gets the value of the document property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDocument() {
        return document;
    }

    /**
     * Sets the value of the document property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDocument(JAXBElement<String> value) {
        this.document = value;
    }

}
