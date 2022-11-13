
package com.sagag.services.dvse.wsdl.tmconnect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetSessionReply complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetSessionReply">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="SessionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ServiceConfiguration" type="{http://topmotive.eu/TMConnect}ServiceConfiguration" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetSessionReply", propOrder = {
    "sessionId",
    "serviceConfiguration"
})
public class GetSessionReply
    extends BaseResponse
{

    @XmlElement(name = "SessionId")
    protected String sessionId;
    @XmlElement(name = "ServiceConfiguration")
    protected ServiceConfiguration serviceConfiguration;

    /**
     * Gets the value of the sessionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Sets the value of the sessionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionId(String value) {
        this.sessionId = value;
    }

    /**
     * Gets the value of the serviceConfiguration property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceConfiguration }
     *     
     */
    public ServiceConfiguration getServiceConfiguration() {
        return serviceConfiguration;
    }

    /**
     * Sets the value of the serviceConfiguration property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceConfiguration }
     *     
     */
    public void setServiceConfiguration(ServiceConfiguration value) {
        this.serviceConfiguration = value;
    }

}
