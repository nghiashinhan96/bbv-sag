package com.sagag.services.dvse.wsdl.unicat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "sendOrderResult"
})
@XmlRootElement(name = "SendOrderResponse")
public class SendOrderResponse {

    @XmlElement(name = "SendOrderResult")
    protected GetBackOrder sendOrderResult;

    /**
     * Gets the value of the sendOrderResult property.
     *
     * @return
     *     possible object is
     *     {@link GetBackOrder }
     *
     */
    public GetBackOrder getSendOrderResult() {
        return sendOrderResult;
    }

    /**
     * Sets the value of the sendOrderResult property.
     *
     * @param value
     *     allowed object is
     *     {@link GetBackOrder }
     *
     */
    public void setSendOrderResult(GetBackOrder value) {
        this.sendOrderResult = value;
    }

}
