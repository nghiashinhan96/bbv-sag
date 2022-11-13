
package com.sagag.services.dvse.wsdl.tmconnect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Credentials complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Credentials">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ExternalIdentityProviderId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CatalogUserCredentials" type="{http://topmotive.eu/TMConnect}User" minOccurs="0"/>
 *         &lt;element name="SalesAdvisorCredentials" type="{http://topmotive.eu/TMConnect}User" minOccurs="0"/>
 *         &lt;element name="SecurityToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IsEncrypted" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Salt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Credentials", propOrder = {
    "externalIdentityProviderId",
    "catalogUserCredentials",
    "salesAdvisorCredentials",
    "securityToken",
    "isEncrypted",
    "salt"
})
public class Credentials {

    @XmlElement(name = "ExternalIdentityProviderId")
    protected String externalIdentityProviderId;
    @XmlElement(name = "CatalogUserCredentials")
    protected User catalogUserCredentials;
    @XmlElement(name = "SalesAdvisorCredentials")
    protected User salesAdvisorCredentials;
    @XmlElement(name = "SecurityToken")
    protected String securityToken;
    @XmlElement(name = "IsEncrypted")
    protected boolean isEncrypted;
    @XmlElement(name = "Salt")
    protected String salt;

    /**
     * Gets the value of the externalIdentityProviderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalIdentityProviderId() {
        return externalIdentityProviderId;
    }

    /**
     * Sets the value of the externalIdentityProviderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalIdentityProviderId(String value) {
        this.externalIdentityProviderId = value;
    }

    /**
     * Gets the value of the catalogUserCredentials property.
     * 
     * @return
     *     possible object is
     *     {@link User }
     *     
     */
    public User getCatalogUserCredentials() {
        return catalogUserCredentials;
    }

    /**
     * Sets the value of the catalogUserCredentials property.
     * 
     * @param value
     *     allowed object is
     *     {@link User }
     *     
     */
    public void setCatalogUserCredentials(User value) {
        this.catalogUserCredentials = value;
    }

    /**
     * Gets the value of the salesAdvisorCredentials property.
     * 
     * @return
     *     possible object is
     *     {@link User }
     *     
     */
    public User getSalesAdvisorCredentials() {
        return salesAdvisorCredentials;
    }

    /**
     * Sets the value of the salesAdvisorCredentials property.
     * 
     * @param value
     *     allowed object is
     *     {@link User }
     *     
     */
    public void setSalesAdvisorCredentials(User value) {
        this.salesAdvisorCredentials = value;
    }

    /**
     * Gets the value of the securityToken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecurityToken() {
        return securityToken;
    }

    /**
     * Sets the value of the securityToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecurityToken(String value) {
        this.securityToken = value;
    }

    /**
     * Gets the value of the isEncrypted property.
     * 
     */
    public boolean isIsEncrypted() {
        return isEncrypted;
    }

    /**
     * Sets the value of the isEncrypted property.
     * 
     */
    public void setIsEncrypted(boolean value) {
        this.isEncrypted = value;
    }

    /**
     * Gets the value of the salt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Sets the value of the salt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalt(String value) {
        this.salt = value;
    }

}
