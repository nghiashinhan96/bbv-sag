
package com.sagag.services.stakis.erp.wsdl.cis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OutSales complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutSales">
 *   &lt;complexContent>
 *     &lt;extension base="{DVSE.WebApp.CISService}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="Sales" type="{DVSE.WebApp.CISService}ArrayOfSale" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutSales", propOrder = {
    "sales"
})
public class OutSales
    extends BaseResponse
{

    @XmlElementRef(name = "Sales", namespace = "DVSE.WebApp.CISService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfSale> sales;

    /**
     * Gets the value of the sales property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSale }{@code >}
     *     
     */
    public JAXBElement<ArrayOfSale> getSales() {
        return sales;
    }

    /**
     * Sets the value of the sales property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSale }{@code >}
     *     
     */
    public void setSales(JAXBElement<ArrayOfSale> value) {
        this.sales = value;
    }

}
