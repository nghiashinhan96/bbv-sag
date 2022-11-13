package com.sagag.services.dvse.wsdl.unicat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AvailableState", propOrder = {
    "availState",
    "availDescription",
    "availIconUrl"
})
public class AvailableState {

    @XmlElement(name = "AvailState")
    protected int availState;
    @XmlElement(name = "AvailDescription")
    protected String availDescription;
    @XmlElement(name = "AvailIconUrl")
    protected String availIconUrl;

    /**
     * Gets the value of the availState property.
     *
     */
    public int getAvailState() {
        return availState;
    }

    /**
     * Sets the value of the availState property.
     *
     */
    public void setAvailState(int value) {
        this.availState = value;
    }

    /**
     * Gets the value of the availDescription property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAvailDescription() {
        return availDescription;
    }

    /**
     * Sets the value of the availDescription property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAvailDescription(String value) {
        this.availDescription = value;
    }

    /**
     * Gets the value of the availIconUrl property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAvailIconUrl() {
        return availIconUrl;
    }

    /**
     * Sets the value of the availIconUrl property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAvailIconUrl(String value) {
        this.availIconUrl = value;
    }

}
