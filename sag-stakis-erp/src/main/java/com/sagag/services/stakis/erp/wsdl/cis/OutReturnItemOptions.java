
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OutReturnItemOptions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutReturnItemOptions">
 *   &lt;complexContent>
 *     &lt;extension base="{DVSE.WebApp.CISService}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="ReturnStates" type="{DVSE.WebApp.CISService}ArrayOfState" minOccurs="0"/>
 *         &lt;element name="ReturnReasons" type="{DVSE.WebApp.CISService}ArrayOfReturnReason" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutReturnItemOptions", propOrder = {
    "returnStates",
    "returnReasons"
})
public class OutReturnItemOptions
    extends BaseResponse
{

    @XmlElementRef(name = "ReturnStates", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfState> returnStates;
    @XmlElementRef(name = "ReturnReasons", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfReturnReason> returnReasons;

    /**
     * Gets the value of the returnStates property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfState }{@code >}
     *     
     */
    public JAXBElement<ArrayOfState> getReturnStates() {
        return returnStates;
    }

    /**
     * Sets the value of the returnStates property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfState }{@code >}
     *     
     */
    public void setReturnStates(JAXBElement<ArrayOfState> value) {
        this.returnStates = value;
    }

    /**
     * Gets the value of the returnReasons property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfReturnReason }{@code >}
     *     
     */
    public JAXBElement<ArrayOfReturnReason> getReturnReasons() {
        return returnReasons;
    }

    /**
     * Sets the value of the returnReasons property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfReturnReason }{@code >}
     *     
     */
    public void setReturnReasons(JAXBElement<ArrayOfReturnReason> value) {
        this.returnReasons = value;
    }

}
