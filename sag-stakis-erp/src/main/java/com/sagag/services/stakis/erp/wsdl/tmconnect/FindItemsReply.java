
package com.sagag.services.stakis.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FindItemsReply complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FindItemsReply">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="MasterData" type="{http://topmotive.eu/TMConnect}MasterData" minOccurs="0"/>
 *         &lt;element name="ErpArticleInformation" type="{http://topmotive.eu/TMConnect}ArrayOfErpInformation" minOccurs="0"/>
 *         &lt;element name="DoAdditionalErpInformationRequest" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="TypeId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindItemsReply", propOrder = {
    "masterData",
    "erpArticleInformation",
    "doAdditionalErpInformationRequest",
    "typeId"
})
public class FindItemsReply
    extends BaseResponse
{

    @XmlElementRef(name = "MasterData", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<MasterData> masterData;
    @XmlElementRef(name = "ErpArticleInformation", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfErpInformation> erpArticleInformation;
    @XmlElement(name = "DoAdditionalErpInformationRequest")
    protected boolean doAdditionalErpInformationRequest;
    @XmlElement(name = "TypeId")
    protected int typeId;

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
     * Gets the value of the erpArticleInformation property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfErpInformation }{@code >}
     *     
     */
    public JAXBElement<ArrayOfErpInformation> getErpArticleInformation() {
        return erpArticleInformation;
    }

    /**
     * Sets the value of the erpArticleInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfErpInformation }{@code >}
     *     
     */
    public void setErpArticleInformation(JAXBElement<ArrayOfErpInformation> value) {
        this.erpArticleInformation = value;
    }

    /**
     * Gets the value of the doAdditionalErpInformationRequest property.
     * 
     */
    public boolean isDoAdditionalErpInformationRequest() {
        return doAdditionalErpInformationRequest;
    }

    /**
     * Sets the value of the doAdditionalErpInformationRequest property.
     * 
     */
    public void setDoAdditionalErpInformationRequest(boolean value) {
        this.doAdditionalErpInformationRequest = value;
    }

    /**
     * Gets the value of the typeId property.
     * 
     */
    public int getTypeId() {
        return typeId;
    }

    /**
     * Sets the value of the typeId property.
     * 
     */
    public void setTypeId(int value) {
        this.typeId = value;
    }

}
