
package com.sagag.services.dvse.wsdl.tmconnect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
public class SendOrderDocumentRequest
    extends BaseRequest
{

    @XmlElement(name = "MasterData")
    protected MasterData masterData;
    @XmlElement(name = "OrderCollection")
    protected OrderCollection orderCollection;
    @XmlElement(name = "DocType")
    protected int docType;
    @XmlElement(name = "Document")
    protected String document;

    /**
     * Gets the value of the masterData property.
     * 
     * @return
     *     possible object is
     *     {@link MasterData }
     *     
     */
    public MasterData getMasterData() {
        return masterData;
    }

    /**
     * Sets the value of the masterData property.
     * 
     * @param value
     *     allowed object is
     *     {@link MasterData }
     *     
     */
    public void setMasterData(MasterData value) {
        this.masterData = value;
    }

    /**
     * Gets the value of the orderCollection property.
     * 
     * @return
     *     possible object is
     *     {@link OrderCollection }
     *     
     */
    public OrderCollection getOrderCollection() {
        return orderCollection;
    }

    /**
     * Sets the value of the orderCollection property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderCollection }
     *     
     */
    public void setOrderCollection(OrderCollection value) {
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
     *     {@link String }
     *     
     */
    public String getDocument() {
        return document;
    }

    /**
     * Sets the value of the document property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocument(String value) {
        this.document = value;
    }

}
