
package com.sagag.services.autonet.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetAccountInformationReply complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetAccountInformationReply">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="AccountData" type="{http://topmotive.eu/TMConnect}ArrayOfAccountData" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetAccountInformationReply", propOrder = {
    "accountData"
})
public class GetAccountInformationReplyType
    extends BaseResponseType
{

    @XmlElementRef(name = "AccountData", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfAccountDataType> accountData;

    /**
     * Gets the value of the accountData property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfAccountDataType }{@code >}
     *     
     */
    public JAXBElement<ArrayOfAccountDataType> getAccountData() {
        return accountData;
    }

    /**
     * Sets the value of the accountData property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfAccountDataType }{@code >}
     *     
     */
    public void setAccountData(JAXBElement<ArrayOfAccountDataType> value) {
        this.accountData = value;
    }

}
