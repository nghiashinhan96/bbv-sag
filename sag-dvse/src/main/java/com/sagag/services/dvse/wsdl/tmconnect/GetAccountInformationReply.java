
package com.sagag.services.dvse.wsdl.tmconnect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
public class GetAccountInformationReply
    extends BaseResponse
{

    @XmlElement(name = "AccountData")
    protected ArrayOfAccountData accountData;

    /**
     * Gets the value of the accountData property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAccountData }
     *     
     */
    public ArrayOfAccountData getAccountData() {
        return accountData;
    }

    /**
     * Sets the value of the accountData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAccountData }
     *     
     */
    public void setAccountData(ArrayOfAccountData value) {
        this.accountData = value;
    }

}
